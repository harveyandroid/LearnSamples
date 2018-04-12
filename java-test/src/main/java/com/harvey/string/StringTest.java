package com.harvey.string;


public class StringTest {
    private static int time = 500000;

    public static void main(String[] args) {
        System.out.println(test1());
        System.out.println(test2());
        System.out.println(test3());
        System.out.println(test4());
        System.out.println(test5());
        System.out.println(test6());
        System.out.println(test7());
        System.out.println(test8());
        testString();
        testStringBuffer();
        testStringBuilder();
        test1String();
        test2String();
    }

    public static void testString() {
        String s = "";
        long begin = System.currentTimeMillis();
        for (int i = 0; i < time; i++) {
            s += "java";
        }
        long over = System.currentTimeMillis();
        System.out.println("操作" + s.getClass().getName() + "类型使用的时间为：" + (over - begin) + "毫秒");
    }

    public static void testStringBuffer() {
        StringBuffer sb = new StringBuffer();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < time; i++) {
            sb.append("java");
        }
        long over = System.currentTimeMillis();
        System.out.println("操作" + sb.getClass().getName() + "类型使用的时间为：" + (over - begin) + "毫秒");
    }

    public static void testStringBuilder() {
        StringBuilder sb = new StringBuilder();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < time; i++) {
            sb.append("java");
        }
        long over = System.currentTimeMillis();
        System.out.println("操作" + sb.getClass().getName() + "类型使用的时间为：" + (over - begin) + "毫秒");
    }

    public static void test1String() {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < time; i++) {
            String s = "I" + "love" + "java";
        }
        long over = System.currentTimeMillis();
        System.out.println("字符串直接相加操作：" + (over - begin) + "毫秒");
    }

    public static void test2String() {
        String s1 = "I";
        String s2 = "love";
        String s3 = "java";
        long begin = System.currentTimeMillis();
        for (int i = 0; i < time; i++) {
            String s = s1 + s2 + s3;
        }
        long over = System.currentTimeMillis();
        System.out.println("字符串间接相加操作：" + (over - begin) + "毫秒");
    }

    /**
     * 一个指向字符串常量池，一个指向new出来的堆内存块，new的字符串在编译期是无法确定的。所以输出false
     *
     * @return false
     */
    public static boolean test1() {
        String ok = "ok";
        String ok1 = new String("ok");
        return ok == ok1;
    }

    /**
     * 编译期ok和ok1都是确定的，字符串都为apple1，所以ok和ok1都指向字符串常量池里的字符串apple1。指向同一个对象，所以为true.
     *
     * @return true
     */
    public static boolean test2() {
        String ok = "apple1";
        String ok1 = "apple" + 1;
        return ok == ok1;
    }

    /**
     * ok和ok1能否在编译期确定，ok是确定的，放进并指向常量池，而ok1含有变量导致不确定，所以不是同一个对象.输出false
     *
     * @return false
     */
    public static boolean test3() {
        String ok = "apple1";
        int temp = 1;
        String ok1 = "apple" + temp;
        return ok == ok1;
    }

    /**
     * ok确定，加上final后使得ok1也在编译期能确定，所以输出true
     *
     * @return true
     */
    public static boolean test4() {
        String ok = "apple1";
        final int temp = 1;
        String ok1 = "apple" + temp;
        return ok == ok1;
    }

    /**
     * ok一样是确定的。而ok1不能确定，需要运行代码获得temp,所以不是同一个对象，输出false。
     *
     * @return false
     */
    public static boolean test5() {
        String ok = "apple1";
        final int temp = getTemp();
        String ok1 = "apple" + temp;
        return ok == ok1;
    }

    public static int getTemp() {
        return 1;
    }

    /**
     * 　　由于有符号引用的存在，所以  String c = b + 2;不会在编译期间被优化，不会把b+2当做字面常量来处理的，因此这种方式生成的对象事实上是保存在堆上的。因此a和c指向的并不是同一个对象。javap -c得到的内容：
     *
     * @return false
     */
    public static boolean test6() {
        String a = "hello2";
        String b = "hello";
        String c = b + 2;
        return a == c;

    }

    /**
     * 对于被final修饰的变量，会在class文件常量池中保存一个副本，也就是说不会通过连接而进行访问，
     * 对final变量的访问在编译期间都会直接被替代为真实的值。那么String c = b + 2;在编译期间就会被优化成：String c = "hello" + 2
     *
     * @return true
     */
    public static boolean test7() {
        String a = "hello2";
        final String b = "hello";
        String c = b + 2;
        return a == c;
    }

    /**
     * 　 这里面虽然将b用final修饰了，但是由于其赋值是通过方法调用返回的，那么它的值只能在运行期间确定，因此a和c指向的不是同一个对象。
     *
     * @return false
     */
    public static boolean test8() {
        String a = "hello2";
        final String b = getHello();
        String c = b + 2;
        return a == c;
    }

    public static String getHello() {
        return "hello";
    }

}