package com.cartrack.android.bartshouldirun;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sami Ebadi on 10/24/2015.
 */
public class StationListAdapter extends BaseAdapter {

    private Context mContext;
    private static LayoutInflater mInflator = null;
    private ArrayList<Station> mData;
    private ImageView mImageView;
    private TextView mStationName;

    public StationListAdapter(Context context, ArrayList<Station> data) {
        mContext = context;
        mInflator = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position == -1){
            View emptyView = convertView;
            if(emptyView == null)
                emptyView = mInflator.inflate(R.layout.empty_item, null);
            return emptyView;
        }else {
            View stationView = convertView;
            if (stationView == null)
                stationView = mInflator.inflate(R.layout.station_list_item, null);
//            mImageView = (ImageView) stationView.findViewById(R.id.item_image);
     mStationName = (TextView) stationView.findViewById(R.id.station_name);
            if (position % 3 == 0) {
//                mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ashby));
            } else if (position % 3 == 1) {
//                mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.montgomery));
            } else {
//                mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.elcerritoplaza));
            }
            mStationName.setText(mData.get(position).getName());
            return stationView;
        }
    }
}
