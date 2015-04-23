package com.wiwit.eplweb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.model.Image;
import com.wiwit.eplweb.model.Team;
import com.wiwit.eplweb.service.ImageService;
import com.wiwit.eplweb.service.TeamService;

public class ImageTest {

	public static void test1(ClassPathXmlApplicationContext context) {
		TeamService ts = context.getBean(TeamService.class);
		ImageService is = context.getBean(ImageService.class);

		Team t = ts.findById(15);
		System.out.println(t.getName());
		
		Image i = new Image();
		i.setFileType("asfd");
		i.setImageType("eee");
		i.setLocalFileName("loc");
		i.setOutputFileName("out");
		i.setTeam(t);
		is.saveImage(i);
		context.close();
		
	}

	public static void main(String[] asfd) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");
		test1(context);
	}

}
