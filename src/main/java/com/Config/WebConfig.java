package com.Config;

import com.Interceptor.LoginRequestInterceptor;
import com.Interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private PassportInterceptor passportInterceptor;

    @Autowired
    private LoginRequestInterceptor loginRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequestInterceptor).addPathPatterns("/WriteBlog","/show")
                .excludePathPatterns( "/","/in","/index","/blog/*","/error","/userForgetPassword","/userLogin","/userRegister",
                        "/Login","/register","/sendEmail");
    }
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //获取文件的真实路径
        String path = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\img\\";
        //       /images/**是对应resource下工程目录
        registry.addResourceHandler("/img/**").addResourceLocations("file:"+path);
    }

}
