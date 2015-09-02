package com.britwit.worldwordwars.activities;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Rect;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.britwit.worldwordwars.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;


public class ExitConfirmationActivity extends AppCompatActivity {

    @Bind(R.id.leave_feedback_btn)
    Button leaveFeedbackButton;

    @Bind(R.id.cancel_btn)
    Button cancelButton;

    @Bind(R.id.exit_btn)
    Button exitButton;

    AnimatorSet alphaScaleAnimator;
    private final Rect mHelperRect = new Rect();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_exit_confirmation);
        ButterKnife.bind(this);

        alphaScaleAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.alpha_scale);
    }

    private void initBoundingRect(View view, Rect boundingRect) {
        if (boundingRect == null) {
            throw new IllegalArgumentException("boundingRect should not be null");
        }

        boundingRect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    }

    @OnTouch(R.id.exit_btn)
    public boolean exit(View v, MotionEvent e) {
        handleTouchEvent(v, e);
        return false;
    }

    @OnTouch(R.id.cancel_btn)
    public boolean cancel(View v, MotionEvent e) {
        handleTouchEvent(v, e);
        return false;
    }

    @OnTouch(R.id.leave_feedback_btn)
    public boolean leaveFeedback(View v, MotionEvent e) {
        handleTouchEvent(v, e);
        return false;
    }

    private void handleTouchEvent(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
//            alphaScaleAnimator.setTarget(v);
//            alphaScaleAnimator.start();
            v.animate().scaleX(1.3f).scaleY(1.3f).setDuration(100l);
            initBoundingRect(v, mHelperRect);
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            if (mHelperRect.contains((int) e.getRawX(), (int) e.getRawY())) {
                performButtonAction(v);
            }
        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            if (!mHelperRect.contains((int) e.getRawX(), (int) e.getRawY())) {
                v.animate().scaleX(1f).scaleY(1f).setDuration(100l);
            } else {
                v.animate().scaleX(1.3f).scaleY(1.3f).setDuration(100l);
            }
        }
    }

    private void performButtonAction(View v) {
        if (v == exitButton) {
            finishAffinity();
        } else if (v == cancelButton) {
            finish();
        } else if (v == leaveFeedbackButton) {
            // TODO implement leave feedback button
        }
    }
}