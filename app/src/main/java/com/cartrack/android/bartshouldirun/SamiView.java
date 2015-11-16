package com.cartrack.android.bartshouldirun;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.*;

class SamiView extends FrameLayout implements  OnLayoutChangeListener {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private FrameLayout mCurve;
    private float mCurveRatio = 0.3f;
    private float mMinScale = 0.75f;
    private int mItemsInCurve = 20;
    private float mCurveXPower = 10;
    private final int ITEM_HEIGHT = 175;
    private int mHeadOfScrollView = 0;
    private int mCurveHeight = 0;
    private int mRecyclerViewHeight = 0;
    private ArrayList<SamiItem> mData;
    private int mOriginalOffset = 0;

    public SamiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        // Initializing Curve
        FrameLayout.LayoutParams theCurveLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        theCurveLayoutParams.gravity = Gravity.BOTTOM;
        mCurve = new FrameLayout(context);
        mCurve.setLayoutParams(theCurveLayoutParams);
        mCurve.setBackgroundColor(Color.BLACK);
        // Initializing RecyclerView
        FrameLayout.LayoutParams theRecyclerViewLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setLayoutParams(theRecyclerViewLayoutParams);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnLayoutChangeListener(SamiView.this);
    }

    public void initSamiView(ArrayList<SamiItem> data) {
        mData = data;
        SamiViewAdapter adapter = new SamiViewAdapter(data);
        mRecyclerView.setAdapter(adapter);
        this.addView(mRecyclerView);
        this.addView(mCurve);
        setScrollListener();
        putCurveItems();
    }

    public void initSamiView(ArrayList<SamiItem> anAdapter, float aCurveRatio) {
        mCurveRatio = aCurveRatio;
        initSamiView(anAdapter);
    }

    // WARNING! Should be removed
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public int getItemCount() {
        if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
            return mRecyclerView.getAdapter().getItemCount();
        }
        return 0;
    }

    // OnLayoutChangeListener Methods
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        FrameLayout.LayoutParams theCurveLayoutParams = (FrameLayout.LayoutParams) mCurve.getLayoutParams();
        mRecyclerViewHeight = mRecyclerView.getHeight();
        mCurveHeight = (int) (mRecyclerView.getHeight() * mCurveRatio);
        theCurveLayoutParams.height = mCurveHeight;
        mOriginalOffset = (mRecyclerViewHeight - mCurveHeight) % ITEM_HEIGHT;
    }

    private void setScrollListener() {
        mRecyclerView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mHeadOfScrollView += dy;
                putCurveItems();
            }
        });
    }

    private int getIndexOfCurveFirstItem() {
        int headPlusHeight = mHeadOfScrollView + mRecyclerViewHeight;
        int minusCurveHeight = headPlusHeight - mCurveHeight;
        int finalResult = minusCurveHeight / ITEM_HEIGHT;
        return finalResult;
    }


    private void putCurveItems() {
        mCurve.removeAllViews();
        int firstItemIndex = getIndexOfCurveFirstItem();
        int upperLimit = Math.min(firstItemIndex + mItemsInCurve, mData.size());
        int lowerLimit = Math.max(0, firstItemIndex);

        for (int i = (upperLimit -1); i >= lowerLimit; i--) {
            MainPageTrain theTrain = mData.get(i).mTrain1;
            if (!theTrain.IsEmpty) {
                mCurve.addView(trainViewMaker(theTrain, i - lowerLimit));
            }
        }
    }

    private View trainViewMaker(MainPageTrain aTrainData, int curveIndex) {
        View resultTrain = LayoutInflater.from(mContext).inflate(R.layout.train, null, false);
        TextView personName = (TextView) resultTrain.findViewById(R.id.destination);
        TextView personAge = (TextView) resultTrain.findViewById(R.id.time_left);
        LinearLayout backgroundLayout = (LinearLayout) resultTrain.findViewById(R.id.background_layout);
        personName.setText(aTrainData.XmlDestination);
        personAge.setText(aTrainData.getMinuteString());
        backgroundLayout.setBackgroundColor(aTrainData.getColor());
        final float scale = mContext.getResources().getDisplayMetrics().density;
        int height = (int) (50 * scale);
        int width = (int) (200 * scale);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.FILL_PARENT);
        params.width = width;
        params.height = height;
        params.gravity = Gravity.TOP;
//        params.leftMargin = curveIndex * 50;
        params.topMargin = (int)(getTopMargin(curveIndex)*0.85);
        resultTrain.setLayoutParams(params);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(resultTrain, "scaleX",1 - thisPercentage);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(resultTrain, "scaleY",1 - thisPercentage);
        scaleXAnimator.setDuration(0);
        scaleYAnimator.setDuration(0);
        scaleXAnimator.start();
       scaleYAnimator.start();
        return resultTrain;
    }
    float thisPercentage;

    private int getTopMargin(int curveIndex) {
        int curveActualSize = mItemsInCurve * ITEM_HEIGHT;
        int itemInActualCurvePosition = getItemActualPositionInCurve(curveIndex);
        float percentage = (float)itemInActualCurvePosition / (float)curveActualSize;
        thisPercentage = Math.max(percentage,0);
        if(itemInActualCurvePosition <= 0) {
            thisPercentage = 0;
            return itemInActualCurvePosition;
        }

        float axePercentage = 1 - percentage;
        double powereAxePercentage = power(axePercentage);
        double newPercentage = 1 - powereAxePercentage;
        double result = mCurveHeight * newPercentage;
//        return itemInActualCurvePosition;
//double result = mCurveHeight * getLocationPercentage(curveIndex);
        return (int)result;
    }

    private double power(float zeroToOne){
        return Math.pow(zeroToOne,  mCurveXPower);
    }

    private int getItemActualPositionInCurve(int curveIndex) {
        int remaining = mHeadOfScrollView % ITEM_HEIGHT;
        int curveToHeight = curveIndex * ITEM_HEIGHT;
        int realRemaining = (remaining + mOriginalOffset);
        if (realRemaining == 175) {
            realRemaining = 0;
        } else if (realRemaining == 176) {
            realRemaining = 1;
        } else if (realRemaining == 177) {
            realRemaining = 2;
        } else if (realRemaining == 178) {
            realRemaining = 3;
        }
        return curveToHeight - realRemaining;
    }
}
