package com.android.suapp.suapp.sdk;

import org.apache.http.client.methods.HttpGet;
import com.android.suapp.suapp.server.database.objects.Student;
import com.android.suapp.suapp.server.utility.http.RequestUtility;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class RegistrationUtility {
    private static Integer getInteger(Boolean b){
        return b ? 1 : 0;
    }
    public static String registerANewStudent(Student student) throws URISyntaxException, IOException {
        StringBuilder params = new StringBuilder(ServerSettings.SERVER_IP);
        params.append("registration?");

        params.append("type=").append("student").append('&');
        params.append("first_name=").append(student.getFirstName()).append('&');
        params.append("middle_name=").append(student.getMiddleName()).append('&');
        params.append("last_name=").append(student.getLastName()).append('&');
        params.append("gender=").append(student.getGender()).append('&');
        params.append("birthday=").append(student.getBirthday()).append('&');
        params.append("group=").append(student.getGroupNumber()).append('&');
        params.append("group_proforg=").append(getInteger(student.isGroupProforg())).append('&');
        params.append("group_manager=").append(getInteger(student.isGroupManager())).append('&');
        params.append("group_president=").append(getInteger(student.isGroupPresident())).append('&');
        params.append("login=").append(student.getLogin()).append('&');
        params.append("password=").append(student.getPassword());

        HttpGet httpGet = new HttpGet();

        httpGet.setURI(new URI(params.toString()));

        return RequestUtility.apacheGET(httpGet, true).getAnswer();

    }
}
