package com.wiwit.eplweb.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wiwit.eplweb.model.Image;
import com.wiwit.eplweb.model.input.SortedImageModelInput;
import com.wiwit.eplweb.model.view.SimpleResult;
import com.wiwit.eplweb.service.ImageService;
import com.wiwit.eplweb.service.TeamService;
import com.wiwit.eplweb.util.ApiPath;
import com.wiwit.eplweb.util.ImageUtil;
import com.wiwit.eplweb.util.ImageUtil.ImageType;
import com.wiwit.eplweb.util.WebappProps;

@RestController
public class SlideShowController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(SlideShowController.class);

	@Autowired
	private TeamService teamService;
	@Autowired
	private ImageService imageService;

	// Sorted images slideshow
	@RequestMapping(value = ApiPath.IMAGES_SORTED, method = RequestMethod.PUT, consumes = CONTENT_TYPE_JSON)
	public ResponseEntity sortedImage(
			@RequestBody List<SortedImageModelInput> sortedImages) {
		logger.info("PUT /api/images/sortedImage size=" + sortedImages.size());
		imageService.saveImageOrder(sortedImages);
		return new ResponseEntity(HttpStatus.OK);
	}

	// Delte image by imageId
	@RequestMapping(value = ApiPath.IMAGES, method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity deleteImageById(@PathVariable("imageId") int imageId,
			HttpServletResponse res, HttpServletRequest req) throws IOException {
		logger.info("DELETE /api/images/" + imageId);
		// Fetch image properties
		String it = req.getParameter(WebappProps.getImageTypeKey());
		if (it == null) {
			res.sendError(404);
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		// Validate image type
		ImageType imageType = null;
		try {
			imageType = ImageType.valueOf(it.toUpperCase());
		} catch (Exception e) {
			res.sendError(404);
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		if (imageType == null) {
			res.sendError(404);
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		// Find the image
		Image image = imageService.findById(imageId);
		if (image == null) {
			return new ResponseEntity(HttpStatus.OK);
		}

		String imagePath = WebappProps.getImageFileDir()
				+ image.getLocalFileName();
		String thumbnailPath = WebappProps.getThumbnailFileDir()
				+ image.getLocalFileName();


		try {
			// Delete image data from database.
			imageService.deleteImage(image);

			// Delete actual image file
			File imageFile = new File(imagePath);
			if (imageFile.isFile()) {
				try {
					imageFile.delete();
				} catch (Exception e) {
				}
			}

			// Delete actual thumnail image file
			File thumbnailFile = new File(thumbnailPath);
			if (thumbnailFile.isFile()) {
				try {
					thumbnailFile.delete();
				} catch (Exception e) {
				}
			}

			logger.info("DELETE /api/images/" + imageId + ", success");
			return new ResponseEntity(HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Get image by id
	@RequestMapping(value = ApiPath.IMAGES, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity getImageById(@PathVariable("imageId") int imageId,
			HttpServletResponse response) throws IOException {

		logger.info("GET /api/images/" + imageId);

		Image image = imageService.findById(imageId);

		if (image == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		String imageDir = WebappProps.getImageFileDir();
		String imagePath = imageDir + image.getLocalFileName();

		InputStream is = new FileInputStream(imagePath);
		org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
		response.flushBuffer();
		
		return new ResponseEntity(HttpStatus.OK);
	}

	// Get images list by teamId
	@RequestMapping(value = ApiPath.SLIDE_SHOW, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<SimpleResult> getSlideShow(
			@PathVariable("teamId") int teamId) {

		logger.info("GET /api/images/slideshow/teamId/" + teamId);

		List<Image> images = imageService.findAllSlideShowByTeam(teamId);
		SimpleResult result = SimpleResult.generateResult(images);

		return new ResponseEntity<SimpleResult>(result, HttpStatus.OK);
	}

	// Upload new slideshow image
	@RequestMapping(value = ApiPath.SLIDE_SHOW_UPLOAD, method = RequestMethod.POST, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<String> uploadSlideShow(
			@PathVariable("teamId") int teamId,
			@RequestParam("file") MultipartFile file)
			throws JsonGenerationException, JsonMappingException, IOException {

		logger.info("POST /api/upload/slideshow/teamId/" + teamId);

		if (!file.isEmpty()) {
			try {
				String imageDir = WebappProps.getImageFileDir();

				String originalFileName = file.getOriginalFilename();
				String contentType = file.getContentType();
				String localFileName = ImageUtil.getFileName(originalFileName);

				byte[] bytes = file.getBytes();

				// Save image
				File f = new File(imageDir + localFileName);
				FileOutputStream fos = new FileOutputStream(f);
				BufferedOutputStream stream = new BufferedOutputStream(fos);
				stream.write(bytes);
				stream.close();
				fos.close();

				// Save thumbnail
				ImageUtil.saveScaledImage(f);

				// Save image data to database
				Image image = new Image();
				image.setTeam(teamService.findById(teamId));
				image.setFileType(contentType);
				image.setLocalFileName(localFileName);
				image.setOutputFileName(originalFileName);
				image.setImageType(ImageUtil.ImageType.SLIDESHOW.toString());
				image.setPosition(0);
				imageService.saveImage(image);

			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<String>(
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
