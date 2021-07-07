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
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.qiniu.android.http.ResponseInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cn.nodemedia.qlive.contract.APIPost;
import cn.nodemedia.qlive.entity.Result;
import cn.nodemedia.qlive.utils.view.RetrofitManager;
import cn.nodemedia.qlive.view.PushActivity;
import cn.nodemedia.qlive.widget.PicChooseDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PicChooseHelper {

    private static final int FROM_ALBUM = 1;
    private static final int FROM_CROP = 0;
    private static final int FROM_CAMERA = 2;
    private static final int CAMERA_PERMISSION_REQ_CODE = 3;
    private static final String TAG = "null";
    private static final int WRITE_PERMISSION_REQ_CODE = 4;
    private static final String ALBUM = "album";
    private static final String CAMERA = "camera";
    private static final String urlPost = "http://47.99.171.180:8082";
    private static final String urlGet = "http://47.99.171.180:8082/streaming/";
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
            assert false;
            mActivity.startActivityForResult(intentCamera, FROM_CAMERA);
        }
    }


    private void choosePicFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setType("image/*");
        mActivity.startActivityForResult(intent, FROM_ALBUM);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) throws IOException {
        if (requestCode == FROM_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                startCrop(uri, ALBUM);

            }
        } else if (requestCode == FROM_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                Log.e("cropUri", cropUri.getEncodedPath());
                Log.e("cropUri", cropUri.getPath());


//                uploadToAliy(cropUri.getPath());
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


//    private void uploadToAliy(String path) throws IOException {
//
//        File file = new File(path);
//        InputStream fileIn = new FileInputStream(file);
//        byte[] image = new byte[(int) file.length()];
//        fileIn.read(image);
//        Retrofit retrofit = RetrofitManager.getInstance(urlPost);
//        APIPost api = retrofit.create(APIPost.class);
//        Call<Result> call = api.request(image, "images/" + file.getName());//ask在前面赋予了各个字段的数据，在接口api中转成了json格式的数据，发送请求
//        call.enqueue(new Callback<Result>() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onResponse(Call<Result> call, Response<Result> response) {
////                接受到的机器人回复的数据
//                String actionStatus = response.body().getStatus();
//                Log.e("status", actionStatus, null);
//                if (actionStatus.equals("true")) {
////                    上传成功
//                    if (onChooseListener != null) {
//                        onChooseListener.onSuccess(urlGet+response.body().getData());
//                    }
//                } else {
//
//                    if (onChooseListener != null) {
//                        onChooseListener.onFail();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Result> call, Throwable t) {
//
//                Log.e("status", t.getMessage(), null);
//            }
//        });
//
//
//    }

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
        String dirPath = mActivity.getExternalFilesDir(null) + "/" + mActivity.getApplication().getPackageName() + "/";
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
