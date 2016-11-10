package com.ai2020lab.cafe.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ai2020lab.aiutils.common.LogUtils;
import com.ai2020lab.aiutils.common.ResourcesUtils;
import com.ai2020lab.aiutils.common.TimeUtils;
import com.ai2020lab.aiutils.common.ViewUtils;
import com.ai2020lab.aiviews.textview.ImageTextButton;
import com.ai2020lab.cafe.R;
import com.ai2020lab.cafe.data.meeting.MeetingInfo;
import com.ai2020lab.cafe.data.meeting.MeetingState;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

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
	private Drawable opJoinDrawable;
	private Drawable opQuitDrawable;
	private Drawable opDismissDrawable;
	private Drawable opCancelDrawable;

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
		opJoinDrawable = ResourcesUtils.getDrawable(R.mipmap.meeting_join);
		opQuitDrawable = ResourcesUtils.getDrawable(R.mipmap.meeting_quit);
		opDismissDrawable = ResourcesUtils.getDrawable(R.mipmap.meeting_dismiss);
		opCancelDrawable = ResourcesUtils.getDrawable(R.mipmap.meeting_cancel);
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

//		// 设置时间
//		holder.dateTv.setText(getUploadDateStr(growthInfo));
//		// 设置体重
//		holder.weightTv.setText(getWeightStr(growthInfo));
//		// 设置增长体重
//		holder.weightIncreaseTv.setText(getIncreasedStr(growthInfo));
	}


	// 根据时间分组
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
//		holder.headerTimeView.setText(getYMD(context, growthInfo.collectedTime));
//		holder.headerTimeView.getPaint().setFakeBoldText(true);
	}

	/**
	 * 获取分组时间显示
	 */
	private String getYMD(Context context, long timeStamp) {
		String YMD = TimeUtils.formatTimeStamp(timeStamp, TimeUtils.Template.YMD);
		if (!TextUtils.isEmpty(YMD)) {
			String year = YMD.substring(0, 4);
			String month = YMD.substring(5, 7);
			String day = YMD.substring(8, 10);
//			return String.format(context.getString(R.string.year_month_day),
//					year, month, day);
		}
		return null;
	}

	/**
	 * 获取会议显示图标
	 *
	 * @param meetingInfo MeetingInfo
	 */
	private Drawable getTypeDrawable(MeetingInfo meetingInfo) {
		switch (meetingInfo.state) {
			case MeetingState.HISTORY:
				// 只是创建
				if (meetingInfo.createdFlag && !meetingInfo.participatedFlag) {
					return typeCreatedDrawable;
				}
				// 只是参与
				else if (!meetingInfo.createdFlag && meetingInfo.participatedFlag) {
					return typeParticipatedDrawable;
				}
				// 创建和参与
				else if (meetingInfo.createdFlag && meetingInfo.participatedFlag) {
					return typePartiCreatedDrawable;
				}
			case MeetingState.PROGRESS:
			case MeetingState.APPOINTMENT:
				break;
		}
		return typeDefaultDrawable;
	}

	/**
	 * Item的ViewHolder类
	 */
	static class ItemViewHolder extends RecyclerView.ViewHolder {
		// 主区域
		ImageView typeIv;
		TextView nameTv;
		TextView startTimeTv;
		ImageView QRCodeIv;
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
			QRCodeIv = (ImageView) view.findViewById(R.id.qrcode_iv);
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


}
