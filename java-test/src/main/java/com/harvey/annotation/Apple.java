package com.harvey.annotation;

/**
 * Created by hanhui on 2017/9/14 0014 14:23
 */

public class Apple {

    @FruitName("Apple")
    private String appleName;

    @FruitColor(fruitColor = FruitColor.Color.RED)
    private String appleColor;

    @FruitProvider(id = 1, name = "陕西红富士集团", address = "陕西省西安市延安路89号红富士大厦")
    private String appleProvider;

    public String getAppleColor() {
        return appleColor;
    }

    public void setAppleColor(String appleColor) {
        this.appleColor = appleColor;
    }

    public String getAppleName() {
        return appleName;
    }

    public void setAppleName(String appleName) {
        this.appleName = appleName;
    }

    public void displayName() {
        System.out.println("水果的名字是：苹果");
    }

    public String getAppleProvider() {
        return appleProvider;
    }

    public void setAppleProvider(String appleProvider) {
        this.appleProvider = appleProvider;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "appleName='" + appleName + '\'' +
                ", appleColor='" + appleColor + '\'' +
                '}';
    }
}
