package com.selonj.gson.enclosed;

/**
 * Created by Administrator on 2016-03-31.
 */
public class User {
    public String name;
    public String mail;

    public User() {
    }

    public User(String name) {
        this(name, null);
    }

    public User(String name, String mail) {
        this.name = name;
        this.mail = mail;
    }

    public String toString() {
        return "name:" + name + ", mail:" + mail;
    }
}
