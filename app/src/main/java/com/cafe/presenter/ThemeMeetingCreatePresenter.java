package com.cafe.presenter;

import com.cafe.common.mvp.MVPPresenter;
import com.cafe.contract.ThemeMeetingCreateContract;

/**
 * Created by Rocky on 2016/11/10.
 */

public class ThemeMeetingCreatePresenter extends MVPPresenter<ThemeMeetingCreateContract.View, ThemeMeetingCreateContract.Model>
        implements ThemeMeetingCreateContract.Presenter<ThemeMeetingCreateContract.View, ThemeMeetingCreateContract.Model> {
    @Override
    public ThemeMeetingCreateContract.Model initModel() {
        return null;
    }

    @Override
    public void submit() {
        getView().showLoadingProgress();

        // TODO: 2016/11/11 add submit logic

        getView().dismissLoadingProgress();

        getView().submitDone(true);
    }
}
