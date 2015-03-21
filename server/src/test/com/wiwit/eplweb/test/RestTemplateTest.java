package com.wiwit.eplweb.test;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.web.client.RestTemplate;

public class RestTemplateTest {

	@JsonIgnoreProperties(ignoreUnknown = true)
	private class Page {

		private String name;
		private String about;
		private String phone;
		private String website;

		public String getName() {
			return name;
		}

		public String getAbout() {
			return about;
		}

		public String getPhone() {
			return phone;
		}

		public String getWebsite() {
			return website;
		}
	}

	public static void main(String args[]) {
		RestTemplate restTemplate = new RestTemplate();
		Page page = restTemplate.getForObject(
				"http://graph.facebook.com/pivotalsoftware", Page.class);
		System.out.println("Name:    " + page.getName());
		System.out.println("About:   " + page.getAbout());
		System.out.println("Phone:   " + page.getPhone());
		System.out.println("Website: " + page.getWebsite());
	}
}
