package com.smartearth.order;

import com.smartearth.order.annotation.RequestLimit;
import com.smartearth.order.pojo.RequestTime;
import com.smartearth.order.util.ClientUtil;
import com.smartearth.order.util.ResponseUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RequestLimitInterceptor extends HandlerInterceptorAdapter {

    public static final String INTERFACE_LIMIT = "请求太过频繁，请稍后重试";

    private Map<String, List<RequestTime>> ipRequestTimeMap = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String currentPath = request.getServletPath();
        long currentTimeMillis = System.currentTimeMillis();
        String ip = ClientUtil.getIpAddr(request);
        List<RequestTime> requestTimeList = ipRequestTimeMap.get(ip);  //此处不用加锁，因为不会存在同一个ip同时请求接口，并且requestTimeList == null的情况。但是要防止get和put方法内部出现并发访问所以使用ConcurrentHashMap
        if (requestTimeList == null) {
            requestTimeList = new ArrayList<>();
            ipRequestTimeMap.put(ip, requestTimeList);
        }
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RequestLimit annotation = method.getAnnotation(RequestLimit.class);
            if (annotation != null) {
                RequestTime requestTime = null;
                for (RequestTime requestTimeItem : requestTimeList) {
                    if (currentPath.equals(requestTimeItem.getPath())) {
                        requestTime = requestTimeItem;
                        if ((currentTimeMillis - requestTime.getTime()) < annotation.value()) {
                            ResponseUtil.send(response, INTERFACE_LIMIT);
                            return false;
                        }
                    }
                }
                if (requestTime == null) {
                    requestTime = new RequestTime();
                    requestTimeList.add(requestTime);
                }
                requestTime.setPath(currentPath);
                requestTime.setTime(currentTimeMillis);
                return true;
            }
        }
        RequestTime requestTime = null;
        if (requestTimeList.size() != 0) {
            requestTime = requestTimeList.get(0);
            if ((currentTimeMillis - requestTime.getTime()) < OrderApplication.INTERFACE_LIMIT) {
                ResponseUtil.send(response, INTERFACE_LIMIT);
                return false;
            }
        }
        if (requestTime == null) {
            requestTime = new RequestTime();
            requestTimeList.add(0, requestTime);
        }
        requestTime.setPath(currentPath);
        requestTime.setTime(currentTimeMillis);
        return true;
    }
}
