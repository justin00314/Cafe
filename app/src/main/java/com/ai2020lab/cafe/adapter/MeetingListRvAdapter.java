package com.ai2020lab.cafe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ai2020lab.aiutils.common.LogUtils;
import com.ai2020lab.aiutils.common.TimeUtils;
import com.ai2020lab.aiutils.common.ViewUtils;
import com.ai2020lab.cafe.R;
import com.ai2020lab.cafe.data.meeting.MeetingInfo;
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

	/**
	 * 构造方法
	 *
	 * @param context Context
	 */
	public MeetingListRvAdapter(Context context) {
		this.context = context;
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
		// 加载网络图片
//		ImageLoader.getInstance().displayImage(growthInfo.pigPhoto, holder.pigPhotoRiv,
//				ImageLoaderManager.getImageOptions(context),
//				new AnimationImageLoadingListener());
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
	 * Item的ViewHolder类
	 */
	static class ItemViewHolder extends RecyclerView.ViewHolder {
		ImageView stateIv;
		TextView nameTv;
		TextView startTimeTv;
		ImageView QRcodeIv;

		ItemViewHolder(View view) {
			super(view);
//			pigPhotoRiv = (ImageView) view.findViewById(R.id.pig_photo_riv);
//			dateTv = (TextView) view.findViewById(R.id.date_tv);
//			weightTv = (TextView) view.findViewById(R.id.weight_tv);
//			weightIncreaseTv = (TextView) view.findViewById(R.id.weight_increase_tv);
		}
	}

	/**
	 * Header的ViewHolder类
	 */
	public static class HeaderViewHolder extends RecyclerView.ViewHolder {
		TextView headerTimeView;

		HeaderViewHolder(View itemView) {
			super(itemView);
//			headerTimeView = (TextView) itemView.findViewById(R.id.growth_history_time_tv);
		}


	}


}
