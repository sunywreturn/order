package com.smartearth.order.util;

import com.smartearth.order.pojo.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtil {
    /**
     * 向HTTP响应中发送错误消息
     *
     * @param response HttpServletResponse对象，用于设置响应属性和输出内容
     * @param msg 要返回的错误消息内容
     */
    public static void sendError(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        Response errorResponse = Response.getFailResult();
        errorResponse.setMsg(msg);
        PrintWriter out = response.getWriter();
        out.print(JsonUtil.toJson(errorResponse));
        out.flush();
        out.close();
    }
}
