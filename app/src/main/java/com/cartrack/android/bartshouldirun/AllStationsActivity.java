package com.cartrack.android.bartshouldirun;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class AllStationsActivity extends AppCompatActivity implements ScrollViewListener {

    private ObservableScrollView mScrollView;
    private View mShadow;
    private boolean isShadowVisible = false;
    private int mShadowThresholdInPixel = -1;
    private View mEastBay;
    private View mEastBayFill;
    private boolean isEastBayFilled;
    private View mSanFrancisco;
    private View mSanFranciscoFill;
    private View mSanFranciscoEmpty;
    private boolean isSanFranciscoFilled;
    private View mHeader;
    private View mContentLayout;
    private RelativeLayout mRoot;
    private int mScrollY;
    private int mCurrentTopMargin;
    private View mPittsburgLayout;
    private View mMacArthurLayout;
    private View mWestOaklandLayout;
    private View mLakeMerritteLayout;
    private View mHaywardLayout;
    private View mEmbarcaderoLayout;
    private View mMillbraeLayout;
    private AppCompatTextView mSelectedSignText;
    private AppCompatTextView mOriginalSelectedSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stations);
        mRoot = (RelativeLayout)findViewById(R.id.all_stations_root);
        mPittsburgLayout = findViewById(R.id.pittsburg_layout);
        mMacArthurLayout = findViewById(R.id.macarthur_layout);
        mWestOaklandLayout = findViewById(R.id.west_oakland_layout);
        mLakeMerritteLayout = findViewById(R.id.lake_merritte_layout);
        mHaywardLayout = findViewById(R.id.hayward_layout);
        mEmbarcaderoLayout = findViewById(R.id.embarcadero_layout);
        mMillbraeLayout = findViewById(R.id.millbrae_layout);
        mScrollView = (ObservableScrollView)findViewById(R.id.all_stations_scroll_view);
        initSanFrancisco();
        mShadow = findViewById(R.id.header_shadow);
        mScrollView.setScrollViewListener(this);
        mEastBay = findViewById(R.id.east_bay);
        mEastBayFill = findViewById(R.id.east_bay_fill);
        mSanFranciscoFill = findViewById(R.id.san_francisco_fill);
        mSanFranciscoEmpty = findViewById(R.id.san_francisco_empty);
        mHeader = findViewById(R.id.all_stations_header);
        mContentLayout = findViewById(R.id.content_layout);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mSelectedSignText != null) {
            mSelectedSignText.setAlpha(0);
            AppCompatTextView copy = new AppCompatTextView(this);
            copy.setText(mSelectedSignText.getText());
            copy.setTextSize(15);
            copy.setTextColor(mSelectedSignText.getTextColors());
            copy.setTypeface(null, Typeface.BOLD);
            copy.setBackground(mSelectedSignText.getBackground());

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.topMargin = 10;
            copy.setLayoutParams(params);
            mSelectedSignText = copy;
            mRoot.addView(mSelectedSignText);

            ObjectAnimator moveDown = ObjectAnimator.ofFloat(copy, "translationY", mCurrentTopMargin - 10);
            moveDown.setStartDelay(70);
            moveDown.setDuration(200);
            moveDown.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mOriginalSelectedSign.setAlpha(1);
                    showHeader();
                    showContents();
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            moveDown.start();
        }
    }



    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        mScrollY = y;
        if(y < getShadowThreshold()){
            showHideShadow(false);
        }else if(!isShadowVisible){
            showHideShadow(true);
        }

        if(y <= 0){
            isEastBayFilled = false;
            fillEmptyEastBay(false);
        }else if(!isEastBayFilled){
            isEastBayFilled = true;
            fillEmptyEastBay(true);
        }
        if(y >= mSanFrancisco.getY() && !isSanFranciscoFilled){
            isSanFranciscoFilled = true;
            showHideEastBay(false);
            fillEmptySanFrancisco(true);
        }else if(y < mSanFrancisco.getY() && isSanFranciscoFilled){
            isSanFranciscoFilled = false;
            fillEmptySanFrancisco(false);
            showHideEastBay(true);
        }
    }

    private void initSanFrancisco(){
        LinearLayout layout = (LinearLayout)mScrollView.getChildAt(0);
        mSanFrancisco = layout.getChildAt(1);
    }

    private void fillEmptySanFrancisco(boolean fill) {
        if (fill) {
            mSanFrancisco.setAlpha(0);
            mSanFranciscoEmpty.setAlpha(1);
            ObjectAnimator fillAnim = ObjectAnimator.ofFloat(mSanFranciscoFill, "alpha", 1);
            fillAnim.setDuration(300);
            fillAnim.start();
            ObjectAnimator emptyAnim = ObjectAnimator.ofFloat(mSanFranciscoEmpty, "alpha", 0);
            emptyAnim.setDuration(300);
            emptyAnim.start();
        } else {
            mSanFrancisco.setAlpha(1);
            mSanFranciscoFill.setAlpha(0);
            // To fix the bug of two san francisco
            ObjectAnimator fillAnim = ObjectAnimator.ofFloat(mSanFranciscoFill, "alpha", 0);
            fillAnim.setDuration(300);
            fillAnim.start();
        }

    }

    private void showHideShadow(boolean show){
        isShadowVisible = show;
        float alpha = (show)?1f:0f;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mShadow,"alpha",alpha);
        objectAnimator.setDuration(300);
        objectAnimator.start();
    }

    private int getShadowThreshold(){
        if(mShadowThresholdInPixel < 0){
            mShadowThresholdInPixel = (int) getResources().getDimension(R.dimen.shadow_threshold);
        }
        return mShadowThresholdInPixel;
    }

    private void fillEmptyEastBay(boolean fill){
        float fillAlpha = (fill)?1:0;
        ObjectAnimator fillAnimator = ObjectAnimator.ofFloat(mEastBayFill, "alpha", fillAlpha);
        fillAnimator.setDuration(300);
        ObjectAnimator emptyAnimator = ObjectAnimator.ofFloat(mEastBay, "alpha", 1 - fillAlpha);
        emptyAnimator.setDuration(300);
        fillAnimator.start();
        emptyAnimator.start();
    }

    private void showHideEastBay(boolean show){
        float alpha = (show)?1f:0f;
        if(show){
            ObjectAnimator moveAnim = ObjectAnimator.ofFloat(mEastBayFill, "translationY", 0);
            moveAnim.setDuration(200);
            moveAnim.start();
        }else {
            ObjectAnimator moveAnim = ObjectAnimator.ofFloat(mEastBayFill, "translationY", -100);
            moveAnim.setDuration(200);
            moveAnim.start();
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mEastBay,"alpha",alpha);
        objectAnimator.setDuration(300);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(mEastBayFill,"alpha",alpha);
        objectAnimator2.setDuration(300);
        objectAnimator2.start();
    }

    public void onStationClick(View view){
        onStationClick((AppCompatTextView) view, 0);
    }

    public void onStationClick2(View view){
        onStationClick((AppCompatTextView) view, (int) mPittsburgLayout.getY());
    }

    public void onStationClick3(View view){
        onStationClick((AppCompatTextView) view, (int) (mPittsburgLayout.getY() + mMacArthurLayout.getY()));
    }

    public void onStationClick4(View view){
        onStationClick((AppCompatTextView) view, (int) (mWestOaklandLayout.getY() + mPittsburgLayout.getY() + mMacArthurLayout.getY()));
    }

    public void onStationClick5(View view){
        onStationClick((AppCompatTextView) view, (int) mLakeMerritteLayout.getY());
    }

    public void onStationClick6(View view){
        onStationClick((AppCompatTextView) view, (int) mHaywardLayout.getY());
    }

    public void onStationClick7(View view){
        onStationClick((AppCompatTextView) view, (int) mEmbarcaderoLayout.getY() - getShadowThreshold());
    }

    public void onStationClick8(View view){
        onStationClick((AppCompatTextView) view, (int) (mMillbraeLayout.getY() + mEmbarcaderoLayout.getY() - getShadowThreshold()));
    }

    private void onStationClick(final AppCompatTextView view, int topMargin) {
        mOriginalSelectedSign= view;
        view.setAlpha(0);
        hideHeader();
        hideContents();
        mSelectedSignText = createCopyTextView(view, topMargin);
        mRoot.addView(mSelectedSignText);
        ObjectAnimator moveUp = ObjectAnimator.ofFloat(mSelectedSignText, "translationY", -(mCurrentTopMargin - 10));
        moveUp.setDuration(200);
        moveUp.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startStationActivity(Integer.valueOf((String) view.getTag()));
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        moveUp.start();
    }

    private void hideHeader(){
        ObjectAnimator hideAnim = ObjectAnimator.ofFloat(mHeader,"alpha",0);
        hideAnim.setDuration(100);
        hideAnim.start();
    }

    private void showHeader() {
        ObjectAnimator showAnim = ObjectAnimator.ofFloat(mHeader,"alpha",1);
        showAnim.setDuration(100);
        showAnim.start();
    }

    private void hideContents(){
        ObjectAnimator hideAnim = ObjectAnimator.ofFloat(mContentLayout,"alpha",0);
        hideAnim.setDuration(100);
        hideAnim.start();
    }

    private void showContents(){
        ObjectAnimator showAnim = ObjectAnimator.ofFloat(mContentLayout,"alpha",1);
        showAnim.setDuration(100);
        showAnim.start();

        showAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mOriginalSelectedSign.setAlpha(1);
                mSelectedSignText.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
    }

    private AppCompatTextView createCopyTextView(AppCompatTextView station, int topMargin){
        AppCompatTextView copy  = new AppCompatTextView(this);
        copy.setText(station.getText());
        copy.setTextSize(15);
        copy.setTextColor(station.getTextColors());
        copy.setTypeface(null, Typeface.BOLD);
        copy.setBackground(station.getBackground());

        RelativeLayout.LayoutParams params =new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mCurrentTopMargin =(int)getStationCurrentMargin(station, topMargin);
        params.topMargin = mCurrentTopMargin;
        copy.setLayoutParams(params);
        return copy;
    }

    private float getStationCurrentMargin(AppCompatTextView station, int topMargin){
        float shadowThreshold = getResources().getDimension(R.dimen.shadow_threshold);
        return topMargin + shadowThreshold+  mContentLayout.getY() + station.getY() - mScrollY;

    }

    private void startStationActivity(int position){
        Intent intent = new Intent(AllStationsActivity.this, StationActivity.class);
        Station station =  Utils.getStation(AllStationsActivity.this, position);
        intent.putExtra(IntentHelper.STATION_NAME, station.getName());
        intent.putExtra(IntentHelper.STATION_ABBR, station.getAbbr());
        intent.putExtra(IntentHelper.STATION_PLATFORM, 2);
        intent.putExtra(IntentHelper.POSITION, position);
        AllStationsActivity.this.startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
