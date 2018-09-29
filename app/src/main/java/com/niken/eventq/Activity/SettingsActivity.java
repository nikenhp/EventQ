package com.niken.eventq.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.niken.eventq.R;
import com.niken.eventq.Session.SessionManager;

public class SettingsActivity extends AppCompatActivity {
    TextView tvLogout, tvChangePass, tvHubungi, tvVersi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tvLogout = (TextView) findViewById(R.id.tvLogout);
        tvChangePass = (TextView) findViewById(R.id.tvChangePass);
        tvHubungi = (TextView) findViewById(R.id.hubungiapp);
        tvVersi = (TextView) findViewById(R.id.tentangapp);

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        tvChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, ChangePassActivity.class);
                startActivity(intent);
            }
        });

        tvHubungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, HubungiKamiActivity.class);
                startActivity(intent);
            }
        });

        tvVersi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, InformasiVersiActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder konfirmasiPendaftaran = new AlertDialog.Builder(this);
        konfirmasiPendaftaran.setTitle("Logout");
        final SessionManager sessionManager = new SessionManager(this);
        konfirmasiPendaftaran.setMessage("Anda yakin ingin keluar?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sessionManager.logoutUser();

                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        finish();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
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
}
