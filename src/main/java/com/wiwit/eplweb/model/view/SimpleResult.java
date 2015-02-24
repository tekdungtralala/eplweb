package com.wiwit.eplweb.model.view;

public class SimpleResult {

	private Object result;
	
	public SimpleResult(Object o) {
		this.result = o;
	}
	
	public static SimpleResult generateResult(Object o){
		return new SimpleResult(o);
	}

	public Object getResult() {
		return result;
	}
}
