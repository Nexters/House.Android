package com.nexters.house.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {
//	format = yyyy-MM-dd HH:mm:ss 
	public static String getDateToString(Timestamp timestamp){
		DateTime time = new DateTime(timestamp.getTime());
		
		String string = time.getYear() + "-" + time.getMonthOfYear() + "-" + time.getDayOfMonth() + " " + time.getHourOfDay() + ":" + time.getMinuteOfHour() + ":" + time.getSecondOfMinute();
		return string;
	}
	
	public static Timestamp getStringToDate(String string) throws ParseException {
		DateTimeFormatter dateStringFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime time = dateStringFormat.parseDateTime(string);
		return new Timestamp(time.getMillis());
	}
}
