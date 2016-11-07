package com.ai2020lab.cafe.presenter.test;

import android.content.Context;
import android.content.Intent;

import com.ai2020lab.aiutils.common.LogUtils;
import com.ai2020lab.cafe.common.DiskCacheManager;
import com.ai2020lab.cafe.common.DiskCachePath;
import com.ai2020lab.cafe.common.mvp.MVPPresenter;
import com.ai2020lab.cafe.contract.TestContract;
import com.ai2020lab.cafe.service.FunfManagerService;

/**
 * 测试Presenter
 * Created by Justin Z on 2016/11/1.
 * 502953057@qq.com
 */

public class TestPresenter extends MVPPresenter<TestContract.View, TestContract.Model>
		implements TestContract.Presneter {

	private final static String TAG = TestPresenter.class.getSimpleName();

	private Context context;

	private Intent intent;

	public TestPresenter(Context context) {
		this.context = context;
		this.intent = new Intent(context, FunfManagerService.class);
	}

	@Override
	public TestContract.Model initModel() {
		return null;
	}

	@Override
	public void saveToDiskLruCache() {

		DiskCacheManager.getInstance().putString(DiskCachePath.AUDIO_DB,
				"123123123", "75");
		DiskCacheManager.getInstance().putString(DiskCachePath.AUDIO_DB,
				"321321321", "60");
		DiskCacheManager.getInstance().putString(DiskCachePath.AUDIO_DB,
				"456456456", "50");
		DiskCacheManager.getInstance().putString(DiskCachePath.AUDIO_DB,
				"456456456", "50,60,80");

	}

	@Override
	public void getFromDiskLruCache() {
		String s1 = DiskCacheManager.getInstance().getString(DiskCachePath.AUDIO_DB, "123123123");
		String s2 = DiskCacheManager.getInstance().getString(DiskCachePath.AUDIO_DB, "321321321");
		String s3 = DiskCacheManager.getInstance().getString(DiskCachePath.AUDIO_DB, "456456456");
		LogUtils.i(TAG, "s1-->" + s1);
		LogUtils.i(TAG, "s2-->" + s2);
		LogUtils.i(TAG, "s3-->" + s3);

	}

	@Override
	public void startService() {
		context.startService(intent);

	}

	@Override
	public void stopService() {
		context.stopService(intent);
	}


}
