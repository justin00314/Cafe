package com.cafe.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cafe.R;
import com.cafe.common.TextInputValidater;
import com.cafe.common.mvp.MVPActivity;
import com.cafe.contract.MeetingSearchContract;
import com.cafe.data.meeting.JoinMeetingRequest;
import com.cafe.data.meeting.MeetingInfo;
import com.cafe.data.meeting.MeetingState;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.presenter.MeetingSearchPresenter;

import org.justin.utils.common.ViewUtils;

import java.util.List;

public class SearchMeetingActivity extends MVPActivity<MeetingSearchContract.View,
        MeetingSearchContract.Presenter> implements MeetingSearchContract.View {

    private RecyclerView mMeetintsRv;
    private EditText mMeetintId;
    private TextInputValidater mTextValidater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_meeting);

        supportToolbar(true);

        setupUI();

        setToolbarLeft(R.mipmap.back, new OnLeftClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        setToolbarTitle(getString(R.string.search_meeting));
    }

    @Override
    public MeetingSearchContract.Presenter initPresenter() {
        return new MeetingSearchPresenter(this);
    }



    @Override
    public void showSearchResults(boolean result, List<MeetingInfo> meetings) {
        if (result) {
            if (meetings != null && meetings.size() > 0) {
                for (MeetingInfo meeting: meetings) {
                    meeting.id = Integer.parseInt(mMeetintId.getText().toString()) ;
                }
                mMeetintsRv.setAdapter(new SearchMeetingListAdapter(meetings));
            } else {
                Toast.makeText(this, R.string.query_no_meeting, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.query_fail, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void joinMeetingResult(boolean result) {
        int strId = result ? R.string.prompt_join_success : R.string.prompt_join_failure;

        Toast.makeText(this, strId, Toast.LENGTH_SHORT).show();
    }

    public void search(View v) {
        if (validate()) {
            String idStr = mMeetintId.getText().toString();
            getPresenter().search(Integer.parseInt(idStr));
        }

    }

    private void setupUI() {
        mMeetintsRv = (RecyclerView) findViewById(R.id.meeting_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMeetintsRv.setLayoutManager(layoutManager);

        mMeetintId = (EditText) findViewById(R.id.editor_id);

        mMeetintId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);

                    search(null);
                    return true;
                }

                return false;
            }
        });

        mTextValidater = new TextInputValidater();
        mTextValidater.putValidateItem(mMeetintId, getString(R.string.prompt_need_meeting_id));
    }

    private boolean validate() {
        if (mTextValidater.validate()) {
            String idStr = mMeetintId.getText().toString();

            try {
                Integer.parseInt(idStr);
                return true;
            } catch (NumberFormatException e) {
                Toast.makeText(this, R.string.prompt_meeting_id_wrong, Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return false;
    }

    private boolean meetingCanJoin(MeetingInfo meeting) {
        return meeting.state == MeetingState.PROGRESS;
    }

	private class SearchMeetingListAdapter extends RecyclerView.Adapter<SearchMeetingListAdapter.ItemViewHolder> {

        private final List<MeetingInfo> mmMeetings;

        public SearchMeetingListAdapter(List<MeetingInfo> meetings) {
            mmMeetings = meetings;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = ViewUtils.makeView(parent.getContext(), R.layout.listitem_search_meeting_list, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return mmMeetings.size();
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            MeetingInfo meeting = mmMeetings.get(position);

            holder.themeName.setText(meeting.name);
            holder.id.setText("" + meeting.id);
            holder.date.setText(meeting.startTime);
            holder.place.setText(meeting.meetingRoomName);
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            TextView themeName;
            TextView id;
            TextView date;
            TextView place;

            ItemViewHolder(View view) {
                super(view);

                final Context context = view.getContext();

                themeName = (TextView) view.findViewById(R.id.theme).findViewById(R.id.content);
                id = (TextView) view.findViewById(R.id.id).findViewById(R.id.content);
                date = (TextView) view.findViewById(R.id.date).findViewById(R.id.content);
                place = (TextView) view.findViewById(R.id.place).findViewById(R.id.content);

                TextView themeTitle = (TextView) view.findViewById(R.id.theme).findViewById(R.id.label);
                themeTitle.setText(context.getString(R.string.dialog_qrcode_theme));
                TextView idTitle = (TextView) view.findViewById(R.id.id).findViewById(R.id.label);
                idTitle.setText(context.getString(R.string.dialog_qrcode_id));
                TextView dateTitle = (TextView) view.findViewById(R.id.date).findViewById(R.id.label);
                dateTitle.setText(context.getString(R.string.dialog_qrcode_time));
                TextView placeTitle = (TextView) view.findViewById(R.id.place).findViewById(R.id.label);
                placeTitle.setText(context.getString(R.string.dialog_qrcode_location));

                View joinBtn = view.findViewById(R.id.join);
                joinBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();

                        MeetingInfo meeting = mmMeetings.get(position);

                        if (meetingCanJoin(meeting)) {
                            JoinMeetingRequest request = new JoinMeetingRequest();
                            request.id = meeting.id;

                            getPresenter().joinMeeting(request);
                        } else {
                            Toast.makeText(SearchMeetingActivity.this, R.string.meeting_not_start, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        }
    }
}
