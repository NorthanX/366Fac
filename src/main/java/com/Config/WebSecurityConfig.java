package com.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.formLogin()// 表单登录  来身份认证
//            .loginPage("/Login.html")// 自定义登录页面
//            .loginProcessingUrl("/userLogin")// 自定义登录路径
//            .and()
//            .authorizeRequests()// 对请求授权
//            // error  127.0.0.1 将您重定向的次数过多
//            .antMatchers("/Login.html", "/Register1.html",
//                    "/index.html","/ForgetPassword.html","/css/**","/js/**").permitAll()// 这些页面不需要身份认证,其他请求需要认证
//            .and().logout().logoutUrl("/userLogout").logoutSuccessUrl("/userLogin").permitAll()
//            .and()
//            .csrf().disable();// 禁用跨站攻击
        http.authorizeRequests()
                .anyRequest().permitAll().and().logout().permitAll()
                .and()
                .csrf()
                .disable();//配置不需要登录验证
    }



}
