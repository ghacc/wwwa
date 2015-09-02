package com.britwit.worldwordwars.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

public class CountdownTimer extends View {

    public enum State {
        STOPPED,
        PLAYING
    }

    private int mTime;
    private int mTimeRemaining;
    private Paint mPaintBackground;
    private Paint mPaintForeground;
    private State mState;

    public CountdownTimer(Context context) {
        super(context);
        init();
    }

    public CountdownTimer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountdownTimer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected final void init() {
        this.mState = State.STOPPED;
    }

    private final Runnable mTicker = new Runnable() {
        public void run() {

            if (mState == State.PLAYING) {
                mTimeRemaining -= 1000l;
                invalidate();
            }

            long next = SystemClock.uptimeMillis() + 1000l;

            getHandler().postAtTime(mTicker, next);
        }
    };


    public void setTime(int millis) {
        mTime = millis;
        mTimeRemaining = millis;
    }

    public int getTime() {
        return mTime;
    }

    public int getTimeRemaining() {
        return mTimeRemaining;
    }

    public void reset() {
        mTimeRemaining = mTime;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
//        canvas.drawR

    }
}
