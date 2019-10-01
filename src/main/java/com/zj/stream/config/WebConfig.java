/**
 * Copyright (C), 2019
 * FileName: WebConfig
 * Author:   zhangjian
 * Date:     2019/9/2 18:31
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.zj.stream.config;

import com.zj.stream.filter.LogFilter;
import com.zj.stream.filter.SecretFilter;
import com.zj.stream.interceptor.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.ArrayList;
import java.util.List;

/**
 * 各种配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private MyInterceptor myInterceptor;

    // 重写WebMvcConfigurerAdapter类的addInterceptors方法，添加自己的拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor)
                .addPathPatterns("/**");
    }

    /**
     * 解决跨域问题
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*");
        //.allowedOrigins("http://localhost:8082");
        //.allowedOrigins("*");
    }


    @Bean
    public FilterRegistrationBean secretFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new SecretFilter());
        filterRegistrationBean.setName("secretFilter");

        //设置启动等级,值越小约先启动
        filterRegistrationBean.setOrder(1);

        // 设置filter在指定url起作用
        List<String> urlList = new ArrayList<>();
        urlList.add("/*");

        filterRegistrationBean.setUrlPatterns(urlList);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setName("logFilter");
        filterRegistrationBean.setOrder(2);

        List<String> urlList = new ArrayList<>();
        urlList.add("/*");

        filterRegistrationBean.setUrlPatterns(urlList);
        return filterRegistrationBean;
    }
}
