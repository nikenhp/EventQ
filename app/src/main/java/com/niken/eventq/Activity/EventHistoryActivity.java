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
import android.widget.EditText;
import android.widget.Toast;

import com.niken.eventq.Adapter.EventAdapter;
import com.niken.eventq.Adapter.EventGridAdapter;
import com.niken.eventq.Model.Event.Event;
import com.niken.eventq.Model.Event.EventModel;
import com.niken.eventq.Model.EventParticipant.EventParticipantModel;
import com.niken.eventq.R;
import com.niken.eventq.Rest.APICall;
import com.niken.eventq.Rest.APIClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventHistoryActivity extends AppCompatActivity {

    ProgressDialog loading;
    EventModel list_event;
    RecyclerView rvHistory;
    List<Event> event;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_history);

        rvHistory = (RecyclerView) findViewById(R.id.rvHistory);
        searchView = (SearchView) findViewById(R.id.search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        LinearLayoutManager upcoming = new LinearLayoutManager(EventHistoryActivity.this.getApplicationContext());

        rvHistory.setLayoutManager(upcoming);
        searchView.setQueryHint(getString(R.string.searchTxt));

        showEventHistory();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchEvent(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void showEventHistory () {
        APICall api = APIClient.getRetrofit().create(APICall.class);
        Call<EventModel> call = api.getHistory();

        call.enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                list_event = response.body();
                EventAdapter adapter = new EventAdapter(EventHistoryActivity.this, list_event);
                rvHistory.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                Toast.makeText(EventHistoryActivity.this, "jaringan anda bermasalah", Toast.LENGTH_SHORT).show();
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
                EventAdapter adapter = new EventAdapter(EventHistoryActivity.this, list_event);
                rvHistory.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                Toast.makeText(EventHistoryActivity.this, "Something went wrong"
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }
}
