package io.manbang.easybytecoder.plugin.mybatismock.runtime.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author GaoYang
 * 2019/1/12
 */
public class HashUtil {
    public static long toHash(String s) {
        long seed = 131;
        long hash = 0;
        for (int i = 0; i < s.length(); i++) {
            hash = (hash * seed) + s.charAt(i);
        }
        return hash;
    }


    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }


}
