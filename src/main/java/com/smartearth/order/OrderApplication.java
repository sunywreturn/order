package com.smartearth.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class OrderApplication {

	public static final int CODE_EXPIRE = 5 * 60 *1000;

	public static final int INTERFACE_LIMIT = 2 * 1000;

	public static final String NO_PERMISSION = "没有权限";

	public static boolean test;

	@Value("${test}")
	public void setEnvironmentTest(boolean test) {
		OrderApplication.test = test;
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

}
