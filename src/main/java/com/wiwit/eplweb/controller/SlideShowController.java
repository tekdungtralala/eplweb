package com.wiwit.eplweb.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wiwit.eplweb.util.ApiPath;
import com.wiwit.eplweb.util.ImageUtil;
import com.wiwit.eplweb.util.WebappProps;

@RestController
public class SlideShowController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(SlideShowController.class);

	@RequestMapping(value = ApiPath.UPLOAD_SLIDE_SHOW, method = RequestMethod.POST)
	public ResponseEntity<String> uploadSlideShow(
			@PathVariable("teamId") int teamId,
			@RequestParam("filename") MultipartFile file)
			throws JsonGenerationException, JsonMappingException, IOException {

		logger.info("POST /api/upload/slideshow/teamId/" + teamId);

		if (!file.isEmpty()) {
			try {
				String imageDir = WebappProps.getImageFileDir();
				
				String originalFileName = file.getOriginalFilename();
				String contentType = file.getContentType();
				
				String localFileName = ImageUtil.getFileName(originalFileName);
				
				byte[] bytes = file.getBytes();
				File f = new File(imageDir + localFileName);
				FileOutputStream fos = new FileOutputStream(f);
				BufferedOutputStream stream = new BufferedOutputStream(fos);
				stream.write(bytes);
				stream.close();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
