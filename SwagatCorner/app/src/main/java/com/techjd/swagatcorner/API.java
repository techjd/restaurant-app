package com.techjd.swagatcorner;

import com.techjd.swagatcorner.models.AllBranches;
import com.techjd.swagatcorner.models.AllCartItems;
import com.techjd.swagatcorner.models.AllCategories;
import com.techjd.swagatcorner.models.AllFoodItem;
import com.techjd.swagatcorner.models.Cart;
import com.techjd.swagatcorner.models.History;
import com.techjd.swagatcorner.models.NewUser;
import com.techjd.swagatcorner.models.Orders;
import com.techjd.swagatcorner.models.Token;
import com.techjd.swagatcorner.models.UpdateOrder;
import com.techjd.swagatcorner.models.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface API {

    @FormUrlEncoded
    @POST("addUser")
    Call<Users> addUser(
            @Header("token") String token,
            @Field("sub") String sub,
            @Field("email") String email,
            @Field("name") String name
    );

    @GET("categories/getAllCategory")
    Call<AllCategories> getAllCategories();

    @GET("allBranches")
    Call<List<AllBranches>> getAllBranches();

    @GET("cat/{category}")
    Call<List<AllFoodItem>> getAllFoodItems(@Path("category") String category);

    @FormUrlEncoded
    @POST("allCartItems")
    Call<Cart> addToCart(
            @Header("token") String token,
            @Field("foodId") String foodId,
            @Field("itemName") String itemName,
            @Field("price") Integer price,
            @Field("quantity") Integer quantity
    );

    @GET("allCartItems")
    Call<List<AllCartItems>> getAllCartItems(
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("addToOrders")
    Call<Orders> addToOrder(
            @Header("token") String token,
            @Field("branch") String branch
    );


    @GET("getOrder")
    Call<List<History>> getOrders(
            @Header("token") String token
    );

    @PUT("update/{orderid}")
    Call<UpdateOrder> updateOrder(
            @Header("token") String token,
            @Path("orderid") String orderid
    );

    @DELETE("deleteItem/{id}")
    Call<UpdateOrder> deleteOrder(
            @Header("token") String token,
            @Path("id") String id
    );

    @FormUrlEncoded
    @PUT("editItem/{id}")
    Call<UpdateOrder> updateCartItem(
            @Header("token") String token,
            @Path("id") String id,
            @Field("quantity") Integer quantity
    );

    @FormUrlEncoded
    @POST("registerUser")
    Call<Token> addNewUser(
            @Field("email") String email,
            @Field("name") String name,
            @Field("password") String password,
            @Field("number") String number
    );

    @FormUrlEncoded
    @POST("loginUser")
    Call<Token> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );


}
