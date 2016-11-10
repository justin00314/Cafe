package com.ai2020lab.cafe.presenter;

import com.ai2020lab.cafe.common.mvp.MVPPresenter;
import com.ai2020lab.cafe.contract.ThemeMeetingCreateContract;

/**
 * Created by Rocky on 2016/11/10.
 */

public class ThemeMeetingCreatePresenter extends MVPPresenter<ThemeMeetingCreateContract.View, ThemeMeetingCreateContract.Model> implements ThemeMeetingCreateContract.Presenter {
    @Override
    public ThemeMeetingCreateContract.Model initModel() {
        return null;
    }
}
