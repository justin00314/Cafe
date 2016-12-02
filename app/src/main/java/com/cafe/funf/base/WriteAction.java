package com.cafe.funf.base;

import android.content.Context;
import android.text.TextUtils;

import com.cafe.common.DiskCacheManager;
import com.cafe.common.net.HttpManager;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.common.net.UrlName;
import com.cafe.data.funf.FunfData;
import com.cafe.data.funf.FunfDataRequest;
import com.cafe.data.funf.FunfDataResponse;
import com.cafe.data.funf.KeysData;

import org.justin.utils.common.JsonUtils;
import org.justin.utils.common.LogUtils;
import org.justin.utils.system.NetworkUtils;
import org.justin.utils.thread.ThreadUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

/**
 * 数据存储上传Action
 * Created by Justin Z on 2016/11/2.
 * 502953057@qq.com
 */
public abstract class WriteAction extends Action {

	private final static String TAG = Action.class.getSimpleName();

	private final static String TIMESTAMP = "_timeStamp";

	private final static int UPLOAD_NUMBER_LIMIT = 30;
	private final static int UPLOAD_DELAY = 5000;
	private final static int UPLOAD_PERIOD = 3000;
	/**
	 * 存储数据路径
	 */
	private String savePath;
	/**
	 * 存储Key的路径
	 */
	private String keyPath;
	/**
	 * 存储空间最大值
	 */
	private long maxSize;

	/**
	 * 上传数据定时器
	 */
	private Timer uploadTimer;
	/**
	 * 上传数据任务
	 */
	private TimerTask uploadTask;

	/**
	 * 构造方法
	 *
	 * @param context  Context
	 * @param savePath String
	 * @param maxSize  long
	 */
	public WriteAction(Context context, String savePath, long maxSize) {
		super(context);
		this.savePath = savePath;
		this.keyPath = savePath + TIMESTAMP;
		this.maxSize = maxSize;
	}

	@Override
	public void onStart() {
		super.onStart();
//		clearCache();
		// 打开磁盘缓存
		openCache();
		// 启动上传任务
		startUpload();
	}

	@Override
	public void onStop() {
		super.onStop();
		stopUpload();
		// 关闭磁盘缓存
		closeCache();
	}

	private void openCache() {
		DiskCacheManager.getInstance().openCache(getContext(),
				maxSize, savePath);
		DiskCacheManager.getInstance().openCache(getContext(),
				maxSize, keyPath);
	}

	private void closeCache() {
		DiskCacheManager.getInstance().closeCache(savePath);
		DiskCacheManager.getInstance().closeCache(keyPath);
	}

	private void startUpload() {
		LogUtils.i(TAG, "--开始上传数据--");
		uploadTimer = new Timer();
		uploadTask = new TimerTask() {
			@Override
			public void run() {
				LogUtils.i(TAG, "上传数据线程id-->" + Thread.currentThread().getId());
				// 上传数据
				uploadData();
			}
		};
		uploadTimer.schedule(uploadTask, UPLOAD_DELAY, UPLOAD_PERIOD);
	}

	private void stopUpload() {
		LogUtils.i(TAG, "--停止上传数据--");
		if (uploadTask != null)
			uploadTask.cancel();
		if (uploadTimer != null)
			uploadTimer.cancel();
	}

	/**
	 * 保存数据,子类调用这个方法缓存String类型的数据到磁盘缓存
	 *
	 * @param key  时间作为key
	 * @param data 保存的JSON数据
	 */
	protected void saveData(String key, String data) {
		LogUtils.i(TAG, "保存数据线程id-->" + Thread.currentThread().getId());
		// 状态结束的时候不再保存数据
		if (getState() != State.START) return;

		DiskCacheManager.getInstance().putString(savePath, key, data);
		LogUtils.i(TAG, "存入data-->" + data);
		// 保存key
		String keyStr = DiskCacheManager.getInstance().getString(keyPath, keyPath);
		// 判断是否是第一次存key
		if (!TextUtils.isEmpty(keyStr)) {
			KeysData keysData = JsonUtils.getInstance().deserializeToObj(keyStr, KeysData.class);
			keysData.keys.put(key, key);
			String saveKeyStr = JsonUtils.getInstance().serializeToJson(keysData);
			LogUtils.i(TAG, "存入的key-->" + saveKeyStr);
			DiskCacheManager.getInstance().putString(keyPath, keyPath, saveKeyStr);

		} else {
			// 第一次存入key
			KeysData keysData = new KeysData();
			keysData.keys = new HashMap<>();
			keysData.keys.put(key, key);
			String saveKeyStr = JsonUtils.getInstance().serializeToJson(keysData);
			LogUtils.i(TAG, "第一次存入key-->" + saveKeyStr);
			DiskCacheManager.getInstance().putString(keyPath, keyPath, saveKeyStr);
		}

	}

