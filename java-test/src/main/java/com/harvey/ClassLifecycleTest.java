package com.harvey;

import java.util.concurrent.TimeUnit;

public class ClassLifecycleTest {


    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        //  主动引用引起类的初始化一: new对象、读取或设置类的静态变量、调用类的静态方法。
        //  new InitClass();
//          InitClass.a = "";
//          String a = InitClass.a;
//          InitClass.method();

        //  主动引用引起类的初始化二：通过反射实例化对象、读取或设置类的静态变量、调用类的静态方法。
//          Class cls = InitClass.class;
//          cls.newInstance();

//          Field f = cls.getDeclaredField("a");
//          f.get(null);
//          f.set(null, "s");

        //  Method md = cls.getDeclaredMethod("method");
        //  md.invoke(null, null);

        //  主动引用引起类的初始化三：实例化子类，引起父类初始化。
//        new SubInitClass2();
//          String a = SubInitClass.a;// 引用父类的静态字段，只会引起父类初始化，而不会引起子类的初始化
//        String b = SubInitClass.b;// 引用子类的静态字段，引起父类初始化和子类的初始化

//        String b = InitClass.b;// 使用类的常量不会引起类的初始化
//        SubInitClass[] sc = new SubInitClass[10];// 定义类数组不会引起类的初始化

        System.out.println(TimeUnit.SECONDS.toMillis(3));
    }

}

class InitClass {
    public final static String b = "b";
    public static String a = null;

    static {
        System.out.println("初始化InitClass");
    }

    public static void method() {
    }
}

class SubInitClass extends InitClass {
    public static String b = null;

    static {
        System.out.println("初始化SubInitClass");
    }
}

class InitClass2 {
    public static Field1 f1 = new Field1();
    public static Field1 f2;

    static {
        System.out.println("运行父类静态代码");
    }
}

class SubInitClass2 extends InitClass2 {
    public static Field2 f2 = new Field2();

    static {
        System.out.println("运行子类静态代码");
    }
}

class Field1 {
    public Field1() {
        System.out.println("Field1构造方法");
    }
}

class Field2 {
    public Field2() {
        System.out.println("Field2构造方法");
    }
}