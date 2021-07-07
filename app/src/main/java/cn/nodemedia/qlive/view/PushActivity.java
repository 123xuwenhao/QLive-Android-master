package cn.nodemedia.qlive.view;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSimpleMsgListener;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.nodemedia.NodeCameraView;
import cn.nodemedia.qlive.MyApplication;
import cn.nodemedia.qlive.R;
import cn.nodemedia.qlive.contract.PushContract;
import cn.nodemedia.qlive.entity.ChatMsgInfo;
import cn.nodemedia.qlive.entity.Constants;
import cn.nodemedia.qlive.entity.GiftCmdInfo;
import cn.nodemedia.qlive.entity.GiftInfo;
import cn.nodemedia.qlive.entity.GoodsInfo;
import cn.nodemedia.qlive.entity.ILVCustomCmd;
import cn.nodemedia.qlive.entity.ILVLiveConstants;
import cn.nodemedia.qlive.entity.ILVText;
import cn.nodemedia.qlive.entity.RoomInfo;
import cn.nodemedia.qlive.entity.UserInfo;
import cn.nodemedia.qlive.utils.BaseRequest;
import cn.nodemedia.qlive.utils.GenerateTestUserSig;
import cn.nodemedia.qlive.utils.HostControlState;
import cn.nodemedia.qlive.utils.view.BottomControlView;
import cn.nodemedia.qlive.utils.view.ChatMsgListView;
import cn.nodemedia.qlive.utils.view.ChatView;
import cn.nodemedia.qlive.utils.view.DanmuView;
import cn.nodemedia.qlive.utils.view.GiftFullView;
import cn.nodemedia.qlive.utils.view.GiftRepeatView;
import cn.nodemedia.qlive.utils.view.GoodView;
import cn.nodemedia.qlive.utils.view.GoodsListView;
import cn.nodemedia.qlive.utils.view.HostControlDialog;
import cn.nodemedia.qlive.utils.view.SizeChangeRelativeLayout;
import cn.nodemedia.qlive.utils.view.TitleView;
import cn.nodemedia.qlive.utils.view.VipEnterView;
import cn.nodemedia.qlive.view.MyRequest.GetGoodsInfoRequest;
import cn.nodemedia.qlive.view.MyRequest.GetLiveRoomRequest;
import cn.nodemedia.qlive.view.MyRequest.HeartBeatRequest;
import cn.nodemedia.qlive.view.MyRequest.QuitRoomRequest;
import tyrantgit.widget.HeartLayout;
import xyz.tanwb.airship.utils.Log;
import xyz.tanwb.airship.view.BaseActivity;

public class PushActivity extends BaseActivity<PushContract.Presenter> implements PushContract.View {

    private static  int FRONT_CAMERA = 0;
    private static  int BACK_CAMERA = 1;
    private NodeCameraView pushSurface;
    SharedPreferences spf;
    int userId;
    String userName;
    String userAvatar;
    String streamId;
    ArrayList<Integer> goodsArr;
    ArrayList<GoodsInfo> selectedGoods=new ArrayList<>();
    String goodsIds;
    ArrayList<GoodsInfo> goodsListValue = new ArrayList<>();
    private static final String urlGet = "http://47.99.171.180:8082/streaming/";

    private SizeChangeRelativeLayout mSizeChangeLayout;
    private TitleView mTitleView;
    private BottomControlView mControlView;
    private GoodsListView mGoodsListView;
    private ChatView mChatView;
    private VipEnterView mVipEnterView;
    private ChatMsgListView mChatListView;
    private DanmuView mDanmuView;
    private GoodView goodView;
    private TextView mGoodsNum;
    private FrameLayout selectedGoodsButton;
    private Button goodsListVis;

    private  Timer heartBeatTimer = new Timer();
    private  Timer heartTimer = new Timer();
    private  Random heartRandom = new Random();
    private HeartLayout heartLayout;
    private GiftRepeatView giftRepeatView;
    private GiftFullView giftFullView;

    private HostControlState hostControlState;

    private String UserSig;
    private int status;
    private static  Gson GsonInstance = new Gson();


    private HeartBeatRequest mHeartBeatRequest = null;

    private boolean isFlashLightOn = false;

    UserInfo appUserProfile = MyApplication.getApplication().getSelfProfile();

