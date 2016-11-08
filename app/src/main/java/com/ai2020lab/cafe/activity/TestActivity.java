package com.ai2020lab.cafe.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ai2020lab.aiutils.common.ToastUtils;
import com.ai2020lab.cafe.R;
import com.ai2020lab.cafe.common.DiskCacheManager;
import com.ai2020lab.cafe.common.DiskCacheMaxSize;
import com.ai2020lab.cafe.common.DiskCachePath;
import com.ai2020lab.cafe.common.mvp.MVPActivity;
import com.ai2020lab.cafe.contract.TestContract;
import com.ai2020lab.cafe.presenter.test.TestPresenter;


/**
 * 测试页面
 */
public class TestActivity extends MVPActivity<TestContract.View,
		TestPresenter> implements TestContract.View {

	private final static String TAG = TestActivity.class.getSimpleName();

	private Button saveDataButton;
	private Button showDataButton;
	private Button startServiceButton;
	private Button stopServiceButton;


	private void initViews(){
		saveDataButton = (Button) findViewById(R.id.save_data);
		saveDataButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getPresenter().saveToDiskLruCache();
			}
		});
		showDataButton = (Button) findViewById(R.id.show_data);
		showDataButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getPresenter().getFromDiskLruCache();
			}
		});

		startServiceButton = (Button) findViewById(R.id.start_service);
		startServiceButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getPresenter().startService();
			}
		});

		stopServiceButton = (Button) findViewById(R.id.stop_service);
		stopServiceButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getPresenter().stopService();
			}
		});
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		initViews();
		setToolbar();
		// 打开磁盘缓存
		DiskCacheManager.getInstance().openCache(this, DiskCacheMaxSize.AUDIO_DB,
				DiskCachePath.AUDIO_DB);
	}

	@Override
	public TestPresenter initPresenter() {
		return new TestPresenter(this);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		DiskCacheManager.getInstance().closeCache();

	}

	private void setToolbar(){
		supportToolbar(true);
		setToolbarTitle("主页");
		setToolbarLeft(R.mipmap.back, new OnLeftClickListener() {
			@Override
			public void onClick() {
				ToastUtils.getInstance().showToast(getActivity(), "点了注销");
			}
		});
		setToolbarRight1(R.mipmap.search, new OnRightClickListener() {
			@Override
			public void onClick() {
				ToastUtils.getInstance().showToast(getActivity(), "点了搜索");
			}
		});
		setToolbarRight2(R.mipmap.scan, new OnRightClickListener() {
			@Override
			public void onClick() {
				ToastUtils.getInstance().showToast(getActivity(), "点了扫描");
			}
		});
	}





}
