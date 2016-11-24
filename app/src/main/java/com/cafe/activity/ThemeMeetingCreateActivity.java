package com.cafe.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.cafe.R;
import com.cafe.common.mvp.MVPActivity;
import com.cafe.contract.ThemeMeetingCreateContract;
import com.cafe.data.meeting.CreateMeetingRequest;
import com.cafe.data.meeting.MeetingInfo;
import com.cafe.fragment.SublimePickerFragment;
import com.cafe.presenter.ThemeMeetingCreatePresenter;
import com.rey.material.widget.Spinner;

import org.justin.utils.common.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThemeMeetingCreateActivity extends MVPActivity<ThemeMeetingCreateContract.View,
        ThemeMeetingCreateContract.Presenter> implements ThemeMeetingCreateContract.View {

    private final int DATE_TYPE_START = 1;
    private final int DATE_TYPE_FINISH = 2;

    private final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private TextView mStartDate;
    private TextView mFinishDate;
    private EditText mMeetingTheme;
    private EditText mParticipantNumber;
    private Calendar mStartCalendar;
    private Calendar mFinishCalendar;

//    private Spinner mOrganizerSpinner;
    private Spinner mLocationSpinner;

    private int mDateType = DATE_TYPE_START;

    SublimePickerFragment.Callback mFragmentCallback = new SublimePickerFragment.Callback() {
        @Override
        public void onCancelled() {

        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {
            switch (mDateType) {
                case DATE_TYPE_START:
                    handleStartDateSelecte(selectedDate, hourOfDay, minute);
                    break;
                case DATE_TYPE_FINISH:
                    handleFinishDateSelecte(selectedDate, hourOfDay, minute);
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_meeting_create);

        setupUI();
        setToolbar();
        loadData();
    }

    @Override
    public ThemeMeetingCreatePresenter initPresenter() {
        return new ThemeMeetingCreatePresenter(this);
    }

    public void submit(View v) {
        CreateMeetingRequest request = new CreateMeetingRequest();
        request.attendance = Integer.decode(mParticipantNumber.getText().toString()).intValue() ;
        request.startTime = mStartDate.getText().toString();
        request.endTime = mFinishDate.getText().toString();
        request.type = MeetingInfo.TYPE_THEME;
        request.meetingRoomId = "1";
        request.name = mMeetingTheme.getText().toString();

        getPresenter().createNewMeeting(request);
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
        TextInputLayout theme = (TextInputLayout) findViewById(R.id.theme).findViewById(R.id.editor_layout);
        theme.setHint(getString(R.string.meeting_theme));
        mMeetingTheme = (EditText) findViewById(R.id.theme).findViewById(R.id.editor_content);

        TextInputLayout number = (TextInputLayout) findViewById(R.id.number).findViewById(R.id.editor_layout);
        number.setHint(getString(R.string.meeting_number));
        mParticipantNumber = (EditText) findViewById(R.id.number).findViewById(R.id.editor_content);
        mParticipantNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

//        mOrganizerSpinner =
//                (Spinner) findViewById(R.id.organizer).findViewById(R.id.spinner);

        mLocationSpinner =
                (Spinner) findViewById(R.id.place).findViewById(R.id.spinner);

        TextView meetingDate = (TextView) findViewById(R.id.start_date).findViewById(R.id.title);
        meetingDate.setText(R.string.meeting_date);
        mStartDate = (TextView) findViewById(R.id.start_date).findViewById(R.id.content);

        ImageButton dateButton = (ImageButton) findViewById(R.id.start_date).findViewById(R.id.button);
        dateButton.setImageResource(R.mipmap.icon_calendar);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDateType = DATE_TYPE_START;
                showDateTimePicker();
            }
        });

        TextView meetingFinishDate = (TextView) findViewById(R.id.finish_date).findViewById(R.id.title);
        meetingFinishDate.setText(R.string.meeting_finish_date);
        mFinishDate = (TextView) findViewById(R.id.finish_date).findViewById(R.id.content);

        ImageButton finishDateButton = (ImageButton) findViewById(R.id.finish_date).findViewById(R.id.button);
        finishDateButton.setImageResource(R.mipmap.icon_calendar);

        finishDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStartCalendar == null) {
                    Toast.makeText(ThemeMeetingCreateActivity.this, R.string.prompt_select_start_date, Toast.LENGTH_SHORT).show();
                } else {
                    mDateType = DATE_TYPE_FINISH;
                    showDateTimePicker();
                }

            }
        });
    }

    @Override
    public void submitDone(boolean success, long meetingId) {
        if (success) {
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.success),
                    Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.fail),
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
//        List<String> organizerData = new ArrayList<>();
//        organizerData.add("Justin");
//        organizerData.add("Lily");
//
//        OrganizerAdapter organizerAdapter = new OrganizerAdapter(this, organizerData);
//
//        mOrganizerSpinner.setAdapter(organizerAdapter);
//        organizerAdapter.notifyDataSetChanged();
//
//        mOrganizerSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(Spinner parent, View view, int position, long id) {
//                String oranizerName = (String) parent.getAdapter().getItem(position);
//                Toast.makeText(ThemeMeetingCreateActivity.this, oranizerName, Toast.LENGTH_SHORT).show();
//            }
//        });


        List<String> locationData = new ArrayList<>();
        locationData.add("Chengdu");
        locationData.add("Beijing");

        LocationAdapter locationAdapter = new LocationAdapter(this, locationData);

        mLocationSpinner.setAdapter(locationAdapter);
        locationAdapter.notifyDataSetChanged();

        mLocationSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {
                String locationName = (String) parent.getAdapter().getItem(position);
                Toast.makeText(ThemeMeetingCreateActivity.this, locationName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDateTimePicker() {
        SublimePickerFragment pickerFrag = new SublimePickerFragment();
        pickerFrag.setCallback(mFragmentCallback);

        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;

        displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;
        displayOptions |= SublimeOptions.ACTIVATE_TIME_PICKER;

        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
        options.setDisplayOptions(displayOptions);

        switch (mDateType) {
            case DATE_TYPE_START:
                setInitCalendar(options, mStartCalendar);
                break;
            case DATE_TYPE_FINISH:
                if (mFinishCalendar == null) {
                    setInitCalendar(options, mStartCalendar);
                } else {
                    setInitCalendar(options, mFinishCalendar);
                }

                break;
            default:
                break;
        }


        Bundle bundle = new Bundle();
        bundle.putParcelable(SublimePickerFragment.KEY_SUBLIME_PICKER, options);
        pickerFrag.setArguments(bundle);

        pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");
    }

    private void setInitCalendar(SublimeOptions options, Calendar calendar) {
        if (calendar != null) {
            options.setDateParams(calendar);
            options.setTimeParams(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        }
    }

    private void handleStartDateSelecte(SelectedDate selectedDate,
                                        int hourOfDay, int minute) {

        final Calendar date = selectedDate.getFirstDate();
        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
        date.set(Calendar.MINUTE, minute);

        mStartCalendar = selectedDate.getFirstDate();
        mStartCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mStartCalendar.set(Calendar.MINUTE, minute);

        SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
        String datetime = fmt.format(mStartCalendar.getTime());
        mStartDate.setText(datetime);
    }

    private void handleFinishDateSelecte(SelectedDate selectedDate,
                                         int hourOfDay, int minute) {
        final Calendar date = selectedDate.getFirstDate();
        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
        date.set(Calendar.MINUTE, minute);

        if (date.after(mStartCalendar)) {
            mFinishCalendar = date;
            SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
            String datetime = fmt.format(date.getTime());
            mFinishDate.setText(datetime);
        } else {
            Toast.makeText(this, R.string.prompt_finish_date_wrong, Toast.LENGTH_LONG).show();
        }


    }

	private class OrganizerAdapter extends BaseAdapter {

        private List<String> mmData;
        private Context mmContext;

        public OrganizerAdapter(Context context, List<String> organizers) {

            mmContext = context;

            mmData = organizers;
        }

        @Override
        public int getCount() {
            return mmData.size();
        }

        @Override
        public Object getItem(int i) {
            return mmData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = ViewUtils.makeView(mmContext, R.layout.listitem_organizer,
                    parent, false);

            TextView name = (TextView) convertView.findViewById(R.id.organizer_name);
            name.setText(mmData.get(position));


            return convertView;
        }
    }

    private class LocationAdapter extends BaseAdapter {

        private List<String> mmData;
        private Context mmContext;

        public LocationAdapter(Context context, List<String> organizers) {

            mmContext = context;

            mmData = organizers;
        }

        @Override
        public int getCount() {
            return mmData.size();
        }

        @Override
        public Object getItem(int i) {
            return mmData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = ViewUtils.makeView(mmContext, R.layout.listitem_organizer,
                    parent, false);

            TextView name = (TextView) convertView.findViewById(R.id.organizer_name);
            name.setText(mmData.get(position));

            return convertView;
        }
    }
}
