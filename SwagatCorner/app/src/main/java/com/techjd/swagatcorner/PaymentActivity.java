package com.techjd.swagatcorner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.techjd.swagatcorner.models.UpdateOrder;


import org.json.JSONObject;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentActivity extends AppCompatActivity implements PaymentResultWithDataListener {
    private String priceFromPrevious;
    private Integer totalPrice;
    private String orderId;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Checkout.preload(getApplicationContext());
        textView = findViewById(R.id.id);
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_qxDuKzTTaQoeLH");

        totalPrice = getIntent().getIntExtra("PRICE", 0);
        orderId = getIntent().getStringExtra("ORDERID");
        Log.d("ORDERIS", orderId);
        //        totalPrice = Integer.parseInt(priceFromPrevious);
//        Log.d("TOTALPRICE", totalPrice.toString());

        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "Swagat Corner");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", getOrderID());//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", totalPrice * 100);//pass amount in currency subunits
            options.put("prefill.email", "jaydeepparmar253@gmail.com");
            options.put("prefill.contact", "7600068797");
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("ERROR", "Error in starting Razorpay Checkout", e);
        }
    }

    private String getOrderID() {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder((100000 + rnd.nextInt(900000)) + "-");
        for (int i = 0; i < 5; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);

        return sb.toString();
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {

        Log.e("TAG", " payment successfull "+ s.toString());
        Intent intent  = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
//        deleteCartDetails();
        updateToPaid();
        try {
            Toast.makeText(this, "Payment successfully done! " +s, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
//            Log.d("PAYMENT DATA", paymentData.getOrderId());
//            Intent intent  = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            textView.setText(s);
//            Toast.makeText(this, "Payment Done", Toast.LENGTH_LONG).show();


    }

    private void updateToPaid() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL+"allCartItems/")
                .addConverterFactory(GsonConverterFactory.create())

                .build();

        API api = retrofit.create(API.class);
//        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getApplication());
//        String token = googleSignInAccount.getIdToken();

        SharedPreferences sharedPreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", "");
        Log.d("TOKEN", token);

        Call<UpdateOrder> updateOrderCall = api.updateOrder(token, orderId);

        updateOrderCall.enqueue(new Callback<UpdateOrder>() {
            @Override
            public void onResponse(Call<UpdateOrder> call, Response<UpdateOrder> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PaymentActivity.this, "Payment SuccessFully Done", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<UpdateOrder> call, Throwable t) {
                Log.d("WHATS THE ERROR", t.getMessage());
            }
        });
    }


    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.e("main",  "error code "+String.valueOf(i)+" -- Payment failed "+s.toString()  );
        Intent intent  = new Intent(this, MainActivity.class);
            startActivity(intent);
        try {
            Toast.makeText(this, "Payment error ! Try Again After Some Time", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }
}