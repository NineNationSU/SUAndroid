package com.android.suapp.suapp.server.responses;

import com.google.gson.Gson;

public class OKResponse {
    private String response;

    public OKResponse(){
        response = "OK";
    }

    public OKResponse(String str){
        response = str;
    }


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
