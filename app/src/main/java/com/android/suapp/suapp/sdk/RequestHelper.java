package com.android.suapp.suapp.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

abstract class RequestHelper {
    static String getRequest(String url) throws IOException {
        url = url.replaceAll("\n", " ");
        URL u = new URL(url);
        System.err.println(url);
        URLConnection conn = u.openConnection();
        conn.setConnectTimeout(3000);
        BufferedReader br;
        try{
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        }catch (Exception e){
            throw new ConnectException(e.getMessage());
        }
        StringBuilder receivedString = new StringBuilder();
        String temp;
        while ((temp = br.readLine()) != null) {
            System.out.println(temp);
            receivedString.append(temp).append('\n');
        }
        if(!receivedString.toString().isEmpty()){
            receivedString.deleteCharAt(receivedString.length() - 1);
        }
        return receivedString.toString();
    }
}