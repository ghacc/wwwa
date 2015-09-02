package com.britwit.worldwordwars.util;

import android.view.View;

public class UiUtils {

    public static void toggleVisibleGone(View v) {
        int visibility = v.getVisibility();
        if(visibility == View.GONE) {
            v.setVisibility(View.VISIBLE);
        } else if (visibility == View.VISIBLE){
            v.setVisibility(View.GONE);
        }
    }


}
