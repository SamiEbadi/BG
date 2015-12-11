package com.cartrack.android.bartshouldirun;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Station implements Parcelable {

    private Location mLocation;
    private String mName;
    private String mAbbr;
    public String XmlMessage;
    public ArrayList<Train> Trains;
    public int mNumberOfPlatforms;
    public Message MessageObject;

    public Station(){

    }

    public Station(String name, String abbr, double longitude, double latitude){
        mLocation = new Location("");
        mLocation.setLongitude(longitude);
        mLocation.setLatitude(latitude);
        mName = name;
        mAbbr = abbr;
        mNumberOfPlatforms = 2;
    }

    public void addTrains(List<Train> trains){
        if(trains == null){
            trains = new ArrayList<>();
        }
        trains.addAll(trains);
    }

    public double distanceTo(Location location){
        return mLocation.distanceTo(location);
    }

    public String getName(){
        return mName;
    }

    public String getAbbr(){
        return mAbbr;
    }

    public int getNumberOfPlatform(){
        return mNumberOfPlatforms;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