	// 上传数据
	private void uploadData() {
		// 判断是否有网络
		if (!NetworkUtils.isNetworkAvailable(getContext())) {
			LogUtils.i(TAG, "-->没有网络，不上传数据");
			return;
		}

		// 读取Key,没有key的情况不会上传
		String keyStr = DiskCacheManager.getInstance().getString(keyPath, keyPath);
		if (TextUtils.isEmpty(keyStr)) return;
		KeysData keysData = JsonUtils.getInstance().deserializeToObj(keyStr, KeysData.class);
		// 用于临时存储已经发出的key
		FunfDataRequest funfDataRequest = new FunfDataRequest();
		funfDataRequest.funfDatas = new ArrayList<>();

		// 从key缓存中读取key，并限制上传数据总数不超过100条
		int i = 0;
		for (String key : keysData.keys.keySet()) {
			String uploadDataStr = DiskCacheManager.getInstance()
					.getString(savePath, key);
			if (TextUtils.isEmpty(uploadDataStr)) {
				continue;
			}
			FunfData funfData = JsonUtils.getInstance()
					.deserializeToObj(uploadDataStr, FunfData.class);
			funfDataRequest.funfDatas.add(funfData);
			i++;
			if (i > UPLOAD_NUMBER_LIMIT) {
				break;
			}
		}
		if (funfDataRequest.funfDatas == null || funfDataRequest.funfDatas.size() == 0)
			return;
		// 发送数据从小到大排序
		Collections.sort(funfDataRequest.funfDatas, new Comparator<FunfData>() {
			@Override
			public int compare(FunfData data1, FunfData data2) {
				if (data1.timestamp > data2.timestamp) {
					return 1;
				} else if (data1.timestamp < data2.timestamp) {
					return -1;
				}
				return 0;
			}
		});
		// 上传数据到服务端
		HttpManager.postJson(getContext(), UrlName.UPLOAD_DATA.getUrl(),
				funfDataRequest, responseHandler);

	}

	// 请求响应处理
	private JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler<FunfDataResponse>() {
		@Override
		public void onHandleSuccess(int statusCode, Header[] headers,
		                            final FunfDataResponse jsonObj) {
			// 在线程中处理上传成功后的数据操作
			ThreadUtils.runOnSubThread(new Runnable() {
				@Override
				public void run() {
					handleUploadSuccess(jsonObj.data.funfDatas);
				}
			});

		}
	};

	/**
	 * 处理上传成功的结果
	 */
	private void handleUploadSuccess(List<FunfData> funfDatas) {
		if (funfDatas == null || funfDatas.size() == 0) return;

		// 读取当前磁盘缓存中的keys
		String keyStr = DiskCacheManager.getInstance().getString(keyPath, keyPath);
		if (TextUtils.isEmpty(keyStr)) return;
		KeysData keysData = JsonUtils.getInstance().deserializeToObj(keyStr, KeysData.class);
		// 遍历上传成功的数据
		for (FunfData funfData : funfDatas) {
			DiskCacheManager.getInstance().remove(savePath, funfData.time);
			keysData.keys.remove(funfData.time);
			LogUtils.i(TAG, this.getClass().getSimpleName() + "--remove-->" + funfData.time);
		}
		String saveKeyStr = JsonUtils.getInstance().serializeToJson(keysData);
		LogUtils.i(TAG, "上传成功后处理的keyStr-->" + saveKeyStr);
		DiskCacheManager.getInstance().putString(keyPath, keyPath, saveKeyStr);

	}


}
