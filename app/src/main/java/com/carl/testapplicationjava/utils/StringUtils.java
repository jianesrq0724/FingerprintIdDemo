package com.carl.testapplicationjava.utils;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Carl
 * version 1.0
 */
public class StringUtils {

    public static int getNum(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    /**
     * unicode转中文
     *
     * @param str
     * @return
     * @author yutao
     * @date 2017年1月24日上午10:33:25
     */
    public static String unicodeToString(String str) {

        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

        Matcher matcher = pattern.matcher(str);

        char ch;

        while (matcher.find()) {

            ch = (char) Integer.parseInt(matcher.group(2), 16);

            str = str.replace(matcher.group(1), ch + "");

        }

        return str;

    }


    /**
     * SHA加密
     *
     * @param strSrc 明文
     * @return 加密之后的密文
     */
    public static String shaEncrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            // 将此换成SHA-1、SHA-512、SHA-384等参数
            md = MessageDigest.getInstance("SHA-256");
            md.update(bt);
            // to HexString
            strDes = bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * byte数组转换为16进制字符串
     *
     * @param bts 数据源
     * @return 16进制字符串
     */
    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;

    }

    public static String toBase64(String strSrc) {
        byte[] bytes = strSrc.getBytes();
        String base64Str = Base64.encodeToString(bytes, Base64.NO_WRAP);
        return base64Str;
    }


    public static String trimName(String text) {
        int start = 0;
        int end = text.length();
        //先判断连续数字开始位置
        while (start < text.length() && !isNumericzidai(text.substring(start, end))) {
            start++;
        }
        return text.substring(0, start);
    }

    public static String trimIdCard(String text) {
        int start = 0;
        int end = text.length();
        //先判断连续数字开始位置
        while (start < text.length() && !isNumericzidai(text.substring(start, end))) {
            start++;
        }
        return text.substring(start, end);
    }

    /**
     * 判断是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumericzidai(String str) {
        String regEx = "^-?[0-9xX]+$";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(str);
        if (mat.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 存在汉字
     *
     * @param str
     * @return
     */
    public static boolean exitHanzi(String str) {
        if (str.getBytes().length == str.length()) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 判断全部是汉字
     *
     * @param str
     * @return
     */
    public static boolean isHanzi(String str) {
        int n = 0;
        for (int i = 0; i < str.length(); i++) {
            n = (int) str.charAt(i);
            if (!(19968 <= n && n < 40869)) {
                return false;
            }
        }
        return true;
    }


    /**
     * url拼接
     *
     * @param url
     * @param params
     * @return
     */
    public static String getRqstUrl(String url, Map<String, String> params) {
        StringBuilder builder = new StringBuilder(url);
        boolean isFirst = true;
        for (String key : params.keySet()) {
            if (key != null && params.get(key) != null) {
                if (isFirst) {
                    isFirst = false;
                    builder.append("?");
                } else {
                    builder.append("&");
                }
                builder.append(key)
                        .append("=")
                        .append(params.get(key));
            }
        }
        return builder.toString();
    }

    public static String deleteNum(String content) {
        String str = content.replaceAll("\\d+", "");
        return str;
    }


    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 获取16进制随机数
     *
     * @param len
     * @return
     */
    public static String randomHexString(int len) {
        try {
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < len; i++) {
                result.append(Integer.toHexString(new Random().nextInt(16)));
            }
            return result.toString().toLowerCase();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public static String getSimpleCookie(String cookie) {
        if (cookie.contains("path")) {
            cookie = cookie.substring(0, cookie.indexOf("path"));
        }
        cookie = cookie.replace("HttpOnly", "");
        return cookie;
    }


    public static String convertStream2String(InputStream in) throws IOException {
        //inputStream转换为String 要进行gbk或者utf-8转码，否则乱码
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "GBK"));
//        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        return sb.toString();
    }


    /**
     * 产生随机的2位数
     *
     * @return
     */
    public static String getRandomTwo() {
        Random rad = new Random();

        String result = rad.nextInt(100) + "";

        if (result.length() == 1) {
            result = "0" + result;
        }
        return result;
    }


    public static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    public static List<String> getStrList(String inputString, int length,
                                          int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length,
                    (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    /**
     * 分割字符串，如果开始位置大于字符串长度，返回空
     *
     * @param str 原始字符串
     * @param f   开始位置
     * @param t   结束位置
     * @return
     */
    public static String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }

    public static String subShortString(String str) {
        if (isEmpty(str)) {
            return "";
        }
        str = str.toLowerCase();
        if (str.length() > 18) {
            return str.substring(str.length() - 18);
        } else {
            return str;
        }
    }

    public static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0;
    }


    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }


}
