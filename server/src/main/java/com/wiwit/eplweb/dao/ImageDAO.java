package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Image;
import com.wiwit.eplweb.util.ImageUtil.ImageType;

@Service
public class ImageDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(ImageDAO.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public void saveImage(Image image) {
		Session session = this.sessionFactory.openSession();
		session.persist(image);
		session.close();
	}

	@Transactional
	public List<Image> findAllByTeamId(int teamId, ImageType imageType) {
		Session session = this.sessionFactory.openSession();
		List<Image> result = session.createQuery(
				"from Image where team.id=" + teamId + " and imageType='"
						+ imageType.toString() + "' order by position asc").list();

		session.close();
		if (result == null || result.size() == 0) {
			logger.info("Can't find slide show with teamId=" + teamId);
			return null;
		}
		logger.info("Image slide show size=" + result.size());
		return result;
	}

	@Transactional
	public Image findById(int id) {
		Session session = this.sessionFactory.openSession();
		List<Image> result = session.createQuery("from Image where id=" + id)
				.list();

		session.close();
		if (result == null || result.size() == 0) {
			logger.info("Can't find slide show with id=" + id);
			return null;
		}
		logger.info("Image loaded successfully, image id=" + id);
		return result.get(0);
	}

	@Transactional
	public void deleteImage(Image image) {
		Session session = this.sessionFactory.openSession();
		session.delete(image);
		session.close();
	}

	@Transactional
	public void updateMore(List<Image> images) {
		Session session = this.sessionFactory.openSession();
		for (Image i : images) {
			session.update(i);
		}
		session.close();
	}
}
