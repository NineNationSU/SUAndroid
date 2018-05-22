package com.android.suapp.suapp.server.notes;

import com.android.suapp.suapp.server.database.objects.StudyGroup;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Note {
    @Expose
    private Integer id;

    @Expose
    private StudyGroup group;

    @Expose
    private String lesson;

    @Expose
    private String text;


    public Note(){}

    public Note(ResultSet set) throws SQLException {
        group = new StudyGroup().setNumber(set.getString("group_number"));
        lesson = set.getString("lesson");
        text = set.getString("text");
    }

    public Integer getId() {
        return id;
    }

    public Note setId(Integer id) {
        this.id = id;
        return this;
    }

    public StudyGroup getGroup() {
        return group;
    }

    public Note setGroup(StudyGroup group) {
        this.group = group;
        return this;
    }

    public String getLesson() {
        return lesson;
    }

    public Note setLesson(String lesson) {
        this.lesson = lesson;
        return this;
    }

    public String getText() {
        return text;
    }

    public Note setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public String toString(){
        return new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }
}
