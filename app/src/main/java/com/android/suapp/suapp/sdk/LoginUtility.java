package com.android.suapp.suapp.sdk;

import com.android.suapp.suapp.server.database.objects.Student;

import org.apache.http.client.methods.HttpGet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public abstract class LoginUtility {
    private static Integer getInteger(Boolean b){
        return b ? 1 : 0;
    }
    public static String loginANewStudent(String login, String password) throws URISyntaxException, IOException {
        StringBuilder params = new StringBuilder(ServerSettings.SERVER_IP);
        params.append("authorize?");
        params.append("login=").append(login).append('&');
        params.append("password=").append(password);
        HttpGet httpGet = new HttpGet();

        httpGet.setURI(new URI(params.toString()));

        URL url = new URL(params.toString());
        System.err.println(params.toString());
        url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
        StringBuilder receivedString = new StringBuilder();
        String temp;
        while ((temp = br.readLine()) != null) {
            System.out.println(temp);
            receivedString.append(temp);
        }
        return receivedString.toString();

    }
}
