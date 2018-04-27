package com.android.suapp.suapp.server.timetable;

import com.android.suapp.suapp.server.database.exceptions.IllegalObjectStateException;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class Couple {
    @SerializedName("by_subgroups")
    private Boolean bySubgroups;

    @SerializedName("all_group")
    private Lesson allGroup;

    @SerializedName("first_subgroup")
    private Lesson firstSubgroup;

    @SerializedName("second_subgroup")
    private Lesson secondSubgroup;

    public Couple(Boolean bySubgroups){
        this.bySubgroups = bySubgroups;
        if (bySubgroups){
            firstSubgroup = new Lesson();
            secondSubgroup = new Lesson();
        }else {
            allGroup = new Lesson();
        }
    }

    public Boolean getBySubgroups() {
        return bySubgroups;
    }

    /**
     *
     * @return ссылка на объект <code>Lesson</code>
     * @throws IllegalObjectStateException если пара проводится по подгруппам
     */
    public Lesson getAllGroup() throws IllegalObjectStateException {
        if (bySubgroups){
            throw new IllegalObjectStateException("Данная пара проводится по подгруппам!");
        }
        return allGroup;
    }

    /**
     *
     * @throws IllegalObjectStateException если пара проводится по подгруппам
     */
    public Couple setAllGroup(Lesson allGroup) throws IllegalObjectStateException {
        if (bySubgroups){
            throw new IllegalObjectStateException("Данная пара проводится по подгруппам!");
        }
        this.allGroup = allGroup;
        return this;
    }

    /**
     * @throws IllegalObjectStateException если пара проводится всей группой
     */
    public Lesson getFirstSubgroup() throws IllegalObjectStateException {
        if (!bySubgroups){
            throw new IllegalObjectStateException("Данная пара проводится всей группой!");
        }
        return firstSubgroup;
    }

    /**
     * @throws IllegalObjectStateException если пара проводится всей группой
     */
    public Couple setFirstSubgroup(Lesson firstSubgroup) throws IllegalObjectStateException {
        if (!bySubgroups){
            throw new IllegalObjectStateException("Данная пара проводится всей группой!");
        }
        this.firstSubgroup = firstSubgroup;
        return this;
    }

    /**
     * @throws IllegalObjectStateException если пара проводится всей группой
     */
    public Lesson getSecondSubgroup() throws IllegalObjectStateException {
        if (!bySubgroups){
            throw new IllegalObjectStateException("Данная пара проводится всей группой!");
        }
        return secondSubgroup;
    }

    /**
     * @throws IllegalObjectStateException если пара проводится всей группой
     */
    public Couple setSecondSubgroup(Lesson secondSubgroup) throws IllegalObjectStateException {
        if (!bySubgroups){
            throw new IllegalObjectStateException("Данная пара проводится всей группой!");
        }
        this.secondSubgroup = secondSubgroup;
        return this;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
