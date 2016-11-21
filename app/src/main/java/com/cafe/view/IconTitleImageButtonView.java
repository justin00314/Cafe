package com.cafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cafe.R;


/**
 * Created by Rocky on 2016/11/15.
 */

public class IconTitleImageButtonView extends FrameLayout {

    public ImageView icon;
    public TextView title;
    public TextView content;
    public ImageButton button;

    public IconTitleImageButtonView(Context context) {
        super(context);

        init(context);
    }

    public IconTitleImageButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public IconTitleImageButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    private void init(Context context) {
        inflate(context, R.layout.include_icon_title_imagebutton, this);

        icon = (ImageView) findViewById(R.id.icon);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        button = (ImageButton) findViewById(R.id.button);
    }
}
