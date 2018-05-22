package com.android.suapp.suapp.server.responses;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class ServerResponse {
    private String response;

    @SerializedName("error")
    private String errorType;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
