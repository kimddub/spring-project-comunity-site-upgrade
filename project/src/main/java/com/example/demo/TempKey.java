package com.example.demo;

import java.util.Random;

public class TempKey {
	private int length;
	
	public TempKey() {
		length = 30;
	}
	
	public TempKey(int length) {
		this.length = length;
	}
	
	public String getKey() {
		return makeKey();
	}
	
	private String makeKey() {
		StringBuffer buffer = new StringBuffer();
		
		while (buffer.length() < length) {
			Random random = new Random();
			int key = random.nextInt(75) + 48;
			// 숫자 대문자 영문자 조합
			if ( (key >= 48 && 57 >= key) || (key >= 65 && 90 >= key) || (key >= 97 && 122 >= key)) {
				buffer.append((char)key);
			}
		}
		
		return buffer.toString();
	}
}
