package com.android.suapp.suapp.server.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utility {
    public static String getMD5(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(text.getBytes());

        byte byteData[] = md.digest();

        //конвертируем байт в шестнадцатеричный формат первым способом
        StringBuilder sb = new StringBuilder();
        for (byte aByteData : byteData) {
            sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
