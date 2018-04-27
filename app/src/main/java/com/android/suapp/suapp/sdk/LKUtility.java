package com.android.suapp.suapp.sdk;

import org.apache.http.client.methods.HttpGet;
import com.android.suapp.suapp.server.database.objects.Student;
import com.android.suapp.suapp.server.utility.http.RequestUtility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public abstract class LKUtility {

    public static String getTimeTable(Student student, String token) throws URISyntaxException, IOException {
        StringBuilder params = new StringBuilder(ServerSettings.SERVER_IP);
        params.append("getTimeTable?");

        params.append("my_id=").append(student.getId()).append('&');
        params.append("token=").append(token);

        HttpGet httpGet = new HttpGet();

        httpGet.setURI(new URI(params.toString()));

        System.out.println(params.toString());
        URL url = new URL(params.toString());
        url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
        StringBuilder receivedString = new StringBuilder();
        String temp;
        while ((temp = br.readLine()) != null)
            receivedString.append(temp);
        return receivedString.toString();


//        return RequestUtility.apacheGET(httpGet, true).getAnswer();
    }

    public static String addLK(Student student, String token, String login, String password) throws URISyntaxException, IOException {
        StringBuilder params = new StringBuilder(ServerSettings.SERVER_IP);
        params.append("addLK?");

        params.append("my_id=").append(student.getId()).append('&');
        params.append("login=").append(login).append('&');
        params.append("password=").append(password).append('&');
        params.append("token=").append(token);

        HttpGet httpGet = new HttpGet();

        httpGet.setURI(new URI(params.toString()));

        return RequestUtility.apacheGET(httpGet, true).getAnswer();
    }

}
