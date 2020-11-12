package com.techjd.swagatcorneradmin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllBranches {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("branchName")
    @Expose
    private String branchName;
    @SerializedName("branchAddress")
    @Expose
    private String branchAddress;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public AllBranches(String id, String branchName, String branchAddress) {
        this.id = id;
        this.branchName = branchName;
        this.branchAddress = branchAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}
