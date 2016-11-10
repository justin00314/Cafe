package com.ai2020lab.cafe.common;


import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/**
 * 自定义FAB的动效,伴随RecyclerView的滑动，上下隐藏
 * Created by Justin Z on 2016/11/10.
 * 502953057@qq.com
 */
public class FABBehavior extends FloatingActionButton.Behavior {
	//我们还可以加一个加速器实现弹射效果
	private FastOutLinearInInterpolator interpolator = new FastOutLinearInInterpolator();

	private boolean mIsAnimatingOut = false;

	public FABBehavior(Context context, AttributeSet attr) {
		super();
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
	                                   FloatingActionButton child, View directTargetChild,
	                                   View target, int nestedScrollAxes) {
		//开始滑监听---当观察recyclerview开始发生滑动的时候回调
		//nestedScrollAxes滑动关联的方向
		return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
				|| super.onStartNestedScroll(coordinatorLayout, child,
				directTargetChild, target, nestedScrollAxes);
	}

	@Override
	public void onNestedScroll(CoordinatorLayout coordinatorLayout,
	                           FloatingActionButton child, View target,
	                           int dxConsumed, int dyConsumed,
	                           int dxUnconsumed, int dyUnconsumed) {
		super.onNestedScroll(coordinatorLayout, child, target,
				dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

		if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE && !mIsAnimatingOut) {
			// User scrolled down and the FAB is currently visible -> hide the FAB
//			child.hide();
			animateOut(child);
		} else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
			// User scrolled up and the FAB is currently not visible -> show the FAB
//			child.show();
			animateIn(child);

		}
	}

	//滑进来
	private void animateIn(FloatingActionButton button) {
		button.setVisibility(View.VISIBLE);
		//属性动画
		ViewCompat.animate(button).translationY(0)
				.setInterpolator(interpolator).withLayer().setListener(null)
				.start();
	}

	//滑出去
	private void animateOut(FloatingActionButton button) {
		//属性动画
		//设置监听判断状态
		ViewCompat.animate(button).translationY(button.getHeight() + getMarginBottom(button))
				.setInterpolator(interpolator).withLayer()
				.setListener(new ViewPropertyAnimatorListener() {
					public void onAnimationStart(View view) {
						mIsAnimatingOut = true;
					}

					public void onAnimationCancel(View view) {
						mIsAnimatingOut = false;
					}

					public void onAnimationEnd(View view) {
						mIsAnimatingOut = false;
						view.setVisibility(View.GONE);
					}
				}).start();
	}

	@Override
	public void onStopNestedScroll(CoordinatorLayout coordinatorLayout,
	                               FloatingActionButton child, View target) {
		super.onStopNestedScroll(coordinatorLayout, child, target);
	}

	private int getMarginBottom(View v) {
		int marginBottom = 0;
		final ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
		if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
			marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
		}
		return marginBottom;
	}
}


