package com.example.taupstairs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	/*
	 * 替换
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			str = str.replace(" ","+");
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

}
