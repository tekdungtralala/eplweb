package com.wiwit.eplweb.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class ImageUtil {
	
	public enum ImageType {
		SLIDESHOW
	}

	public static String getFileName(String originalFileName) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_");
		return df.format(new Date()) + originalFileName;
	}

	public static void saveScaledImage(File imageFile) {
		try {
			String thumbnailDir = WebappProps.getThumbnailFileDir();

			BufferedImage sourceImage = ImageIO.read(imageFile);
			BufferedImage scaledImg = Scalr.resize(sourceImage, 100);
			ImageIO.write(scaledImg, "jpg", new File(thumbnailDir + imageFile.getName()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
