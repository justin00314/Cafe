package com.cafe.common.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.cafe.R;
import com.cafe.activity.LoginActivity;
import com.cafe.data.base.ErrorMessageResponse;
import com.cafe.data.base.ResponseData;
import com.loopj.android.http.TextHttpResponseHandler;

import org.justin.utils.common.JsonUtils;
import org.justin.utils.common.LogUtils;
import org.justin.utils.common.StringUtils;
import org.justin.utils.common.ToastUtils;

import java.lang.reflect.ParameterizedType;

import cz.msebera.android.httpclient.Header;

/**
 * Http响应处理抽象类，具体业务实现这个类的抽象方法
 * Created by Justin on 2015/11/11.
 * Email:502953057@qq.com,zhenghx3@asiainfo.com
 */
public abstract class JsonHttpResponseHandler<T extends ResponseData> extends TextHttpResponseHandler {

	private final static String TAG = JsonHttpResponseHandler.class.getSimpleName();

	private Class<T> mEntityClz;

	private Context context;

	@SuppressWarnings("unchecked")
	public JsonHttpResponseHandler() {
		mEntityClz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public JsonHttpResponseHandler(Context context) {
		this();
		this.context = context;
	}

	/**
	 * Fired when the request is started, override to handle in your own code
	 */
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public final void onSuccess(int statusCode, Header[] headers, String responseString) {
		LogUtils.i(TAG, "成功返回JSON为-->" + responseString);
		// 成功返回先解析外层数据
		T data = JsonUtils.getInstance().deserializeToObj(responseString,
				mEntityClz);
		if (data == null) {
			LogUtils.i(TAG, "----返回数据解析出错----");
//			ToastUtils.getInstance().showToast(context, context.getString(R.string.request_failure));
			onHandleFailure("返回数据解析出错");
			return;
		}
		if (data.desc == null) {
			LogUtils.i(TAG, "----返回数据desc为空----");
//			ToastUtils.getInstance().showToast(context, context.getString(R.string.request_failure));
			onHandleFailure("返回数据desc为空");
			return;
		}
		if (data.desc.result_code != ResultCode.SUCCESS) {
			LogUtils.i(TAG, "----返回数据desc.result_code不为success----");

			String msg = "返回数据desc.result_code不为success";
			// TODO:处理登录失效,统一跳转到登录界面
			if (data.desc.result_code == ResultCode.LOGIN_FAILURE) {
				if (context != null && context instanceof Activity) {
					context.startActivity(new Intent(context, LoginActivity.class));
					((Activity) context).finish();
					ToastUtils.getInstance().showToast(context, R.string.prompt_login_failure);
					return;
				}
			}
			ErrorMessageResponse error = JsonUtils.getInstance().deserializeToObj(responseString,
					ErrorMessageResponse.class);

			if (error != null && error.data.message != null) {
				msg = error.data.message;
			}
			onHandleFailure(msg);
			return;
		}
		if (data.data == null) {
			LogUtils.i(TAG, "----返回数据data.data为空----");
			onHandleFailure("返回数据data.data为空");
		} else {
			LogUtils.i(TAG, "----返回数据成功----");
			onHandleSuccess(statusCode, headers, data);
		}
	}

	@Override
	public void onFinish() {
		LogUtils.i(TAG, "请求结束");

	}

	@Override
	public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
		LogUtils.i(TAG, "请求失败:" + responseString);
//		ToastUtils.getInstance().showToast(context, "Get data failure!");
		onHandleFailure(responseString);
	}

	/**
	 * 请求取消回调
	 */
	@Override
	public void onCancel() {
		LogUtils.i(TAG, "请求取消");
	}

	/**
	 * 成功回调
	 *
	 * @param statusCode 状态码
	 * @param headers    Header
	 * @param jsonObj    服务端返回的对象
	 */
	public abstract void onHandleSuccess(int statusCode, Header[] headers, T jsonObj);

	/**
	 * 错误回调
	 *
	 * @param errorMsg 错误提示
	 */
	public void onHandleFailure(String errorMsg) {
	}
}
