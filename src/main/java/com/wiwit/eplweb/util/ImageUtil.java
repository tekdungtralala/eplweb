package com.wiwit.eplweb.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtil {

	public static String getFileName(String originalFileName) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_");
		return df.format(new Date()) + originalFileName;
	}
}
