package com.android.suapp.suapp.server.utility;

import com.android.suapp.suapp.server.database.objects.Message;
import com.android.suapp.suapp.server.notes.Note;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessagesListWrapper {
    @Expose
    @SerializedName("response")
    private List<Message> list;

    public List<Message> getList() {
        return list;
    }

    public MessagesListWrapper setList(List<Message> list) {
        this.list = list;
        return this;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }
}
