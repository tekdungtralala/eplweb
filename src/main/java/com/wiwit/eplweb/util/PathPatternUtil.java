package com.wiwit.eplweb.util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PathPatternUtil {
	public static List<PathPattern> getAllPath() {
		return new ArrayList<PathPattern>(EnumSet.allOf(PathPattern.class));
	}

	public static List<PathPattern> getSecuredPath() {
		List<PathPattern> all = getAllPath();

		List<PathPattern> result = new ArrayList<PathPattern>();
		for (PathPattern p : all) {
			if (p.isSecuredPath())
				result.add(p);
		}

		return result;
	}

	public static boolean isSecuredPath(String path) {
		for (PathPattern p : getSecuredPath()) {
			if (path.matches(p.getRequestPattern())) {
				return true;
			}
		}
		return false;
	}

	public static PathPattern getPathPattern(String path) {
		for (PathPattern p : getAllPath()) {
			if (path.matches(p.getRequestPattern())) {
				return p;
			}
		}
		return null;
	}
}
