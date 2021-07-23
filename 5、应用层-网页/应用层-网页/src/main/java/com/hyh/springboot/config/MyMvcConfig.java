package com.hyh.springboot.config;

import com.hyh.springboot.component.LoginHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/main.html").setViewName("dashboard");
        registry.addViewController("/chart.html").setViewName("chart");
    }
 //   @Override
 //   public void addInterceptors(InterceptorRegistry registry) {
 //       registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns("/templates/","/assets/","/index.html","/","/user/login","/webjars/**","/i18n");
 //   }
}
