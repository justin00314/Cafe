package com.aiviews.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.aiviews.R;
import com.rey.material.widget.Button;

import org.justin.utils.common.LogUtils;
import org.justin.utils.common.ViewUtils;

/**
 * 警告对话框
 * Created by Justin Z on 2016/11/24.
 * 502953057@qq.com
 */

public class AlertDialog extends DialogFragment {
	private final static String TAG = AlertDialog.class.getSimpleName();

	private TextView titleTv;

	private TextView contentTv;

	private Button ensureBtn;

	private Button cancelBtn;

	private String title;
	private String content;
	private String ensureText;
	private String cancelText;

	private OnClickDialogBtnListener<Void> listener;

	public static AlertDialog newInstance() {
		return new AlertDialog();
	}

	/**
	 * 创建对话框
	 *
	 * @param loadAnim boolean
	 * @param title    String
	 * @param content  String
	 * @return AlertDialog
	 */
	public static AlertDialog newInstance(String title, String content) {
		AlertDialog alertDialog = new AlertDialog();
		alertDialog.title = title;
		alertDialog.content = content;
		return alertDialog;
	}

	public static AlertDialog newInstance(String title, String content,
	                                      String ensure, String cancel) {
		AlertDialog alertDialog = new AlertDialog();
		alertDialog.title = title;
		alertDialog.content = content;
		alertDialog.ensureText = ensure;
		alertDialog.cancelText = cancel;
		return alertDialog;
	}

	public void setOnClickDialogBtnListener(OnClickDialogBtnListener<Void> listener) {
		this.listener = listener;
	}

	public void setTitle(String title) {
		if (titleTv == null) return;
		titleTv.setText(title);
	}

	public void setContent(String content) {
		if (contentTv == null) return;
		contentTv.setText(content);
	}

	public void setEnsureText(String text) {
		if (ensureBtn == null) return;
		ensureBtn.setText(text);
	}

	public void setCancelText(String text) {
		if (cancelBtn == null) return;
		cancelBtn.setText(text);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LogUtils.i(TAG, "--onCreateDialog--");
		View contentView = ViewUtils.makeView(getActivity(), R.layout.dialog_alert);
		BaseDialog dialog = createDialog(contentView);
		assignViews(contentView);
		setAlertInfo();
		return dialog;
	}

	/**
	 * 创建Dialog
	 */
	private BaseDialog createDialog(View contentView) {
		LogUtils.i(TAG, "--显示警告对话框--");
		BaseDialog.Builder builder = new BaseDialog.Builder(getActivity(), contentView);
//		builder.setWidth(DisplayUtils.getScreenWidth(getActivity()));
//		builder.setHeight(DisplayUtils.getScreenHeight(getActivity()));
		builder.setGravity(Gravity.CENTER);
		builder.setStyle(R.style.BaseAlertDialog);
		builder.setAnimStyle(R.style.windowAnimScale);
		BaseDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	private void assignViews(View contentView) {
		titleTv = (TextView) contentView.findViewById(R.id.title_tv);
		contentTv = (TextView) contentView.findViewById(R.id.content_tv);
		ensureBtn = (Button) contentView.findViewById(R.id.ensure_btn);
		ensureBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (listener != null)
					listener.onClickEnsure(AlertDialog.this, null);
			}
		});
		cancelBtn = (Button) contentView.findViewById(R.id.cancel_btn);
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (listener != null)
					listener.onClickCancel(AlertDialog.this);
			}
		});
	}

	private void setAlertInfo() {
		if (!TextUtils.isEmpty(title))
			titleTv.setText(title);
		if (!TextUtils.isEmpty(content))
			contentTv.setText(content);
		if (!TextUtils.isEmpty(ensureText))
			ensureBtn.setText(ensureText);
		if (!TextUtils.isEmpty(cancelText))
			cancelBtn.setText(cancelText);
	}


}
