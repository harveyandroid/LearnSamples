package com.harvey.collection;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Collection<--List<--Vector
 * Collection<--List<--ArrayList
 * Collection<--List<--LinkedList
 * <p>
 * Created by hanhui on 2017/10/9 0009 10:18
 */

public class SetTest {

    public static void main(String[] args) {
        HashSet hashSet = new HashSet();
        hashSet.add(1);
        hashSet.add(2);
        hashSet.add(3);
        hashSet.add(4);
        hashSet.add(5);
        hashSet.add(1);
        hashSet.add(2);
        hashSet.remove(1);
        System.out.println("hashSet:" + hashSet);
        LinkedHashSet linkedHashSet=new LinkedHashSet();
    }
}
