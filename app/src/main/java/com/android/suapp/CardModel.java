package com.android.suapp;

import android.widget.TextView;

import com.android.suapp.suapp.server.database.objects.Message;

import java.util.ArrayList;
import java.util.List;

public class CardModel {
    private String icon, userName, messageBody;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public static List<CardModel> getObjectList(List<Message> messages) {

        List<CardModel> dataList = new ArrayList<>();

        for (int i = messages.size() - 1; i > -1; i--) {
            System.out.println(messages.get(i));
            CardModel nature = new CardModel();
            nature.icon = Integer.toString(messages.get(i).getSenderId());
            nature.userName = Integer.toString(messages.get(i).getSenderId());
            nature.messageBody = messages.get(i).getBody();
            dataList.add(nature);
        }
        return dataList;
    }


}
