package com.article.util;



//转换markDown工具类
public class StringFromHtmlUtil {
    public static String getString(String html) {
        //从html中提取纯文本
        //剔出<html>的标签
        String txtcontent = html.replaceAll("</?[^>]+>", "");
        //去除字符串中的空格,回车,换行符,制表符
        txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");
        return txtcontent;
    }
}

