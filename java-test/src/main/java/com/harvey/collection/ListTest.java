package com.harvey.collection;

import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

/**
 * Collection<--List<--Vector
 * Collection<--List<--ArrayList
 * Collection<--List<--LinkedList
 * <p>
 * Created by hanhui on 2017/10/9 0009 10:18
 */

public class ListTest {

    public static void main(String[] args) {
        Vector vector = new Vector();
        vector.add(1);
        ArrayList list = new ArrayList();
        list.add(1);
    }
}
