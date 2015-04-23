package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Image;
import com.wiwit.eplweb.util.ImageUtil.ImageType;

@Service
public class ImageDAO extends AbstractDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(ImageDAO.class);

	@Transactional
	public void saveImage(Image image) {
		openSession();
		
		getSession().persist(image);
		
		commitAndClose();
	}

	@Transactional
	public List<Image> findAllByTeamId(int teamId, ImageType imageType) {
		openSession();
		List<Image> result = getSession().createQuery(
				"from Image where team.id=" + teamId + " and imageType='"
						+ imageType.toString() + "' order by position asc").list();

		commitAndClose();
		if (result == null || result.size() == 0) {
			logger.info("Can't find slide show with teamId=" + teamId);
			return null;
		}
		logger.info("Image slide show size=" + result.size());
		return result;
	}

	@Transactional
	public Image findById(int id) {
		openSession();
		List<Image> result = getSession().createQuery("from Image where id=" + id)
				.list();

		commitAndClose();
		if (result == null || result.size() == 0) {
			logger.info("Can't find slide show with id=" + id);
			return null;
		}
		logger.info("Image loaded successfully, image id=" + id);
		return result.get(0);
	}

	@Transactional
	public void deleteImage(Image image) {
		openSession();
		getSession().delete(image);
		commitAndClose();
	}

	@Transactional
	public void updateMore(List<Image> images) {
		openSession();
		for (Image i : images) {
			getSession().update(i);
		}
		commitAndClose();
	}
}
