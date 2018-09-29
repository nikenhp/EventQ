package com.niken.eventq.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.niken.eventq.Controller.AppConfig;
import com.niken.eventq.Controller.DateFormator;
import com.niken.eventq.Model.Event.Event;
import com.niken.eventq.Model.EventParticipant.EventParticipantModel;
import com.niken.eventq.R;
import com.niken.eventq.Rest.APICall;
import com.niken.eventq.Rest.APIClient;
import com.niken.eventq.Session.SessionManager;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.niken.eventq.Controller.AppConfig.BASE_POSTER_URL;

public class DetailEventActivity extends AppCompatActivity {

    public static String ITEM_ID = "item_id";
    public static String ITEM_NAME = "item_name";
    public static String ITEM_LOCATION = "item_location";
    public static String ITEM_KUOTA = "item_kuota";
    public static String ITEM_TANGGAL = "item_tanggal";
    public static String ITEM_HTM = "item_htm";
    public static String ITEM_DESC = "item_desc";
    public static String ITEM_PHOTO = "item_photo";
    public static String IS_DAFTAR = "is_daftar";

    ImageView backdrop;
    TextView txtLocation, txtKuota, txtTanggal, txtHtm, txtDesc;
    Toolbar toolbar;
    Button btnDaftar;

    Event list_event;
    String user_id, event_id;
    String name, location, tanggal, htm, photo, desc, kuota;
    SessionManager sessionManager;
    ProgressDialog loading;
    Dialog popUp;

    private boolean isDaftar = false;
    private int daftar;


    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtKuota = (TextView) findViewById(R.id.txtKuota);
        txtTanggal = (TextView) findViewById(R.id.txtTanggal);
        txtHtm = (TextView) findViewById(R.id.txtHtm);
        txtDesc = (TextView) findViewById(R.id.txtdesc);
        backdrop = (ImageView) findViewById(R.id.backdrop);
        btnDaftar = (Button) findViewById(R.id.btnDaftar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        popUp = new Dialog(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        list_event = new GsonBuilder()
//                .create()
//                .fromJson(getIntent().getStringExtra("event"), Event.class);

        name = getIntent().getStringExtra(ITEM_NAME);
        location = getIntent().getStringExtra(ITEM_LOCATION);
        kuota = getIntent().getStringExtra(ITEM_ID);
        tanggal = getIntent().getStringExtra(ITEM_TANGGAL);
        htm  = getIntent().getStringExtra(ITEM_HTM);
        desc = getIntent().getStringExtra(ITEM_DESC);
        photo = getIntent().getStringExtra(ITEM_PHOTO);

        Log.d("Test", "onCreate: " + kuota);
        list_event = getIntent().getParcelableExtra(AppConfig.EVENT_DETAIL);

        Picasso.with(DetailEventActivity.this)
                .load(BASE_POSTER_URL+photo)
                .into(backdrop);

        txtLocation.setText(location);
        txtKuota.setText(kuota);
        txtTanggal.setText(tanggal);
        txtHtm.setText(htm);
        txtDesc.setText(desc);

        getSupportActionBar().setTitle(name);

        event_id = getIntent().getStringExtra(ITEM_ID);
        sessionManager = new SessionManager(DetailEventActivity.this);
        user_id  = sessionManager.getUserDetail().get(sessionManager.KEY_ID);

        daftar = getIntent().getIntExtra(IS_DAFTAR, 0);
        if (daftar == 1){
            isDaftar = true;
            btnDaftar.setText("Anda Telah terdaftar");
        }

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDaftar){
                    showDialog();
                }else {
                    showPopup();
                }
            }
        });

    }

    private void registerEvent (final String user_id, final String event_id) {
        loading = new ProgressDialog(DetailEventActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        loading.setMessage("Loading ..");
        loading.show();
        APICall api = APIClient.getRetrofit().create(APICall.class);
        Call<EventParticipantModel> call = api.postEventPart(user_id, event_id);

        call.enqueue(new Callback<EventParticipantModel>() {
            @Override
            public void onResponse(Call<EventParticipantModel> call, Response<EventParticipantModel> response) {
                if (response.body().getCode() == 200) {
                    loading.dismiss();
                    Toast.makeText(DetailEventActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    showPopup();
                } else if (response.body().getCode() == 201) {
                    loading.dismiss();
                    Toast.makeText(DetailEventActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else if(response.body().getCode() == 202) {
                    loading.dismiss();
                    Toast.makeText(DetailEventActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else if (response.body().getCode() == 203) {
                    loading.dismiss();
                    Toast.makeText(DetailEventActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    showPopup();
                } else if(response.body().getCode() == 500){
                    loading.dismiss();
                    Toast.makeText(DetailEventActivity.this, "Event ini telah berakhir", Toast.LENGTH_SHORT).show();
                } else {
                    loading.dismiss();
                    Toast.makeText(DetailEventActivity.this, "Gagal mendaftar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventParticipantModel> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(DetailEventActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder konfirmasiPendaftaran = new AlertDialog.Builder(this);
        konfirmasiPendaftaran.setTitle("Daftar event?");

        konfirmasiPendaftaran.setMessage("Klik ya untuk melanjutkan pendaftaran")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        event_id = getIntent().getStringExtra(ITEM_ID);
                        sessionManager = new SessionManager(DetailEventActivity.this);
                        user_id  = sessionManager.getUserDetail().get(sessionManager.KEY_ID);
                        registerEvent(user_id, event_id);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = konfirmasiPendaftaran.create();
        alertDialog.show();
    }

    public void showPopup() {
        TextView txtclose, txtName, txtEmail, txtEventName, txtEventLokasi, txtEventBiaya;
        popUp.setContentView(R.layout.custompopup);

        txtclose =(TextView) popUp.findViewById(R.id.txtclose);
        txtName = (TextView) popUp.findViewById(R.id.txtNama);
        txtEmail = (TextView) popUp.findViewById(R.id.txtEmail);
        txtEventName = (TextView) popUp.findViewById(R.id.txtEventName);
        txtEventLokasi = (TextView) popUp.findViewById(R.id.txtEventLokasi);
        txtEventBiaya = (TextView) popUp.findViewById(R.id.txtEventBiaya);


        txtName.setText(sessionManager.getUserDetail().get(sessionManager.KEY_NAMAUSER));
        txtEmail.setText(sessionManager.getUserDetail().get(sessionManager.KEY_EMAILUSER));

        txtEventName.setText(name);
        txtEventLokasi.setText(location);
        txtEventBiaya.setText(htm);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });
        popUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUp.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            super.onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
