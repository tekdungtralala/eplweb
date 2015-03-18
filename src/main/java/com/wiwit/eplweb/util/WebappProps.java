package com.wiwit.eplweb.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class WebappProps {
	public enum WebappEnum {
		ADMIN_SESSION_KEY, 				// Placed in the header, used for validation request which come in
										//  for upload file placed on query param
		
		IMAGE_FILE_DIRECTORY, 			// images directory
		THUMBNAIL_FILE_DIRECTORY, 		// thumbnail directory
		
		QUERY_PARAM_IMAGE_TYPE			// query param for image_type attribute on table image
	}

	public static String getImageTypeKey() {
		return getValue(WebappEnum.QUERY_PARAM_IMAGE_TYPE);
	}

	public static String getAdminSessionKey() {
		return getValue(WebappEnum.ADMIN_SESSION_KEY);
	}

	public static String getImageFileDir() {
		return getValue(WebappEnum.IMAGE_FILE_DIRECTORY);
	}

	public static String getThumbnailFileDir() {
		return getValue(WebappEnum.THUMBNAIL_FILE_DIRECTORY);
	}

	public static String getValue(WebappEnum e) {
		return getProperties().getProperty(e.toString());
	}

	public static Properties getProperties() {
		Resource resource = new ClassPathResource("webapp.properties");

		try {
			return PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
