package com.britwit.worldwordwars.activities;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.britwit.worldwordwars.R;
import com.britwit.worldwordwars.util.UiUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;


public class MenuActivity extends AppCompatActivity {

    @Bind(R.id.menu_layout) ViewGroup mMenuViewGroup;
    @Bind(R.id.single_player_layout) ViewGroup mSinglePlayerLayout;
    @Bind(R.id.multi_player_layout) ViewGroup mMultiPlayerLayout;

    @Bind(R.id.single_player_submenu_layout) ViewGroup mSinglePlayerSubmenuLayout;
    @Bind(R.id.multi_player_submenu_layout) ViewGroup mMultiPlayerSubmenuLayout;

    @Bind(R.id.single_player_6_btn) ImageButton mSinglePlayer6Button;
    @Bind(R.id.single_player_7_btn) ImageButton mSinglePlayer7Button;
    @Bind(R.id.single_player_8_btn) ImageButton mSinglePlayer8Button;
    @Bind(R.id.single_player_9_btn) ImageButton mSinglePlayer9Button;
    @Bind(R.id.single_player_10_btn) ImageButton mSinglePlayer10Button;
    @Bind(R.id.single_player_11_btn) ImageButton mSinglePlayer11Button;
    @Bind(R.id.single_player_12_btn) ImageButton mSinglePlayer12Button;

    @Bind(R.id.exit_btn) ImageButton mExitButton;

    private AnimationDrawable mExitButtonAnimationDrawable;
    private AnimatorSet alphaScaleAnimator;
    private Animation alphaAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        alphaScaleAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.alpha_scale);
//        alphaAnimation = AnimationUtils.loadAnimation(this, R.animator.alpha);

        // Set up the layout transition used when hiding submenu
        LayoutTransition layoutTransition = mMenuViewGroup.getLayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);

        MenuOnClickListener menuOnClickListener =
                new MenuOnClickListener(mSinglePlayerSubmenuLayout, mMultiPlayerSubmenuLayout);

        mSinglePlayerLayout.setOnClickListener(menuOnClickListener);
        mMultiPlayerLayout.setOnClickListener(menuOnClickListener);

        mExitButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mExitButton.animate().scaleX(1.3f);
                    mExitButton.animate().scaleY(1.3f);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mExitButton.animate().scaleX(1f);
                    mExitButton.animate().scaleY(1f);
                }
                return false;
            }
        });

        mSinglePlayerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mSinglePlayerLayout.animate().scaleX(1.1f).setDuration(50l);
                    mSinglePlayerLayout.animate().scaleY(1.1f).setDuration(50l);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mSinglePlayerLayout.animate().scaleX(1f);
                    mSinglePlayerLayout.animate().scaleY(1f);
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//        alphaScaleAnimator.setTarget(mExitButton);
//        alphaScaleAnimator.start();
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        mExitButton.startAnimation(scaleAnimation);
////        StateListDrawable sld = (StateListDrawable)mExitButton.getDrawable();
////        Drawable currentDrawable = sld.getCurrent();
////        if (currentDrawable instanceof AnimationDrawable) {
////            mExitButtonAnimationDrawable = (AnimationDrawable)currentDrawable;
////        }
////        mExitButtonAnimationDrawable.start();
//    }

    @OnClick(R.id.exit_btn)
    public void exit(View v) {
//        alphaScaleAnimator.setTarget(mExitButton);
//        alphaScaleAnimator.start();
//        mExitButtonAnimationDrawable.stop();
//        mExitButtonAnimationDrawable.start();
        Intent intent = new Intent(this, ExitConfirmationActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.single_player_6_btn,
            R.id.single_player_7_btn,
            R.id.single_player_8_btn,
            R.id.single_player_9_btn,
            R.id.single_player_10_btn,
            R.id.single_player_11_btn,
            R.id.single_player_12_btn})
    public void pickGame(View v) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    private class MenuOnClickListener implements View.OnClickListener {

        ViewGroup[] submenus;

        public MenuOnClickListener(ViewGroup... submenus) {
            this.submenus = submenus;
        }

        @Override
        public void onClick(View v) {

            ViewGroup vg = (ViewGroup) v;
            View selectedSubmenu = vg.getChildAt(vg.getChildCount() - 1);
            int visibility = selectedSubmenu.getVisibility();
            if (visibility == View.GONE) {
                selectedSubmenu.setVisibility(View.VISIBLE);
                for (int i = 0; i < submenus.length; i++) {
                    ViewGroup submenu = submenus[i];
                    if (selectedSubmenu == submenu) {
                        continue;
                    } else {
                        submenu.setVisibility(View.GONE);
                    }
                }
            } else {
                selectedSubmenu.setVisibility(View.GONE);
                for (int i = 0; i < submenus.length; i++) {
                    ViewGroup submenu = submenus[i];
                    if (selectedSubmenu == submenu) {
                        continue;
                    } else {
                        submenu.setVisibility(View.GONE);
                    }
                }
            }

        }

    }
}
