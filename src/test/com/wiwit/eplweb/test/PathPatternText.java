package com.wiwit.eplweb.test;

import com.wiwit.eplweb.util.PathPattern;
import com.wiwit.eplweb.util.PathPatternUtil;

public class PathPatternText {

	public static void main(String[] sdf) {
		for (PathPattern p : PathPatternUtil.getAllPath()) {
			if (p.isSecuredPath()) {
				System.out.println("");
				System.out.println(p.getRequestPattern());
				for(String m : p.getMethods()){
					System.out.println(m);
				}
			}
		}
	}
}
