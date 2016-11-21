package com.cafe.common.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Rocky on 2016/11/9.
 */
public class HeadOnlyCameraPreview extends SurfaceView implements SurfaceHolder.Callback  {
    private static final String TAG = "CameraPreview";

    private SurfaceHolder mHolder;
    private Camera mCamera;
    public float headRadius;
    public float headX, headY;
    private float mLeft, mTop, mRight, mBottom;



    public void setCallback(SurfaceHolder.Callback callback) {
        this.mCallback = callback;
    }

    private SurfaceHolder.Callback mCallback;

    public HeadOnlyCameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
   //     mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public HeadOnlyCameraPreview(Context context) {
        this(context, null);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
     //   mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (mCallback != null) {
            mCallback.surfaceCreated(holder);
        }
        // The Surface has been created, now tell the camera where to draw the preview.
//        try {
//            mCamera.setPreviewDisplay(holder);
//            mCamera.startPreview();
//        } catch (IOException e) {
//            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
//        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCallback != null) {
            mCallback.surfaceDestroyed(holder);
        }
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        if (mCallback != null) {
            mCallback.surfaceChanged(holder, format, w, h);
        }
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

//        if (mHolder.getSurface() == null){
//            // preview surface does not exist
//            return;
//        }
//
//        // stop preview before making changes
//        try {
//            mCamera.stopPreview();
//        } catch (Exception e){
//            // ignore: tried to stop a non-existent preview
//        }
//
//        // set preview size and make any resize, rotate or
//        // reformatting changes here
//
//        // start preview with new settings
//        try {
//            mCamera.setPreviewDisplay(mHolder);
//            mCamera.startPreview();
//
//        } catch (Exception e){
//            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
//        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Path path = new Path();
        int count = canvas.save();

        headX = getWidth() / 2;
        headY = getHeight() / 3.5f;
        headRadius = (float) (getWidth() / 3.5);
        headRadius = Math.min(headRadius, headX);
        headRadius = Math.min(headRadius, headY);


//        headX = (getWidth() * (mRight + mLeft)) / 2;
//        headY = (getHeight() * (mTop + mBottom)) / 2;
//        headRadius =  Math.min(getWidth() * (mRight - mLeft), getHeight() * (mBottom - mTop));


        path.addCircle(headX, headY, headRadius, Path.Direction.CW);

        canvas.clipPath(path);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(count);
    }

    /**
     * 设置头像框的相对坐标（百分比）
     * @param left 左边缘在宽度中的百分比
     * @param top 上边缘在宽度中的百分比
     * @param right 右边缘在宽度中的百分比
     * @param bottom 下边缘在宽度中的百分比
     */
    public void setHeadRect(float left, float top, float right, float bottom) {
        mLeft = left;
        mTop = top;
        mRight = right;
        mBottom = bottom;
    }
}
