package com.harvey.ffmpeg;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用了ffmpeg的支持
 * ffmpeg的官网下载地址如下
 * https://ffmpeg.zeranoe.com/builds/
 * 建议下载对应自己系统的static压缩包 即可免编译使用 如果想深入学习的话 可以下载源代码
 * 下载完以后压缩并在环境变量里Path下配置ffmpeg.exe的bin目录
 * cmd下输入ffmpeg -version显示了版本信息则说明安装成功 可以使用了
 * Created by hanhui on 2017/8/1 0001 09:27
 */

public class FFmpegTest {

    public static void main(String[] args) throws NoSuchMethodException {
        getVideoImage("D:/ffmpeg/bin/ffmpeg.exe",
                "F:/MyGitHub/LearnSamples/java-test/aa.mp4",
                "F:/MyGitHub/LearnSamples/java-test/TEST1.png");
    }

    /**
     * 获得视频缩略图，获取成功返回true，获取失败返回false
     *
     * @param ffmpegPath   是ffmpeg.exe存放的路径
     * @param path         是视频文件的存放路径
     * @param outImagePath 输出缩略图的保存路径
     * @return
     */
    public static boolean getVideoImage(String ffmpegPath, String path, String outImagePath) {
        File file = new File(path);
        if (!file.exists()) {//判断文件是否存在
            System.out.println("[" + path + "]视频文件不存在!");
            return false;
        }
        //设置参数
        List<String> commands = new ArrayList<String>();
        commands.add(ffmpegPath);//这里设置ffmpeg.exe存放的路径
        commands.add("-i");
        commands.add(path);//这里是设置要截取缩略图的视频的路径
        commands.add("-y");
        commands.add("-f");
        commands.add("image2");
        commands.add("-ss");//添加参数＂-ss＂，该参数指定截取的起始时间
        commands.add("0");//这里设置的是要截取视频开始播放多少秒后的图，可以自己设置时间
        commands.add("-t");//添加参数＂-t＂，该参数指定持续时间
        commands.add("0.001");// 添加持续时间为1毫秒
        commands.add("-s");//添加参数＂-s＂，该参数指定截取的图片大小
        commands.add("320x240");//这里设置输出图片的大小
        commands.add(outImagePath);//这里设置输出的截图的保存路径
        try {
            //截取缩略图并保存
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commands);
            builder.redirectErrorStream(true);
            System.out.println("视频截图开始...");
            Process process = builder.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[1024];
            System.out.print("正在进行截图，请稍候");
            while (in.read(re) != -1) {
                System.out.print(".");
            }
            System.out.println("");
            in.close();
            System.out.println("视频截图完成...");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
