package com.wiwit.eplweb.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wiwit.eplweb.model.Team;
import com.wiwit.eplweb.service.TeamService;
import com.wiwit.eplweb.util.ApiPath;
import com.wiwit.eplweb.util.youtube.api.VideoService;


// Any video which uploaded to our server will be forwarded to youtube.com
@RestController
public class VideoController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(VideoController.class);
	
	@Autowired
	private TeamService teamService;
	
	// Post video thumnail
	@RequestMapping(value = ApiPath.CHANGE_VIDEO_THUMBNAIL, method = RequestMethod.POST, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<String> changeThumbnail(
			@PathVariable("teamId") int teamId,
			@RequestParam("file") MultipartFile file)
			throws JsonGenerationException, JsonMappingException, IOException {
		
		logger.info("POST /api/upload/videothumbnail/teamId/" + teamId);
		
		Team team = teamService.findById(teamId);
		if (team == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				
				// do change thumbnail
				logger.info("doChangeThumbnail");
				VideoService.doChangeThumbnail(team.getVideoId(), new ByteArrayInputStream(bytes));
				
				return new ResponseEntity<String>(HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	
	// Submit a new video
	@RequestMapping(value = ApiPath.UPLOAD_VIDEO, method = RequestMethod.POST, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<String> uploadVideo(
			@PathVariable("teamId") int teamId,
			@RequestParam("file") MultipartFile file)
			throws JsonGenerationException, JsonMappingException, IOException {
		
		logger.info("POST /api/upload/video/teamId/" + teamId);
		
		Team team = teamService.findById(teamId);
		if (team == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				
				// Delete the latest video before upload the new one
				VideoService.doDelete(team.getVideoId());
				
				// Upload video and get videoId from youtube
				logger.info("doUploadVideo");
				String videoId = null; 
				videoId = VideoService.doUpload(team.getName(), new ByteArrayInputStream(bytes));
				
				// Save videoId to db
				team.setVideoId(videoId);
				teamService.updateTeam(teamId, team);
				
				return new ResponseEntity<String>(videoId, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
}
