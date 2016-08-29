package com.cdjdgm.dip.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String toStr(Date date, String format){
		SimpleDateFormat d = new SimpleDateFormat(format);
		return d.format(date);
	}
}
