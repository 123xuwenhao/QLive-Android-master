package cn.nodemedia.qlive.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;


import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;

import cn.nodemedia.qlive.R;
import cn.nodemedia.qlive.entity.RoomInfo;
import cn.nodemedia.qlive.utils.BaseRequest;

import cn.nodemedia.qlive.utils.DialogUtil;
import cn.nodemedia.qlive.utils.ImgUtils;
import cn.nodemedia.qlive.utils.PicChooseHelper;

import cn.nodemedia.qlive.utils.TCConstants;
import cn.nodemedia.qlive.utils.TCUtils;
import cn.nodemedia.qlive.view.MyRequest.CreateRoomRequest;


public class CreateRoomActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mCoverImg;
    private View mSetCoverView;
    private TextView mCoverTipTxt, mPushBtn;
    private EditText mTitleEt;
    SharedPreferences.Editor editorWrite;
    SharedPreferences spf;
    Intent intentIn, intentOut;
    int userId;
    String userName,userAvatar;
    ArrayList<Integer> goods;


    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        intentIn = getIntent();
        editorWrite = getSharedPreferences("data", Context.MODE_PRIVATE).edit();
        spf = getSharedPreferences("data", Context.MODE_PRIVATE);
        //取消严格模式  FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        findAllViews();
        initValues();

    }



    private void findAllViews() {
        mPushBtn = findViewById(R.id.push_button);
        mPushBtn.setOnClickListener(this);
        mSetCoverView = findViewById(R.id.set_cover);
        mSetCoverView.setOnClickListener(this);
        mCoverImg = findViewById(R.id.cover);
        mCoverTipTxt = findViewById(R.id.tv_pic_tip);
        mTitleEt = findViewById(R.id.title);
    }

    private void initValues() {
        userId=intentIn.getIntExtra("userId",0);
        userName=intentIn.getStringExtra("userName");
        userAvatar=intentIn.getStringExtra("userAvatar");
        goods=intentIn.getIntegerArrayListExtra("selectedGoods");
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.push_button:
                editorWrite.putString("streamTitle", mTitleEt.getText().toString());
                editorWrite.putString("streamCover", coverUrl);
                editorWrite.putInt("userId", userId);
                editorWrite.putString("userName", userName);
                editorWrite.putString("userAvatar",userAvatar);
                editorWrite.apply();
                if (TextUtils.isEmpty(mTitleEt.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "请输入非空直播标题", Toast.LENGTH_SHORT).show();
                } else if (TCUtils.getCharacterNum(mTitleEt.getText().toString()) > TCConstants.TV_TITLE_MAX_LEN) {
                    Toast.makeText(getApplicationContext(), "直播标题过长 ,最大长度为" + TCConstants.TV_TITLE_MAX_LEN / 2, Toast.LENGTH_SHORT).show();
                } else if (coverUrl == "") {
                    Toast.makeText(getApplicationContext(), getString(R.string.publish_wait_uploading), Toast.LENGTH_SHORT).show();
                } else if (!TCUtils.isNetworkAvailable(this)) {
                    Toast.makeText(getApplicationContext(), "当前网络环境不能发布直播", Toast.LENGTH_SHORT).show();
                } else {
                    updateRoom();
                }
                break;
            case R.id.set_cover:
                showPicChooseDialog();
                break;
        }
    }

    private void updateRoom() {
        //先去请求服务器，获取新的roomId
        CreateRoomRequest request = new CreateRoomRequest();
        CreateRoomRequest.createRoomParam param = new CreateRoomRequest.createRoomParam();
        param.userId = userId;
        param.StreamTitle =mTitleEt.getText().toString();
        param.StreamCover = coverUrl;
        param.GoodsSelected=goods.toString();
        request.setOnResultListener(new BaseRequest.OnResultListener<RoomInfo>() {
            @Override
            public void onFail(int code, String msg) {
                Toast.makeText(CreateRoomActivity.this, "错误代码："+code+",创建房间失败：" + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(RoomInfo data) {
                Toast.makeText(CreateRoomActivity.this,"创建房间成功，房间ID："+"yb"+data.user_id,Toast.LENGTH_SHORT).show();
                editorWrite.putString("streamId", "yb"+data.user_id);
                editorWrite.apply();
                intentOut=new Intent(getApplication(),PushActivity.class);
                intentOut.putIntegerArrayListExtra("selectedGoods",goods);
                startActivity(intentOut);
                finish();

            }
        });
        request.request(param);
    }

    private String coverUrl = "";
    private PicChooseHelper picChooseHelper;

    private void showPicChooseDialog() {
        picChooseHelper = new PicChooseHelper(this, PicChooseHelper.PicType.Cover);
        picChooseHelper.setOnChooseListener(new PicChooseHelper.onChooseListener() {
            @Override
            public void onSuccess(String url) {
                //上传成功,更新直播封面
                updateCover(url);
            }

            @Override
            public void onFail() {
                Toast.makeText(CreateRoomActivity.this, "上传失败请重试", Toast.LENGTH_SHORT).show();
                //上传失败
            }
        });
        picChooseHelper.showDialog();
    }

    private void updateCover(String url) {
        coverUrl = url;
        ImgUtils.load(url, mCoverImg);
        mCoverTipTxt.setVisibility(View.GONE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (picChooseHelper != null) {
            try {
                picChooseHelper.onActivityResult(requestCode, resultCode, data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onDestroy() {
        if(picChooseHelper!=null){
            DialogUtil.closeDialog(picChooseHelper.dialog.dialog);
        }

        super.onDestroy();
    }
}