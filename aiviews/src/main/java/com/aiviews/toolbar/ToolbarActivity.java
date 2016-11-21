package com.aiviews.toolbar;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ai2020lab.aiutils.common.ResourcesUtils;
import com.ai2020lab.aiutils.common.ViewUtils;
import com.ai2020lab.aiviews.R;
import com.aiviews.rippleview.RippleView;

/**
 * 自定义ToolbarActivity,实现Toolbar的自定义封装
 * Created by Justin on 2015/12/10.
 * Email:502953057@qq.com,zhenghx3@asiainfo.com
 */
public class ToolbarActivity extends AppCompatActivity {

	private final static String TAG = ToolbarActivity.class.getSimpleName();

	private Activity activity;

	private Toolbar toolbar;

	private FrameLayout contentContainer;

	private AppBarLayout toolbarContainer;

	private View toolbarLeftView;

	private View toolbarRightView;

	private TextView toolbarTitle;
	private ImageView toolbarLeftIv;
	private ImageView toolbarRightIv1;
	private ImageView toolbarRightIv2;


	/**
	 * 程序入口
	 *
	 * @param savedInstanceState Bundle
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	/**
	 * 获取Activity对象的引用
	 *
	 * @return 返回Activity对象的引用
	 */
	public Activity getActivity() {
		return activity;
	}

	// 初始化数据和界面布局
	private void init() {
		activity = this;
		super.setContentView(R.layout.activity_toolbar);
		contentContainer = (FrameLayout) findViewById(R.id.activity_content);
		toolbarContainer = (AppBarLayout) findViewById(R.id.toolbar_layout);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
	}

	/**
	 * 获取工具栏View
	 *
	 * @return 返回工具栏View
	 */
	public Toolbar getToolbar() {
		return toolbar;
	}

	/**
	 * 获取工具栏是否显示
	 *
	 * @return true-显示，false-不显示
	 */
	public boolean getToolbarVisibility() {
		return contentContainer.getVisibility() == View.VISIBLE;
	}

	/**
	 * 设置contentView
	 *
	 * @param layoutResID contentView的布局文件
	 */
	public void setContentView(int layoutResID) {
		View contentView = ViewUtils.makeView(this, layoutResID);
		if (contentView == null) {
			throw new IllegalArgumentException("布局资源文件找不到");
		}
		contentContainer.addView(contentView);
	}

	/**
	 * 设置contentView
	 *
	 * @param view contentView对象
	 */
	public void setContentView(View view) {
		if (view == null) {
			throw new IllegalArgumentException("布局资源文件找不到");
		}
		contentContainer.addView(view);
	}

	/**
	 * 是否支持toolbar
	 *
	 * @param isSupport true-toolbar显示，false-toolbar不显示
	 */
	public void supportToolbar(boolean isSupport) {
		if (isSupport) {
			toolbarContainer.setVisibility(View.VISIBLE);
			toolbar.setVisibility(View.VISIBLE);
			intToolbar();
		} else {
			toolbarContainer.setVisibility(View.GONE);
			toolbar.setVisibility(View.GONE);
		}
	}

	private void intToolbar() {
		toolbar.setTitle("");
		toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
		setSupportActionBar(toolbar);
		toolbarLeftView = ViewUtils.makeView(this, R.layout.toolbar_left);
		toolbar.addView(toolbarLeftView);
		toolbarLeftIv = (ImageView) toolbarLeftView.findViewById(R.id.toolbar_left_iv);
		toolbarTitle = (TextView) toolbarLeftView.findViewById(R.id.toolbar_title);
		toolbarRightView = ViewUtils.makeView(this, R.layout.toolbar_right);
		toolbar.addView(toolbarRightView);
		toolbarRightIv1 = (ImageView) toolbarRightView.findViewById(R.id.toolbar_right_iv1);
		toolbarRightIv2 = (ImageView) toolbarRightView.findViewById(R.id.toolbar_right_iv2);
		Toolbar.LayoutParams lp = (Toolbar.LayoutParams) toolbarRightView.getLayoutParams();
		lp.gravity = Gravity.END | Gravity.CENTER_VERTICAL;

	}

	/**
	 * 设置toolbar标题
	 *
	 * @param title 标题的文字显示
	 */
	public void setToolbarTitle(CharSequence title) {
//		toolbarTitle.getPaint().setFakeBoldText(true);
		toolbarTitle.setText(title);
	}

