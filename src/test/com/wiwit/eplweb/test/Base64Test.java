package com.wiwit.eplweb.test;

import org.apache.commons.codec.binary.Base64;

public class Base64Test {

	public static void main(String [] args){
		String adminEmail = "admin@eplweb.com";
		byte[] encodedBytes = Base64.encodeBase64(adminEmail.getBytes());
		System.out.println("encodedBytes " + new String(encodedBytes));
		byte[] decodedBytes = Base64.decodeBase64(encodedBytes);
		System.out.println("decodedBytes " + new String(decodedBytes));
		
		
		String pass64 = "cGFzc3dvcmQ=";
		System.out.println("pass64 " + pass64);
		decodedBytes = Base64.decodeBase64(pass64);
		System.out.println("pass64 " + new String(decodedBytes));
	}
}
