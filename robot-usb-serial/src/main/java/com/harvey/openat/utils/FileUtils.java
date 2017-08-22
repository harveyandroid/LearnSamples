package com.harvey.openat.utils;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 名称：FileUtils.java 描述：文件操作工具类
 */
public class FileUtils {

	/**
	 * 向文件中写入数据
	 *
	 * @param filePath
	 *            目标文件全路径
	 * @return true表示写入成功 false表示写入失败
	 */
	public static boolean writeString(String filePath, String str) {
		try {
			FileWriter writer = new FileWriter(filePath);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			bufferedWriter.write(str);
			bufferedWriter.flush();
			writer.close();
			bufferedWriter.close();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * 从文件中读取数据
	 *
	 * @param file
	 * @return
	 */
	public static String readFistLine(String file) {
		try {
			FileReader inReader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(inReader);
			String lineContent;
			while ((lineContent = bufReader.readLine()) != null) {
				if (!TextUtils.isEmpty(lineContent)) {
					return lineContent;
				}
			}
			bufReader.close();
			inReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 从文件中读取数据，返回类型是字符串String类型
	 *
	 * @param file
	 *            文件路径
	 * @return
	 */
	public static String readString(String file) {
		String ret = readFistLine(file);
		System.out.println("读取的字符串" + ret);
		return ret;
	}
	/**
	 * 从文件中读取数据，返回类型是字符串String类型
	 *
	 * @param file
	 *            文件路径
	 * @return
	 */
	public static int readInt(String file) {
		String ret = readString(file);
		if (!TextUtils.isEmpty(ret)) {
			return Integer.parseInt(ret);
		}
		return -1;
	}
}
