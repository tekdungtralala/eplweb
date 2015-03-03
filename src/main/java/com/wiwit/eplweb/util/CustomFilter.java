package com.wiwit.eplweb.util;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class CustomFilter implements Filter {

	private static final Logger logger = LoggerFactory
			.getLogger(CustomFilter.class);

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest rq, ServletResponse rs,
			FilterChain chain) throws IOException, ServletException {
		// String url = ((HttpServletRequest) req).getRequestURL().toString();
		// String queryString = ((HttpServletRequest) req).getQueryString();
		
		HttpServletRequest req = (HttpServletRequest) rq;
		
		String path = req.getServletPath();
		String method = req.getMethod();

		PathPattern p = PathPatternUtil.getPathPattern(path);
		if (p != null) {
			if (p.isSecuredPath() && p.getMethods().contains(method)) {
				logger.info(method + " SECURED : " + path);

				Resource resource = new ClassPathResource("webapp.properties");
				Properties props = PropertiesLoaderUtils
						.loadProperties(resource);

				 String authKey = props.getProperty(WebappProps.ADMIN_SESSION_KEY.toString());
				 String authVal = req.getHeader(authKey);
				 
				 logger.info(method + " authKey : " + authKey);
				 logger.info(method + " authVal : " + authVal);
			}
		} else {
			logger.info(method + " FAIL : " + path);
		}

		chain.doFilter(rq, rs);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
