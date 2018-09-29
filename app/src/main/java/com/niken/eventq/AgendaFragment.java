package com.niken.eventq;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.niken.eventq.Activity.EventAkanDatangActivity;
import com.niken.eventq.Activity.EventHistoryActivity;
import com.niken.eventq.Adapter.EventGridAdapter;
import com.niken.eventq.Model.Event.EventModel;
import com.niken.eventq.Rest.APICall;
import com.niken.eventq.Rest.APIClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.niken.eventq.R.drawable.datakosong;


/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaFragment extends Fragment {

    EventModel list_event;
    RecyclerView mView, rvHistory;
    TextView allAkandatang, history;
    ProgressDialog loading;
    ImageView imgDataKosong1, imgDataKosong2;

    public AgendaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agenda, container, false);
        mView = (RecyclerView) view.findViewById(R.id.rvAkandatang);
        allAkandatang = (TextView) view.findViewById(R.id.allAkandatang);
        rvHistory = (RecyclerView) view.findViewById(R.id.rvHistory);
        history = (TextView) view.findViewById(R.id.histori);
        imgDataKosong1 = (ImageView) view.findViewById(R.id.imgDataKosong1);
        imgDataKosong2 = (ImageView) view.findViewById(R.id.imgDataKosong2);

        allAkandatang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EventAkanDatangActivity.class);
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EventHistoryActivity.class);
                startActivity(intent);
            }
        });

        LinearLayoutManager upcoming = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mView.setLayoutManager(upcoming);

        LinearLayoutManager history = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvHistory.setLayoutManager(history);

        showEventUpcoming();
        showEventHistory();
        return view;
    }

    private void showEventUpcoming () {
        loading = new ProgressDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
        loading.setMessage("Memuat ..");
        loading.show();
        APICall api = APIClient.getRetrofit().create(APICall.class);
        Call<EventModel> call = api.getNewEvent();

        call.enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                if (response.body().getCode() == 200) {
                    list_event = response.body();
                    EventGridAdapter adapter = new EventGridAdapter(getContext(), list_event);
                    mView.setAdapter(adapter);
                    loading.dismiss();
                } else {
                    imgDataKosong1.setImageResource(R.drawable.datakosong);
                }
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                Toast.makeText(getContext(), "jaringan anda bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEventHistory () {
        APICall api = APIClient.getRetrofit().create(APICall.class);
        Call<EventModel> call = api.getHistory();

        call.enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                if (response.body().getCode() == 200) {
                    list_event = response.body();
                    EventGridAdapter adapter = new EventGridAdapter(getContext(), list_event);
                    rvHistory.setAdapter(adapter);
                    loading.dismiss();
                } else {
                    imgDataKosong1.setImageResource(R.drawable.datakosong);
                }
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                Toast.makeText(getContext(), "jaringan anda bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
