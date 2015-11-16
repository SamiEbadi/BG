package com.cartrack.android.bartshouldirun;

import android.graphics.Color;

public class MainPageTrain {

    private String mXmlMinutes;
    private int mMinutes;
    public String XmlPlatform;
    public String XmlLength;
    public String XmlDestination;
    public String XmlAbbr;
    private String mXmlHexColor;
    public boolean IsEmpty;
    private int mTrainColor;

    public MainPageTrain(){
        IsEmpty = true;
    }

    public MainPageTrain(String dest, String xmlMinutes, String xmlColor){
        XmlDestination = dest;
        setMinutes(xmlMinutes);
        setColor(xmlColor);
    }

    public void setMinutes(String xmlMinute){
        mXmlMinutes = xmlMinute;
        try{
            mMinutes = Integer.parseInt(xmlMinute);
        }catch(Exception ex){
            mMinutes = 0;
        }
    }

    public String getMinuteString(){
        return mXmlMinutes;
    }

    public int getMinutes(){
        return mMinutes;
    }

    public void setColor(String xmlHexColor){
        mXmlHexColor = xmlHexColor;
        try{
            mTrainColor = Color.parseColor(xmlHexColor);
        }catch (Exception ex){

        }
    }

    public int getColor(){
        return mTrainColor;
    }

    public String getHexColor(){
        return mXmlHexColor;
    }
}
