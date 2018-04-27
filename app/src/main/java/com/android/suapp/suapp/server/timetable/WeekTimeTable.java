package com.android.suapp.suapp.server.timetable;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class WeekTimeTable {
    private int weekNumber;
    private List<DayTimeTable> days;

    public WeekTimeTable() {
        days = new ArrayList<>();
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public WeekTimeTable setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
        return this;
    }

    public List<DayTimeTable> getDays() {
        return days;
    }

    public WeekTimeTable setDays(List<DayTimeTable> days) {
        this.days = days;
        return this;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
