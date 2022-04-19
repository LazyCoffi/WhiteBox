package com.cheng.whiteboxmodel.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class TokenGenerator {
    public static String genToken(String account) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("md5");
            String token = account + System.currentTimeMillis() + new Random().nextInt(9999);
            messageDigest.update(token.getBytes(StandardCharsets.UTF_8));
            return bytes2hex02(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getStampKey(String account, String token) {
        return account + token;
    }

    private static String bytes2hex02(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        String buffer;
        for (byte b : bytes) {
            buffer = Integer.toHexString(0xFF & b);
            if (buffer.length() == 1) {
                buffer = "0" + buffer;
            }
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString();
    }
}
