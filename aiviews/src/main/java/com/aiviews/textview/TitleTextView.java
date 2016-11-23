package com.aiviews.textview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiviews.R;

/**
 * 带标题的TextView
 * Created by Justin Z on 2016/11/23.
 * 502953057@qq.com
 */

public class TitleTextView extends LinearLayout {

	private TextView titleTv;
	private TextView contentTv;

	public TitleTextView(Context context) {
		super(context);
		init(context);
	}

	public TitleTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.title_text_view, this, true);
		titleTv = (TextView) findViewById(R.id.text_title);
		contentTv = (TextView) findViewById(R.id.text_content);
	}

	public void setTitleText(CharSequence title){
		titleTv.setText(title);
	}

	public void setContentText(CharSequence content){
		contentTv.setText(content);
	}

	public void setTitleSize(int size){
		titleTv.setTextSize(size);
	}

	public void setContentSize(int size){
		contentTv.setTextSize(size);
	}

	public void setTitleColor(int color){
		titleTv.setTextColor(color);
	}

	public void setContentColor(int color){
		contentTv.setTextColor(color);
	}

	public void setAllTextSize(int size){
		titleTv.setTextSize(size);
		contentTv.setTextSize(size);
	}

	public void setAllTextColor(int color){
		titleTv.setTextColor(color);
		contentTv.setTextColor(color);
	}


}
