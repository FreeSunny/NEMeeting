/*
 * Copyright (c) 2014-2020 NetEase, Inc.
 * All right reserved.
 */

package com.netease.meetinglib.demo.view;



import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.manu.mdatepicker.MDatePickerDialog;
import com.netease.meetinglib.demo.R;
import com.netease.meetinglib.demo.ToastCallback;
import com.netease.meetinglib.demo.adapter.ScheduleMeetingAdapter;
import com.netease.meetinglib.demo.base.BaseFragment;
import com.netease.meetinglib.demo.data.ScheduleMeetingItem;
import com.netease.meetinglib.demo.databinding.FragmentScheduleBinding;
import com.netease.meetinglib.demo.utils.CalendarUtil;
import com.netease.meetinglib.demo.viewmodel.ScheduleViewModel;
import com.netease.meetinglib.sdk.NEMeetingError;
import com.netease.meetinglib.sdk.NEMeetingItem;
import com.netease.meetinglib.sdk.NEMeetingItemSetting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ScheduleMeetingFragment extends BaseFragment<FragmentScheduleBinding> {
    private static final String TAG = ScheduleMeetingFragment.class.getSimpleName();
    private List<ScheduleMeetingItem> dataList = new ArrayList<>();
    private ScheduleMeetingAdapter mAdapter;
    private ScheduleViewModel mViewModel;
    private long startTime, endTime;
    private boolean isAttendeeAudioOff, isUsePwd;

    public static ScheduleMeetingFragment newInstance() {
        return new ScheduleMeetingFragment();
    }

    @Override
    protected FragmentScheduleBinding getViewBinding() {
        return FragmentScheduleBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);
        mAdapter = new ScheduleMeetingAdapter(getActivity(), new ArrayList<>(), mViewModel.passWord, mViewModel.tittle);
        binding.rvScheduleMeeting.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter.setHasStableIds(true);
        binding.rvScheduleMeeting.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((view, position) -> {
            ScheduleMeetingItem item = dataList.get(position);
            switch (item.getClickAction()) {
                case ScheduleMeetingItem.SET_START_TIME_ACTION:
                    CalendarUtil.showDatePickerDialog(getActivity(), new MDatePickerDialog.OnDateResultListener() {
                        @Override
                        public void onDateResult(long date) {
                            startTime = date;
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(date);
                            SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
                            dateFormat.applyPattern("yyyy-MM-dd HH:mm");
                            mAdapter.getData().get(ScheduleMeetingItem.SET_START_TIME_ACTION).setTimeTip(dateFormat.format(new Date(date)));
                            mAdapter.updateData(ScheduleMeetingItem.SET_START_TIME_ACTION, mAdapter.getData().get(ScheduleMeetingItem.SET_START_TIME_ACTION));
                        }
                    });
                    break;
                case ScheduleMeetingItem.SET_END_TIME_ACTION:
                    CalendarUtil.showDatePickerDialog(getActivity(), new MDatePickerDialog.OnDateResultListener() {
                        @Override
                        public void onDateResult(long date) {
                            //TODO 必须大于当前开始时间
                            endTime = date;
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(date);
                            SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
                            dateFormat.applyPattern("yyyy-MM-dd HH:mm");
                            mAdapter.getData().get(ScheduleMeetingItem.SET_END_TIME_ACTION).setTimeTip(dateFormat.format(new Date(date)));
                            mAdapter.updateData(ScheduleMeetingItem.SET_END_TIME_ACTION, mAdapter.getData().get(ScheduleMeetingItem.SET_END_TIME_ACTION));
                        }
                    });
                    break;
            }

        });


        mAdapter.setOnCheckedChangeListener((compoundButton, enable, clickAction) -> {
            if (compoundButton != null && compoundButton.getId() == R.id.sb_meeting_switch) {
                switch (clickAction) {
                    case ScheduleMeetingItem.ENABLE_MEETING_PWD_ACTION:
                        isUsePwd = enable;
                        break;
                    case ScheduleMeetingItem.ENABLE_MEETING_MUTE_ACTION:
                        isAttendeeAudioOff = enable;
                        break;
                }
            }
        });

        mViewModel.observeItemMutableLiveData(getActivity(), new Observer<NEMeetingItem>() {
            @Override
            public void onChanged(NEMeetingItem neMeetingItem) {
                if (neMeetingItem != null) {
                    neMeetingItem.setSubject(mViewModel.tittle.getValue())
                            .setStartTime(startTime)
                            .setEndTime(endTime);
                    if (isUsePwd) {
                        neMeetingItem.setPassword(mViewModel.passWord.getValue());
                    }
                    NEMeetingItemSetting setting = new NEMeetingItemSetting();
                    setting.isAttendeeAudioOff = isAttendeeAudioOff;
                    neMeetingItem.setSetting(setting);
                    mViewModel.scheduleMeeting(neMeetingItem, new ToastCallback<NEMeetingItem>(getActivity(), "scheduleMeeting") {
                        @Override
                        public void onResult(int resultCode, String resultMsg, NEMeetingItem resultData) {
                            super.onResult(resultCode, resultMsg, resultData);
                            if (resultCode == NEMeetingError.ERROR_CODE_SUCCESS) {
                                Navigation.findNavController(getView()).popBackStack();
                            }
                        }
                    });
                }
            }
        });

        binding.btnStartScheduleMeeting.setOnClickListener(view -> {
//            String pwd = mViewModel.passWord.getValue();
//            if (TextUtils.isEmpty(mViewModel.tittle.getValue())) {
//                Toast.makeText(getActivity(), "请输入正确的会议主题", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (startTime == 0 || endTime == 0 || startTime >= endTime || endTime - startTime > 1000 * 60 * 60 * 24) {
//                Toast.makeText(getActivity(), "请输入合法时间", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (isUsePwd && (pwd == null || pwd.length() != 6)) {
//                Toast.makeText(getActivity(), "请输入正确的6位密码", Toast.LENGTH_SHORT).show();
//                return;
//            }

            mViewModel.createScheduleMeetingItem();
        });
    }

    @Override
    protected void initData() {
        if (dataList != null) {
            dataList.clear();
        }
        dataList.add(new ScheduleMeetingItem("会议主题", ScheduleMeetingItem.EDIT_TEXT_TITTLE_ACTION));
        dataList.add(new ScheduleMeetingItem("开始时间", "请选择开始入会时间", ScheduleMeetingItem.SET_START_TIME_ACTION));
        dataList.add(new ScheduleMeetingItem("结束时间", "请选择会议结束时间", ScheduleMeetingItem.SET_END_TIME_ACTION));
        dataList.add(new ScheduleMeetingItem("会议密码", false, ScheduleMeetingItem.ENABLE_MEETING_PWD_ACTION));
        dataList.add(new ScheduleMeetingItem("自动静音", "参会者加入会议时自动静音", false, ScheduleMeetingItem.ENABLE_MEETING_MUTE_ACTION));

        mAdapter.resetData(dataList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        CalendarUtil.closeOptionsMenu();
    }
}
