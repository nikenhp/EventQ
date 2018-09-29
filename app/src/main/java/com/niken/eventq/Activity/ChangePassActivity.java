package com.niken.eventq.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.niken.eventq.Model.User.User;
import com.niken.eventq.Model.User.UserModel;
import com.niken.eventq.R;
import com.niken.eventq.Rest.APICall;
import com.niken.eventq.Rest.APIClient;
import com.niken.eventq.Session.SessionManager;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.niken.eventq.Controller.AppConfig.BASE_PROFILE_URL;

public class ChangePassActivity extends AppCompatActivity {

    EditText txtPassNow, txtPassNew, txtPassKonf;
    Button btnChangePass;
    SessionManager sessionManager;
    String passNow, passNew, passKonf;
    String id, name, address, email, gender, regency_id;

    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        txtPassNow = (EditText) findViewById(R.id.txtPassNow);
        txtPassNew = (EditText) findViewById(R.id.txtPassNew);
        txtPassKonf = (EditText) findViewById(R.id.txtPassKonf);
        btnChangePass = (Button) findViewById(R.id.btnChangPass);

        sessionManager = new SessionManager(getApplicationContext());
        id  = sessionManager.getUserDetail().get(sessionManager.KEY_ID);

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passNow = txtPassNow.getText().toString();
                passNew = txtPassNew.getText().toString();
                passKonf = txtPassKonf.getText().toString();

                if (!passNow.isEmpty() && !passNew.isEmpty() && !passKonf.isEmpty()) {
                    doUpdatePass(id, passNow, passNew);
                } else {
                    Toast.makeText(ChangePassActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void doUpdatePass(final String id, final String oldpassword, final String newpassword) {
        loading = new ProgressDialog(ChangePassActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        loading.setMessage("Update Password ...");
        loading.show();
        APICall api = APIClient.getRetrofit().create(APICall.class);
        Call<UserModel> call = api.postPass(id, oldpassword, newpassword);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (newpassword.equals(passKonf)) {
                    if(response.body().getCode() == 200 ) {
                        loading.dismiss();
                        Intent intent = new Intent(ChangePassActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (response.body().getCode() == 300){
                        loading.dismiss();
                        Toast.makeText(ChangePassActivity.this, "sandi lama tidak sesuai", Toast.LENGTH_SHORT).show();
                    } else {
                        loading.dismiss();
                        Toast.makeText(ChangePassActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePassActivity.this, "Sandi baru dan konfirmasi sandi tidak cocok", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(ChangePassActivity.this, "jaringan bermasalah", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });
    }
}
