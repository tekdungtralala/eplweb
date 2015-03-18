package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wiwit.eplweb.dao.ImageDAO;
import com.wiwit.eplweb.model.Image;
import com.wiwit.eplweb.util.ImageUtil.ImageType;

@Component
public class ImageService {

	@Autowired
	private ImageDAO imageDAO;

	public void saveImage(Image image) {
		imageDAO.saveImage(image);
	}

	public List<Image> findAllSlideShowByTeam(int teamId) {
		return imageDAO.findAllByTeamId(teamId, ImageType.SLIDESHOW);
	}

	public Image findById(int id) {
		return imageDAO.findById(id);
	}
	
	public void deleteImage(Image image) {
		imageDAO.deleteImage(image);
	}
}
