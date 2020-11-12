package com.techjd.swagatcorneradmin;

import com.techjd.swagatcorneradmin.models.AllBranches;
import com.techjd.swagatcorneradmin.models.Orders;
import com.techjd.swagatcorneradmin.models.Token;

import java.util.List;

import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface API {
    @GET("allBranches")
    Call<List<AllBranches>> getBranches();

//    @GET("getOrders/{branchid}")
//    Call<List<Orders>> getOrders(
//            @Path("branchid") String branchid
//    );

    @GET("get/AllOrders")
    Call<List<Orders>> getOrders(
           @Header("token") String token
    );

    @FormUrlEncoded
    @POST("loginBranch")
    Call<Token> loginUser(
            @Field("branchEmail") String email,
            @Field("branchPassword") String password
    );

    @FormUrlEncoded
    @POST("loginUser")
    Call<Token> superAdminLoginUser(
            @Field("email") String email,
            @Field("password") String password
    );

}
