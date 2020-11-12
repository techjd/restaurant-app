package com.techjd.swagatcorner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.techjd.swagatcorner.models.Users;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StartActivity extends AppCompatActivity {

    private ProgressDialog progressDialog ;
    private static int RC_SIGN_IN = 1;
    private SignInButton signInButton;
    private GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("1094645218276-e0omn9bna89nioudcbimo73pm9cd0bpd.apps.googleusercontent.com")
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        googleSignInClient.silentSignIn().addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
            @Override
            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                try {
                    GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                    String token =  googleSignInAccount.getIdToken();
                    Log.d("IS IT DIFFERENT", token);

                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    intent.putExtra("TRY THIS TOKEN", token);
                    startActivity(intent);
                    finish();

                } catch (ApiException e) {
                    e.printStackTrace();
                }

            }
        });

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if (googleSignInAccount == null) {
            signInButton = findViewById(R.id.sign_in_button);
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog = new ProgressDialog(view.getContext());
                    progressDialog.setTitle("Loading");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    signIn();
                    progressDialog.dismiss();
                }
            });
        } else {


            updateUI();
        }
    }

    private void signIn() {


        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            String idToken = account.getIdToken();
            String name = account.getDisplayName();
            String id = account.getId();



            Log.d("TOKEN", idToken);
            Log.d("NAME", name);
            Log.d("ID", id);
            updateUI();
        } catch (ApiException e) {
            Log.d("SIGNIN", String.valueOf(e.getMessage()));
        }
    }

    private void updateUI() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://agile-everglades-70176.herokuapp.com/api/users/").addConverterFactory(GsonConverterFactory.create()).build();

        API api = retrofit.create(API.class);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplication());
        String token = account.getIdToken();
        String sub = account.getId();
        String email = account.getEmail();
        String name = account.getDisplayName();

//        Log.d("TOEKN", token);
        Call<Users> usersCall = api.addUser(token, sub, email, name);

        usersCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(StartActivity.this, "User Added To The Database Successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {

            }
        });


        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }
}