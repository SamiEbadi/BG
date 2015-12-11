package com.cartrack.android.bartshouldirun;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TimeLineRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_WITH_TRAIN = 1;
    private static final int TYPE_NO_TRAIN = 2;

    Context mContext;
    ArrayList<String> mTimeList;
    ArrayList<Integer> mBoldMins;

    TimeLineRecyclerViewAdapter(Context context, ArrayList<String> times, ArrayList<Integer> boldMins){
        mContext = context;
        mTimeList = times;
        mBoldMins = boldMins;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View emptyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_line_item, parent, false);
        return new TimeLineItemViewHolder(emptyView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TimeLineItemViewHolder trainHolder = (TimeLineItemViewHolder) holder;
        trainHolder.mTimeText.setText(mTimeList.get(position));
        switch (holder.getItemViewType()) {
            case TYPE_WITH_TRAIN:
                trainHolder.mTimeText.setAlpha(1);
                trainHolder.mTimeText.setTextSize(14);
                trainHolder.mTimeText.setTypeface(null, Typeface.BOLD);
                break;
            case TYPE_NO_TRAIN:
                trainHolder.mTimeText.setAlpha(0.2f);
                trainHolder.mTimeText.setTextSize(12);
                trainHolder.mTimeText.setTypeface(null, Typeface.NORMAL);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mTimeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mBoldMins.contains(position)) {
            return TYPE_WITH_TRAIN;
        }
        return TYPE_NO_TRAIN;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class TimeLineItemViewHolder extends RecyclerView.ViewHolder {

        TextView mTimeText;

        TimeLineItemViewHolder(View itemView) {
            super(itemView);
            mTimeText = (TextView)itemView.findViewById(R.id.time_line_item_text);
        }
    }
}