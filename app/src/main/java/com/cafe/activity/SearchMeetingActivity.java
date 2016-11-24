package com.cafe.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cafe.R;
import com.cafe.common.mvp.MVPActivity;
import com.cafe.contract.MeetingSearchContract;
import com.cafe.data.meeting.MeetingInfo;
import com.cafe.presenter.MeetingSearchMockPresenter;

import org.justin.utils.common.ViewUtils;

import java.util.List;

public class SearchMeetingActivity extends MVPActivity<MeetingSearchContract.View,
        MeetingSearchContract.Presenter> implements MeetingSearchContract.View {

    private RecyclerView mMeetintsRv;
    private EditText mMeetintId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_meeting);

        setupUI();
    }

    @Override
    public MeetingSearchContract.Presenter initPresenter() {
        return new MeetingSearchMockPresenter();
    }



    @Override
    public void showSearchResults(List<MeetingInfo> meetings) {
        mMeetintsRv.setAdapter(new SearchMeetingListAdapter(meetings));
    }

    public void search(View v) {
        getPresenter().search("");
    }

    private void setupUI() {
        mMeetintsRv = (RecyclerView) findViewById(R.id.meeting_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMeetintsRv.setLayoutManager(layoutManager);

    }

	public static  class SearchMeetingListAdapter extends RecyclerView.Adapter<SearchMeetingListAdapter.ItemViewHolder> {

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
            holder.id.setText(meeting.id);
            holder.date.setText(meeting.startTime);
            holder.place.setText(meeting.meetingRoomId);
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

                        Toast.makeText(view.getContext(), meeting.id, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
