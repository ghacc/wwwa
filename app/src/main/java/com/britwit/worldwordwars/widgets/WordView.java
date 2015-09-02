package com.britwit.worldwordwars.widgets;

import static android.animation.LayoutTransition.*;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.britwit.worldwordwars.BuildConfig;
import com.britwit.worldwordwars.interfaces.OnKeyboardEventListener;

public class WordView extends ViewGroup implements OnKeyboardEventListener {

    private static final String TAG = "WordView";
    public static final int MAX_CHARS = 12;

    private View[] mNotGoneViews = new View[MAX_CHARS];
    private Path mTextPath = new Path();
    private PathMeasure mPathMeasure = new PathMeasure();
    private float mPathLength2;
    private Paint debugPaint;
    private LayoutParams mChildLayoutParams;
    private LayoutTransition mLayoutTransition;


    public WordView(Context context) {
        super(context);
        init();
    }

    public WordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected final void init() {
        mChildLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutTransition = initLayoutTransition();
        setLayoutTransition(mLayoutTransition);

        if (BuildConfig.DEBUG) {
            setWillNotDraw(true);
            if (debugPaint == null) {
                debugPaint = new Paint();
                debugPaint.setStyle(Paint.Style.STROKE);
                debugPaint.setColor(Color.argb(255, 255, 0, 0));
                debugPaint.setStrokeWidth(3f);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxHeight = 0;
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }
        }
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        setMeasuredDimension(
                getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                maxHeight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout called");
        int childCount = getChildCount();
        if (childCount > MAX_CHARS || childCount == 0) {
            return;
        }
        if (changed) calculatePath(l, t, r, b);
        int textWidth = 0;
        int j = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                textWidth += child.getMeasuredWidth();
                mNotGoneViews[j] = child;
                j++;
            }
        }

        float startPos = mPathLength2 - (textWidth / 2f) + (textWidth / childCount / 2f);
        float nextPos = startPos;
        int notGoneChildCount = j;
        int childWidth, childHeight;
        float cl, ct, cr, cb;
        float[] pathPos = new float[2];
        float[] tangent = new float[2];
        double rotationAngle;
        for (int i = 0; i < notGoneChildCount; i++) {
            View child = mNotGoneViews[i];
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            mPathMeasure.getPosTan(nextPos, pathPos, tangent);
            rotationAngle = Math.toDegrees(Math.atan2(tangent[1], tangent[0]));
//            Log.d(TAG, "onLayout tan[0]=" + tangent[0] + " tangent[1]=" + tangent[1] + " rotationAngle=" + rotationAngle);
            cl = pathPos[0] + childWidth / -2f;
            ct = pathPos[1] + childHeight / -2f;
            cr = pathPos[0] + childWidth / 2f;
            cb = pathPos[1] + childHeight / 2f;
            child.setRotation((float) rotationAngle);
            child.layout((int) cl, (int) ct, (int) cr, (int) cb);
            nextPos += childWidth;
        }
    }

    private void calculatePath(int l, int t, int r, int b) {
        float textHeight2 = getMeasuredHeight() / -2f;// mTextPaint.getFontMetrics().top;
        RectF rect = new RectF(-textHeight2, -textHeight2, getMeasuredWidth() + textHeight2, getMeasuredWidth() + textHeight2); //t - textHeight + (r + textHeight - l + textHeight));
//        RectF rect = new RectF(l - textHeight, t - textHeight, r + textHeight, t + r - l + textHeight); //t - textHeight + (r + textHeight - l + textHeight));
        mTextPath.addArc(rect, 180f, 180f);
        mPathMeasure.setPath(mTextPath, false);
        mPathLength2 = mPathMeasure.getLength() / 2f;

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mTextPath, debugPaint);
        dispatchDraw(canvas);
    }

    @Override
    public void onKeyPressed(String keyValue) {
        int childCount = getChildCount();
        if (childCount == MAX_CHARS) {
            return;
        }
        TextView charView = new TextView(getContext());
        charView.setText(keyValue);
        charView.setAllCaps(true);
        charView.setTextColor(Color.parseColor("#b3b3b3"));
        charView.setTextSize(40f);
        addView(charView, childCount, mChildLayoutParams);
    }

    @Override
    public void onSubmitPressed(String word) {
        removeAllViews();
    }

    @Override
    public void onDeletePressed() {
        int childCount = getChildCount();
        if (childCount > 0) {
            removeViewAt(childCount - 1);
        }
    }


    protected final LayoutTransition initLayoutTransition() {

        LayoutTransition layoutTransition = new LayoutTransition();

        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1);
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1);
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0, 1);
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 1);
        PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofFloat("rotation", 0f, 1f);

        Animator defaultChangeIn = ObjectAnimator.ofPropertyValuesHolder((Object) null,
                pvhLeft, pvhTop, pvhRight, pvhBottom, pvhRotation);
        defaultChangeIn.setDuration(30l);
        defaultChangeIn.setStartDelay(0l);
        defaultChangeIn.setInterpolator(new FastOutLinearInInterpolator());

        Animator defaultChangeOut = defaultChangeIn.clone();
        defaultChangeOut.setStartDelay(25l);
        defaultChangeOut.setInterpolator(new FastOutLinearInInterpolator());

//        Animator defaultChange = defaultChangeIn.clone();
//        defaultChange.setStartDelay(mChangingDelay);
//        defaultChange.setInterpolator(mChangingInterpolator);

        Animator defaultFadeIn = ObjectAnimator.ofFloat(null, "alpha", 0f, 1f);
        defaultFadeIn.setDuration(30l);
        defaultFadeIn.setStartDelay(20l);
        defaultFadeIn.setInterpolator(new FastOutLinearInInterpolator());

        Animator defaultFadeOut = ObjectAnimator.ofFloat(null, "alpha", 1f, 0f);
        defaultFadeOut.setDuration(30l);
        defaultFadeOut.setStartDelay(0l);
        defaultFadeOut.setInterpolator(new FastOutLinearInInterpolator());

        layoutTransition.setAnimator(APPEARING, defaultFadeIn);
        layoutTransition.setAnimator(DISAPPEARING, defaultFadeOut);
        layoutTransition.setAnimator(CHANGE_APPEARING, defaultChangeIn);
        layoutTransition.setAnimator(CHANGE_DISAPPEARING, defaultChangeOut);

        return layoutTransition;
    }
}
