package com.cafe.adapter;

import android.support.v7.widget.RecyclerView;

import com.cafe.data.meeting.MeetingState;
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

	/**
	 * 根据对象的ID找到在列表中的index
	 */
	public int getIndex(int meetingId) {
		if (items != null) {
			int size = items.size();
			for (int i = 0; i < size; i++) {
				MeetingUserInfo meetingInfo = items.get(i);
				if (meetingInfo.id == meetingId)
					return i;
			}
		}
		return -1;
	}


	// 找到列表中历史记录的第一条的index
	public int getHistoryOne() {
		int position = -1;
		int size = items.size();
		for (int i = 0; i < size; i++) {
			MeetingUserInfo meetingInfo = items.get(i);
			if (meetingInfo.state == MeetingState.HISTORY) {
				position = i;
				break;
			}
		}
		return position;
	}

	public int getProgressOne() {
		int position = -1;
		int size = items.size();
		for (int i = 0; i < size; i++) {
			MeetingUserInfo meetingInfo = items.get(i);
			if (meetingInfo.state == MeetingState.PROGRESS) {
				position = i;
				break;
			}
		}
		return position;
	}

	public int getAppointOne() {
		int position = -1;
		int size = items.size();
		for (int i = 0; i < size; i++) {
			MeetingUserInfo meetingInfo = items.get(i);
			if (meetingInfo.state == MeetingState.APPOINTMENT) {
				position = i;
				break;
			}
		}
		return position;
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
