package com.android.suapp.suapp.server.utility;


import com.android.suapp.suapp.server.notes.Note;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NoteListWrapper {
    @Expose
    @SerializedName("response")
    private List<Note> list;

    public List<Note> getList() {
        return list;
    }

    public NoteListWrapper setList(List<Note> list) {
        this.list = list;
        return this;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }
}
