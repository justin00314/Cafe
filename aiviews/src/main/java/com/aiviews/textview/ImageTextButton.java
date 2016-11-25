package com.aiviews.textview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiviews.R;
import com.aiviews.rippleview.RippleView;

import org.justin.utils.common.LogUtils;
import org.justin.utils.common.ResourcesUtils;


/**
 * Created by Justin Z on 2016/11/8.
 * 502953057@qq.com
 */

public class ImageTextButton extends RippleView {

	private final static String TAG = ImageTextButton.class.getSimpleName();

	private ImageView titleIv;

	private TextView contentTv;


	public ImageTextButton(Context context) {
		super(context);
		init(context);
	}

	public ImageTextButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.image_text_button, this, true);
		titleIv = (ImageView) findViewById(R.id.image_text_iv);
		contentTv = (TextView) findViewById(R.id.image_text_tv);
//		setClickable(true);
//		setFocusable(true);
//		setEnabled(true);
	}

	/**
	 * 设置按钮文字内容
	 *
	 * @param title
	 */
	public void setText(CharSequence title) {
		contentTv.setText(title);
	}

	/**
	 * 设置文字大小
	 *
	 * @param size int 单位为sp
	 */
	public void setTextSize(int size) {
		contentTv.setTextSize(size);
	}

	/**
	 * 设置文字颜色
	 *
	 * @param colorResID 颜色资源ID
	 */
	public void setTextColor(int colorResID) {
		contentTv.setTextColor(ResourcesUtils.getColor(colorResID));
	}

	/**
	 * 设置按钮的图形标题
	 *
	 * @param drawableResID Drawable资源ID
	 */
	public void setImage(int drawableResID) {
		Drawable drawable = ResourcesUtils.getDrawable(drawableResID);
		if (drawable == null) {
			LogUtils.i(TAG, "Drawable资源文件找不到");
			return;
		}
		titleIv.setImageDrawable(drawable);
	}

	public void setImage(Drawable drawable) {
		if (drawable == null) {
			LogUtils.i(TAG, "Drawable资源文件找不到");
			return;
		}
		titleIv.setImageDrawable(drawable);
	}


}
