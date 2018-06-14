package com.harvey.collection;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hanhui on 2018/6/8 0008 17:56
 */

public class MapTest {
	public static void main(String[] args) {
		Hashtable hashtable=new Hashtable();
		HashMap<Integer, String> map = new HashMap();
		for (int i = 100; i < 120; i++)
			map.put(i, "value");
		Iterator<Map.Entry<Integer, String>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, String> entry = iterator.next();
			if (entry.getKey() % 2 == 0) {
				 iterator.remove();
//				map.remove(entry.getKey());//在迭代器创建之后，不能进行修改
			}
		}
		System.out.println(map.toString());
		LinkedHashMap linkedHashMap=new LinkedHashMap();

		TreeMap treeMap=new TreeMap();
		//HashMap 同步代替类
		ConcurrentHashMap concurrentHashMap=new ConcurrentHashMap();
		Map<Integer, String> sysMap=new HashMap();
		Map<Integer, String> sysHashMap= Collections.synchronizedMap(sysMap);
	}
}
