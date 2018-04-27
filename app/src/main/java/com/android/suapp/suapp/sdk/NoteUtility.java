package com.android.suapp.suapp.sdk;

import com.android.suapp.suapp.server.database.objects.Student;
import com.android.suapp.suapp.server.notes.Note;
import com.android.suapp.suapp.server.utility.http.RequestUtility;

import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class NoteUtility {

    public static String addNote(Student student, Note note, String token) throws URISyntaxException, IOException {
        StringBuilder params = new StringBuilder(ServerSettings.SERVER_IP);
        params.append("addNote?");

        params.append("my_id=").append(student.getId()).append('&');
        params.append("group=").append(student.getGroupNumber()).append('&');
        params.append("date=").append(note.getDate()).append('&');
        params.append("lesson_number=").append(note.getLessonNumber()).append('&');
        params.append("title=").append(note.getTitle()).append('&');
        params.append("text=").append(note.getText()).append('&');
        params.append("token=").append(token);

        HttpGet httpGet = new HttpGet();

        httpGet.setURI(new URI(params.toString()));

        return RequestUtility.apacheGET(httpGet, true).getAnswer();
    }


    public static String getNote(Student student, String date, String token) throws URISyntaxException, IOException {
        StringBuilder params = new StringBuilder(ServerSettings.SERVER_IP);
        params.append("getNote?");

        params.append("my_id=").append(student.getId()).append('&');
        params.append("group=").append(student.getGroupNumber()).append('&');
        params.append("date=").append(date).append('&');
        params.append("token=").append(token);

        HttpGet httpGet = new HttpGet();

        httpGet.setURI(new URI(params.toString()));

        return RequestUtility.apacheGET(httpGet, true).getAnswer();
    }
}
