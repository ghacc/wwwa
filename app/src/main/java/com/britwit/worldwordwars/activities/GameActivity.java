package com.britwit.worldwordwars.activities;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.LayoutTransition;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.britwit.worldwordwars.R;
import com.britwit.worldwordwars.widgets.CircularLayout;
import com.britwit.worldwordwars.widgets.WordView;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.animation.LayoutTransition.APPEARING;

public class GameActivity extends AppCompatActivity {

    @Bind(R.id.wordView)
    WordView wordView;
    @Bind(R.id.keyboard)
    CircularLayout keyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordView.onSubmitPressed("word");
            }
        });
        for (int i = 0; i < keyboard.getChildCount(); i++) {
            keyboard.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wordView.onKeyPressed("G");
                }
            });
        }

        wordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordView.onDeletePressed();
            }
        });


    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

    }

}
