package com.cafe.contract;

import com.cafe.common.mvp.base.BaseModel;
import com.cafe.common.mvp.base.BaseView;
import com.cafe.common.mvp.base.IMVPPresent;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.data.account.RegisterRequest;
import com.cafe.data.base.ResultResponse;

/**
 * Created by Rocky on 2016/11/11.
 */

public interface RegisterContract {

    /**
     * View的接口方法，由Activity去实现
     */
    interface View extends BaseView {
        void registerDone(boolean success);
        void initCameraSurface();
        void loadCameraFail();
    }

    /**
     * Presenter接口方法，一般是Activity中的业务逻辑方法
     */
    interface Presenter<V extends RegisterContract.View, M extends RegisterContract.Model> extends IMVPPresent<V, M> {
        void register(RegisterRequest request, String headFilePath, JsonHttpResponseHandler<ResultResponse> handler);
        void loadCamera();
    }

    /**
     * Model接口方法，一般是数据操作方法,不一定存在Model
     */
    interface Model extends BaseModel {
        void register(RegisterRequest request, String headFilePath, JsonHttpResponseHandler<ResultResponse> handler);
    }
}
