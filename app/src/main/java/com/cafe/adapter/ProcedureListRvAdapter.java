package com.cafe.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiviews.anim.AnimationImageLoadingListener;
import com.cafe.R;
import com.cafe.common.CommonUtils;
import com.cafe.data.meeting.ProcedureInfo;
import com.cafe.data.meeting.SpeakState;
import com.cafe.data.meeting.SpeakType;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.justin.utils.common.LogUtils;
import org.justin.utils.common.ResourcesUtils;
import org.justin.utils.common.ViewUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 会议过程详情列表适配器
 * Created by Justin Z on 2016/11/29.
 * 502953057@qq.com
 */

public class ProcedureListRvAdapter extends ProcedureListAdapter<ProcedureListRvAdapter.ItemViewHolder> {

	private final static String TAG = ProcedureListRvAdapter.class.getSimpleName();

	private LayoutInflater inflater;
	private Context context;

	private String speakNormal;
	private String speakOverTime;
	private String speakTheme;
	private String speakEpisode;

	public ProcedureListRvAdapter(Context context) {
		this.context = context;
		init();
	}

	private void init() {
		speakNormal = context.getString(R.string.speak_normal);
		speakOverTime = context.getString(R.string.speak_over_time);
		speakTheme = context.getString(R.string.speak_theme);
		speakEpisode = context.getString(R.string.speak_episode);
	}

	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LogUtils.i(TAG, "--onCreateViewHolder--");
		View view = ViewUtils.makeView(context, R.layout.listitem_procedure, parent, false);
		return new ProcedureListRvAdapter.ItemViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ItemViewHolder holder, int position) {
		LogUtils.i(TAG, "--onBindViewHolder--");
		ProcedureInfo procedureInfo = getItem(position);
		// 设置说话人名字
		holder.speakerNameTv.setText(procedureInfo.userName);
		// 设置说话人发言类型
		holder.speakTypeTv.setText(getSpeakType(procedureInfo.type));
		// 设置说话人发言时长
		holder.speakDurationTv.setText(getSpeakDuration(procedureInfo.duration));
		// 设置说话人发言状态
		holder.speakStateTv.setText(getSpeakState(procedureInfo.state));
		// 加载说话人头像
		ImageLoader.getInstance().displayImage(procedureInfo.userPortrait,
				holder.portraitCiv, CommonUtils.getPortraitOptions(),
				new AnimationImageLoadingListener());
	}

	/**
	 * 获取说话类型文字显示
	 */
	private String getSpeakType(int type) {
		switch (type) {
			case SpeakType.THEME:
				return speakTheme;
			case SpeakType.EPISODE:
				return speakEpisode;
		}
		return speakTheme;
	}

	/**
	 * 获取说话状态文字显示
	 */
	private String getSpeakState(int state) {
		switch (state) {
			case SpeakState.NORMAL:
				return speakNormal;
			case SpeakState.OVER_TIME:
				return speakOverTime;
		}
		return speakNormal;
	}

	/**
	 * 获取说话时长文字显示
	 */
	private String getSpeakDuration(int time) {
		int minute = CommonUtils.getMinute(time);
		int second = CommonUtils.getSecond(time);
		if (minute > 0) {
			return String.format(context.getString(R.string.speak_duration_ms),
					minute, second);
		}
		return String.format(context.getString(R.string.speak_duration_s),
				second);
	}

	static class ItemViewHolder extends RecyclerView.ViewHolder {

		CircleImageView portraitCiv;
		TextView speakerNameTv;
		TextView speakTypeTv;
		TextView speakDurationTv;
		TextView speakStateTv;

		ItemViewHolder(View view) {
			super(view);
			portraitCiv = (CircleImageView) view.findViewById(R.id.main_area);
			speakerNameTv = (TextView) view.findViewById(R.id.speaker_name_tv);
			speakTypeTv = (TextView) view.findViewById(R.id.speak_type_tv);
			speakDurationTv = (TextView) view.findViewById(R.id.speak_duration_tv);
			speakStateTv = (TextView) view.findViewById(R.id.speak_state_tv);
		}

	}
}
