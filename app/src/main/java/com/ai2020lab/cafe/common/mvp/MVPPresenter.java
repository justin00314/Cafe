package com.ai2020lab.cafe.common.mvp;

import com.ai2020lab.cafe.common.mvp.base.BaseModel;
import com.ai2020lab.cafe.common.mvp.base.BaseView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Presenter基础接口类
 * Presenter同时持有View和Model(可能没有)的引用，用于协调View和Model之间的工作窜接
 * Created by Justin Z on 2016/10/14.
 * 502953057@qq.com
 */
public abstract class MVPPresenter<V extends BaseView, M extends BaseModel> {

	public Reference<V> mViewRef;

	public Reference<M> mModelRef;

	public MVPPresenter() {
		mModelRef = new WeakReference<>(initModel());
	}

	public abstract M initModel();

	/**
	 * 初始化Presenter
	 */
	public void attachView(V view) {
		mViewRef = new WeakReference<>(view);
	}

	/**
	 * 销毁Presenter持有的View引用
	 */
	public void detachView() {
		if (mViewRef != null) {
			mViewRef.clear();
			mViewRef = null;
		}
	}

	public V getView() {
		if (mViewRef == null) return null;
		return mViewRef.get();
	}

	public M getModel() {
		if (mModelRef == null) return null;
		return mModelRef.get();
	}

}
