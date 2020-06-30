package com.Config;

import com.Interceptor.LoginRequestInterceptor;
import com.Interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private PassportInterceptor passportInterceptor;

    @Autowired
    private LoginRequestInterceptor loginRequestInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequestInterceptor).addPathPatterns("/writeBlog","/user/show","/blog/{blogId}/like","/blog/{blogId}/comment","/user/{userId}/blogList/follow",
                "/news","/blog/{blogId}/download/{filename}")
                .excludePathPatterns( "/","/in","/index","/blog/*","/error","/userForgetPassword","/userLogin","/userRegister",
                        "/Login","/register","/sendEmail","/search","/searchTag");
    }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
//映射图片保存地址
            registry.addResourceHandler("/upload/**").addResourceLocations("file:/root/accessory/");
        }


}
