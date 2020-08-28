package com.lht.base_library.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class EmojiUtil {

    public static String string2Unicode(String str) {
        String unicodeStr = "";
        for (int i = 0; i < str.length(); i++) {
            int ch = (int) str.charAt(i);
            if (ch > 255)
                unicodeStr += "\\u" + Integer.toHexString(ch);
            else
                unicodeStr += String.valueOf(str.charAt(i));
        }
        return unicodeStr;
    }

    public static String stringToUtf8(String str) {
        String result = null;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogUtil.e("stringToUtf8 error " + e.getClass() + " " + e.getMessage());
        }
        return result;
    }

    public static String unicode2String(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] hex = str.split("\\\\\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            stringBuilder.append((char) data);
        }
        return stringBuilder.toString();
    }

    public static String utf8ToString(String str) {
        String result = null;
        try {
            result = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogUtil.e("utf8ToString error " + e.getClass() + " " + e.getMessage());
        }
        return result;
    }

    public static String ellipsizeText(String text, int maxEms) {
        String str = "";
        if (!TextUtils.isEmpty(text)) {
            int codePointCount = text.codePointCount(0, text.length());
            if (codePointCount > maxEms) {
                str = text.substring(0, text.offsetByCodePoints(0, maxEms - 1)) + "...";
            } else {
                str = text;
            }
        }
        return str;
    }

}
