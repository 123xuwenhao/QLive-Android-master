package cn.nodemedia.qlive.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.qiniu.android.http.ResponseInfo;

import java.io.File;
import java.io.IOException;

import cn.nodemedia.qlive.view.PushActivity;
import cn.nodemedia.qlive.widget.PicChooseDialog;

public class PicChooseHelper {

    private static final int FROM_ALBUM = 1;
    private static final int FROM_CROP = 0;
    private static final int FROM_CAMERA = 2;
    private static final int CAMERA_PERMISSION_REQ_CODE = 3;
    private static final String TAG = "null";
    private static final int WRITE_PERMISSION_REQ_CODE = 4;
    private static final String ALBUM = "album";
    private static final String CAMERA = "camera";
    private Activity mActivity;
    private Uri mCameraFileUri;
    private Uri cropUri;
    private PicType mPicType;
    public PicChooseDialog dialog;

    public static enum PicType {
        Avatar, Cover
    }

    public PicChooseHelper(Activity activity, PicType picType) {
        mActivity = activity;
        mPicType = picType;
    }

    public void showDialog() {
        dialog = new PicChooseDialog(mActivity);
        dialog.setOnDialogClickListener(new PicChooseDialog.OnDialogClickListener() {
            @Override
            public void onCamera() {
                //拍照
                takePicFromCamera();
            }

            @Override
            public void onAlbum() {
                //相册
                choosePicFromAlbum();
            }
        });
        dialog.show();
    }


    private void takePicFromCamera() {
        mCameraFileUri = getCropUri();
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (mActivity == null) {
            mActivity.startActivityForResult(intentCamera, FROM_CAMERA);
        }
    }



    private void choosePicFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setType("image/*");
        mActivity.startActivityForResult(intent, FROM_ALBUM);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FROM_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                startCrop(uri, ALBUM);

            }
        } else if (requestCode == FROM_CROP) {
            if (resultCode == Activity.RESULT_OK) {

                uploadTo7niu(cropUri.getPath());

            }
        } else if (requestCode == FROM_CAMERA) {
            //从相机选择返回
            if (resultCode == Activity.RESULT_OK) {
                startCrop(mCameraFileUri, CAMERA);
            }
        }
    }


    private void uploadTo7niu(String path) {

        File file = new File(path);
        QnUploadHelper.uploadPic(path, file.getName(), new QnUploadHelper.UploadCallBack() {
            @Override
            public void success(String url) {
                //上传成功
                if (onChooseListener != null) {
                    onChooseListener.onSuccess(url);
                }

            }

            @Override
            public void fail(String key, ResponseInfo info) {
                //上传失败
                if (onChooseListener != null) {
                    onChooseListener.onFail();
                }
            }
        });
    }

    public interface onChooseListener {
        void onSuccess(String url);

        void onFail();

    }

    private onChooseListener onChooseListener;

    public void setOnChooseListener(onChooseListener l) {
        onChooseListener = l;
    }

    private void startCrop(Uri uri, String type) {

        cropUri = getCropUri();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.putExtra("crop", true);
        intent.putExtra("return_data", false);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        if (mPicType == PicType.Avatar) {
            intent.putExtra("aspectX", 300);
            intent.putExtra("aspectY", 300);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
        } else if (mPicType == PicType.Cover) {
            intent.putExtra("aspectX", 500);
            intent.putExtra("aspectY", 300);
            intent.putExtra("outputX", 500);
            intent.putExtra("outputY", 300);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        mActivity.startActivityForResult(intent, FROM_CROP);

    }


    private Uri getCropUri() {
        String fileName = System.currentTimeMillis() + "_avatar_crop.jpg";
        String dirPath = mActivity.getExternalFilesDir(null) + "/"+mActivity.getApplication().getPackageName()+"/";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File picFile = new File(dirPath, fileName);
        if (picFile.exists()) {
            picFile.delete();
        }
        try {
            picFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(picFile);
    }


}
