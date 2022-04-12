package com.fairhr.module_support.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtils {

    public static String formatHtml(String content){
        //判断string中是否包含img标签
        if (!TextUtils.isEmpty(content) && content.indexOf("<img") != -1) {
        //img标签正则
            String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
            Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
            Matcher m_image = p_image.matcher(content);
            //循环去掉img标签
            while (m_image.find()) {
                String group = m_image.group();
                content = content.replace(group, "");
            }
        }
        return content;
    }
}
