package com.ai2020lab.cafe.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ai2020lab.cafe.R;
import com.ai2020lab.cafe.common.mvp.MVPActivity;
import com.ai2020lab.cafe.contract.ThemeMeetingCreateContract;
import com.ai2020lab.cafe.presenter.ThemeMeetingCreatePresenter;

public class ThemeMeetingCreateActivity extends MVPActivity<ThemeMeetingCreateContract.View,
        ThemeMeetingCreatePresenter> implements ThemeMeetingCreateContract.View {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_meeting_create);

        setupUI();
        setToolbar();
    }

    @Override
    public ThemeMeetingCreatePresenter initPresenter() {
        return new ThemeMeetingCreatePresenter();
    }

    public void submit(View v) {

    }


    /**
     * 设置工具栏
     */
    private void setToolbar() {
        supportToolbar(true);
        setToolbarLeft(R.mipmap.logout, new OnLeftClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        setToolbarTitle(getString(R.string.meeting_theme));
    }

    private void setupUI() {
        TextView theme = (TextView) findViewById(R.id.theme).findViewById(R.id.editor_title);
        theme.setText(R.string.meeting_theme);

        TextView number = (TextView) findViewById(R.id.number).findViewById(R.id.editor_title);
        number.setText(R.string.meeting_number);

        com.rey.material.widget.Spinner organizer = (com.rey.material.widget.Spinner) findViewById(R.id.organizer).findViewById(R.id.title);
   //     organizer.getLabelView.setText(R.string.meeting_organizer);

        com.rey.material.widget.Spinner place = (com.rey.material.widget.Spinner) findViewById(R.id.place).findViewById(R.id.title);
   //     place.getLabelView.setText(R.string.meeting_place);
    }


}
