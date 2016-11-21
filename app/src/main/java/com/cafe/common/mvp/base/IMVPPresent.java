package com.cafe.common.mvp.base;

/**
 * Created by Rocky on 2016/11/11.
 */

public interface IMVPPresent<V extends BaseView, M extends BaseModel> {

    M initModel();

    /**
     * 初始化Presenter
     */
    void attachView(V view);

    /**
     * 销毁Presenter持有的View引用
     */
    void detachView();

    V getView();

    M getModel();
}
