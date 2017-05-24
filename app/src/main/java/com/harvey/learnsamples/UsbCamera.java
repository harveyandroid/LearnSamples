package com.harvey.learnsamples;

/**
 * Created by Administrator on 2016/8/9 0009.
 */
public class UsbCamera {
    static {
        System.loadLibrary("UsbCamera");
    }
    //控制
    public native int[] getCameraList();//获得已经连接的摄像头cameraId列表

    public native int openCamera(int video,int width,int height);//开启摄像头
    //public native int startCamera();//开启采集
    public native int readCameraFrame(byte[] buffer);//采集视频图像数据
    public native void readCameraFrameBmp(Object bitmap);//采集视频图像数据并将图像数据绘制到bitmap
    public native void stopCamera();//停止摄像头采集
    //public native void destoryCamera();//销毁摄像头

    //参数设置
    public native int setBrightness(int value);//设置亮度
    public native int setContrast(int value);//设置对比度
    public native int setSaturation(int value);//设置饱和度
    public native int setHue(int value);//设置色调
    public native int setSharpness(int value);//设置清晰度
    public native int setGain(int value);//设置增益
    public native int setGamma(int value);//设置珈玛
    public native int setAutoExposure(int flag);//是否自动曝光
    public native int setExposure(int value);//设置曝光度
    public native int setWhiteBalanceAuto(int flag);//是否自动白平衡
    public native int setWhiteBalance(int value);//设置白平衡的值
    public native int setFocusingAuto(int flag);//是否自动对焦
    public native int setFocusing(int value);//设置焦点值

}
