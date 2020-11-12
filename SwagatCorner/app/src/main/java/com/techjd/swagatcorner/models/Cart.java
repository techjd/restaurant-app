package com.techjd.swagatcorner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cart {
    @SerializedName("foodId")
    @Expose
    private String foodId;
    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    public Cart(String foodId , String itemName, Integer price, Integer quantity) {
        this.foodId = foodId;
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
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

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }
}
