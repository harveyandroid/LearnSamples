package com.harvey.algorithm;

import java.util.Arrays;

/**
 * Created by hanhui on 2018/3/16 0016 11:46
 */

public class ReviewTest {

    public static void main(String[] args) throws NoSuchMethodException {

        int[] data = {9, 4, 8, 2, 5, 1};
        System.out.println(Arrays.toString(data));
        System.out.println(Arrays.toString(insetSort(data)));
    }

    public static int[] insetSort(int[] a) {
        int size = a.length;
        for (int i = 1; i < size; i++) {
            int temp = a[i];
            int j = i - 1;
            for (; j >= 0; j--) {
                if (a[j] > temp) {
                    a[j + 1] = a[j];
                } else {
                    break;
                }
            }
            a[j + 1] = temp;
        }
        return a;
    }
}
