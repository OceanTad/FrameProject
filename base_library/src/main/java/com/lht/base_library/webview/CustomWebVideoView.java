package com.lht.base_library.webview;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class CustomWebVideoView extends FrameLayout {

    public CustomWebVideoView(Context ctx) {
        super(ctx);
        setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        return true;
    }

}
