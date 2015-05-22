package com.wiwit.eplweb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wiwit.eplweb.dao.ImageDAO;
import com.wiwit.eplweb.model.Image;
import com.wiwit.eplweb.model.input.SortedImageModelInput;
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

	public void saveImageOrder(List<SortedImageModelInput> sortedImages) {
		List<Image> images = new ArrayList<Image>();
		for (SortedImageModelInput m : sortedImages) {
			// Find image
			Image i = imageDAO.findById(m.getId());
			if (i != null) {
				// Set new position
				i.setPosition(m.getPosition());
				images.add(i);
			}
		}
		// Save new images data
		imageDAO.updateMore(images);
	}
}
