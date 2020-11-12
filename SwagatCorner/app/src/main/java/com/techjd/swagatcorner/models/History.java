package com.techjd.swagatcorner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class History {
    @SerializedName("isPaid")
    @Expose
    private Boolean isPaid;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("orderItems")
    @Expose
    private List<OrderItem> orderItems = null;
    @SerializedName("totalPrice")
    @Expose
    private Integer totalPrice;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public History(Boolean isPaid, Integer totalPrice) {
        this.isPaid = isPaid;
        this.totalPrice = totalPrice;
    }

    public History(Boolean isPaid, String id, String user, List<OrderItem> orderItems, Integer totalPrice) {
        this.isPaid = isPaid;
        this.id = id;
        this.user = user;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}
