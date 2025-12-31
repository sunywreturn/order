package com.smartearth.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    /*@Autowired
    private PermissionInterceptor permissionInterceptor;*/

    /*@Autowired
    private RequestLimitInterceptor requestLimitInterceptor;*/

    @Autowired
    private RequestLogInterceptor requestLogInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO Auto-generated method stub
        /*registry.addInterceptor(requestLimitInterceptor).addPathPatterns("/**");*/
        registry.addInterceptor(requestLogInterceptor).addPathPatterns("/**");
        /*registry.addInterceptor(permissionInterceptor).addPathPatterns("/**").excludePathPatterns(
                "/general/sendCode", "/general/checkCode","/file/**",
                "/user/login", "/user/register", "/user/companyName", "/user/updatePassword","/general/upload"
        );*/
    }
}