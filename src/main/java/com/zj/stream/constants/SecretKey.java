/**
 * Copyright (C), 2019
 * FileName: key
 * Author:   zhangjian
 * Date:     2019/9/17 13:58
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.zj.stream.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * yml配置信息加载java类，一定要有set方法,变量设置为static
 * 如果想在filter中加载此类，注入bean，或者直接在doFilter里使用
 */
@Component
@ConfigurationProperties(prefix="key")
public class SecretKey {
    public static  String privateKeyBase64;
    public static String publicKeyBase64;
    public static String salt;

    public static void setSalt(String salt) {
        SecretKey.salt = salt;
    }

    public void setPrivateKeyBase64(String privateKeyBase64) {
        this.privateKeyBase64 = privateKeyBase64;
    }

    public void setPublicKeyBase64(String publicKeyBase64) {
        this.publicKeyBase64 = publicKeyBase64;
    }
}
