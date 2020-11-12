package com.techjd.swagatcorner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Users {
    @SerializedName("sub")
    @Expose
    private String sub;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
