package com.niken.eventq;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.niken.eventq.Activity.EditProfilActivity;
import com.niken.eventq.Activity.ImageDetailActivity;
import com.niken.eventq.Activity.SettingsActivity;
import com.niken.eventq.Model.User.UserModel;
import com.niken.eventq.Rest.APICall;
import com.niken.eventq.Rest.APIClient;
import com.niken.eventq.Session.SessionManager;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.niken.eventq.Controller.AppConfig.BASE_POSTER_URL;
import static com.niken.eventq.Controller.AppConfig.BASE_PROFILE_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    TextView txtNama, txtEmail, txtAlamat, txtGender;
    ImageView imgProf;
    Button btnEdit;
    ImageButton btnSetting;
    SessionManager sessionManager;
    Context mContext;
    ProgressDialog loading;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txtNama = (TextView) view.findViewById(R.id.txtNama);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtAlamat = (TextView) view.findViewById(R.id.txtAlamat);
        txtGender = (TextView) view.findViewById(R.id.txtGender);

        imgProf = (ImageView) view.findViewById(R.id.imgProf);
        btnEdit = (Button) view.findViewById(R.id.btnEdit);

        btnSetting = (ImageButton) view.findViewById(R.id.btnSetting);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfilActivity.class);
                startActivity(intent);
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        imgProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ImageDetailActivity.class);
                startActivity(intent);
            }
        });

        sessionManager = new SessionManager(getContext());
        String id  = sessionManager.getUserDetail().get(sessionManager.KEY_ID);
        showDetailUser(id);
        return  view;
    }

    private void showDetailUser (final String id) {
        loading = new ProgressDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
        loading.setMessage("Memuat ...");
        loading.show();
        APICall api = APIClient.getRetrofit().create(APICall.class);
        Call<UserModel> call = api.getDetailUser(id);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.code() == 200) {
                    loading.dismiss();
                    txtNama.setText(response.body().getData().getName());
                    txtAlamat.setText(response.body().getData().getAddress());
                    txtEmail.setText(response.body().getData().getEmail());
                    txtGender.setText((CharSequence) response.body().getData().getGender());
                    if (response.body().getCode() == 200) {
                        Picasso.with(getContext())
                                .load(BASE_PROFILE_URL + response.body().getData().getPhoto())
                                .into(imgProf);
                    } else if (response.body().getCode() == 300) {
                        imgProf.getResources().getDrawable(R.drawable.datakosong);
                    }
                } else {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }
}
