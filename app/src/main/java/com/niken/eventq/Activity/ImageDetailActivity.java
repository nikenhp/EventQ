package com.niken.eventq.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

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

public class ImageDetailActivity extends AppCompatActivity {

    ImageView photoDetail;
    ProgressDialog loading;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        photoDetail = (ImageView) findViewById(R.id.photoDetail);
        sessionManager = new SessionManager(ImageDetailActivity.this);

        String id  = sessionManager.getUserDetail().get(sessionManager.KEY_ID);
        showDetailUser(id);
    }

    private void showDetailUser (final String id) {
        loading = new ProgressDialog(ImageDetailActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        loading.setMessage("Memuat ...");
        loading.show();
        APICall api = APIClient.getRetrofit().create(APICall.class);
        Call<UserModel> call = api.getDetailUser(id);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.code() == 200) {
                    loading.dismiss();
                    Picasso.with(ImageDetailActivity.this)
                            .load(BASE_PROFILE_URL + response.body().getData().getPhoto())
                            .into(photoDetail);
                } else {
                    Toast.makeText(ImageDetailActivity.this, "gagal mengambil gambar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }
}
