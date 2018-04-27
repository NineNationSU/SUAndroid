package com.android.suapp.suapp.server.timetable;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class DayTimeTable {
    private Integer dayOfWeek;
    private List<Couple> couple;

    public DayTimeTable() {
        couple = new ArrayList<>();
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public DayTimeTable setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    public List<Couple> getCouple() {
        return couple;
    }

    public DayTimeTable setCouple(List<Couple> couple) {
        this.couple = couple;
        return this;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
