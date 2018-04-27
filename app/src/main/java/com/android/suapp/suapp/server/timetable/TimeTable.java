package com.android.suapp.suapp.server.timetable;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class TimeTable {
    private List<String> time;
    private List<WeekTimeTable> weeks;

    public TimeTable() {
        time = new ArrayList<>();
        weeks = new ArrayList<>();
    }

    public List<String> getTime() {
        return time;
    }

    public TimeTable setTime(List<String> time) {
        this.time = time;
        return this;
    }

    public List<WeekTimeTable> getWeeks() {
        return weeks;
    }

    public TimeTable setWeeks(List<WeekTimeTable> weeks) {
        this.weeks = weeks;
        return this;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
