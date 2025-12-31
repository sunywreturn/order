package com.smartearth.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Component
public class RequestLogInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(RequestLogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StringBuilder sb = new StringBuilder();
        String url = request.getRequestURI();
        sb.append(url);
        sb.append(" ");
        Enumeration<String> parameterNames =  request.getParameterNames();
        while (parameterNames.hasMoreElements()){
            String parameterName = parameterNames.nextElement();
            String value = request.getParameter(parameterName);
            sb.append(parameterName);
            sb.append(":");
            sb.append(value);
            sb.append(" ");
        }
        LOG.info(sb.toString());
        return true;
    }
}
