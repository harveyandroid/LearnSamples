package com.harvey.myapplication;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    //题目：古典问题：有一对兔子，从出生后第3个月起每个月都生一对兔子，小兔子长到第3个月后每个月又生一对兔子，假如兔子都不死，问每个月的兔子总数为多少？
    public int calculateRabbit(int month) {
        int sumPair = 0;
        if (month == 1 || month == 2) {
            sumPair = 1;
        } else {
            sumPair = calculateRabbit(month - 1) + calculateRabbit(month - 2);
        }
        return sumPair;
    }

    @Test
    public void testRabbit() {
        for (int i = 1; i < 13; i++) {
            System.out.println(calculateRabbit(i));
        }
    }

    //  题目：判断101-200之间有多少个素数，并输出所有素数。
    //1.程序分析：判断素数的方法：用一个数分别去除2到本身(这个数)，如果能被整除，则表明此数不是素数，反之是素数。
    @Test
    public void testPrimeNumber() {
        int sumPrime = 0;
        for (int i = 101; i < 200; i++) {
            boolean isPrime = true;
            for (int j = 2; j < i; j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                sumPrime++;
                System.out.println("素数：" + i);
            }
        }
        System.out.println("素数数量：" + sumPrime);
    }

    public int findPrimeNumber(int num) {
        return 0;
    }
}