package com.cafe.common;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Interpolator;

import jp.wasabeef.recyclerview.animators.BaseItemAnimator;

/**
 * Created by Justin Z on 2016/11/25.
 * 502953057@qq.com
 */

public class DefaultAnimator extends BaseItemAnimator {

	public DefaultAnimator() {
	}

	public DefaultAnimator(Interpolator interpolator) {
		mInterpolator = interpolator;
	}

	@Override protected void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
		ViewCompat.animate(holder.itemView)
				.scaleX(1.5f)
				.scaleY(1.5f)
				.setDuration(getRemoveDuration())
				.setInterpolator(mInterpolator)
				.setListener(new BaseItemAnimator.DefaultRemoveVpaListener(holder))
				.setStartDelay(getRemoveDelay(holder))
				.start();
	}

	@Override protected void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
		ViewCompat.setAlpha(holder.itemView, 0);
		ViewCompat.setScaleX(holder.itemView, 1.5f);
		ViewCompat.setScaleY(holder.itemView, 1.5f);
	}

	@Override protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
		ViewCompat.animate(holder.itemView)
				.scaleX(1)
				.scaleY(1)
				.setDuration(getAddDuration())
				.setInterpolator(mInterpolator)
				.setListener(new BaseItemAnimator.DefaultAddVpaListener(holder))
				.setStartDelay(getAddDelay(holder))
				.start();
	}
}
