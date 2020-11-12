package com.techjd.swagatcorneradmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.techjd.swagatcorneradmin.models.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;

    private String email, password;
    private Button login;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        sharedPreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        login = findViewById(R.id.cirLoginButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();

                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Logging You In");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL+"allBranches/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                API api = retrofit.create(API.class);

                Call<Token> tokenCall = api.loginUser(email, password);

                tokenCall.enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        if (response.isSuccessful()) {
                            Log.d("TOKEN", response.body().getToken());
                            Toast.makeText(LoginActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            editor.putString("TOKEN", response.body().getToken());
                            editor.commit();

                            Intent navigate = new Intent(LoginActivity.this, OrdersActivity.class);
                            startActivity(navigate);

                        } else {
                            Log.d("ERROR", response.message());
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "There is some Error ! Please Try Again Later", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Log.d("ERROR", t.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "There is some Error ! Please Try Again Later", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}