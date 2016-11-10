package com.ai2020lab.cafe.adapter;

import android.support.v7.widget.RecyclerView;

import com.ai2020lab.aiutils.common.LogUtils;
import com.ai2020lab.cafe.data.meeting.MeetingInfo;

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

	private List<MeetingInfo> items = new ArrayList<>();

	/**
	 * 构造方法
	 */
	public MeetingListAdapter() {
		setHasStableIds(true);
	}

	public void add(MeetingInfo object) {
		items.add(object);
		notifyDataSetChanged();
	}

	public void add(int index, MeetingInfo object) {
		items.add(index, object);
		// 只有调用这个方法动画才能生效
		notifyItemInserted(index);
//		notifyDataSetChanged();
	}

	public void addAll(Collection<? extends MeetingInfo> collection) {
		if (collection != null) {
			items.addAll(collection);
			notifyDataSetChanged();
		}
	}

	public void addAll(MeetingInfo... items) {
		addAll(Arrays.asList(items));
	}

	public void clear() {
		LogUtils.i(TAG, "数据清空");
		items.clear();
		notifyDataSetChanged();
	}

	public void remove(MeetingInfo object) {
		items.remove(object);
		notifyDataSetChanged();
	}

	public void remove(int position) {
		items.remove(position);
		notifyItemRemoved(position);
	}

	public MeetingInfo getItem(int position) {
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
