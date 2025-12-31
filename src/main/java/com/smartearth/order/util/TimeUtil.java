package com.smartearth.order.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

	public static String DATE_FORMAT_STR_ALL = "yyyy-MM-dd HH:mm:ss";

	private SimpleDateFormat SDF = new SimpleDateFormat(DATE_FORMAT_STR_ALL, Locale.getDefault());

	public String getCurrentDateTime() {
		return SDF.format(new Date());
	}
	public Date getDate(String date) {
		try {
			return SDF.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
