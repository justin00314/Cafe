package com.cafe.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin Z on 2016/12/6.
 * 502953057@qq.com
 */

public class ActivityManager {

	private final static String TAG = ActivityManager.class.getSimpleName();

	private List<Activity> activities = new ArrayList<>();

	private ActivityManager() {
	}

	private static class SingletonHolder {
		public final static ActivityManager INSTANCE = new ActivityManager();
	}

	public static ActivityManager getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public void add(Activity activity) {
		activities.add(activity);
	}

	public void finishAll() {
		for (Activity activity : activities) {
			if (!activity.isFinishing()) {
				activity.finish();
			}
			activities.clear();
		}

	}


}
