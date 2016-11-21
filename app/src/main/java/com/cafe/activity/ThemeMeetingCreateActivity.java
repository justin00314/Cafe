package com.cafe.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cafe.R;
import com.cafe.common.mvp.MVPActivity;
import com.cafe.contract.ThemeMeetingCreateContract;
import com.cafe.presenter.ThemeMeetingCreatePresenter;

public class ThemeMeetingCreateActivity extends MVPActivity<ThemeMeetingCreateContract.View,
        ThemeMeetingCreateContract.Presenter> implements ThemeMeetingCreateContract.View {


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
        getPresenter().submit();
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

        com.rey.material.widget.Spinner organizer =
                (com.rey.material.widget.Spinner) findViewById(R.id.organizer).findViewById(R.id.spinner);
        //     organizer.getLabelView.setText(R.string.meeting_organizer);

        com.rey.material.widget.Spinner place =
                (com.rey.material.widget.Spinner) findViewById(R.id.place).findViewById(R.id.spinner);
        //     place.getLabelView.setText(R.string.meeting_place);

        TextView meetingDate = (TextView) findViewById(R.id.date).findViewById(R.id.title);
        meetingDate.setText(R.string.meeting_date);

        ImageButton dateButton = (ImageButton) findViewById(R.id.date).findViewById(R.id.button);
        dateButton.setImageResource(R.mipmap.icon_calendar);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void submitDone(boolean success) {
        if (success) {
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.success),
                    Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.fail),
                    Snackbar.LENGTH_SHORT).show();
        }
    }
}
