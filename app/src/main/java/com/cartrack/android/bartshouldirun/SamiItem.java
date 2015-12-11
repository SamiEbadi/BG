package com.cartrack.android.bartshouldirun;

/**
 * Created by Lenovo on 11/11/2015.
 */
public class SamiItem {

    public Train mTrain1;
    public Train mTrain2;
    public Train mTrain3;
    public Train mTrain4;
    public String mTimeLabel;

    public SamiItem(Train train1, Train train2, String timeLabel){
        init2Trains(train1, train2, timeLabel);
    }

    public SamiItem(Train train1, Train train2, Train train3, String timeLabel) {
        init3Trains(train1,train2,train3,timeLabel);
    }

    public SamiItem(Train train1, Train train2, Train train3, Train train4, String timeLabel) {
        mTrain4 = train4;
        init3Trains(train1,train2,train3, timeLabel);
    }

    private void init2Trains(Train train1, Train train2, String timeLabel){
        mTrain1 = train1;
        mTrain2 = train2;
        mTimeLabel = timeLabel;
    }

    private void init3Trains(Train train1, Train train2, Train train3, String timeLabel){
        init2Trains(train1,train2,timeLabel);
        mTrain3 = train3;
    }

}
