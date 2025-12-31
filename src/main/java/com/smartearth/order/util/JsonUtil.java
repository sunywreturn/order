package com.smartearth.order.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	private static ObjectMapper objectMapper = new ObjectMapper();

	public static String toJson(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			System.out.println("json转换异常");
		}
		return null;
	}

	public static <T> T parseObject(String jsonStr, Class<T> targetClass) {
		try {
			return objectMapper.readValue(jsonStr, targetClass);
		} catch (IOException e) {
			System.out.println("json转换异常");
		}
		return null;
	}

}
