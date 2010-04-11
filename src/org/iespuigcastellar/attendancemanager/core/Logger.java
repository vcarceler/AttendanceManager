package org.iespuigcastellar.attendancemanager.core;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	public static void log(String m) {
		Date d = new Date();
		System.err.println(dateFormat.format(d) + " " + timeFormat.format(d) + " -> " + m);
	}
}
