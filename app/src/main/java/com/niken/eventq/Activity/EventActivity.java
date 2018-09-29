package com.niken.eventq.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.Button;
import android.widget.Toast;

import com.niken.eventq.Adapter.EventAdapter;
import com.niken.eventq.Model.Event.Event;
import com.niken.eventq.Model.Event.EventModel;
import com.niken.eventq.R;
import com.niken.eventq.Rest.APICall;
import com.niken.eventq.Rest.APIClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventActivity extends AppCompatActivity {

    RecyclerView rvEvent;
    ProgressDialog loading;
    EventModel list_event;
    SearchView searchView;
    List<Event> event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        rvEvent = (RecyclerView) findViewById(R.id.rvAllEvent);
        searchView = (SearchView) findViewById(R.id.search);
        LinearLayoutManager listAcara = new LinearLayoutManager(EventActivity.this.getApplicationContext());
        rvEvent.setLayoutManager(listAcara);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchEvent(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        showAllEvent();
    }

    private void showAllEvent () {
        loading = new ProgressDialog(EventActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        loading.setMessage("Memuat ..");
        loading.show();
        APICall api = APIClient.getRetrofit().create(APICall.class);
        Call<EventModel> call = api.getEventAll();

        call.enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                loading.dismiss();
                list_event = response.body();
                EventAdapter adapter = new EventAdapter(EventActivity.this, list_event);
                rvEvent.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                Toast.makeText(EventActivity.this, "jaringan anda bermasalah", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });
    }

    private void searchEvent(final String key){
        APICall api = APIClient.getRetrofit().create(APICall.class);
        Call<EventModel> call = api.getSearch(key);

        event = new ArrayList<>();
        call.enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                list_event = response.body();
                EventAdapter adapter = new EventAdapter(EventActivity.this, list_event);
                rvEvent.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                Toast.makeText(EventActivity.this, "Something went wrong"
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }
}