	/**
	 * 设置Toolbar背景
	 *
	 * @param drawableResID Drawable资源ID
	 */
	public void setToolbarBackground(int drawableResID) {
		Drawable drawable = ResourcesUtils.getDrawable(drawableResID);
		if (drawable == null) {
			throw new IllegalArgumentException("toolbar背景Drawable资源文件找不到");
		}
		toolbar.setBackground(drawable);
	}

	/**
	 * 设置工具栏左边按钮图标
	 *
	 * @param drawableResID Drawable资源ID
	 * @param listener      OnLeftClickListener
	 */
	public void setToolbarLeft(int drawableResID, final OnLeftClickListener listener) {
		if (toolbarLeftView == null) {
			throw new IllegalArgumentException("还没有初始化toolbar");
		}
		Drawable drawable = ResourcesUtils.getDrawable(drawableResID);
		if (drawable == null) {
			throw new IllegalArgumentException("toolbar左边按钮Drawable资源文件找不到");
		}
		toolbarLeftIv.setImageDrawable(drawable);
		toolbarLeftView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (listener != null)
					listener.onClick();
			}
		});
	}

	/**
	 * 设置工具栏左边按钮的尺寸
	 *
	 * @param width  宽度
	 * @param height 高度
	 */
	public void setToolbarLeftDimension(int width, int height) {
		if (toolbarLeftView == null) {
			throw new IllegalArgumentException("还没有初始化toolbar");
		}
		RippleView.LayoutParams lp = (RippleView.LayoutParams) toolbarLeftIv.getLayoutParams();
		if (width > -2) {
			lp.width = width;
		}
		if (height > -2) {
			lp.height = height;
		}
		toolbarLeftIv.setLayoutParams(lp);
	}

	/**
	 * 设置工具栏右边按钮图标1
	 *
	 * @param drawableResID int
	 * @param listener      OnRightClickListener
	 */
	public void setToolbarRight1(int drawableResID, final OnRightClickListener listener) {
		if (toolbarRightView == null) {
			throw new IllegalArgumentException("还没有初始化toolbar");
		}
		Drawable drawable = ResourcesUtils.getDrawable(drawableResID);
		if (drawable == null) {
			throw new IllegalArgumentException("toolbar左边按钮Drawable资源文件找不到");
		}
		toolbarRightIv1.setImageDrawable(drawable);
		toolbarRightIv1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (listener != null)
					listener.onClick();
			}
		});
	}

	/**
	 * 设置工具栏右边按钮图标2
	 *
	 * @param drawableResID int
	 * @param listener      OnRightClickListener
	 */
	public void setToolbarRight2(int drawableResID, final OnRightClickListener listener) {
		if (toolbarRightView == null) {
			throw new IllegalArgumentException("还没有初始化toolbar");
		}
		Drawable drawable = ResourcesUtils.getDrawable(drawableResID);
		if (drawable == null) {
			throw new IllegalArgumentException("toolbar左边按钮Drawable资源文件找不到");
		}
		toolbarRightIv2.setImageDrawable(drawable);
		toolbarRightIv2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (listener != null)
					listener.onClick();
			}
		});
	}

	/**
	 * 设置工具栏右边按钮1的尺寸
	 *
	 * @param width  宽度
	 * @param height 高度
	 */
	public void setToolbarRight1Dimension(int width, int height) {
		if (toolbarRightView == null) {
			throw new IllegalArgumentException("还没有初始化toolbar");
		}
		RippleView.LayoutParams lp = (RippleView.LayoutParams) toolbarRightIv1.getLayoutParams();
		if (width > -2) {
			lp.width = width;
		}
		if (height > -2) {
			lp.height = height;
		}
		toolbarRightIv1.setLayoutParams(lp);
	}

	/**
	 * 设置工具栏右边按钮2的尺寸
	 *
	 * @param width  宽度
	 * @param height 高度
	 */
	public void setToolbarRight2Dimension(int width, int height) {
		if (toolbarRightView == null) {
			throw new IllegalArgumentException("还没有初始化toolbar");
		}
		RippleView.LayoutParams lp = (RippleView.LayoutParams) toolbarRightIv2.getLayoutParams();
		if (width > -2) {
			lp.width = width;
		}
		if (height > -2) {
			lp.height = height;
		}
		toolbarRightIv2.setLayoutParams(lp);
	}


	/**
	 * 导航栏左边按钮点击监听
	 */
	public interface OnLeftClickListener {
		void onClick();
	}

	/**
	 * 导航栏右边按钮点击监听
	 */
	public interface OnRightClickListener {
		void onClick();
	}


}
