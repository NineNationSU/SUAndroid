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

    @Expose
    private String start;

    @Expose
    private String deadline;


    public Note(){}


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

    public String getStart() {
        return start;
    }

    public Note setStart(String start) {
        this.start = start;
        return this;
    }

    public String getDeadline() {
        return deadline;
    }

    public Note setDeadline(String deadline) {
        this.deadline = deadline;
        return this;
    }

    @Override
    public String toString(){
        return new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }
}
