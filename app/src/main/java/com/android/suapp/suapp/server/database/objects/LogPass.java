package com.android.suapp.suapp.server.database.objects;

public class LogPass {
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public LogPass setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LogPass setPassword(String password) {
        this.password = password;
        return this;
    }
}
