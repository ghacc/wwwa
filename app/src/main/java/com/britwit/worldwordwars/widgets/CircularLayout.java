package com.britwit.worldwordwars.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.britwit.worldwordwars.R;

public class CircularLayout extends ViewGroup {

    private static final String TAG = "CircularLayout";

    private Rect mChildRect = new Rect();
    private float mChildrenSizeRatio;

    public CircularLayout(Context context) {
        super(context);
    }

    public CircularLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParams(attrs);
    }

    public CircularLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams(attrs);
    }

    private void initParams(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CircularLayout);
        mChildrenSizeRatio = array.getFloat(R.styleable.CircularLayout_childrenSizeRatio, 1f);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int childrenCount = getChildCount();
        final double angle = 2 * Math.PI / childrenCount;
        final double angle2 = angle / 2;
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        final int minDimension = Math.min(width, height);
        final int availableWidth = width - getPaddingLeft() - getPaddingRight();
        final int availableHeight = height - getPaddingTop() - getPaddingBottom();
        final int minSpaceAvailableInDimension = Math.min(availableWidth, availableHeight);

        final int childrenSize = (int)
                (minSpaceAvailableInDimension * Math.sin(angle2) / (1 + Math.sin(angle2)));
        final int childrenMeasureSpec = MeasureSpec.makeMeasureSpec(childrenSize, MeasureSpec.EXACTLY);
        for (int i = 0; i < childrenCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.measure(childrenMeasureSpec, childrenMeasureSpec);
            }
        }

        setMeasuredDimension(minDimension, minDimension);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childrenCount = getChildCount();
        int leftPos = getPaddingLeft();
        int rightPos = r - l - getPaddingRight();
        double centerHorizontalPos = (rightPos - leftPos) / 2d + getPaddingLeft();
        int topPos = getPaddingTop();
        int bottomPos = b - t - getPaddingBottom();
        double centerVerticalPos = (bottomPos - topPos) / 2d + getPaddingTop();
        int availableWidth = rightPos - leftPos;
        int availableHeight = bottomPos - topPos;
        double[] unitVectorXPos = calculateUnitVectorXPositions(childrenCount);
        double[] unitVectorYPos = calculateUnitVectorYPositions(childrenCount);

        int childrenSize = 0;
        for (int i = 0; i < childrenCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                childrenSize = Math.min(child.getMeasuredWidth(), child.getMeasuredHeight());
                break;
            }
        }

        double childrenSize2 = childrenSize / 2d;
        double[] offsetFromCenter = new double[4];
        offsetFromCenter[0] = offsetFromCenter[1] = -childrenSize2;
        offsetFromCenter[2] = offsetFromCenter[3] = childrenSize2;

        double radiusOfIntracentricCicle = availableWidth / 2d - childrenSize / 2d;

        for (int i = 0; i < childrenCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                double centerX = centerHorizontalPos + radiusOfIntracentricCicle * unitVectorXPos[i];
                double centerY = centerVerticalPos + radiusOfIntracentricCicle * unitVectorYPos[i];
                child.layout((int) (centerX + offsetFromCenter[0] * mChildrenSizeRatio),
                        (int) (centerY + offsetFromCenter[1] * mChildrenSizeRatio),
                        (int) (centerX + offsetFromCenter[2] * mChildrenSizeRatio),
                        (int) (centerY + offsetFromCenter[3] * mChildrenSizeRatio));
            }
        }


    }

    private double[] calculateUnitVectorXPositions(int count) {
        double[] res = new double[count];
        double angle = 2 * Math.PI / count;
        for (int i = 0; i < count; i++) {
            double curAngle = i * angle;
            res[i] = Math.sin(curAngle);
        }
        return res;
    }

    private double[] calculateUnitVectorYPositions(int count) {
        double[] res = new double[count];
        double angle = 2 * Math.PI / count;
        for (int i = 0; i < count; i++) {
            double curAngle = i * angle;
            res[i] = Math.cos(curAngle);
        }
        return res;
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

}
