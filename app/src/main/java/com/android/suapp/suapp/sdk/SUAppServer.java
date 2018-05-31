package com.android.suapp.suapp.sdk;

import android.support.annotation.NonNull;

import com.android.suapp.suapp.server.database.objects.Message;
import com.android.suapp.suapp.server.database.objects.Student;
import com.android.suapp.suapp.server.database.objects.StudyGroup;
import com.android.suapp.suapp.server.notes.Note;

import java.io.IOException;
import java.net.URISyntaxException;

public abstract class SUAppServer {
    public static final String SERVER_IP = "http://192.168.43.113:4567/";
    public static String getTimeTable(@NonNull String token) throws IOException {
        String params = SERVER_IP + "getTimeTable?" + "token=" + token;
        return RequestHelper.getRequest(params);
    }

    public static String authorize(@NonNull String login, @NonNull String password) throws IOException {
        String params = SERVER_IP + "authorize?" +
                "login=" + login + '&' +
                "password=" + password;

        return RequestHelper.getRequest(params);
    }

    public static String sendMessage(@NonNull Message message, @NonNull StudyGroup group, @NonNull String token) throws IOException {
        String params = SERVER_IP + "sendMessage?" +
                "recipients=" + group.getNumber() + '&' +
                "body=" + message.getBody() + '&' +
                "token=" + token;
        return RequestHelper.getRequest(params);
    }

    public static String getMessages(@NonNull String token) throws IOException {
        String params = SERVER_IP + "getMessages?" + "token=" + token;
        return RequestHelper.getRequest(params);
    }

    public static String addNote(@NonNull Note note, @NonNull String token) throws IOException {
        String params = SERVER_IP + "addNote?" +
                "lesson=" + note.getLesson() + '&' +
                "text=" + note.getText() + '&' +
                "start=" + note.getStart() + '&';
                if(note.getDeadline() != null){
                    params += "deadline=" + note.getDeadline() + '&';
                }
                params += "token=" + token;
        return RequestHelper.getRequest(params);
    }

    public static String getNote(@NonNull String lessonName, @NonNull String token) throws IOException {
        String params = SERVER_IP + "getNote?" + "lesson=" + lessonName + '&' + "token=" + token;
        return RequestHelper.getRequest(params);
    }

    private static Integer getInteger(Boolean b){
        return b ? 1 : 0;
    }

    public static String registerANewStudent(@NonNull Student student) throws IOException {
        String params = SERVER_IP + "registration?" +
                "type=" + "student" + '&' +
                "gender=" + student.getGender() + '&' +
                "birthday=" + student.getBirthday() + '&' +
                "group_proforg=" + getInteger(student.isGroupProforg()) + '&' +
                "group_manager=" + getInteger(student.isGroupManager()) + '&' +
                "group_president=" + getInteger(student.isGroupPresident()) + '&' +
                "phone_number=" + student.getPhoneNumber() + '&' +
                "login=" + student.getLogin() + '&' +
                "password=" + student.getPassword();

        return RequestHelper.getRequest(params);
    }

    public static String loadStudentSalary(@NonNull String token) throws IOException {
        String params = SERVER_IP + "salary?" + "token=" + token;
        return RequestHelper.getRequest(params);
    }

    public static String loadClassmates(@NonNull String token) throws IOException {
        String params = SERVER_IP + "classmates?" + "token=" + token;
        return RequestHelper.getRequest(params);
    }

    public static String getLessons(@NonNull String token) throws  IOException{
        String params = SERVER_IP + "getLessons?" + "token=" + token;
        return RequestHelper.getRequest(params);
    }
}
