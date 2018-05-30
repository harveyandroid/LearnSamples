package com.harvey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanhui on 2018/3/14 0014 10:18
 */

public class GenericTest {

	void test() {
		// <-----------<? extends E>----------->
		// <? extends E> 是 Upper Bound（上限） 的通配符，用来限制元素的类型的上限，比如
		List<? extends Fruit> fruits;
		// 表示集合中的元素类型上限为Fruit类型，即只能是Fruit或者Fruit的子类，因此对于下面的赋值是合理的
		fruits = new ArrayList<Fruit>();
		fruits = new ArrayList<Apple>();
		fruits = new ArrayList<Banana>();
		fruits = new ArrayList<RedApple>();
		// 如果集合中元素类型为Fruit的父类则会编译出错，比如
		// fruits = new ArrayList<Object>();

		// <-----------写入----------->
		// fruits.add(new Fruit());//编译不通过
		// fruits.add(new Apple());//编译不通过
		fruits.add(null);// 编译通过
		// 因为<? extends Fruit>只是告诉编译器集合中元素的类型上限
		// 但它具体是什么类型编译器是不知道的，
		// fruits可以指向ArrayList<Fruit>，
		// 也可以指向ArrayList<Apple>、ArrayList<Banana>，
		// 也就是说它的类型是不确定的，既然是不确定的，为了类型安全，
		// 编译器只能阻止添加元素了。
		// 举个例子，当你添加一个Apple时，但fruits此时指向ArrayList<Banana>，
		// 显然类型就不兼容了。当然null除外，因为它可以表示任何类型。

		// <-----------读取----------->
		// 无论fruits指向什么，编译器都可以确定获取的元素是Fruit类型，所有读取集合中的元素是允许的
		Fruit fruit = fruits.get(0);// 编译通过
		Object object = fruits.get(0);// 编译通过
		// Apple apple = fruits.get(0);// 编译不通过，不确认类型
		// 补充：<?>是<? extends Object>的简写

		// <-----------<? super E>----------->
		// <? super E> 是 Lower Bound（下限） 的通配符 ，用来限制元素的类型下限，比如
		List<? super Apple> apples;// 表示集合中元素类型下限为Apple类型，即只能是Apple或Apple的父类，因此对于下面的赋值是合理的
		apples = new ArrayList<Apple>();
		apples = new ArrayList<Fruit>();
		apples = new ArrayList<Object>();
		// apples = new ArrayList<RedApple>();//元素类型为Apple的子类,编译不通过
		// <-----------写入----------->
		// apples.add(new Object());
		// apples.add(new Fruit());
		apples.add(new RedApple());
		// <-----------读取----------->
		// 编译器允许从apples中获取元素的，但是无法确定的获取的元素具体是什么类型，只能确定一定是Object类型的子类
		Object object1 = apples.get(0);// 获取的元素为Object类型


		// 引入泛型的目的是为了避免强制类型转换的繁琐操作
		// <-----------PECS法则----------->
		// PECS法则：生产者（Producer）使用extends，消费者（Consumer）使用super
		// 1、生产者
		// 如果你需要一个提供E类型元素的集合，使用泛型通配符<? extends E>。它好比一个生产者，可以提供数据。
		// 2、消费者
		// 如果你需要一个只能装入E类型元素的集合，使用泛型通配符<? super E>。它好比一个消费者，可以消费你提供的数据。
		// 3、既是生产者也是消费者
		// 既要存储又要读取，那就别使用泛型通配符。

	}

	class Fruit {

	}

	class Apple extends Fruit {
	}

	class RedApple extends Apple {
	}

	class Banana extends Fruit {
	}
}
