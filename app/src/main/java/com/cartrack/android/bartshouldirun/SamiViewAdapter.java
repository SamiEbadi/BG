package com.cartrack.android.bartshouldirun;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SamiViewAdapter extends RecyclerView.Adapter<SamiViewAdapter.SamiViewHolder>{

    List<SamiItem> mSamiItems;

    SamiViewAdapter(List<SamiItem> samiItems){
        this.mSamiItems = samiItems;
    }

    @Override
    public SamiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sami_view_item, parent, false);
        SamiViewHolder pvh = new SamiViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(SamiViewHolder holder, int position) {
        holder.train1Name.setText(mSamiItems.get(position).mTrain1.XmlDestination);
        holder.train2Name.setText(mSamiItems.get(position).mTrain1.getMinuteString());
        holder.train1Layout.setBackgroundColor(mSamiItems.get(position).mTrain1.getColor());
        if( mSamiItems.get(position).mTrain1.IsEmpty ){
            holder.train1Name.setAlpha(0.0f);
            holder.train2Name.setAlpha(0.0f);
            holder.train1Layout.setAlpha(0.0f);
        }else{
            holder.train1Name.setAlpha(1.0f);
            holder.train2Name.setAlpha(1.0f);
            holder.train1Layout.setAlpha(1.0f);
        }
    }

    @Override
    public int getItemCount() {
        return mSamiItems.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class SamiViewHolder extends RecyclerView.ViewHolder {

        LinearLayout train1Layout;
        LinearLayout train2Layout;
        LinearLayout train3Layout;
        LinearLayout train4Layout;
        TextView train1Name;
        TextView train1Minute;
        TextView train2Name;
        TextView train2Minute;
        TextView train3Name;
        TextView train3Minute;
        TextView train4Name;
        TextView train4Minute;

        SamiViewHolder(View itemView) {
            super(itemView);
            train1Name = (TextView)itemView.findViewById(R.id.train1_name);
            train2Name = (TextView)itemView.findViewById(R.id.train1_minute);
            train1Layout = (LinearLayout) itemView.findViewById(R.id.train1_layout);
        }
    }
}