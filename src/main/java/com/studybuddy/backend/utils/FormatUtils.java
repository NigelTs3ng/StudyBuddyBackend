package com.studybuddy.backend.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
public class FormatUtils {
	public static String formatDate(Timestamp time, String format) {
		try {
			Date date = new Date(time.getTime());
			SimpleDateFormat fm = new SimpleDateFormat();
			fm.applyPattern(format);
			return fm.format(date);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "";
		}
	}
}
