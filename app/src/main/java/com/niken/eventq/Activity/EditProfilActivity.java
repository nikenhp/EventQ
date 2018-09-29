package com.niken.eventq.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.niken.eventq.Model.Region.Regency;
import com.niken.eventq.Model.Region.RegencyModel;
import com.niken.eventq.Model.User.UserModel;
import com.niken.eventq.R;
import com.niken.eventq.Rest.APICall;
import com.niken.eventq.Rest.APIClient;
import com.niken.eventq.Session.SessionManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.niken.eventq.Controller.AppConfig.BASE_PROFILE_URL;

public class EditProfilActivity extends AppCompatActivity {

    String mediaPath= "";

    EditText txtNama, txtEmail, txtAlamat, txtPass;
    CircleImageView imgProf;
    Button btnUpdate;
    Spinner spRegion, spGender;
    SessionManager sessionManager;

    ArrayList<String> dataRegency;
    String[] stringIdRegency;
    ProgressDialog loading;

    MultipartBody.Part body;
    RequestBody filename;
    String name, regency_id, email, address, gender, id, password;
    String list_name, list_regency_id, list_email, list_address, list_gender;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        imgProf = (CircleImageView) findViewById(R.id.imgProf);
        txtNama = (EditText) findViewById(R.id.txtNama);
        txtAlamat = (EditText) findViewById(R.id.txtAlamat);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        spGender = (Spinner) findViewById(R.id.spGender);
        spRegion = (Spinner) findViewById(R.id.spRegion);
        txtPass = (EditText) findViewById(R.id.txtPass);


        sessionManager = new SessionManager(EditProfilActivity.this);
        id  = sessionManager.getUserDetail().get(sessionManager.KEY_ID);
        showDetailUser(id);
        loadDataRegency();

        imgProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = txtNama.getText().toString();
                email = txtEmail.getText().toString();
                address = txtAlamat.getText().toString();
                regency_id = stringIdRegency[spRegion.getSelectedItemPosition()];
                gender = spGender.getSelectedItem().toString();

                id  = sessionManager.getUserDetail().get(sessionManager.KEY_ID);

                if (!email.isEmpty() && !name.isEmpty() && !address.isEmpty() && !gender.isEmpty()) {
                    updateAll();
                } else {
                    Toast.makeText(EditProfilActivity.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        requestPermission();
        if (requestCode == 0 && resultCode == RESULT_OK && data!= null)
        {
            Uri selectedImage = data.getData();
            Log.v("uri", selectedImage.toString());
            String[] filePathColumn = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

            if(cursor != null) {
                cursor.moveToFirst();
                int indexImg = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(indexImg);

                if (mediaPath != null) {
                    File img = new File(mediaPath);
                    imgProf.setImageBitmap(BitmapFactory.decodeFile(img.getAbsolutePath()));
                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), img);
                    body = MultipartBody.Part.createFormData("photo", img.getName(), reqFile);
                    filename = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

                    updatePhoto();
                }
            }
        } else {
            Toast.makeText(this, "you havn't picked any file", Toast.LENGTH_SHORT).show();
        }
    }
    private void requestPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
            }
        }
    }

    //Fungsi Untuk buka Gallery
    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,0);
    }

    private void showDetailUser (final String id) {
        APICall api = APIClient.getRetrofit().create(APICall.class);
        Call<UserModel> call = api.getDetailUser(id);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                txtNama.setText(response.body().getData().getName());
                txtAlamat.setText(response.body().getData().getAddress());
                txtEmail.setText(response.body().getData().getEmail());

                list_name = response.body().getData().getName();
                list_address = response.body().getData().getAddress();
                list_email = response.body().getData().getEmail();
                list_gender = (String) response.body().getData().getGender();
                list_regency_id = response.body().getData().getRegency_id();

                password = response.body().getData().getPassword();
                Picasso.with(EditProfilActivity.this)
                        .load(BASE_PROFILE_URL + response.body().getData().getPhoto())
                        .into(imgProf);
            }
            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }

    private  void loadDataRegency() {
        APICall api = APIClient.getRetrofit().create(APICall.class);
        Call<RegencyModel> call = api.getRegency();

        call.enqueue(new Callback<RegencyModel>() {
            @Override
            public void onResponse(Call<RegencyModel> call, Response<RegencyModel> response) {
                List<Regency> dataRegencyArrayList = response.body().getData();
                dataRegency = new ArrayList<String>();
                String[] stringArray = new String[dataRegencyArrayList.size()];
                stringIdRegency = new String[dataRegencyArrayList.size()];
                Log.v("", dataRegencyArrayList.size() + "");
                for (int i = 0 ; i<dataRegencyArrayList.size() ; i++)
                {
                    Log.v("", dataRegencyArrayList.get(i).getId());
                    stringArray[i] = dataRegencyArrayList.get(i).getName();
                    stringIdRegency[i] = dataRegencyArrayList.get(i).getId();

                }
                // inisialiasi Array Adapter dengan memasukkan string array di atas
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProfilActivity.this,
                        android.R.layout.simple_spinner_item, stringArray);

                // mengeset Array Adapter tersebut ke Spinner
                spRegion.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<RegencyModel> call, Throwable t) {
            }
        });
    }

    public void updateAll() {
        loading = new ProgressDialog(EditProfilActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        loading.setMessage("Update data terbaru ...");
        loading.show();
        APICall api = APIClient.getRetrofit().create(APICall.class);
        Call<UserModel> call = api.postUpdate(id, name, email, address, gender, regency_id);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.body().getCode() == 400 || response.body().getCode() == 401 || response.body().getCode() == 200 || response.body().getCode()==300) {
                    loading.dismiss();
                    Toast.makeText(EditProfilActivity.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    Intent dashboard = new Intent(EditProfilActivity.this, MainActivity.class);
                    startActivity(dashboard);
                    finish();
                } else {
                    loading.dismiss();
                    Toast.makeText(EditProfilActivity.this, "Gagal Update", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(EditProfilActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });
    }

    public void updatePhoto() {
        loading = new ProgressDialog(EditProfilActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        loading.setMessage("Update foto profile ...");
        loading.show();
        APICall api = APIClient.getRetrofit().create(APICall.class);
        Call<UserModel> call = api.postUpdatePhoto(id, list_name, list_email, list_address, list_gender, list_regency_id, body, filename);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.body().getCode() == 400 || response.body().getCode() == 401) {
                    loading.dismiss();
                    Toast.makeText(EditProfilActivity.this, "Photo berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    Intent dashboard = new Intent(EditProfilActivity.this, MainActivity.class);
                    startActivity(dashboard);
                    finish();
                } else if (response.body().getCode() == 200 || response.body().getCode()== 300){
                    loading.dismiss();
                    Toast.makeText(EditProfilActivity.this, "Silahkan konfirmasi email baru terlebih dahulu", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(EditProfilActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(EditProfilActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });
    }
}
