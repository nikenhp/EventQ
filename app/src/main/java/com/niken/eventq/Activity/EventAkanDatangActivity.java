package com.niken.eventq.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.niken.eventq.Adapter.EventAdapter;
import com.niken.eventq.Adapter.EventGridAdapter;
import com.niken.eventq.Model.Event.Event;
import com.niken.eventq.Model.Event.EventModel;
import com.niken.eventq.Model.EventParticipant.EventParticipantModel;
import com.niken.eventq.R;
import com.niken.eventq.Rest.APICall;
import com.niken.eventq.Rest.APIClient;
import com.niken.eventq.Session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventAkanDatangActivity extends AppCompatActivity {

    RecyclerView rvAkanDatang;
    ProgressDialog loading;
    EventModel list_event;
    List<Event> event;
    SessionManager sessionManager;
    String user_id, event_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_akan_datang);
        rvAkanDatang = (RecyclerView) findViewById(R.id.rvAkandatang);

        LinearLayoutManager upcoming = new LinearLayoutManager(EventAkanDatangActivity.this.getApplicationContext());
        rvAkanDatang.setLayoutManager(upcoming);

        sessionManager = new SessionManager(EventAkanDatangActivity.this);
        user_id  = sessionManager.getUserDetail().get(sessionManager.KEY_ID);

        showEventUpcoming();
    }

    private void showEventUpcoming () {
        loading = new ProgressDialog(EventAkanDatangActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        loading.setMessage("Memuat ..");
        loading.show();
        APICall api = APIClient.getRetrofit().create(APICall.class);
        Call<EventModel> call = api.getNewEvent();

        call.enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                if (response.body().getCode() == 200) {
                    list_event = response.body();
                    EventAdapter adapter = new EventAdapter(EventAkanDatangActivity.this, list_event);
                    rvAkanDatang.setAdapter(adapter);
                    loading.dismiss();
                }else {
                    Toast.makeText(EventAkanDatangActivity.this, "Event Baru tidak tersedia", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                Toast.makeText(EventAkanDatangActivity.this, "jaringan anda bermasalah", Toast.LENGTH_SHORT).show();
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
                EventModel item_event = response.body();
                EventGridAdapter adapter = new EventGridAdapter(event);
                adapter.setData(item_event.getData());
                rvAkanDatang.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                Toast.makeText(EventAkanDatangActivity.this, "Something went wrong"
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getString(R.string.searchTxt));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String keyword) {
                searchEvent(keyword);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

}
