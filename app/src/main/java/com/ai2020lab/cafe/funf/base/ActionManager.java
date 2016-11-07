package com.ai2020lab.cafe.funf.base;

import android.util.SparseArray;

import com.ai2020lab.aiutils.common.LogUtils;
import com.ai2020lab.cafe.data.funf.ActionData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action管理器
 * Created by Justin Z on 2016/11/2.
 * 502953057@qq.com
 */

public class ActionManager {

	private final static String TAG = ActionManager.class.getSimpleName();

	private HashMap<String, Action> actionMap;

	private ActionManager() {
		if (actionMap == null) {
			actionMap = new HashMap<>();
		}
	}

	private static class SingletonHolder {
		final static ActionManager INSTANCE = new ActionManager();
	}

	/**
	 * 获取单例
	 *
	 * @return ActionManager
	 */
	public static ActionManager getInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * 判断某个Action是否已经加入
	 *
	 * @param a Action
	 * @return true or false
	 */
	public boolean hasAction(Action a) {
		if (actionMap.containsKey(a.getClass().getSimpleName()))
			return true;
		return false;
	}

	/**
	 * 判断某个Action是否已经加入
	 *
	 * @param name String
	 * @return true or false
	 */
	public boolean hasAction(String name) {
		if (actionMap.containsKey(name))
			return true;
		return false;
	}

	/**
	 * 返回已经加入的Action个数
	 *
	 * @return int
	 */
	public int getActionSize() {
		return actionMap.size();
	}

	/**
	 * 加入指定的Action
	 *
	 * @param a Action
	 */
	public void addAction(Action a) {
		if (a == null) {
			LogUtils.i(TAG, "--Action不能为空--");
			return;
		}
		String name = a.getClass().getSimpleName();
		if (actionMap.containsKey(name)) {
			LogUtils.i(TAG, "--Action已经存在不能加入--");
			return;
		}
		LogUtils.i(TAG, "--加入Action--" + name);
		actionMap.put(name, a);
	}

	/**
	 * 启动所有的Action
	 */
	public void startActions() {
		LogUtils.i(TAG, "--启动所有的Action--");
		for (Action a : actionMap.values()) {
			if (a.getState() != Action.State.START)
				a.onStart();
		}
	}

	/**
	 * 停止所有的Action
	 */
	public void stopActions() {
		LogUtils.i(TAG, "--停止所有的Action--");
		for (Action a : actionMap.values()) {
			if (a.getState() == Action.State.START)
				a.onStop();
		}
	}

	/**
	 * 开始指定的Action
	 *
	 * @param a Action
	 */
	public void startAction(Action a) {
		if (a == null) {
			LogUtils.i(TAG, "--Action不能为空--");
			return;
		}
		String name = a.getClass().getSimpleName();
		if (!actionMap.containsKey(name)) {
			LogUtils.i(TAG, "--Action在集合中不存在--");
			return;
		}
		if (a.getState() == Action.State.START) {
			LogUtils.i(TAG, "--Action的状态已经开始--");
			return;
		}
		LogUtils.i(TAG, "--启动Action--" + name);
		a.onStart();
	}

	/**
	 * 停止指定的Action
	 *
	 * @param a Action
	 */
	public void stopAction(Action a) {
		if (a == null) {
			LogUtils.i(TAG, "--Action不能为空--");
			return;
		}
		String name = a.getClass().getSimpleName();
		if (!actionMap.containsKey(name)) {
			LogUtils.i(TAG, "--Action在集合中不存在--");
			return;
		}
		if (a.getState() == Action.State.STOP) {
			LogUtils.i(TAG, "--Action的状态已经结束--");
			return;
		}
		LogUtils.i(TAG, "--停止Action--" + name);
		a.onStop();
	}

	/**
	 * 移除指定的Action
	 *
	 * @param a Action
	 */
	public void removeAction(Action a) {
		if (a == null) {
			LogUtils.i(TAG, "--Action不能为空--");
			return;
		}
		String name = a.getClass().getSimpleName();
		if (!actionMap.containsKey(name)) {
			LogUtils.i(TAG, "--Action在集合中不存在--");
			return;
		}
		if (a.getState() == Action.State.START)
			a.onStop();
		LogUtils.i(TAG, "--移除Action--" + a.getClass().getSimpleName());
		actionMap.remove(name);
	}

	/**
	 * 停止并移除所有的Action
	 */
	public void removeActions() {
		for (Map.Entry<String, Action> entry : actionMap.entrySet()) {
			Action a = entry.getValue();
			if (a.getState() == Action.State.START)
				a.onStop();
		}
		actionMap.clear();
	}


}
