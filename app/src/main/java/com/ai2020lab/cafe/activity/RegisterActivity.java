package com.ai2020lab.cafe.activity;

import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ai2020lab.cafe.R;
import com.ai2020lab.cafe.common.CameraUtils;
import com.ai2020lab.cafe.common.camera.HeadOnlyCameraPreview;

import java.util.List;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText mName;
    private EditText mId;
    private EditText mPassword;
    private EditText mRepassword;

    private Camera mCamera;
    private HeadOnlyCameraPreview mPreview;

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

//            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
//            if (pictureFile == null){
//                Log.d(TAG, "Error creating media file, check storage permissions")
//                return;
//            }
//
//            try {
//                FileOutputStream fos = new FileOutputStream(pictureFile);
//                fos.write(data);
//                fos.close();
//            } catch (FileNotFoundException e) {
//                Log.d(TAG, "File not found: " + e.getMessage());
//            } catch (IOException e) {
//                Log.d(TAG, "Error accessing file: " + e.getMessage());
//            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setupUI();

    }

    private void setupUI() {
        ImageView userImage = (ImageView) findViewById(R.id.user_name).findViewById(R.id.editor_icon);
        userImage.setImageResource(R.mipmap.icon_people);
        ImageView idImage = (ImageView) findViewById(R.id.user_id).findViewById(R.id.editor_icon);
        idImage.setImageResource(R.mipmap.icon_id);
        ImageView passwordImage = (ImageView) findViewById(R.id.user_password).findViewById(R.id.editor_icon);
        passwordImage.setImageResource(R.mipmap.icon_key);
        ImageView repasswordImage = (ImageView) findViewById(R.id.user_repassword).findViewById(R.id.editor_icon);
        repasswordImage.setImageResource(R.mipmap.icon_key);

        mName = (EditText) findViewById(R.id.user_name).findViewById(R.id.editor_content);
        mId = (EditText) findViewById(R.id.user_id).findViewById(R.id.editor_content);
        mPassword = (EditText) findViewById(R.id.user_password).findViewById(R.id.editor_content);
        mPassword.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
        mRepassword = (EditText) findViewById(R.id.user_repassword).findViewById(R.id.editor_content);
        mRepassword.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);

        TextView userName = (TextView) findViewById(R.id.user_name).findViewById(R.id.editor_title);
        userName.setText(R.string.user_name);
        TextView passwordText = (TextView) findViewById(R.id.user_password).findViewById(R.id.editor_title);
        passwordText.setText(R.string.password);
        TextView userId = (TextView) findViewById(R.id.user_id).findViewById(R.id.editor_title);
        userId.setText(R.string.user_id);
        TextView repasswordText = (TextView) findViewById(R.id.user_repassword).findViewById(R.id.editor_title);
        repasswordText.setText(R.string.confirm_password);
    }

    private void loadCamera() {
        if (CameraUtils.checkCameraHardware(this)) {
            // Create an instance of Camera
            mCamera = CameraUtils.getFrontCameraInstance();

            if (mCamera != null) {
                mCamera.setDisplayOrientation(90);
                Camera.Parameters ps = mCamera.getParameters();
//                List<Camera.Size> sizes = ps.getSupportedPreviewSizes();

//                for (Camera.Size size : sizes) {
//                    Log.d(TAG, "width:"+ size.width + ", height:" + size.height);
//
//                }

                ps.setPictureFormat(ImageFormat.JPEG);

                List<String> focusModes = ps.getSupportedFocusModes();
                if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                    ps.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }

                mCamera.setParameters(ps);

                // Create our Preview view and set it as the content of our activity.
                mPreview = new HeadOnlyCameraPreview(this, mCamera);
                FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
                preview.addView(mPreview);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;

            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.removeView(mPreview);
        }
    }
}
