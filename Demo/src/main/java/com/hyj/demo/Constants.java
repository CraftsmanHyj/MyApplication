package com.hyj.demo;

import java.io.Serializable;

/**
 * 系统常量类
 *
 * @author async
 */
public class Constants implements Serializable {
    private static final long serialVersionUID = 1L;

    /******************** 会从config.properties中读取数据覆盖这里的值 ********************/
    /**
     * 字段：崩溃日志信息保存方式，ture：JSON格式保存；false：String格式保存
     */
    public static boolean PROP_CRASHINFOSAVEASJSON = false;
    /**
     * 字段：程序是否处于调试状态
     */
    public static boolean PROP_ISDEBUG = false;

    /**
     * 字段：日志打印时输出的tag
     */
    public static String PROP_LOGTAG = "TAG";

    /******************** 文件夹 ********************/
    /**
     * 文件夹：录音存放文件目录
     */
    public static final String DIR_RECORDER = "Talk";
    /**
     * 文件夹：图片缓存文件目录
     */
    public static final String DIR_IMAGECACHE = "ImageCache";
    /**
     * 文件夹：文件下载目录
     */
    public static final String DIR_DOWNLOAD = "Download";
    /**
     * 文件夹：临时数据文件目录
     */
    public static final String DIR_TEMP = "Temp";
    /**
     * 文件夹：日志文件，存放app运行、崩溃等日志
     */
    public static final String DIR_LOG = "Log";

    /******************** 文件名 ********************/
    /**
     * 文件：SharedPreference文件名
     */
    public static final String FILE_NAME_SHARED = "lib_shared";

    /******************** Shared、数据库等中会用到的字段名 ********************/
    /**
     * 字段：密码
     */
    public static final String FIELD_PWD = "password";
    /**
     * 密保问题
     */
    public static final String FIELD_QUESTION = "question";
    /**
     * 密保答案
     */
    public static final String FIELD_ANSWER = "answer";

}
