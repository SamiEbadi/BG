package com.cartrack.android.bartshouldirun;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PersonViewHolder>{

    List<MainPageTrain> persons;

    RecyclerViewAdapter(List<MainPageTrain> persons){
        this.persons = persons;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.train, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.minutes.setText(persons.get(position).XmlDestination);
        holder.trainName.setText(persons.get(position).getMinuteString());
        holder.backgroundLayout.setBackgroundColor(persons.get(position).getColor());
        if( persons.get(position).IsEmpty ){
            holder.trainName.setAlpha(0.0f);
            holder.minutes.setAlpha(0.0f);
            holder.backgroundLayout.setAlpha(0.0f);
        }else{
            holder.trainName.setAlpha(1.0f);
            holder.minutes.setAlpha(1.0f);
            holder.backgroundLayout.setAlpha(1.0f);
        }
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        LinearLayout backgroundLayout;
        TextView minutes;
        TextView trainName;

        PersonViewHolder(View itemView) {
            super(itemView);
            minutes = (TextView)itemView.findViewById(R.id.destination);
            trainName = (TextView)itemView.findViewById(R.id.time_left);
            backgroundLayout = (LinearLayout) itemView.findViewById(R.id.background_layout);
        }
    }
}