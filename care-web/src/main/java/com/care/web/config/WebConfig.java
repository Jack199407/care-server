package com.care.web.config;

import com.care.web.filter.AdminAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<AdminAuthFilter> adminAuthFilter() {
        FilterRegistrationBean<AdminAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AdminAuthFilter());
        registrationBean.addUrlPatterns("/admin/*");
        return registrationBean;
    }
}