package com.harvey;

/**
 * Created by hanhui on 2017/8/1 0001 09:27
 */

public class JavaTest {
    public static final int SYSTEM_UI_FLAG_LOW_PROFILE = 0x00000001;

    public static final int SYSTEM_UI_FLAG_HIDE_NAVIGATION = 0x00000002;
    public static final int SYSTEM_UI_FLAG_FULLSCREEN = 0x00000004;

    public static final int SYSTEM_UI_FLAG_LAYOUT_STABLE = 0x00000100;
    public static final int SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION = 0x00000200;

    public static final int SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN = 0x00000400;

    public static final int SYSTEM_UI_FLAG_IMMERSIVE = 0x00000800;
    public static final int SYSTEM_UI_FLAG_IMMERSIVE_STICKY = 0x00001000;

    public static final int SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = 0x00002000;


    public static final int STATUS_BAR_DISABLE_HOME = 0x00200000;

    public static void main(String[] args) {

        System.out.println(SYSTEM_UI_FLAG_LAYOUT_STABLE|SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

    }
}
