package com.harvey;

import java.util.Optional;

/**
 * <P>Optional实际上是个容器：它可以保存类型T的值，或者仅仅保存null。Optional提供很多有用的方法，这样我们就不用显式进行空值检测</P>
 * Created by hanhui on 2017/8/28 0028 13:46
 */

public class TestOptional {
    /**
     * 如果Optional类的实例为非空值的话，isPresent()返回true，否从返回false。
     * 为了防止Optional为空值，orElseGet()方法通过回调函数来产生一个默认值。
     * map()函数对当前Optional的值进行转化，然后返回一个新的Optional实例。
     * orElse()方法和orElseGet()方法类似，但是orElse接受一个默认值而不是一个回调函数。
     */
    public static void main(String[] args) throws NoSuchMethodException {
        Optional<String> fullName = Optional.ofNullable(null);
        System.out.println("Full Name is set? " + fullName.isPresent());
        System.out.println("Full Name: " + fullName.orElseGet(() -> "[none]"));
        System.out.println(fullName.map(s -> "Hey " + s + "!").orElse("Hey Stranger!"));

    }
}
