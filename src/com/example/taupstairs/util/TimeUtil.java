package com.example.taupstairs.util;

import java.util.Calendar;

import com.example.taupstairs.bean.Time;

public class TimeUtil {

	public static String getDisplayTime(Calendar calendar, String original) {
		String displayTime = null;
		Time now = getNow(calendar);
		Time time = getOriginal(original);
		if (time.getYear() != now.getYear()) {
			displayTime = time.getYear() + "年" + 
					time.getMonth() + "月" +
					time.getDay() + "日    " +
					time.getHour() + ":" +
					time.getMinute();
		} else {
			if (time.getMonth() != now.getMonth()) {
				displayTime = time.getMonth() + "月" +
						time.getDay() + "日    " +
						time.getHour() + ":" +
						time.getMinute();
			} else {
				if (time.getDay() != now.getDay()) {
					displayTime = time.getDay() + "日    " +
							time.getHour() + ":" +
							time.getMinute();
				} else {
					displayTime = time.getHour() + ":" + time.getMinute();
				}
			}
		}
		return displayTime;
	}
	
	private static Time getNow(Calendar calendar) {
		calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		Time now = new Time(year, month, day, hour, minute);
		return now;
	}
	
	private static Time getOriginal(String original) {
		char[] time = original.toCharArray();
		int year = Integer.parseInt(String.copyValueOf(time, 0, 4));
		int month = Integer.parseInt(String.copyValueOf(time, 5, 2));
		int day = Integer.parseInt(String.copyValueOf(time, 8, 2));
		int hour = Integer.parseInt(String.copyValueOf(time, 11, 2));
		int minute = Integer.parseInt(String.copyValueOf(time, 14, 2));
		Time result = new Time(year, month, day, hour, minute);
		return result;
	}
}
