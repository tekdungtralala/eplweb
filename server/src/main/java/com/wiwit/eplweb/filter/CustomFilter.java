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

// Every http request will be through Filter class.
// Filter class will determine if the request is secure path or unsecure path.
// For secure path must has session key, and with that key
//  will be defined a role which key has.
// Session key placed on header or query param.

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

		Session s = null;
		String path = req.getServletPath();
		String method = req.getMethod();
		
		String authKey = WebappProps.getAdminSessionKey();
		// Get auth value from header (session key)
		String authVal = req.getHeader(authKey);
		if (authVal == null) {
			// If not in header, maybe auth value placed in query paramters 
			authVal = req.getParameter(authKey);
		}
		
		if (authVal != null) {
			s = service.findBySession(authVal);
		}

		PathPattern p = PathPatternUtil.getPathPattern(path, method);
		if (p != null) {
			// Known the path
			if (p.isSecuredPath()) {
				logger.info(method + " SECURED : " + path);

				if (s != null) {
					// Validate the role with path role
					if (s.getRole() == p.getRole()) {
						req.setAttribute(SESSION_ID, s.getId());
						chain.doFilter(rq, rs);
					} else {
						// Return 403 when the role is not match
						res.setStatus(HttpStatus.FORBIDDEN.value());
					}
				} else {
					// Return 403 when try secure path without auth val (session key)
					res.setStatus(HttpStatus.FORBIDDEN.value());
				}
			} else {
				logger.info(method + " NOT SECURED : " + path);
				if (s != null) {
					req.setAttribute(SESSION_ID, s.getId());
				}
				chain.doFilter(rq, rs);
			}
		} else {
			// Return 404 while the path is not available
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