    AudioManager audioManager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_push;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getParams();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);  //默认竖屏
        assignViews();
        login();

    }
    private void getSelectedGoods(){
        GetGoodsInfoRequest getGoodsInfoRequest=new GetGoodsInfoRequest();
        GetGoodsInfoRequest.getGoodsParam param=new GetGoodsInfoRequest.getGoodsParam();
        param.goodsIdList=goodsArr.toString();
        getGoodsInfoRequest.setOnResultListener(new BaseRequest.OnResultListener<ArrayList<GoodsInfo>>() {
            @Override
            public void onFail(int code, String msg) {
//                Toast.makeText(getContext(), "错误代码："+code+",商品请求失败：" + msg, Toast.LENGTH_SHORT).show();
                Log.e("getGoodsInfoRequest",msg,null);
            }

            @Override
            public void onSuccess(ArrayList<GoodsInfo> goods) {
                selectedGoods.addAll(goods);

            }
        });
        getGoodsInfoRequest.request(param);

    }

    private void getParams() {
        spf = getSharedPreferences("data", Context.MODE_PRIVATE);
        userId = spf.getInt("userId", 0);
        userName = spf.getString("userName", "");
        userAvatar = spf.getString("userAvatar", "");
        streamId = spf.getString("streamId", "");
        UserSig = GenerateTestUserSig.genTestUserSig(userId + "");
        hostControlState=new HostControlState();
        audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        status = V2TIMManager.getInstance().getLoginStatus();
        goodsArr= getIntent().getIntegerArrayListExtra("selectedGoods");

        GetLiveRoomRequest getLiveRoomRequest = new GetLiveRoomRequest();
        GetLiveRoomRequest.getLiveRoomParam param = new GetLiveRoomRequest.getLiveRoomParam();
        param.streamId = streamId;
        getLiveRoomRequest.setOnResultListener(new BaseRequest.OnResultListener<RoomInfo>() {
            @Override
            public void onFail(int code, String msg) {
                Log.e("getLiveRoomRequest", code + "," + msg, null);
            }

            @Override
            public void onSuccess(RoomInfo roomInfo) {
                goodsIds = roomInfo.goods_selected;
                Log.e("goodsIds", goodsIds, null);
            }
        });
        getLiveRoomRequest.request(param);

    }

    private void login() {
        if (status == V2TIMManager.V2TIM_STATUS_LOGINED) {
            createRoom();
        } else if (status == V2TIMManager.V2TIM_STATUS_LOGOUT) {
            V2TIMManager.getInstance().login(userId + "", UserSig, new V2TIMCallback() {
                @Override
                public void onError(int code, String desc) {
                    Toast.makeText(PushActivity.this, "错误代码：" + code + ",登录失败：" + desc, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess() {
                    Toast.makeText(PushActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    createRoom();
                }
            });
        }
    }

    private void startHeartAnim() {
        heartTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                heartLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        heartLayout.addHeart(getRandomColor());
                    }
                });
            }
        }, 0, 1000); //1秒钟

    }
    private int getRandomColor() {
        return Color.rgb(heartRandom.nextInt(255), heartRandom.nextInt(255), heartRandom.nextInt(255));
    }


    private void startHeartBeat() {
        heartBeatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //发送心跳包
                if (mHeartBeatRequest == null) {
                    mHeartBeatRequest = new HeartBeatRequest();
                }
                String userId = MyApplication.getApplication().getSelfProfile().getUser_id()+"";
                HeartBeatRequest.getHeartBeatParam heartBeatParam=new HeartBeatRequest.getHeartBeatParam();
                heartBeatParam.userId=userId;
                heartBeatParam.streamId=streamId;
                mHeartBeatRequest.request(heartBeatParam);
            }
        }, 0, 4000); //4秒钟 。服务器是10秒钟去检测一次。
    }

    private void createRoom() {

        V2TIMManager.getInstance().createGroup(V2TIMManager.GROUP_TYPE_AVCHATROOM, "yb" + userId, "123", new V2TIMValueCallback<String>() {

            @Override
            public void onError(int code, String desc) {
                Toast.makeText(PushActivity.this,"错误代码："+code+",创建房间失败："+desc,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String s) {
                assignIMViews();
                addMsgListener();
                startHeartBeat();
                startHeartAnim();
                getSelectedGoods();

            }
        });


    }

    private void addMsgListener() {
        V2TIMManager.getInstance().addSimpleMsgListener(new V2TIMSimpleMsgListener() {
            @Override
            public void onRecvGroupCustomMessage(String msgID, String groupID, V2TIMGroupMemberInfo sender, byte[] customData) {
                ILVCustomCmd cmd = GsonInstance.fromJson(new String(customData), ILVCustomCmd.class);
                UserInfo userProfile = cmd.getUserProfile();
                String cmdStreamId = cmd.getStreamId();
                //接收到自定义消息
                if (cmd.getCmd() == Constants.CMD_CHAT_MSG_LIST) {
                    String content = cmd.getParam();
                    ChatMsgInfo info = ChatMsgInfo.createListInfo(content, cmdStreamId, userProfile.getUser_avatar());
                    mChatListView.addMsgInfo(info);
                } else if (cmd.getCmd() == Constants.CMD_CHAT_MSG_DANMU) {
                    String content = cmd.getParam();
                    ChatMsgInfo info = ChatMsgInfo.createListInfo(cmd.getParam(), cmdStreamId, userProfile.getUser_avatar());
                    mChatListView.addMsgInfo(info);
                    String name = userProfile.getUser_name();
                    if (TextUtils.isEmpty(name)) {
                        name = userProfile.getUser_id() + "";
                    }
                    ChatMsgInfo danmuInfo = ChatMsgInfo.createDanmuInfo(content, cmdStreamId, userProfile.getUser_avatar(), name);
                    mDanmuView.addMsgInfo(danmuInfo);
                } else if (cmd.getCmd() == Constants.CMD_CHAT_GIFT) {
                    //界面显示礼物动画。
                    GiftCmdInfo giftCmdInfo = new Gson().fromJson(cmd.getParam(), GiftCmdInfo.class);
                    int giftId = giftCmdInfo.giftId;
                    String repeatId = giftCmdInfo.repeatId;
                    GiftInfo giftInfo = GiftInfo.getGiftById(giftId);
                    if (giftInfo == null) {
                        return;
                    }
                    if (giftInfo.type == GiftInfo.Type.ContinueGift) {
                        giftRepeatView.showGift(giftInfo, repeatId, userProfile);
                    } else if (giftInfo.type == GiftInfo.Type.FullScreenGift) {
                        //全屏礼物
                        giftFullView.showGift(giftInfo, userProfile);
                    }
                } else if (cmd.getCmd() == ILVLiveConstants.ILVLIVE_CMD_ENTER) {
                    //用户进入直播
                    mTitleView.addWatcher(userProfile);
                    mVipEnterView.showVipEnter(userProfile);
                } else if (cmd.getCmd() == ILVLiveConstants.ILVLIVE_CMD_LEAVE) {
                    //用户离开消息
                    mTitleView.removeWatcher(userProfile);
                }


            }
        });

    }

    private void assignIMViews() {

        mSizeChangeLayout = findViewById(R.id.size_change_layout);
        mSizeChangeLayout.setOnSizeChangeListener(new SizeChangeRelativeLayout.OnSizeChangeListener() {
            @Override
            public void onLarge() {
                //键盘隐藏
                mChatView.setVisibility(View.INVISIBLE);
                mControlView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSmall() {
                //键盘显示
            }
        });

        mTitleView = findViewById(R.id.title_view);
        mTitleView.setHost(MyApplication.getApplication().getSelfProfile());

        mControlView = findViewById(R.id.control_view);
        mControlView.setIsHost(true);
        mControlView.setOnControlListener(new BottomControlView.OnControlListener() {
            @Override
            public void onChatClick() {
                //点击了聊天按钮，显示聊天操作栏
                mChatView.setVisibility(View.VISIBLE);
                mControlView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCloseClick() {
                // 点击了关闭按钮，关闭直播
                mPresenter.pushStop();
                finish();
            }

            @Override
            public void onGiftClick() {
                //主播界面，不能发送礼物
            }

            @Override
            public void onOptionClick(View view) {
                //显示主播操作对话框

                boolean beautyOn = hostControlState.isBeautyOn();
                boolean flashOn = isFlashLightOn;
                boolean voiceOn = hostControlState.isVoiceOn();

                HostControlDialog hostControlDialog = new HostControlDialog(PushActivity.this);

                hostControlDialog.setOnControlClickListener(controlClickListener);
                hostControlDialog.updateView(beautyOn, flashOn, voiceOn);
                hostControlDialog.show(view);
            }

        });

        mChatView = findViewById(R.id.chat_view);
        mChatView.setOnChatSendListener(new ChatView.OnChatSendListener() {
            @Override
            public void onChatSend( ILVCustomCmd customCmd) {
                //发送消息
                customCmd.setStreamId(streamId);
                customCmd.setUserProfile(appUserProfile);
                byte[] customData = GsonInstance.toJson(customCmd).getBytes();
                V2TIMManager.getInstance().sendGroupCustomMessage(customData, streamId, V2TIMMessage.V2TIM_PRIORITY_NORMAL, new V2TIMValueCallback<V2TIMMessage>() {
                    @Override
                    public void onError(int code, String desc) {

                    }

                    @Override
                    public void onSuccess(V2TIMMessage v2TIMMessage) {
                        if (customCmd.getCmd() == Constants.CMD_CHAT_MSG_LIST) {
                            //如果是列表类型的消息，发送给列表显示
                            String chatContent = customCmd.getParam();
                            UserInfo user = MyApplication.getApplication().getSelfProfile();
                            String userId = MyApplication.getApplication().getSelfProfile().getUser_id() + "";
                            String avatar = MyApplication.getApplication().getSelfProfile().getUser_avatar();
                            ChatMsgInfo info = ChatMsgInfo.createListInfo(chatContent, userId, avatar);
                            mChatListView.addMsgInfo(info);
                        } else if (customCmd.getCmd() == Constants.CMD_CHAT_MSG_DANMU) {
                            String chatContent = customCmd.getParam();
                            String userId = MyApplication.getApplication().getSelfProfile().getUser_id() + "";
                            String avatar = MyApplication.getApplication().getSelfProfile().getUser_avatar();
                            ChatMsgInfo info = ChatMsgInfo.createListInfo(chatContent, userId, avatar);
                            mChatListView.addMsgInfo(info);

                            String name = MyApplication.getApplication().getSelfProfile().getUser_name();
                            if (TextUtils.isEmpty(name)) {
                                name = userId;
                            }
                            ChatMsgInfo danmuInfo = ChatMsgInfo.createDanmuInfo(chatContent, userId, avatar, name);
                            mDanmuView.addMsgInfo(danmuInfo);
                        }

                    }
                });
            }
        });

        mControlView.setVisibility(View.VISIBLE);
        mChatView.setVisibility(View.INVISIBLE);

        mChatListView = findViewById(R.id.chat_list);
        mVipEnterView = findViewById(R.id.vip_enter);
        mDanmuView = findViewById(R.id.danmu_view);

        heartLayout = findViewById(R.id.heart_layout);
        giftRepeatView = findViewById(R.id.gift_repeat_view);
        giftFullView = findViewById(R.id.gift_full_view);

        //商品列表
        mGoodsListView = findViewById(R.id.goods_view_list);
        mGoodsListView.setIsHost(true);
        mGoodsListView.setOnControlListener(new GoodsListView.OnGoodsListener() {
            @Override
            public void onCommonClick(int i, ArrayList<GoodsInfo> mGoodsInfos) {
                Toast.makeText(getContext(), mGoodsInfos.get(i).getGoods_id() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onHostClick(int i, ArrayList<GoodsInfo> mGoodsInfos) {
                Toast.makeText(getContext(), mGoodsInfos.get(i).getGoods_id() + "", Toast.LENGTH_SHORT).show();
                mGoodsListView.setVisibility(View.INVISIBLE);
            }
        });

        //选中商品按钮 goods_list_view
        selectedGoodsButton = findViewById(R.id.goods_list_view);
        selectedGoodsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoodsListView.setVisibility(mGoodsListView.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
            }
        });
        goodsListVis=findViewById(R.id.goods_list_vis);
        goodsListVis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoodsListView.setVisibility(View.INVISIBLE);
            }
        });

        //直播购物界面

        goodView = findViewById(R.id.goods_view_self);


        GetGoodsInfoRequest getGoodsInfoRequest = new GetGoodsInfoRequest();
        GetGoodsInfoRequest.getGoodsParam goodsParam = new GetGoodsInfoRequest.getGoodsParam();
        goodsParam.goodsIdList = goodsIds;
        getGoodsInfoRequest.setOnResultListener(new BaseRequest.OnResultListener<ArrayList<GoodsInfo>>() {
            @Override
            public void onFail(int code, String msg) {
                Log.e("getGoodsInfoRequest", code + ":" + msg, null);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(ArrayList<GoodsInfo> goodsList) {
                Log.e("goodsList", goodsList.toString(), null);
//                goodsListValue= goodsList;
                //初始化商品列表

                mGoodsNum = findViewById(R.id.goods_num);
                Log.e("goodsListValue", goodsListValue.toString(), null);

                mGoodsListView.addGoodsInfos(goodsList);
                mGoodsNum.bringToFront();
                mGoodsNum.setText(goodsList.size() + "");

                goodView.setGoodAvatar(urlGet + goodsList.get(0).getPhoto_path());
                goodView.setGoodName(goodsList.get(0).getName());
                goodView.setGoodPrice(goodsList.get(0).getPrice());
//                goodView.setOnGoodListener(new GoodView.OnGoodListener() {
//                    @Override
//                    public void onGoodClick() {
//                        //TODO:跳转到购物界面
//                        Toast.makeText(getContext(), "跳转到购物界面", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
        getGoodsInfoRequest.request(goodsParam);
    }



    /**
     * 实例化视图控件
     */
    private void assignViews() {
        pushSurface = getView(R.id.push_surface);
    }


    @Override
    public void initPresenter() {
        mPresenter.initPresenter(this);
        mPresenter.pushStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    private  HostControlDialog.OnControlClickListener controlClickListener = new HostControlDialog.OnControlClickListener() {
        @Override
        public void onBeautyClick() {
            //点击美颜
            boolean isBeautyOn = hostControlState.isBeautyOn();
            if (isBeautyOn) {
                //关闭美颜
                mPresenter.setBeautyLevel(0);
                hostControlState.setBeautyOn(false);
            } else {
                //打开美颜
                mPresenter.setBeautyLevel(50);
                hostControlState.setBeautyOn(true);
            }
        }

        @Override
        public void onFlashClick() {
            // 闪光灯
            boolean isFlashOn = isFlashLightOn;
            if (isFlashOn) {
                mPresenter.switchFlash();
                hostControlState.setFlashOn(false);
            } else {
                mPresenter.switchFlash();
                hostControlState.setFlashOn(true);
            }
        }

        @Override
        public void onVoiceClick() {
            //声音
            boolean isVoiceOn = hostControlState.isVoiceOn();
            if (isVoiceOn) {
                //静音
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                hostControlState.setVoiceOn(false);
            } else {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                hostControlState.setVoiceOn(true);
            }
        }

        @Override
        public void onCameraClick() {
            int cameraId = hostControlState.getCameraid();
            if (cameraId == FRONT_CAMERA) {
                mPresenter.switchCamera();
//                FlipAnimatorXViewShow(pushSwitch,pushSwitch,300);
                hostControlState.setCameraid(BACK_CAMERA);
            } else if (cameraId == BACK_CAMERA) {
                mPresenter.switchCamera();
                hostControlState.setCameraid(FRONT_CAMERA);
            }
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
    }

    public void logout() {
        V2TIMManager.getInstance().dismissGroup(streamId, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                Toast.makeText(PushActivity.this, "解散直播群失败!" + code + desc, Toast.LENGTH_LONG).show();
                Log.e("解散失败");
            }

            @Override
            public void onSuccess() {
                Toast.makeText(PushActivity.this, "直播群解散成功！", Toast.LENGTH_SHORT).show();

            }
        });
        V2TIMManager.getInstance().logout(new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                Toast.makeText(PushActivity.this, "互动系统关闭失败!" + code + desc, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(PushActivity.this, "互动系统已经登出！", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void quitLive() {

        ILVCustomCmd customCmd = new ILVCustomCmd();
        customCmd.setType(ILVText.ILVTextType.eGroupMsg);
        customCmd.setCmd(ILVLiveConstants.ILVLIVE_CMD_LEAVE);
        customCmd.setStreamId(streamId);
        customCmd.setUserProfile(appUserProfile);


        byte[] customData = GsonInstance.toJson(customCmd).getBytes();

        //发送退出消息给服务器
        QuitRoomRequest request = new QuitRoomRequest();
        QuitRoomRequest.getRoomParam param = new QuitRoomRequest.getRoomParam();
        param.userId = userId + "";
        param.streamId = streamId;
        request.setOnResultListener(new BaseRequest.OnResultListener<RoomInfo>() {
            @Override
            public void onFail(int code, String msg) {
                Log.e("QuitRoomRequest","请求失败"+msg,null);
            }

            @Override
            public void onSuccess(RoomInfo roomInfo) {
                Log.e("QuitRoomRequest","请求成功",null);

            }
        });
        request.request(param);
        V2TIMManager.getInstance().sendGroupCustomMessage(customData, streamId, V2TIMMessage.V2TIM_PRIORITY_NORMAL, new V2TIMValueCallback<V2TIMMessage>() {
            @Override
            public void onError(int code, String desc) {
                Toast.makeText(PushActivity.this,"发送失败:"+desc,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {
                Toast.makeText(PushActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                if(V2TIMManager.getInstance().getLoginStatus()==V2TIMManager.V2TIM_STATUS_LOGINED) {
                    logout();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        quitLive();
        heartTimer.cancel();
        heartBeatTimer.cancel();


    }

    @Override
    public void exit() {
        finish();
    }

    @Override
    public NodeCameraView getNodeCameraView() {
        return pushSurface;
    }

    @Override
    public void flashChange(boolean onOrOff) {
    }
}
