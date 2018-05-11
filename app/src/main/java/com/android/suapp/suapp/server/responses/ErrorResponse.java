package com.android.suapp.suapp.server.responses;

import com.google.gson.Gson;

public class ErrorResponse {
    private String response = "error";
    private String error = "error_type";

    public String getMessage(){
        return error;
    }

    public ErrorResponse(String error){
        this.error = error;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
