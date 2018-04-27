package com.android.suapp.suapp.server.utility;


import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListWrapper<E> {
    @Expose
    @SerializedName("response")
    private List<E> list;

    public List<E> getList() {
        return list;
    }

    public ListWrapper<E> setList(List<E> list) {
        this.list = list;
        return this;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }
}
