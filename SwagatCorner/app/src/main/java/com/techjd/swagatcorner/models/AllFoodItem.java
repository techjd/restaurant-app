package com.techjd.swagatcorner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllFoodItem {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("itemNo")
    @Expose
    private Integer itemNo;
    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("stockORNot")
    @Expose
    private Boolean stockORNot;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public AllFoodItem(String itemName, Integer price, Boolean stockORNot, String id) {
        this.itemName = itemName;
        this.price = price;
        this.stockORNot = stockORNot;
        this.id = id;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getItemNo() {
        return itemNo;
    }

    public void setItemNo(Integer itemNo) {
        this.itemNo = itemNo;
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

    public Boolean getStockORNot() {
        return stockORNot;
    }

    public void setStockORNot(Boolean stockORNot) {
        this.stockORNot = stockORNot;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}
