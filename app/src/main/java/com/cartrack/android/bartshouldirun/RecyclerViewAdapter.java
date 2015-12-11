package com.cartrack.android.bartshouldirun;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_TRAIN = 1;
    private static final int TYPE_EMPTY = 2;

    Context mContext;
    HashMap<Integer, List<Train>> mTrains;
    private int mMaxMinute;

    RecyclerViewAdapter(Context context, List<Train> trains, int maxMin){
        mContext = context;
        mMaxMinute = maxMin;
        initWithTrains(trains);
    }

    private void initWithTrains(List<Train> trains){
        mTrains = new HashMap<>();
        for(Train train : trains){
            int min = train.getMinutes();
            if(mTrains.containsKey(min)) {
                mTrains.get(min).add(train);
            }else {
                List<Train> tempList = new ArrayList<>();
                tempList.add(train);
                mTrains.put(min,tempList);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_TRAIN:
                View trainView = LayoutInflater.from(parent.getContext()).inflate(R.layout.train, parent, false);
                return new TrainViewHolder(trainView);
            default:
                View emptyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_item, parent, false);
                return new EmptyViewHolder(emptyView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_TRAIN:
                Train theTrain = mTrains.get(position).get(0);
                TrainViewHolder trainHolder = (TrainViewHolder) holder;
                trainHolder.mTrainNameText.setText(theTrain.XmlDestination);
                trainHolder.mMinText.setText(theTrain.getMinuteString());
                trainHolder.mTrainNameText.setTextColor(theTrain.getColor());
                trainHolder.mMinText.setTextColor(theTrain.getColor());
                if(position == 0){
                    trainHolder.mMinMinText.setText("");
                }else{
                    trainHolder.mMinMinText.setTextColor(theTrain.getColor());
                    trainHolder.mMinMinText.setText(mContext.getString(R.string.min));
                }
                for(int i = 0; i< trainHolder.mCarsLayout.getChildCount(); i++) {
                    int trainLeadIndex = getTrainLeadIndex(theTrain.getLength());
                    LinearLayout carView = (LinearLayout)trainHolder.mCarsLayout.getChildAt(i);
                    if(i == trainLeadIndex){
                        carView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.lead_car));
                    }else{
                        carView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.car));
                    }
                    GradientDrawable background = (GradientDrawable) carView.getBackground();
                    if(i>= trainLeadIndex && i < (trainLeadIndex+theTrain.getLength())) {
                        background.setColor(theTrain.getColor());
                    }else{
                        background.setColor(Color.parseColor("#ff888888"));
                    }
                    carView.removeAllViews();
                    if(i == (trainLeadIndex + theTrain.getLength() - 1)){
                        TextView numberText = new TextView(mContext);
                        //String lengthString = theTrain.getLength() + "";
                        //numberText.setText(lengthString);
                        numberText.setTextColor(Color.parseColor("#ff000000"));
                        numberText.setTextSize(12);
                        numberText.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                        numberText.setLayoutParams(params);
                        numberText.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP);
                        carView.addView(numberText);
                    }
                }
        }
    }

    private int getTrainLeadIndex(int trainLength){
        switch (trainLength){
            case 1:
                return 5;
            case 2:
            case 3:
                return 4;
            case 4:
            case 5:
                return 3;
            case 6:
            case 7:
                return 2;
            case 8:
            case 9:
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public int getItemCount() {
        return mMaxMinute;
    }

    @Override
    public int getItemViewType(int position) {
        if (mTrains.containsKey(position)) {
            return TYPE_TRAIN;
        }
        return TYPE_EMPTY;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class TrainViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mTrainRect;
        TextView mMinText;
        TextView mMinMinText;
        TextView mTrainNameText;
        LinearLayout mCarsLayout;

        TrainViewHolder(View itemView) {
            super(itemView);
            mMinText = (TextView)itemView.findViewById(R.id.time_left);
            mTrainNameText = (TextView)itemView.findViewById(R.id.destination);
            mTrainRect = (LinearLayout) itemView.findViewById(R.id.background_layout);
            mCarsLayout = (LinearLayout) itemView.findViewById(R.id.cars_layout);
            mMinMinText = (TextView) itemView.findViewById(R.id.min_text);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout backgroundLayout;

        EmptyViewHolder(View itemView) {
            super(itemView);
            backgroundLayout = (LinearLayout) itemView.findViewById(R.id.empty_layout);
        }
    }
}