package com.niken.eventq.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.niken.eventq.R;
import com.niken.eventq.Model.User.UserModel;
import com.niken.eventq.Rest.APICall;
import com.niken.eventq.Rest.APIClient;


import retrofit2.Call;
import retrofit2.Callback;

public class SignUpActivity extends AppCompatActivity {

    Button btnDaftar;
    EditText txtNama, txtEmail, txtPass;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtNama = (EditText) findViewById(R.id.txtNama);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPass = (EditText) findViewById(R.id.txtPass);

        btnDaftar = (Button) findViewById(R.id.btnDaftar);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TO DO SIGN UP
                String email = txtEmail.getText().toString();
                String password = txtPass.getText().toString();
                String name = txtNama.getText().toString();
                if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty()) {
                    doSignUp(name, email, password);
                } else {
                    Toast.makeText(SignUpActivity.this, "data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void doSignUp(String name, String email, String password){
        loading = new ProgressDialog(SignUpActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        loading.setMessage("Registering..");
        loading.show();
        APICall api = APIClient.getRetrofit().create(APICall.class);
        Call<UserModel> call = api.postRegister(name, email, password);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, retrofit2.Response<UserModel> response) {
                if(response.code() == 200 ){
                    loading.dismiss();
                    Toast.makeText(SignUpActivity.this, "Silahkan Konfirmasi email anda untuk login", Toast.LENGTH_SHORT).show();
                    toSignIn();
                    finish();
                }else{
                    Toast.makeText(SignUpActivity.this, "Data sudah terdaftar", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void toSignIn() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
