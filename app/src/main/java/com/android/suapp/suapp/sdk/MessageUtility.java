package com.android.suapp.suapp.sdk;

import com.android.suapp.suapp.server.database.objects.StudyGroup;
import com.android.suapp.suapp.server.utility.http.RequestUtility;

import org.apache.http.client.methods.HttpGet;
import com.android.suapp.suapp.server.database.objects.Message;
import com.android.suapp.suapp.server.database.objects.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public abstract class MessageUtility {

    /*
     *    Параметр <code>group</code> временный
     *    Будет убран в финальной версии
     */
    public static String sendMessage(Message message, StudyGroup group, String token) throws URISyntaxException, IOException {
        StringBuilder params = new StringBuilder(ServerSettings.SERVER_IP);
        params.append("sendMessage?");

        params.append("my_id=").append(message.getSenderId()).append('&');
        params.append("recipients=").append(group.getNumber()).append('&');
        params.append("body=").append(message.getBody()).append('&');
        params.append("token=").append(token);

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


    public static String getMessages(Student student, String token) throws URISyntaxException, IOException {
        StringBuilder params = new StringBuilder(ServerSettings.SERVER_IP);
        params.append("getMessages?");

        params.append("my_id=").append(student.getId()).append('&');
        params.append("token=").append(token);

        System.out.println(params.toString());

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
