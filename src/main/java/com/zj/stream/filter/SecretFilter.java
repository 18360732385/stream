/**
 * Copyright (C), 2019
 * FileName: MyFilter
 * Author:   zhangjian
 * Date:     2019/9/2 18:49
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.zj.stream.filter;

import com.alibaba.fastjson.JSONObject;
import com.zj.stream.constants.SecretConstant;
import com.zj.stream.constants.SecretKey;
import com.zj.stream.enums.ResultCode;
import com.zj.stream.exception.CustomException;
import com.zj.stream.pojo.Result;
import com.zj.stream.util.OneSecretUtil;
import com.zj.stream.util.RequestWrapper;
import com.zj.stream.util.ResponseWrapper;
import com.zj.stream.util.RsaUtil;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 报文加解密过滤器（sign、content）
 * 关于过滤器中无法注入bean问题，需要设置改bean的变量为staric属性
 */
//@Component
@EnableConfigurationProperties({SecretKey.class})
public class SecretFilter implements Filter {
    private Logger logger =LoggerFactory.getLogger(SecretFilter.class);

    String transformation = "RSA";//加密算法

    @Autowired
    SecretKey secretKey;

    @Override
    public void init(FilterConfig filterConfig)  {
        System.out.println("SecretFilter 初始化");
        System.out.println("init私钥: "+secretKey.privateKeyBase64);
        System.out.println("init测试私钥是否为空: "+ SecretKey.privateKeyBase64);

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("SecretFilter 启动");

        long start = System.currentTimeMillis();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        ResponseWrapper responseWrapper = new ResponseWrapper(response);

        //为每一个请求添加全局流水号
        request.setAttribute("globalId", UUID.randomUUID().toString());

        //请求报文解密（可以判断路径是否需要解密）
        RequestWrapper requestWrapper = null;
        try {
            requestWrapper = reuqestDecrypt(request);
        } catch (CustomException e) {
            //直接返回请求报文异常
            String s = JSONObject.toJSONString(Result.failure(e.getResultCode()));
            response.getOutputStream().write(s.getBytes());
            return;
        }

        //解密报文调用服务。注意，这里要使用替代的request和response！！！
        filterChain.doFilter(requestWrapper, responseWrapper);

        //返回报文加密（可以判断路径是否需要加密）
        try {
            String responseEnCrypt = responseEnCrypt(responseWrapper);
            response.reset();//很重要，清除空行？
            response.getOutputStream().write(responseEnCrypt.getBytes());
        } catch (Exception e) {
            System.out.println("SecretFilter加密错误：" + e);
        }


        System.out.println("SecretFilter 耗时: " + (System.currentTimeMillis() - start)+"ms");
        System.out.println("SecretFilter 结束");
    }

    @Override
    public void destroy() {
        System.out.println("SecretFilter 销毁");
    }

    /**
     * @param request
     * @return 请求报文解密(可以放到工具类util去)
     */
    public RequestWrapper reuqestDecrypt(HttpServletRequest request) throws CustomException {
//        Map<String, Object> args = new HashMap<>();
//        Enumeration<String> parameterNames = request.getParameterNames();
//        while (parameterNames.hasMoreElements()) {
//            String name = parameterNames.nextElement();
//            String parameter = request.getParameter(name);
//            logger.info("请求原文：" + name + "===" + parameter);
//
//            String privateDecryptResult = null;
//            try {
//                privateDecryptResult = RsaUtil.decryptByPrivateKey(parameter, Base64.decodeBase64(SecretKey.privateKeyBase64), transformation);
//                logger.info("私钥解密:" + name + "===" + privateDecryptResult);
//
//                args.put(name, privateDecryptResult);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }


        String key = request.getParameter(SecretConstant.KEY);
        String sign = request.getParameter(SecretConstant.SIGN);
        String content = request.getParameter(SecretConstant.CONTENT);

        if (StringUtils.isEmpty(key) && StringUtils.isEmpty(sign) && StringUtils.isEmpty(content)){
            throw new CustomException(ResultCode.AM000001);
        }else{
            logger.info("\n请求密文：key===" + key +
                        "\nsign===" + sign +
                        "\ncontent===" + content +"\n");
        }

        String decryptContent =null;
        try {
            //私钥解密获得AESKey
            String AESKey = OneSecretUtil.decryptKeyByPrivateKey(key, Base64.decodeBase64(SecretKey.privateKeyBase64));

            //拿AESKey,先对密文content解密,获得明文content
            decryptContent = OneSecretUtil.decrypt(AESKey, content);

            //再拿明文content来验签
            boolean verify = OneSecretUtil.verify(decryptContent, sign, SecretKey.salt);
            if (true != verify){
                throw new CustomException(ResultCode.AM000002);
            }

        } catch (Exception e) {
            logger.error("报文解析失败：",e);
        }

        //利用原始的request对象创建自己扩展的request对象并添加自定义参数
        RequestWrapper requestWrapper = new RequestWrapper(request);
        requestWrapper.addParameters(JSONObject.parseObject(decryptContent));
        requestWrapper.addParameter(SecretConstant.DECRYPTCONTENT,decryptContent);

        return requestWrapper;
    }

    /**
     * @param responseWrapper
     * @return
     * @throws Exception
     * 响应报文加密(可以放到工具类util去)
     */
    public String responseEnCrypt(ResponseWrapper responseWrapper) throws IOException {
        byte[] content = responseWrapper.getResponseData();

        Result encryptResult =null;

        if (content.length > 0) {
            String str = new String(content, "UTF-8");
            StringBuffer responseBuffer = new StringBuffer("\n响应报文原文:" + str);

            try {
                encryptResult = JSONObject.parseObject(str, Result.class);
                String data = (String) encryptResult.getCode();
                String ciphertext = RsaUtil.encryptByPublicKey(data, Base64.decodeBase64(SecretKey.publicKeyBase64), transformation);
                encryptResult.setContent(ciphertext);

                responseBuffer.append("\n响应报文密文:" + encryptResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info(responseBuffer.toString());
        }

        return JSONObject.toJSONString(encryptResult);
    }
}

