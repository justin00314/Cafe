package com.cafe.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiviews.textview.ImageTextButton;
import com.cafe.R;
import com.cafe.data.meeting.MeetingInfo;
import com.cafe.data.meeting.MeetingState;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import org.justin.utils.common.LogUtils;
import org.justin.utils.common.ResourcesUtils;
import org.justin.utils.common.ViewUtils;

/**
 * 会议列表适配器
 * Created by Justin Z on 2016/11/9.
 * 502953057@qq.com
 */

public class MeetingListRvAdapter extends MeetingListAdapter<MeetingListRvAdapter.ItemViewHolder>
		implements StickyRecyclerHeadersAdapter<MeetingListRvAdapter.HeaderViewHolder> {

	private final static String TAG = MeetingListRvAdapter.class.getSimpleName();

	private Context context;

	// 会议图标
	private Drawable typeDefaultDrawable;
	private Drawable typeCreatedDrawable;
	private Drawable typeParticipatedDrawable;
	private Drawable typePartiCreatedDrawable;
	// 操作按钮图标
	private Drawable opAddDrawable;
	private Drawable opQuitDrawable;
	private Drawable opDismissDrawable;
	private Drawable opCancelDrawable;

	private String opAdd;
	private String opCancel;
	private String opDismiss;
	private String opQuit;

	private String stateProgress;
	private String stateAppointment;
	private String stateHistory;

	private OnClickItemListener onClickAddListener;
	private OnClickItemListener onClickQuitListener;
	private OnClickItemListener onClickDismissListener;
	private OnClickItemListener onClickCancelListener;
	private OnClickItemListener onClickQRCodeListener;
	private OnClickItemListener onClickItemListener;

	/**
	 * 构造方法
	 *
	 * @param context Context
	 */
	public MeetingListRvAdapter(Context context) {
		this.context = context;
		init();
	}

	private void init() {
		typeDefaultDrawable = ResourcesUtils.getDrawable(R.mipmap.type_default);
		typeCreatedDrawable = ResourcesUtils.getDrawable(R.mipmap.type_created);
		typeParticipatedDrawable = ResourcesUtils.getDrawable(R.mipmap.type_participated);
		typePartiCreatedDrawable = ResourcesUtils.getDrawable(R.mipmap.type_participated_created);
		opAddDrawable = ResourcesUtils.getDrawable(R.mipmap.meeting_join);
		opQuitDrawable = ResourcesUtils.getDrawable(R.mipmap.meeting_quit);
		opDismissDrawable = ResourcesUtils.getDrawable(R.mipmap.meeting_dismiss);
		opCancelDrawable = ResourcesUtils.getDrawable(R.mipmap.meeting_cancel);
		opAdd = context.getString(R.string.op_add);
		opCancel = context.getString(R.string.op_cancel);
		opDismiss = context.getString(R.string.op_dismiss);
		opQuit = context.getString(R.string.op_quit);
		stateProgress = context.getString(R.string.meeting_state_progress);
		stateAppointment = context.getString(R.string.meeting_state_appointment);
		stateHistory = context.getString(R.string.meeting_state_history);
	}

	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LogUtils.i(TAG, "--onCreateViewHolder--");
		View view = ViewUtils.makeView(context, R.layout.listitem_meeting_list, parent, false);
		return new ItemViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ItemViewHolder holder, int position) {
		LogUtils.i(TAG, "--onBindViewHolder--");
		MeetingInfo meetingInfo = getItem(position);
		// 设置会议名称
		holder.nameTv.setText(meetingInfo.name);
		// 设置会议开始时间
		holder.startTimeTv.setText(meetingInfo.startTime);
		// 设置会议图标
		holder.typeIv.setImageDrawable(getTypeDrawable(meetingInfo));
		// 设置操作区间
		setOperationArea(meetingInfo, holder);

	}

	// 根据会议状态分组
	@Override
	public long getHeaderId(int position) {
		return getItem(position).state;
	}

	@Override
	public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
		LogUtils.i(TAG, "--onCreateHeaderViewHolder--");
		View view = ViewUtils.makeView(context, R.layout.listitem_meeting_list_header,
				parent, false);
		return new HeaderViewHolder(view);
	}

	@Override
	public void onBindHeaderViewHolder(HeaderViewHolder holder, int position) {
		LogUtils.i(TAG, "--onBindHeaderViewHolder--");
		MeetingInfo meetingInfo = getItem(position);
		holder.stateTv.setText(getMeetingState(meetingInfo));
		holder.stateTv.getPaint().setFakeBoldText(true);
	}

	/**
	 * 获取分组时间显示
	 */
	private String getMeetingState(MeetingInfo meetingInfo) {
		switch (meetingInfo.state) {
			case MeetingState.HISTORY:
				return stateHistory;
			case MeetingState.PROGRESS:
				return stateProgress;
			case MeetingState.APPOINTMENT:
				return stateAppointment;
		}
		return stateHistory;
	}

	/**
	 * 获取会议显示图标
	 *
	 * @param meetingInfo MeetingInfo
	 */
	private Drawable getTypeDrawable(MeetingInfo meetingInfo) {
		switch (meetingInfo.state) {
			case MeetingState.HISTORY:
				if (meetingInfo.createdFlag) {
					if (meetingInfo.participatedFlag)
						return typePartiCreatedDrawable;
					else
						return typeCreatedDrawable;
				}
				// 只是参与
				else {
					return typeParticipatedDrawable;
				}
			case MeetingState.PROGRESS:
			case MeetingState.APPOINTMENT:
				break;
		}
		return typeDefaultDrawable;
	}


	/**
	 * 设置操作区域
	 */
	private void setOperationArea(MeetingInfo meetingInfo, ItemViewHolder holder) {
		switch (meetingInfo.state) {
			// 历史会议没有操作区域
			case MeetingState.HISTORY:
				holder.topLineIv.setVisibility(View.VISIBLE);
				holder.RightLineIv.setVisibility(View.GONE);
				holder.BottomLineIv.setVisibility(View.GONE);
				holder.operation1Ibt.setVisibility(View.GONE);
				holder.operation2Ibt.setVisibility(View.GONE);
				holder.QRCodeIb.setVisibility(View.GONE);
				break;
			// 正在进行的会议
			case MeetingState.PROGRESS:
				holder.QRCodeIb.setVisibility(View.VISIBLE);
				holder.topLineIv.setVisibility(View.VISIBLE);
				holder.RightLineIv.setVisibility(View.VISIBLE);
				holder.BottomLineIv.setVisibility(View.VISIBLE);
				// 自己是创建人的情况
				if (meetingInfo.createdFlag) {
					holder.operation2Ibt.setVisibility(View.VISIBLE);
					holder.operation2Ibt.setImage(opDismissDrawable);
					holder.operation2Ibt.setText(opDismiss);
					holder.operation1Ibt.setVisibility(View.VISIBLE);
					// 参与了的就显示为退出会议
					if (meetingInfo.participatedFlag) {
						holder.operation1Ibt.setImage(opQuitDrawable);
						holder.operation1Ibt.setText(opQuit);
					}
					// 没参与就显示为加入会议
					else {
						holder.operation1Ibt.setImage(opAddDrawable);
						holder.operation1Ibt.setText(opAdd);
					}
				}
				// 不是创建人的情况，这种情况下一定是已经加入了会议才显示
				else {
					holder.operation2Ibt.setVisibility(View.GONE);
					holder.operation1Ibt.setVisibility(View.VISIBLE);
					holder.operation1Ibt.setImage(opQuitDrawable);
					holder.operation1Ibt.setText(opQuit);
				}
				break;
			case MeetingState.APPOINTMENT:
				holder.topLineIv.setVisibility(View.VISIBLE);
				holder.RightLineIv.setVisibility(View.VISIBLE);
				holder.BottomLineIv.setVisibility(View.VISIBLE);
				holder.QRCodeIb.setVisibility(View.GONE);
				holder.operation2Ibt.setVisibility(View.GONE);
				holder.operation1Ibt.setVisibility(View.VISIBLE);
				holder.operation1Ibt.setImage(opCancelDrawable);
				holder.operation1Ibt.setText(opCancel);
				break;
		}

	}


	/**
	 * Item的ViewHolder类
	 */
	static class ItemViewHolder extends RecyclerView.ViewHolder {
		// 主区域
		ImageView typeIv;
		TextView nameTv;
		TextView startTimeTv;
		ImageButton QRCodeIb;
		// 操作区域
		ImageView topLineIv;
		ImageView RightLineIv;
		ImageView BottomLineIv;
		ImageTextButton operation1Ibt;
		ImageTextButton operation2Ibt;


		ItemViewHolder(View view) {
			super(view);
			typeIv = (ImageView) view.findViewById(R.id.type_iv);
			nameTv = (TextView) view.findViewById(R.id.name_tv);
			startTimeTv = (TextView) view.findViewById(R.id.start_time_tv);
			QRCodeIb = (ImageButton) view.findViewById(R.id.qrcode_ib);
			topLineIv = (ImageView) view.findViewById(R.id.top_line_iv);
			RightLineIv = (ImageView) view.findViewById(R.id.right_line_iv);
			BottomLineIv = (ImageView) view.findViewById(R.id.bottom_line_iv);
			operation1Ibt = (ImageTextButton) view.findViewById(R.id.operation1_itb);
			operation2Ibt = (ImageTextButton) view.findViewById(R.id.operation2_itb);
		}
	}

	/**
	 * Header的ViewHolder类
	 */
	static class HeaderViewHolder extends RecyclerView.ViewHolder {
		TextView stateTv;

		HeaderViewHolder(View itemView) {
			super(itemView);
			stateTv = (TextView) itemView.findViewById(R.id.state_tv);
		}
	}

//	/**
//	 * 点击加入会议监听
//	 */
//	public interface OnClickAddListener {
//		void onClick(MeetingInfo info);
//	}
//
//	/**
//	 * 点击退出会议监听
//	 */
//	public interface OnClickQuitListener {
//		void onClick(MeetingInfo info);
//	}
//
//	/**
//	 * 点击取消会议监听
//	 */
//	public interface OnClickCancelListener {
//		void onClick(MeetingInfo info);
//	}
//
//	/**
//	 * 点击解散会议监听
//	 */
//	public interface OnClickDismissListener {
//		void onClick(MeetingInfo info);
//	}

	public interface OnClickItemListener {
		void onClick(MeetingInfo info);
	}


}
