package com.techjd.swagatcorner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllCartItems {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public AllCartItems(String itemName, Integer price, Integer quantity, String id) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}
