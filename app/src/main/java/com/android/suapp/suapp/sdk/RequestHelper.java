package com.android.suapp.suapp.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

abstract class RequestHelper {
    static String getRequest(String url) throws IOException {
        URL u = new URL(url);
        System.err.println(url);
        u.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream(), "utf-8"));
        StringBuilder receivedString = new StringBuilder();
        String temp;
        while ((temp = br.readLine()) != null) {
            System.out.println(temp);
            receivedString.append(temp);
        }
        return receivedString.toString();
    }
}
