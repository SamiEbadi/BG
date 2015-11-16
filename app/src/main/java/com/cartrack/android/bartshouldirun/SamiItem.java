package com.cartrack.android.bartshouldirun;

/**
 * Created by Lenovo on 11/11/2015.
 */
public class SamiItem {

    public MainPageTrain mTrain1;
    public MainPageTrain mTrain2;
    public MainPageTrain mTrain3;
    public MainPageTrain mTrain4;
    public String mTimeLabel;

    public SamiItem(MainPageTrain train1, MainPageTrain train2, String timeLabel){
        init2Trains(train1, train2, timeLabel);
    }

    public SamiItem(MainPageTrain train1, MainPageTrain train2, MainPageTrain train3, String timeLabel) {
        init3Trains(train1,train2,train3,timeLabel);
    }

    public SamiItem(MainPageTrain train1, MainPageTrain train2, MainPageTrain train3, MainPageTrain train4, String timeLabel) {
        mTrain4 = train4;
        init3Trains(train1,train2,train3, timeLabel);
    }

    private void init2Trains(MainPageTrain train1, MainPageTrain train2, String timeLabel){
        mTrain1 = train1;
        mTrain2 = train2;
        mTimeLabel = timeLabel;
    }

    private void init3Trains(MainPageTrain train1, MainPageTrain train2, MainPageTrain train3, String timeLabel){
        init2Trains(train1,train2,timeLabel);
        mTrain3 = train3;
    }

}
