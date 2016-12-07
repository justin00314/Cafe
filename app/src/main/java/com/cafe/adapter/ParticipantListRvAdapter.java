package com.cafe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiviews.anim.AnimationImageLoadingListener;
import com.cafe.R;
import com.cafe.common.CommonUtils;
import com.cafe.data.account.UserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.justin.utils.common.LogUtils;
import org.justin.utils.common.ViewUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 会议参与人列表适配器
 * Created by Justin Z on 2016/12/7.
 * 502953057@qq.com
 */

public class ParticipantListRvAdapter extends ParticipantListAdapter<ParticipantListRvAdapter.ItemViewHolder> {

	private final static String TAG = ParticipantListRvAdapter.class.getSimpleName();

	private Context context;

	public ParticipantListRvAdapter(Context context) {
		this.context = context;
	}

	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LogUtils.i(TAG, "--onCreateViewHolder--");
		View view = ViewUtils.makeView(context, R.layout.listitem_participant, parent, false);
		return new ItemViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ItemViewHolder holder, int position) {
		LogUtils.i(TAG, "--onBindViewHolder--");
		UserInfo userInfo = getItem(position);
		// 设置参与者名字
		holder.userNameTv.setText(userInfo.userName);
		// 加载参与者头像
		ImageLoader.getInstance().displayImage(userInfo.userPortrait,
				holder.portraitCiv, CommonUtils.getPortraitOptions(),
				new AnimationImageLoadingListener());
	}


	static class ItemViewHolder extends RecyclerView.ViewHolder {

		CircleImageView portraitCiv;
		TextView userNameTv;

		ItemViewHolder(View view) {
			super(view);
			portraitCiv = (CircleImageView) view.findViewById(R.id.user_portrait_civ);
			userNameTv = (TextView) view.findViewById(R.id.user_name_tv);
		}
	}
}
