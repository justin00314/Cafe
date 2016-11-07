package com.ai2020lab.cafe.funf.base;

import android.content.Context;

/**
 * Action基类
 * Created by Justin Z on 2016/11/2.
 * 502953057@qq.com
 */
public abstract class Action implements Base {

	private final static String TAG = Action.class.getSimpleName();

	private Context context;

	private State mState;

	protected enum State {
		DISABLE, START, STOP
	}

	public Action(Context context) {
		this.mState = State.DISABLE;
		this.context = context;
	}

	@Override
	public void onStart() {
		this.mState = State.START;
	}

	@Override
	public void onStop() {
		this.mState = State.STOP;
	}

	/**
	 * 获取上下文引用
	 *
	 * @return Context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * 获取Action当前状态
	 *
	 * @return State
	 */
	public State getState() {
		return mState;
	}


}
