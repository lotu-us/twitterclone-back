package com.example.twitterclone.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")              //cors를 적용할 URL패턴
                .allowedOrigins("http://localhost:3000", "http://localhost:8080")   //자원 공유를 허락할 Origin
                .allowedMethods("*")   //자원 공유를 허락할 HTTP Method
                .allowedHeaders("*");
    }
}
