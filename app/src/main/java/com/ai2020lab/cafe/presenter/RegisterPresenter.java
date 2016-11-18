package com.ai2020lab.cafe.presenter;

import android.content.Context;

import com.ai2020lab.aimedia.CameraManager;
import com.ai2020lab.cafe.common.mvp.MVPPresenter;
import com.ai2020lab.cafe.contract.RegisterContract;

/**
 * Created by Rocky on 2016/11/11.
 */

public class RegisterPresenter extends MVPPresenter<RegisterContract.View, RegisterContract.Model>
        implements RegisterContract.Presenter<RegisterContract.View, RegisterContract.Model> {

    private Context mContext;

    public RegisterPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void register() {

    }

    @Override
    public void loadCamera() {
        //        if (CameraUtils.checkCameraHardware(this)) {
//            // Create an instance of Camera
//            mCamera = CameraUtils.getFrontCameraInstance();
//
//            if (mCamera != null) {
//                mCamera.setDisplayOrientation(90);
//                Camera.Parameters ps = mCamera.getParameters();
//
//                ps.setPictureFormat(ImageFormat.JPEG);
//
//                List<String> focusModes = ps.getSupportedFocusModes();
//                if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
//                    ps.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//                }
//
//                mCamera.setParameters(ps);
//
//                // Create our Preview view and set it as the content of our activity.
//                mPreview = new HeadOnlyCameraPreview(this, mCamera);
//                FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
//                preview.addView(mPreview);
//            }
//        }


        int result = CameraManager.getInstance().openDriver(mContext, true);

        if (result != -1) {
            getView().initCameraSurface();
        } else {
            getView().loadCameraFail();
        }
    }

    @Override
    public RegisterContract.Model initModel() {
        return null;
    }
}
