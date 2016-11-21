/*
 * Copyright (c) 2016. Justin Z All rights Reserved
 */

package com.aiviews.wheelview;

import android.view.MotionEvent;


final class LoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    final WheelView mLoopView;

    LoopViewGestureListener(WheelView loopView) {
        mLoopView = loopView;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mLoopView.scrollBy(velocityY);
        return true;
    }
}
