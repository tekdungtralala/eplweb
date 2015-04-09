package com.wiwit.eplweb.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;

import com.wiwit.eplweb.util.PathPattern;
import com.wiwit.eplweb.util.PathPatternUtil;
import com.wiwit.eplweb.util.WebappProps;

public class CustomFilter implements Filter {

	private static final Logger logger = LoggerFactory
			.getLogger(CustomFilter.class);
	
	public static final String SESSION_ID = "sessionID";

	private SessionService service;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest rq, ServletResponse rs,
			FilterChain chain) throws IOException, ServletException {
		// String url = ((HttpServletRequest) req).getRequestURL().toString();
		// String queryString = ((HttpServletRequest) req).getQueryString();

		HttpServletRequest req = (HttpServletRequest) rq;
		HttpServletResponse res = (HttpServletResponse) rs;

		String path = req.getServletPath();
		String method = req.getMethod();

		PathPattern p = PathPatternUtil.getPathPattern(path, method);
		if (p != null) {
			if (p.isSecuredPath()) {
				logger.info(method + " SECURED : " + path);

				String authKey = WebappProps.getAdminSessionKey();
				String authVal = req.getHeader(authKey);

				if (authVal == null) {
					authVal = req.getParameter(authKey);
				}

				Session s = service.findBySession(authVal);
				if ( s != null) {
					if (s.getRole() == p.getRole()) {
						req.setAttribute(SESSION_ID, s.getId());
						chain.doFilter(rq, rs);
					} else {
						res.setStatus(HttpStatus.FORBIDDEN.value());
					}
				} else {
					res.setStatus(HttpStatus.FORBIDDEN.value());
				}
			} else {
				logger.info(method + " NOT SECURED : " + path);
				chain.doFilter(rq, rs);
			}
		} else {
			res.setStatus(HttpStatus.NOT_FOUND.value());
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("Init filter");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"filter-context.xml");
		service = context.getBean(SessionService.class);
	}

}
