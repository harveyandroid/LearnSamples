package com.harvey.algorithm;

import java.util.Arrays;

/**
 * Created by hanhui on 2018/3/16 0016 11:01
 */

public class AlgorithmTest {
	public static void main(String[] args) throws NoSuchMethodException {

		// int[] data = {9, 4, 8, 2, 5, 1};
		// System.out.println(Arrays.toString(data));
		// System.out.println(Arrays.toString(InsertSort(data)));
		// selectSort(data);
		// bubbleSort(data);
		int[] data = {1, 2, 3, 4, 5, 6};
		System.out.println(searchRecursive(data, 0, data.length - 1, 2));
		System.out.println(searchRecursive(data, 2));

	}

	/**
	 * 二分查找 算法思想：又叫折半查找，要求待查找的序列有序(升序)。 每次取中间位置的值与待查关键字比较，如果中间位置的值比待查关键字大，
	 * 则在前半部分循环这个查找的过程，如果中间位置的值比待查关键字小， 则在后半部分循环这个查找的过程。直到查找到了为止，否则序列中没有待查的关键字。
	 * 
	 * @param array
	 * @param start
	 * @param end
	 * @param findValue
	 * @return
	 */
	private static int searchRecursive(int[] array, int start, int end, int findValue) {
		System.out.println("start=" + start + ",end=" + end);
		if (array == null)
			return -1;
		else if (start > end) {
			return -1;
		} else {
			int middle = (start + end) / 2;
			int middleValue = array[middle];
			if (findValue == middleValue) {
				return middle;
			} else if (findValue > middleValue) {
				return searchRecursive(array, middle + 1, end, findValue);
			} else {
				return searchRecursive(array, start, middle - 1, findValue);
			}
		}
	}

	private static int searchRecursive(int[] array, int findValue) {
		int start = 0;
		int end = array.length - 1;
		while (start <= end) {
			int middle = (start + end) / 2;
			int middleValue = array[middle];
			if (findValue == middleValue) {
				return middle;
			} else if (findValue > middleValue) {
				start = middle + 1;
			} else {
				end = middle - 1;
			}
		}
		return -1;
	}

	/**
	 * 插入排序(Insertion Sort)的算法描述是一种简单直观的排序算法。它的工作原理是通过构建有序序列，
	 * 对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入。插入排序在实现上，
	 * 通常采用in-place排序(即只需用到O(1)的额外空间的排序)，因而在从后向前扫描过程中，
	 * 需要反复把已排序元素逐步向后挪位，为最新元素提供插入空间。
	 * <p>
	 * 1.从第一个元素开始，该元素可以认为已经被排序； 2.取出下一个元素，在已经排序的元素序列中从后向前扫描；
	 * 3.如果该元素(已排序)大于新元素，将该元素移到下一位置； 4.重复步骤3，直到找到已排序的元素小于或者等于新元素的位置；
	 * 5.将新元素插入到该位置中，重复步骤2。 ，插入排序的时间复杂度和空间复杂度分别为 O(n2 ) 和 O(1)。
	 * </p>
	 *
	 * @param a
	 */
	private static int[] InsertSort(int[] a) {
		long t1 = System.nanoTime();
		// 直接插入排序
		for (int i = 1; i < a.length; i++) {
			// 待插入元素
			int temp = a[i];
			int j;
			for (j = i - 1; j >= 0; j--) {
				// 将大于temp的往后移动一位
				if (a[j] > temp) {
					a[j + 1] = a[j];
				} else {
					break;
				}
			}
			a[j + 1] = temp;// 插入进来
		}
		System.out.println(System.nanoTime() - t1);
		return a;
	}

	/**
	 * 选择排序的基本思想是遍历数组的过程中，以 i 代表当前需要排序的序号， 则需要在剩余的 [i…n-1] 中找出其中的最小值，然后将找到的最小值与
	 * i 指向的值进行交换。 因为每一趟确定元素的过程中都会有一个选择最大值的子流程， 所以人们形象地称之为选择排序。
	 * 选择排序的时间复杂度和空间复杂度分别为 O(n2 ) 和 O(1) 。
	 *
	 * @param a
	 */
	private static void selectSort(int[] a) {
		int size = a.length;
		int temp;
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				if (a[j] > a[i]) {
					temp = a[i];
					a[i] = a[j];
					a[j] = temp;
				}
			}
		}
		System.out.println(Arrays.toString(a));
	}

	/**
	 * 冒泡排序(Bubble Sort) 它重复地走访过要排序的数列，一次比较两个元素， 如果他们的顺序错误就把他们交换过来。
	 * 走访数列的工作是重复地进行直到没有再需要交换， 也就是说该数列已经排序完成。 这个算法的名字由来是因为越小的元素会经由交换慢慢“浮”到数列的顶端。
	 *
	 * @param a
	 */
	private static void bubbleSort(int[] a) {
		int size = a.length;
		for (int i = 0; i < size - 1; i++) {
			for (int j = 1; j < size - i; j++) {
				if (a[j - 1] > a[j]) {
					int temp = a[j];
					a[j] = a[j - 1];
					a[j - 1] = temp;
				}
			}
		}
		System.out.println(Arrays.toString(a));
	}
}
