package com.niken.eventq.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.niken.eventq.Model.User.User;
import com.niken.eventq.Model.User.UserModel;
import com.niken.eventq.R;
import com.niken.eventq.Rest.APICall;
import com.niken.eventq.Rest.APIClient;
import com.niken.eventq.Session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    Button btnLogin, btnDaftar;
    TextView txtEmail, txtPass;

    ProgressDialog loading;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnDaftar = findViewById(R.id.btnDaftar);
        btnLogin = findViewById(R.id.btnLogin);
        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPass);

        sessionManager = new SessionManager(getApplicationContext());

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TO DO LOGIN
                String email = txtEmail.getText().toString();
                String password = txtPass.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()) {
                    doSignIn(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, "data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void doSignIn(final String email, final String password) {
        loading = new ProgressDialog(LoginActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        loading.setMessage("Authenticating..");
        loading.show();
        APICall api = APIClient.getRetrofit().create(APICall.class);
        Call<UserModel> call = api.postLogin(email, password);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                // if untuk intent ke fragment
                if(response.body().getCode() == 200 ) {
                    User user = response.body().getData();
                    sessionManager.CreateLoginSession(user.getId());
                    sessionManager.CreateUser(response.body().getUser().getName());
                    Intent dashboard = new Intent(LoginActivity.this, MainActivity.class);
                    dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(dashboard);
                    finish();
                }
                else if (response.body().getCode() == 500) {
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
                else if (response.body().getCode() == 400){
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
                else {
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "jaringan bermasalah", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });
    }
}
