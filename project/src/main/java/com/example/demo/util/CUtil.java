package com.example.demo.util;

import java.math.BigInteger;

// C(common) Util
// 일반적으로 많이 쓰이는 유틸
public class CUtil {

	public static long getAsLong(Object num) {
		if (num instanceof Long) {
			return (long) num;
		} else if (num instanceof Integer) {
			return (long) num;
		} else if (num instanceof BigInteger) {
			return ((BigInteger) num).longValue();
		}

		return 0;
	}

}