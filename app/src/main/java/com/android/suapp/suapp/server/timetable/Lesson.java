package com.android.suapp.suapp.server.timetable;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.android.suapp.suapp.server.database.objects.Teacher;

public class Lesson {
    private Integer number;
    private String discipline;
    private String type;
    private Teacher teacher;

    @SerializedName("subgroups_info")
    private String subgroupsInfo;

    public Integer getNumber() {
        return number;
    }

    public Lesson setNumber(Integer number) {
        this.number = number;
        return this;
    }

    public String getDiscipline() {
        return discipline;
    }

    public Lesson setDiscipline(String discipline) {
        this.discipline = discipline;
        return this;
    }

    public String getType() {
        return type;
    }

    public Lesson setType(String type) {
        this.type = type;
        return this;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Lesson setTeacher(Teacher teacher) {
        this.teacher = teacher;
        return this;
    }

    public String getSubgroupsInfo() {
        return subgroupsInfo;
    }

    public Lesson setSubgroupsInfo(String subgroupsInfo) {
        this.subgroupsInfo = subgroupsInfo;
        return this;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
