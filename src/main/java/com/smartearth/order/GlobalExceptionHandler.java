package com.smartearth.order;

import com.smartearth.order.pojo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String ERROR = "服务器繁忙，请稍后重试";

    public static final String ILLEGAL_INPUT = "非法输入";

    @ExceptionHandler
    public Response exceptionHandler(Exception e) {
        LOG.error(e.getMessage(), e);
        Response errorResponse = Response.getFailResult();
        errorResponse.setMsg(ERROR);
        if(ILLEGAL_INPUT.equals(e.getMessage())){
            errorResponse.setMsg(ILLEGAL_INPUT);
        }
        return errorResponse;
    }

}
