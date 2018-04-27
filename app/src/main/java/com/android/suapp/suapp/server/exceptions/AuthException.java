package com.android.suapp.suapp.server.exceptions;

public class AuthException extends Exception {
    public AuthException(){
        super();
    }

    public AuthException(String str){
        super(str);
    }
}
