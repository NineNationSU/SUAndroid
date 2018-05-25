package com.android.suapp.suapp.server.responses;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class ServerResponse {
    private String response;


    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
