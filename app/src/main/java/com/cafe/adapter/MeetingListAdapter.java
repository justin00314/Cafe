package com.cafe.adapter;

import android.support.v7.widget.RecyclerView;

import com.cafe.data.meeting.MeetingUserInfo;

import org.justin.utils.common.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 会议列表Adapter
 * Created by Justin Z on 2016/11/9.
 * 502953057@qq.com
 */

public abstract class MeetingListAdapter<VH extends RecyclerView.ViewHolder>
		extends RecyclerView.Adapter<VH> {

	private final static String TAG = MeetingListAdapter.class.getSimpleName();

	private List<MeetingUserInfo> items = new ArrayList<>();

	/**
	 * 构造方法
	 */
	public MeetingListAdapter() {
		setHasStableIds(true);
	}

	public void add(MeetingUserInfo object) {
		items.add(object);
		notifyDataSetChanged();
	}

	public void add(int index, MeetingUserInfo object) {
		items.add(index, object);
		// 只有调用这个方法动画才能生效
		notifyItemInserted(index);
//		notifyDataSetChanged();
	}

	public void addAll(Collection<? extends MeetingUserInfo> collection) {
		if (collection != null) {
			items.addAll(collection);
			notifyDataSetChanged();
		}
	}

	public void addAll(MeetingUserInfo... items) {
		addAll(Arrays.asList(items));
	}

	public void clear() {
		LogUtils.i(TAG, "数据清空");
		items.clear();
		notifyDataSetChanged();
	}

	public void remove(MeetingUserInfo object) {
		items.remove(object);
		notifyDataSetChanged();
	}

	public void remove(int position) {
		items.remove(position);
		notifyItemRemoved(position);
	}

	public MeetingUserInfo getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).hashCode();
	}

	@Override
	public int getItemCount() {
		return items.size();
	}
}
