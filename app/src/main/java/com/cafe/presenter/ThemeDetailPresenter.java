package com.cafe.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.cafe.common.PreManager;
import com.cafe.common.mvp.MVPPresenter;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.contract.ThemeDetailContract;
import com.cafe.contract.MeetingListContract;
import com.cafe.data.meeting.GetNowTalkerResponse;
import com.cafe.data.meeting.MeetingListResponse;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.data.meeting.ProcedureListRequest;
import com.cafe.data.meeting.ProcedureListResponse;
import com.cafe.model.meeting.ThemeDetailBiz;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.IOException;
import java.net.URI;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

/**
 * 主题会议详情Presenter
 * Created by Justin Z on 2016/11/28.
 * 502953057@qq.com
 */

public class ThemeDetailPresenter extends MVPPresenter<ThemeDetailContract.View,
		ThemeDetailContract.Model> implements ThemeDetailContract.Presenter {

	private final static String TAG = ThemeDetailPresenter.class.getSimpleName();

	private Context context;

	public ThemeDetailPresenter(Context context) {
		this.context = context;
		setModel(initModel());
	}


	@Override
	public ThemeDetailContract.Model initModel() {
		return new ThemeDetailBiz(context);
	}

	/**
	 * 获取当前说话人
	 */
	@Override
	public void getNowTalker(final MeetingUserInfo info) {
		ThemeDetailContract.Model themeDetailBiz = getModel();
		if (themeDetailBiz == null) return;
		themeDetailBiz.getNowTalker(info, new JsonHttpResponseHandler<GetNowTalkerResponse>() {

			@Override
			public void onHandleSuccess(int statusCode, Header[] headers, GetNowTalkerResponse jsonObj) {
				handleSuccess(jsonObj);
			}

			@Override
			public void onCancel() {
				// TODO:不做任何处理

			}

			@Override
			public void onHandleFailure(String errorMsg) {
				// TODO:不做任何处理
			}
		});

	}

	/**
	 * 获取当前说话人成功处理
	 */
	private void handleSuccess(GetNowTalkerResponse jsonObj) {
		ThemeDetailContract.View view = getView();
		if (view == null) return;
		view.setNowTalker(jsonObj.data);
	}

	/**
	 * 加载说话记录列表
	 */
	@Override
	public void loadProcedureList(final MeetingUserInfo info) {
		ThemeDetailContract.Model themeDetailBiz = getModel();
		if (themeDetailBiz == null) return;
		themeDetailBiz.loadProcedureList(info, new JsonHttpResponseHandler<ProcedureListResponse>() {

			@Override
			public void onHandleSuccess(int statusCode, Header[] headers, ProcedureListResponse jsonObj) {
				handleSuccess(jsonObj);
			}

			@Override
			public void onCancel() {
				// TODO:不做任何处理

			}

			@Override
			public void onHandleFailure(String errorMsg) {
				// TODO:不做任何处理
			}
		});

	}

	private void handleSuccess(ProcedureListResponse jsonObj) {
		ThemeDetailContract.View view = getView();
		if (view == null) return;
		if(!TextUtils.isEmpty(jsonObj.data.filterTime)){
			PreManager.setProcedureFilterTime(context, jsonObj.data.filterTime);
		}
		if (jsonObj.data.procedureInfos != null || jsonObj.data.procedureInfos.size() != 0) {
			// 界面加载说话详情列表
			view.loadProcedureList(jsonObj.data.procedureInfos);
		}

	}

	@Override
	public void startTheme(MeetingUserInfo info) {

	}

	@Override
	public void stopTheme(MeetingUserInfo info) {

	}

	@Override
	public void startEpisode(MeetingUserInfo info) {

	}

	@Override
	public void stopEpisode(MeetingUserInfo info) {

	}
}
