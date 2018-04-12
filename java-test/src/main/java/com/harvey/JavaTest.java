package com.harvey;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by hanhui on 2017/8/1 0001 09:27
 */

public class JavaTest {

    public static void main(String[] args) throws NoSuchMethodException {
//        Arrays.asList("a", "b", "d").forEach(e -> System.out.println(e));
//        getVideoImage("F:\\MyGitHub\\LearnSamples\\java-test\\ffmpeg.exe",
//                "F:\\MyGitHub\\LearnSamples\\java-test\\aa.mp4",
//                "F:\\MyGitHub\\LearnSamples\\java-test\\TEST.png");
        int[] arr={3,3,1,2,4,2,5,5,4};
        int res=0;//初始值
        for(int i=0;i<arr.length;i++){
            res ^=arr[i];
            System.out.println(res);
        }
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
        List<String> commands = new java.util.ArrayList<String>();
        commands.add(ffmpegPath);//这里设置ffmpeg.exe存放的路径
        commands.add("-i");
        commands.add(path);//这里是设置要截取缩略图的视频的路径
        commands.add("-y");
        commands.add("-f");
        commands.add("image2");
        commands.add("-ss");
        commands.add("10");//这里设置的是要截取视频开始播放多少秒后的图，可以自己设置时间
        commands.add("-t");
        commands.add("0.001");
        commands.add("-s");
        commands.add("320x240");//这里设置输出图片的大小
        commands.add(outImagePath);//这里设置输出的截图的保存路径
        try {
            //截取缩略图并保存
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commands);
            builder.redirectErrorStream(true);
            System.out.println("视频截图开始...");
//            builder.start();
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
