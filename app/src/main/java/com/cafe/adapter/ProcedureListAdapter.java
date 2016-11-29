package com.cafe.adapter;

import android.support.v7.widget.RecyclerView;

import com.cafe.data.meeting.ProcedureInfo;

import org.justin.utils.common.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 会议说话过程记录列表Adapter
 * Created by Justin Z on 2016/11/29.
 * 502953057@qq.com
 */

public abstract class ProcedureListAdapter<VH extends RecyclerView.ViewHolder>
		extends RecyclerView.Adapter<VH> {

	private final static String TAG = MeetingListAdapter.class.getSimpleName();

	private List<ProcedureInfo> items = new ArrayList<>();

	public ProcedureListAdapter(){
		setHasStableIds(true);
	}

	public void add(ProcedureInfo object) {
		items.add(object);
		notifyDataSetChanged();
	}

	public void add(int index, ProcedureInfo object) {
		items.add(index, object);
		// 只有调用这个方法动画才能生效
		notifyItemInserted(index);
	}

	public void addAll(Collection<? extends ProcedureInfo> collection) {
		if (collection != null) {
			items.addAll(collection);
			notifyDataSetChanged();
		}
	}

	public void addAll(ProcedureInfo... items) {
		addAll(Arrays.asList(items));
	}

	public void clear() {
		LogUtils.i(TAG, "数据清空");
		items.clear();
		notifyDataSetChanged();
	}

	public void remove(ProcedureInfo object) {
		items.remove(object);
		notifyDataSetChanged();
	}

	public void remove(int position) {
		items.remove(position);
		notifyItemRemoved(position);
	}

	public ProcedureInfo getItem(int position) {
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
