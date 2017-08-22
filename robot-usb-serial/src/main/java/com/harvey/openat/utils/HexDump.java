/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.harvey.openat.utils;

/**
 * Clone of Android's HexDump class, for use in debugging. Cosmetic changes
 * only.
 */
public class HexDump {

	public static String toHexString(byte b) {
		return toHexString(toByteArray(b));
	}

	public static String toHexString(byte[] array) {
		return toHexString(array, 0, array.length);
	}
	public static String toHexString(byte[] array, int length) {
		return toHexString(array, 0, length);
	}
	// 转换为16进制字符串中间没有空格换行
	public static String toHexString(byte[] array, int offset, int length) {
		StringBuffer sb = new StringBuffer(length);
		String sTemp;

		for (int i = 0; i < offset + length; i++) {
			sTemp = Integer.toHexString(0xFF & array[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	public static String toHexString(int i) {
		return toHexString(toByteArray(i));
	}

	public static String toHexString(short i) {
		return toHexString(toByteArray(i));
	}

	public static byte[] toByteArray(byte b) {
		byte[] array = new byte[1];
		array[0] = b;
		return array;
	}

	public static byte[] toByteArray(int i) {
		byte[] array = new byte[4];

		array[3] = (byte) (i & 0xFF);
		array[2] = (byte) ((i >> 8) & 0xFF);
		array[1] = (byte) ((i >> 16) & 0xFF);
		array[0] = (byte) ((i >> 24) & 0xFF);

		return array;
	}

	public static byte[] toByteArray(short i) {
		byte[] array = new byte[2];

		array[1] = (byte) (i & 0xFF);
		array[0] = (byte) ((i >> 8) & 0xFF);

		return array;
	}

	private static int toByte(char c) {
		if (c >= '0' && c <= '9')
			return (c - '0');
		if (c >= 'A' && c <= 'F')
			return (c - 'A' + 10);
		if (c >= 'a' && c <= 'f')
			return (c - 'a' + 10);

		throw new RuntimeException("Invalid hex char '" + c + "'");
	}

	/**
	 * float *65536转成4位字节数组
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] floatToByteArray(float data) {
		float fl = data * 65536;
		long l = (long) fl;
		byte[] byteArray = hexStringToByteArray(Long.toHexString(l));
		int length = byteArray.length;
		byte[] newArray = new byte[4];
		if (length > 4) {
			System.arraycopy(byteArray, length - 4, newArray, 0, 4);
		} else {
			System.arraycopy(byteArray, 0, newArray, 4 - length, length);
		}
		return newArray;
	}

	public static float byteArrayToFloat(byte[] array) {
		long l = Long.parseLong(HexDump.toHexString(array), 16);
		float f = l * 1.0f / 65536;
		return f;
	}

	/**
	 * Convert hex string to byte[]
	 *
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToByteArray(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		if (hexString.length() % 2 != 0) {
			hexString = "0" + hexString;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (toByte(hexChars[pos]) << 4 | toByte(hexChars[pos + 1]));
		}
		return d;
	}
	// 求校验和原码
	public static byte getChecksum(byte[] source, int length) {
		int sum = 0;
		// 求和
		for (int i = 0; i < length; i++) {
			sum += source[i] & 0xff;
		}
		// 取低8位
		byte checksum = (byte) (sum & 0xff);
		return checksum;
	}

	// 求校验和反码
	public static byte getChecksum_counter(byte[] source, int length) {
		// 校验和原码
		int checksum = getChecksum(source, length);
		byte checksum_counter = (byte) (~checksum & 0xff);
		return checksum_counter;
	}

}
