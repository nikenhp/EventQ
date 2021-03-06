package com.niken.eventq.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.niken.eventq.Activity.DetailEventActivity;
import com.niken.eventq.Model.Event.Event;
import com.niken.eventq.Model.Event.EventModel;
import com.niken.eventq.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.niken.eventq.Controller.AppConfig.BASE_POSTER_URL;

public class EventAdapter extends RecyclerView.Adapter <EventAdapter.EventHolder> {
    List<Event> list_event;

    private Context mContext;
    private EventModel list_data_event;

    public EventAdapter(Context mContext, EventModel list_data_event) {
        this.mContext = mContext;
        this.list_data_event = list_data_event;
        list_event = list_data_event.getData();
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_event, parent, false);

        return new EventHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventHolder holder, final int position) {

        holder.tittle.setText(list_event.get(position).getName());
        holder.kuota.setText("Kuota : " + Integer.toString(list_event.get(position).getQuota()));
        holder.htm.setText("Rp. " + Integer.toString((int) list_event.get(position).getPrice()));
        Picasso.with(holder.itemView.getContext())
                .load(BASE_POSTER_URL+list_event.get(position).getPhoto())
                .into(holder.Poster);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pindahActivity(position, view);
//                Event data = list_event.get(position);
//                Intent intent = new Intent(holder.itemView.getContext(), DetailEventActivity.class);
//                intent.putExtra("event" , new GsonBuilder().create().toJson(data));
//                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    private void pindahActivity(int position, View v) {
        Intent i = new Intent(v.getContext(), DetailEventActivity.class);
        i.putExtra(DetailEventActivity.ITEM_ID, Integer.toString(list_event.get(position).getId()));
        i.putExtra(DetailEventActivity.ITEM_NAME, list_event.get(position).getName());
        i.putExtra(DetailEventActivity.ITEM_LOCATION, list_event.get(position).getAddress());
        i.putExtra(DetailEventActivity.ITEM_KUOTA, Integer.toString(list_event.get(position).getQuota()));
        i.putExtra(DetailEventActivity.ITEM_TANGGAL, list_event.get(position).getStart_date());
        i.putExtra(DetailEventActivity.ITEM_HTM, Float.toString(list_event.get(position).getPrice()));
        i.putExtra(DetailEventActivity.ITEM_DESC, list_event.get(position).getDescription());
        i.putExtra(DetailEventActivity.ITEM_PHOTO, list_event.get(position).getPhoto());
        v.getContext().startActivity(i);

    }

    public void setData(List<Event> results) {
        this.list_event =list_event;
    }

    @Override
    public int getItemCount() {
        return list_event.size();
    }

    class EventHolder extends RecyclerView.ViewHolder {
        TextView tittle, htm, kuota;
        ImageView Poster;
        public EventHolder(View itemView) {
            super(itemView);

            tittle = (TextView) itemView.findViewById(R.id.tittle);
            htm = (TextView) itemView.findViewById(R.id.txtHtm);
            kuota = (TextView) itemView.findViewById(R.id.kuota);
            Poster = (ImageView) itemView.findViewById(R.id.poster);
        }
    }
}
