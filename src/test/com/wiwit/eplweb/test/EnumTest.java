package com.wiwit.eplweb.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wiwit.eplweb.util.PathPattern;

public class EnumTest {

	public static void main(String[] args) {
//		System.out.println(a.getRequestMapping());
//		System.out.println(a.getRequestPattern());
//		System.out.println(a.isSecuredPath());

		String s = "/api/admin/login/ec18298edf4b4b84b51ca8bb7c1f1ecf";
		String p = PathPattern.ADMIN_SESSION.getRequestPattern();
//		System.out.println(s.matches(p));
//		
//		s = "/api/admin/login";
//		p = PathPattern.ADMIN_LOGIN.getRequestPattern();
//		System.out.println(s.matches(p));
//		
//		s = "/api/chart/week/33/team/33";
//		p = PathPattern.CHART_TEAM_STAT.getRequestPattern();
//		System.out.println(s.matches(p));
//		
//		s = "/api/page/team/33/sfasdfde3";
//		p = PathPattern.INIT_TEAM_PAGE.getRequestPattern();
//		System.out.println(s.matches(p));
		
//		s = "/app/html/adminmenu.html";
//		p = PathPattern.STATIC_FILES.getRequestPattern();
//		System.out.println(s.matches(p));
//		
//		s = "/app/html/adminmenu.js";
//		p = PathPattern.STATIC_FILES.getRequestPattern();
//		System.out.println(s.matches(p));
//		
		s = "/bower_components/bootstrap/dist/fonts/glyphicons-halflings-regular.woff2";
		p = "/bower_components/.+";
		System.out.println(s.matches(p));
		
		
		s = "/sdf/sdf/.-sdf.pdf";
		p = "^.*\\.(jpg|JPG|gif|GIF|doc|DOC|pdf|PDF)$";
		System.out.println(s.matches(p));
	}
}
