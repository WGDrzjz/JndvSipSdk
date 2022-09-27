package com.wgd.jndvsiplibrary.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Author: wangguodong
 * Date: 2022/5/25
 * QQ: 1772889689@qq.com
 * WX: gdihh8180
 * Description: 日志打印工具类
 */
public class JNLogUtil {
    private static final String TAG = "JNDVSDK";
    public static boolean isOpenLog = true;

    public static void e(String msg){
        e(TAG,msg);
    }
    public static void e(String msg, Throwable tr){
        e(TAG,msg,tr);
    }
    public static void e(String tag, String msg){
        if (isOpenLog){
            Log.e(tag,msg);
            saveToFile(tag+" : "+msg);
        }
    }
    public static void e(String tag, String msg, Throwable tr){
        if (isOpenLog){
            Log.e(tag,msg,tr);
            saveToFile(tag+" : "+msg + "\n" + Log.getStackTraceString(tr));
        }
    }

    public static void d(String msg){
        d(TAG,msg);
    }
    public static void d(String msg, Throwable tr){
        d(TAG,msg,tr);
    }
    public static void d(String tag, String msg){
        if (isOpenLog){
            Log.d(tag,msg);
            saveToFile(tag+" : "+msg);
        }
    }
    public static void d(String tag, String msg, Throwable tr){
        if (isOpenLog){
            Log.d(tag,msg,tr);
            saveToFile(tag+" : "+msg + "\n" + Log.getStackTraceString(tr));
        }
    }

    /**
     * 日志打印
     *
     * @param content
     */
    public static void saveToFile( String content) {
        Calendar calendar = Calendar.getInstance();
        String day = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
        String logname = "log"+calendar.get(Calendar.HOUR_OF_DAY)+".txt";

        saveToFile(logname, getSDPath()+"/log/"+day, calendar.get(Calendar.MONTH)+1
                +"-"+calendar.get(Calendar.DAY_OF_MONTH)+"-"+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)
                +":"+calendar.get(Calendar.SECOND)
                +":"+calendar.get(Calendar.MILLISECOND)+"--"+content, true);
    }

    public static String getSaveFilePath(String filename) {
        String path = getSDPath();
        File saveFile = new File(path+filename);
        if(saveFile.canExecute()) {
            if (!saveFile.exists()) {
                File dir = new File(saveFile.getParent());
                dir.mkdirs();
                try {
                    saveFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return path+filename;
    }
    public static String getSDPath() {
        try {
            File sdDir = null;
            boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
            if (sdCardExist) {
                sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            }
            if (null!=sdDir)return sdDir.getAbsolutePath();
        }catch (Exception e){
            e.printStackTrace();
        }
//        context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
        return "/sdcard/Android/data/JCchat/files";
    }
    /**
     * 保存文件
     *
     * @param filename
     * @param content
     */
    public static void saveToFile(String filename, String content) {
        saveToFile(filename, getSDPath(), content, true);
    }

    /**
     * 保存文件
     *
     * @param filename
     * @param content
     */
    public static void saveToFile(String filename, String path, String content, boolean isAppend) {
        File saveFile = new File(path, filename);
        if(saveFile.canExecute()) {
            if (!saveFile.exists()) {
                File dir = new File(saveFile.getParent());
                dir.mkdirs();
                try {
                    saveFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 创建字节输出流对象
            // FileOutputStream fos = new FileOutputStream("fos3.txt");
            // 创建一个向具有指定name的文件中写入数据的输出文件流。如果第二个参数为true,则将字节写入文件末尾处，而不是写入文件开始处
            FileOutputStream fos2 = null;// 第二個参数为true表示程序每次运行都是追加字符串在原有的字符上
            try {
                    fos2 = new FileOutputStream(saveFile, isAppend);
                    // 写数据
                    fos2.write((content).getBytes());
                    fos2.write("\r\n".getBytes());// 写入一个换行
                    fos2.flush();
                    // 释放资源
                    fos2.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            deleteLogFile();
        }
    }

    /**
     * 删除7天之前的log文件
     */
    public static void deleteLogFile() {
        try {
            File file = new File(getSDPath()+"/log");
            File[] files = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    //这里可以用来判断，是文件夹和文件全部显示，还是只显示文件夹
//                if (pathname.isFile()&&!suffix.isEmpty()){
//                    return pathname.getName().endsWith(suffix);
//                }
                    return true;
                }
            });

            if(files!=null) {
                for (int i = 0; i < files.length; i++) {
                    File deleteFile = files[i];
                    try {
                        long tt = (new SimpleDateFormat("yyyy-MM-dd")).parse(deleteFile.getName()).getTime();
                        if (tt < (System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000)) {
                            deleteAllFiles(deleteFile);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        deleteAllFiles(deleteFile);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 删除文件或者文件夹下的所有文件
     * @param root
     */
    private static void deleteAllFiles(File root) {
        try {
            File files[] = root.listFiles();
            if (files != null && files.length>0){
                for (File f : files) {
                    if (f.isDirectory()) { // 判断是否为文件夹
                        deleteAllFiles(f);
                        try {
                            boolean isS = f.delete();
                        } catch (Exception e) {
                        }
                    } else {
                        if (f.exists()) { // 判断是否存在
                            deleteAllFiles(f);
                            try {
                                boolean isS = f.delete();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }else {
                try {
                    boolean isS = root.delete();
                } catch (Exception e) {
                }
            }
        }catch (Exception e){
            Log.e(TAG,"===deleteAllFiles===",e);
        }
    }
}
