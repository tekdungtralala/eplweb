package com.wiwit.eplweb.util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PathPatternUtil {
	// Get all path
	public static List<PathPattern> getAllPath() {
		return new ArrayList<PathPattern>(EnumSet.allOf(PathPattern.class));
	}

	// Get all secured path
	public static List<PathPattern> getSecuredPath() {
		List<PathPattern> all = getAllPath();

		List<PathPattern> result = new ArrayList<PathPattern>();
		for (PathPattern p : all) {
			if (p.isSecuredPath())
				result.add(p);
		}

		return result;
	}

	// Chek is secured path or not
	public static boolean isSecuredPath(String path) {
		for (PathPattern p : getSecuredPath()) {
			if (path.matches(p.getRequestPattern())) {
				return true;
			}
		}
		return false;
	}

	// Get pathpattern by match path and available method
	public static PathPattern getPathPattern(String path, String method) {
		for (PathPattern p : getAllPath()) {
			if (path.matches(p.getRequestPattern()) && p.getMethods().contains(method)) {
				return p;
			}
		}
		return null;
	}
}
