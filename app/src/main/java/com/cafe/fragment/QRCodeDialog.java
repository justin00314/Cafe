package com.cafe.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiviews.dialog.BaseDialog;
import com.aiviews.textview.TitleTextView;
import com.cafe.R;
import com.cafe.data.meeting.MeetingUserInfo;
import com.rey.material.widget.Button;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.justin.utils.common.LogUtils;
import org.justin.utils.common.ViewUtils;
import org.justin.utils.system.DisplayUtils;

/**
 * 展示会议二维码对话框
 * Created by Justin Z on 2016/11/11.
 * 502953057@qq.com
 */

public class QRCodeDialog extends DialogFragment {
	private final static String TAG = QRCodeDialog.class.getSimpleName();

	private boolean loadAnim;
	private MeetingUserInfo info;

	private TextView titleTv;
	private ImageView qrcodeIv;
	private TitleTextView themeTtv;
	private TitleTextView idTtv;
	private TitleTextView timeTtv;
	private TitleTextView locationTtv;
	private Button ensureBtn;

	private Bitmap qrcodeBitmap;


	/**
	 * 创建对话框
	 *
	 * @param loadAnim boolean
	 * @param info     MeetingInfo
	 * @return QRCodeDialog
	 */
	public static QRCodeDialog newInstance(boolean loadAnim, MeetingUserInfo info) {
		QRCodeDialog qrCodeDialog = new QRCodeDialog();
		qrCodeDialog.loadAnim = loadAnim;
		qrCodeDialog.info = info;
		return qrCodeDialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LogUtils.i(TAG, "--onCreateDialog--");
		View contentView = ViewUtils.makeView(getActivity(), R.layout.dialog_qrcode);
		BaseDialog dialog = createDialog(contentView);
		assignViews(contentView);
		setMeetingInfo();
		return dialog;
	}

	/**
	 * 创建Dialog
	 */
	private BaseDialog createDialog(View contentView) {
		LogUtils.i(TAG, "--显示二维码对话框--");
		BaseDialog.Builder builder = new BaseDialog.Builder(getActivity(), contentView);
//		builder.setWidth(DisplayUtils.getScreenWidth(getActivity()));
//		builder.setHeight(DisplayUtils.getScreenHeight(getActivity()));
		builder.setGravity(Gravity.CENTER);
		builder.setStyle(R.style.BaseAlertDialog);
		if (loadAnim)
			builder.setAnimStyle(R.style.windowAnimScale);
		BaseDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	private void assignViews(View contentView) {
		titleTv = (TextView) contentView.findViewById(R.id.qrcode_title_tv);
		qrcodeIv = (ImageView) contentView.findViewById(R.id.qrcode_iv);
		themeTtv = (TitleTextView) contentView.findViewById(R.id.qrcode_theme_ttv);
		idTtv = (TitleTextView) contentView.findViewById(R.id.qrcode_id_ttv);
		timeTtv = (TitleTextView) contentView.findViewById(R.id.qrcode_time_ttv);
		locationTtv = (TitleTextView) contentView.findViewById(R.id.qrcode_location_ttv);
		ensureBtn = (Button) contentView.findViewById(R.id.qrcode_ensure_btn);
		ensureBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
				recycleBitmap();
			}
		});
	}

	private void setMeetingInfo() {
		titleTv.setText(getString(R.string.dialog_qrcode_title));
		themeTtv.setTitleText(getString(R.string.dialog_qrcode_theme));
		themeTtv.setContentText(info.name);
		idTtv.setTitleText(getString(R.string.dialog_qrcode_id));
		idTtv.setContentText(info.id + "");
		timeTtv.setTitleText(getString(R.string.dialog_qrcode_time));
		timeTtv.setContentText(info.startTime);
		locationTtv.setTitleText(getString(R.string.dialog_qrcode_location));
		locationTtv.setContentText(info.meetingRoomName);
		int w = DisplayUtils.getScreenWidth(getActivity()) / 2;
		qrcodeBitmap = CodeUtils.createImage(info.id + "", w, w, null);
		qrcodeIv.setImageBitmap(qrcodeBitmap);
	}

	private void recycleBitmap() {
		if (qrcodeBitmap != null && !qrcodeBitmap.isRecycled()) {
			qrcodeBitmap.recycle();
			qrcodeBitmap = null;
		}
	}

}
