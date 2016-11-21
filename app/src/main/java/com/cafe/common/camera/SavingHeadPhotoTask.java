package com.cafe.common.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import org.justin.media.interfaces.PhotoSavedListener;
import org.justin.utils.common.LogUtils;
import org.justin.utils.image.ImageUtils;
import org.justin.utils.storage.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Rocky on 2016/11/16.
 */

public class SavingHeadPhotoTask  extends AsyncTask<Void, Void, File[]> {

    private final static String TAG = SavingHeadPhotoTask.class.getSimpleName();
    private final static int COMPRESS_QUALITY = 100;

    private Context context;
    private byte[] data;
    private String name;
    private String path;
    private int orientation;
    private boolean isUpdateMedia;
    private ClipRect clipRect;
    private PhotoSavedListener callback;

    /**
     * @param context       Context
     * @param data          byte[]
     * @param name          String
     * @param path          String
     * @param orientation   int
     * @param isUpdateMedia boolean
     * @param clipRect      ClipRect
     * @param callback      PhotoSavedListener
     */
    public SavingHeadPhotoTask(Context context, byte[] data,
                           String name, String path,
                           int orientation, boolean isUpdateMedia,
                           ClipRect clipRect, PhotoSavedListener callback) {
        this.context = context;
        this.data = data;
        this.name = name;
        this.path = path;
        this.orientation = orientation;
        this.isUpdateMedia = isUpdateMedia;
        this.clipRect = clipRect;
        this.callback = callback;
    }

    /**
     * @param context       Context
     * @param data          byte[]
     * @param name          String
     * @param path          String
     * @param orientation   int
     * @param isUpdateMedia boolean
     * @param clipRect      ClipRect
     */
    public SavingHeadPhotoTask(Context context, byte[] data,
                           String name, String path,
                           int orientation, boolean isUpdateMedia,
                           ClipRect clipRect) {
        this(context, data, name, path, orientation, isUpdateMedia, clipRect, null);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (callback != null) {
            callback.savedBefore();
        }
    }

    /**
     * 保存照片后台线程处理
     */
    @Override
    protected File[] doInBackground(Void... params) {
        File[] fileArr = saveByteArrayWithOrientation(data, orientation);
        if (FileUtils.isExist(fileArr[0].getPath())) {
            if (isUpdateMedia)
                // 同步更新到系统图库
                updateToSystemMedia(context, fileArr[0]);
        }
        return fileArr;
    }

    @Override
    protected void onPostExecute(File[] fileArr) {
        super.onPostExecute(fileArr);
        photoSaved(fileArr);
    }

    private void photoSaved(File[] fileArr) {
        if (callback != null) {
            if (fileArr != null && fileArr[0] != null && FileUtils.isExist(fileArr[0].getPath()))
                callback.savedSuccess(fileArr[0], fileArr[1]);
            else
                callback.savedFailure();
        }
    }

    /**
     * 指定旋转角度保存照片
     */
    private File[] saveByteArrayWithOrientation(byte[] data, int orientation) {
        File photo = getPhotoFile();
        File cropPhoto = getCropperPhotoFile();
        File[] fileArr = new File[2];
        fileArr[0] = photo;
        fileArr[1] = cropPhoto;
        LogUtils.i(TAG, "旋转角度-->" + orientation);
        if (photo == null) {
            return fileArr;
        }

        try {
            Bitmap bitmap = null;
            if (data != null) {
                // 从byte数组中读出bitmap
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            }

            if (bitmap != null) {

                // 旋转图片
                if (orientation != 0 && bitmap.getWidth() > bitmap.getHeight()) {
                    bitmap = ImageUtils.getRotateBitmap(bitmap, orientation, true);
                }
                // 保存图片
                ImageUtils.saveBitmapAsJpeg(bitmap, photo.getPath(), COMPRESS_QUALITY);
                // 截取图片
                Bitmap cropBitmap = cropPhoto(bitmap);
                if (cropBitmap != null && cropPhoto != null) {
                    // 保存截取的图片
                    ImageUtils.saveBitmapAsJpeg(cropBitmap, cropPhoto.getPath(), COMPRESS_QUALITY);
                    cropBitmap.recycle();
                }
                bitmap.recycle();
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "--保存旋转照片异常", e);
        }
        return fileArr;
    }

    /**
     * 获取区域截图
     *
     * @param bitmap Bitmap
     * @return Bitmap
     */
    private Bitmap cropPhoto(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        if (clipRect == null) {
            return null;
        }

        int left = (int) (bitmap.getWidth() * clipRect.left);
        int top = (int) (bitmap.getWidth() * clipRect.top);
        int right = (int) (bitmap.getWidth() * clipRect.right);
        int bottom = (int) (bitmap.getWidth() * clipRect.bottom);

        try {
            return Bitmap.createBitmap(bitmap, left, top,
                    right - left, bottom - top);

        } catch (Exception e) {
            LogUtils.e(TAG, "--截图操作异常--", e);
        }
        return null;
    }

    /**
     * 根据文件路径和文件名或者照片文件对象
     */
    private File getPhotoFile() {
        FileUtils.makeDir(path);
        return new File(path + File.separator + name);
    }

    /**
     * 获取照片截取文件对象
     */
    private File getCropperPhotoFile() {
        String tmpName = name.substring(0, name.indexOf("."));
        LogUtils.i(TAG, "没有扩展名的照片文件名-->" + tmpName);
        return new File(path + File.separator + tmpName + "_crop." + FileUtils.ExtensionName.JPG);
    }

    /**
     * 把照片同步更新到系统图库
     * 需要在系统图库同步看到拍照结果可以调用
     */
    private void updateToSystemMedia(Context context, File file) {
        LogUtils.i(TAG, "--同步更新到系统图库");
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
        } catch (FileNotFoundException e) {
            LogUtils.e(TAG, "--更新到系统图库异常", e);
        }
        // 通知系统图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + file.getPath())));
    }

    public static class ClipRect {
        private float left;
        private float top;
        private float right;
        private float bottom;

        public float getLeft() {
            return left;
        }

        /**
         * 设置左边缘在宽度中的百分比（0-1）
         * @param left
         */
        public void setLeft(float left) {
            this.left = left;

            if (this.left > 1) {
                this.left = 1;
            } else if (this.left < 0) {
                this.left = 0;
            }
        }

        public float getTop() {
            return top;
        }

        /**
         * 设置上缘在宽度中的百分比（0-1）
         * @param top
         */
        public void setTop(float top) {
            this.top = top;

            if (this.top > 1) {
                this.top = 1;
            } else if (this.top < 0) {
                this.top = 0;
            }
        }

        public float getRight() {
            return right;
        }

        /**
         * 设置右缘在宽度中的百分比（0-1）
         * @param right
         */
        public void setRight(float right) {
            this.right = right;

            if (this.right > 1) {
                this.right = 1;
            } else if (this.right < 0) {
                this.right = 0;
            }
        }

        public float getBottom() {
            return bottom;
        }

        /**
         * 设置下缘在宽度中的百分比（0-1）
         * @param bottom
         */
        public void setBottom(float bottom) {
            this.bottom = bottom;

            if (this.bottom > 1) {
                this.bottom = 1;
            } else if (this.bottom < 0) {
                this.bottom = 0;
            }
        }
    }
}
