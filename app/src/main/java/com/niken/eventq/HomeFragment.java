package com.niken.eventq;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.niken.eventq.Activity.EventActivity;
import com.niken.eventq.Activity.EventAkanDatangActivity;
import com.niken.eventq.Adapter.EventGridAdapter;
import com.niken.eventq.Model.Event.Event;
import com.niken.eventq.Model.Event.EventModel;
import com.niken.eventq.Rest.APICall;
import com.niken.eventq.Rest.APIClient;
import com.niken.eventq._sliders.FragmentSlider;
import com.niken.eventq._sliders.SliderIndicator;
import com.niken.eventq._sliders.SliderPagerAdapter;
import com.niken.eventq._sliders.SliderView;
import com.niken.eventq.Adapter.EventAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;

    private SliderView sliderView;
    private LinearLayout mLinearLayout;

    RecyclerView rvAcara;
    ProgressDialog loading;
    ImageView imgDataKosong, img;
    TextView allEvent;
    EventModel list_event;
    ArrayList<String> dataEvent;
    String[] stringIdEvent;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        sliderView = (SliderView) view.findViewById(R.id.sliderView);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.pagesContainer);
        imgDataKosong = (ImageView) view.findViewById(R.id.imgDataKosong);
        allEvent = (TextView) view.findViewById(R.id.allEvent);
        img = (ImageView) view.findViewById(R.id.img);

        setupSlider();

        rvAcara = (RecyclerView) view.findViewById(R.id.rvAcara);

        LinearLayoutManager listAcara = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvAcara.setLayoutManager(listAcara);

        showAllEvent();
        allEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EventActivity.class);
                startActivity(intent);
            }
        });
        return view;

    }

    private void setupSlider() {
        APICall api = APIClient.getRetrofit().create(APICall.class);
        Call<EventModel> call = api.getNewEvent();

        call.enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                if (response.body().getCode()== 200) {
                    list_event = response.body();
                    sliderView.setDurationScroll(800);
                    List<Fragment> fragments = new ArrayList<>();

                    List<Event> dataPhotoList = response.body().getData();
                    dataEvent = new ArrayList<String>();
                    String[] stringArray = new String[dataPhotoList.size()];
                    stringIdEvent = new String[dataPhotoList.size()];
                    for (int i = 0 ; i<dataPhotoList.size() ; i++)
                    {
                        Log.v("a", String.valueOf(dataPhotoList.get(i).getId()));
                        stringArray[i] = dataPhotoList.get(i).getPhoto();
                        stringIdEvent[i] = String.valueOf(dataPhotoList.get(i).getId());
                        fragments.add(FragmentSlider.newInstance(stringArray[i]));
                    }

                    mAdapter = new SliderPagerAdapter(getFragmentManager(), fragments);
                    sliderView.setAdapter(mAdapter);
                    mIndicator = new SliderIndicator(getActivity(), mLinearLayout, sliderView, R.drawable.indicator_circle);
                    mIndicator.setPageCount(fragments.size());
                    mIndicator.show();
                } else{
                    sliderView.setDurationScroll(800);
                    List<Fragment> fragments = new ArrayList<>();
                    fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-2.jpg"));
                    fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-3.jpg"));
                    fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-4.jpg"));

                    mAdapter = new SliderPagerAdapter(getFragmentManager(), fragments);
                    sliderView.setAdapter(mAdapter);
                    mIndicator = new SliderIndicator(getActivity(), mLinearLayout, sliderView, R.drawable.indicator_circle);
                    mIndicator.setPageCount(fragments.size());
                    mIndicator.show();
                }
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                Toast.makeText(getContext(), "jaringan anda bermasalah", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showAllEvent () {
        loading = new ProgressDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
        loading.setMessage("Memuat ..");
        loading.show();
        APICall api = APIClient.getRetrofit().create(APICall.class);
        Call<EventModel> call = api.getEventAll();

        call.enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                if (response.body().getCode() == 200) {
                    loading.dismiss();
                    list_event = response.body();
                    EventGridAdapter adapter = new EventGridAdapter(getContext(), list_event);
                    rvAcara.setAdapter(adapter);
                }
                else {
                   imgDataKosong.getResources().getDrawable(R.drawable.datakosong);
                }
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                Toast.makeText(getContext(), "jaringan anda bermasalah", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });
    }
}
