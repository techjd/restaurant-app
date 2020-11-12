package com.techjd.swagatcorner;

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

import com.techjd.swagatcorner.models.Token;

import java.io.IOError;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextPassword;
    private EditText editTextNumber;
    private String email, name, password, number;
    private Button signUp;
    private TextView alreadyAccount;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextNumber = findViewById(R.id.editTextNumber);
        sharedPreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        signUp = findViewById(R.id.register);
        alreadyAccount = findViewById(R.id.already);

        alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                name = editTextName.getText().toString();
                number = editTextNumber.getText().toString();
                password = editTextPassword.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                progressDialog = new ProgressDialog(RegistrationActivity.this);
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Registering " + name);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
//                Toast.makeText(LoginActivity.this, credentials, Toast.LENGTH_LONG).show();
                Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL + "users/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                API api = retrofit.create(API.class);

                Call<Token> tokenCall = api.addNewUser(email, name, password, number);

                tokenCall.enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "Account Successfully Created", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            editor.putString("TOKEN", response.body().getToken());
                            editor.commit();

                            Intent navigate = new Intent(RegistrationActivity.this, MainActivity.class);
                            startActivity(navigate);

                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Log.d("ERROR", t.getMessage());
                        progressDialog.dismiss();
                       Toast.makeText(RegistrationActivity.this, "Please Try Again ! Serve Side Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}