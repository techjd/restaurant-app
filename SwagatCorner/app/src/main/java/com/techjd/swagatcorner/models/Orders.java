package com.techjd.swagatcorner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Orders {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("totalPrice")
    @Expose
    private Integer totalPrice;

    public Orders(String branch) {
        this.branch = branch;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }


}
