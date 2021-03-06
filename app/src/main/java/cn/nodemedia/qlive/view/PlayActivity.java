package cn.nodemedia.qlive.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSimpleMsgListener;
import com.tencent.imsdk.v2.V2TIMUserInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.nodemedia.NodePlayerView;
import cn.nodemedia.qlive.MyApplication;
import cn.nodemedia.qlive.R;
import cn.nodemedia.qlive.contract.PlayContract;
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
import cn.nodemedia.qlive.utils.view.BottomControlView;
import cn.nodemedia.qlive.utils.view.ChatMsgListView;
import cn.nodemedia.qlive.utils.view.ChatView;
import cn.nodemedia.qlive.utils.view.DanmuView;
import cn.nodemedia.qlive.utils.view.GiftFullView;
import cn.nodemedia.qlive.utils.view.GiftRepeatView;
import cn.nodemedia.qlive.utils.view.GoodView;
import cn.nodemedia.qlive.utils.view.GoodsListView;
import cn.nodemedia.qlive.utils.view.SizeChangeRelativeLayout;
import cn.nodemedia.qlive.utils.view.TitleView;
import cn.nodemedia.qlive.utils.view.ViewRoundUtils;
import cn.nodemedia.qlive.utils.view.VipEnterView;
import cn.nodemedia.qlive.view.MyRequest.GetGoodsInfoRequest;
import cn.nodemedia.qlive.view.MyRequest.GetLiveRoomRequest;
import cn.nodemedia.qlive.view.MyRequest.GetSingleUserInfoRequest;
import cn.nodemedia.qlive.view.MyRequest.GetUserInfoRequest;
import cn.nodemedia.qlive.view.MyRequest.GetWatcherRequest;
import cn.nodemedia.qlive.view.MyRequest.HeartBeatRequest;
import cn.nodemedia.qlive.view.MyRequest.JoinRoomRequst;
import cn.nodemedia.qlive.view.MyRequest.QuitRoomRequest;
import cn.nodemedia.qlive.widget.GiftSelectDialog;
import tyrantgit.widget.HeartLayout;
import xyz.tanwb.airship.utils.Log;
import xyz.tanwb.airship.utils.StatusBarUtils;
import xyz.tanwb.airship.view.BaseActivity;

public class PlayActivity extends BaseActivity<PlayContract.Presenter> implements PlayContract.View {

    private NodePlayerView playSurface;
    private int userId;
    private String userName;
    private String userAvatar;
    private String streamId;
    String goodsIds;
    ArrayList<GoodsInfo> goodsListValue = new ArrayList<>();
    private static final String urlGet = "http://47.99.171.180:8082/streaming/";

    private static final String TAG = "gift";
    private SizeChangeRelativeLayout mSizeChangeLayout;
    private TitleView titleView;
    private BottomControlView mControlView;
    private GoodsListView mGoodsListView;
    private ChatView mChatView;
    private ChatMsgListView mChatListView;
    private VipEnterView mVipEnterView;
    private DanmuView mDanmuView;
    private GiftSelectDialog giftSelectDialog;
    private TextView mGoodsNum;
    private FrameLayout selectedGoodsButton;
    private Button goodsListVis;
    private TextView roomId;

    private Timer heartTimer = new Timer();
    private Random heartRandom = new Random();
    private HeartLayout heartLayout;
    private GiftRepeatView giftRepeatView;
    private GiftFullView giftFullView;
    private GoodView goodView;

    //?????????????????????button
//    private Button goodsShopItem;
    private Button goodsSelectedItem;

    private LinearLayout roomFlag;

    private HeartBeatRequest mHeartBeatRequest = null;
    private Timer heartBeatTimer = new Timer();

    private int hostId;

    private String UserSig;
    private int status;
    private static Gson GsonInstance = new Gson();
    private static UserInfo appUserProfile = MyApplication.getApplication().getSelfProfile();

    @Override
    public int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getParams();
        StatusBarUtils.setColorToTransparent(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        assignViews();
        login();

    }

    private void getParams() {
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", 0);
        userName = intent.getStringExtra("userName");
        userAvatar = intent.getStringExtra("userAvatar");
        streamId = intent.getStringExtra("streamId");
        hostId = intent.getIntExtra("hostId", -1);
        UserSig = GenerateTestUserSig.genTestUserSig(userId + "");
        status = V2TIMManager.getInstance().getLoginStatus();
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



    private void joinRoom() {
        if (hostId < 0 || TextUtils.isEmpty(streamId)) {
            return;
        }
        GetLiveRoomRequest getLiveRoomRequest = new GetLiveRoomRequest();
        GetLiveRoomRequest.getLiveRoomParam liveRoomParam = new GetLiveRoomRequest.getLiveRoomParam();
        liveRoomParam.streamId = streamId;
        getLiveRoomRequest.setOnResultListener(new BaseRequest.OnResultListener<RoomInfo>() {
            @Override
            public void onFail(int code, String msg) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                MyAlertDialog myAlertDialog = new MyAlertDialog(getContext()).builder()
                        .setTitle("??????")
                        .setMsg("???????????????????????????????????????????????????")
                        .setPositiveButton("??????", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                heartTimer.cancel();
                                heartBeatTimer.cancel();
                                finish();
                            }
                        }).setCanceledOnTouchOutside(false);
                myAlertDialog.show();

            }

            @Override
            public void onSuccess(RoomInfo roomInfo) {
                //????????????
                V2TIMManager.getInstance().joinGroup(streamId, "AVChatroom", new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {
                        Toast.makeText(PlayActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onSuccess() {
                        //????????????
                        joinRoomRequst();
                        //???????????????
                        assignIMViews();
                        //??????????????????
                        startHeartAnim();
                        //????????????????????????????????????
                        sendEnterRoomMsg();
                        //?????????????????????
                        updateTitleView();
                        //???????????????
                        startHeartBeat();
                        //?????????????????????
                        addMsgListener();
                    }
                });
            }
        });
        getLiveRoomRequest.request(liveRoomParam);



    }

    private void joinRoomRequst() {
        JoinRoomRequst requst = new JoinRoomRequst();
        JoinRoomRequst.getWatcherParam param = new JoinRoomRequst.getWatcherParam();
        param.streamId = streamId;
        param.userId = userId + "";
        requst.request(param);
    }


    //???????????????????????????????????????????????????????????????

    private void addMsgListener() {

        //?????????????????????
        V2TIMManager.getInstance().addSimpleMsgListener(new V2TIMSimpleMsgListener() {
            @Override
            public void onRecvGroupCustomMessage(String msgID, String groupID, V2TIMGroupMemberInfo sender, byte[] customData) {
                ILVCustomCmd cmd = GsonInstance.fromJson(new String(customData), ILVCustomCmd.class);
                UserInfo userProfile = cmd.getUserProfile();
                String cmdStreamId = cmd.getStreamId();

                //????????????????????????
                if (cmd.getCmd() == Constants.CMD_CHAT_MSG_LIST) {
                    String content = cmd.getParam();
                    ChatMsgInfo info = ChatMsgInfo.createListInfo(content, cmdStreamId, userProfile.getUser_avatar());
                    mChatListView.addMsgInfo(info);
                } else if (cmd.getCmd() == Constants.CMD_CHAT_MSG_DANMU) {
                    String content = cmd.getParam();
                    ChatMsgInfo info = ChatMsgInfo.createListInfo(content, cmdStreamId, userProfile.getUser_avatar());
                    mChatListView.addMsgInfo(info);

                    String name = userProfile.getUser_name();
                    if (TextUtils.isEmpty(name)) {
                        name = userProfile.getUser_id() + "";
                    }
                    ChatMsgInfo danmuInfo = ChatMsgInfo.createDanmuInfo(content, cmdStreamId, userProfile.getUser_avatar(), name);
                    mDanmuView.addMsgInfo(danmuInfo);
                } else if (cmd.getCmd() == ILVLiveConstants.ILVLIVE_CMD_LEAVE) {
                    //??????????????????
                    if (hostId == userProfile.getUser_id()) {
                        //?????????????????????
                        MyAlertDialog myAlertDialog = new MyAlertDialog(getContext()).builder()
                                .setTitle("??????")
                                .setMsg("???????????????????????????????????????????????????")
                                .setPositiveButton("??????", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        heartTimer.cancel();
                                        heartBeatTimer.cancel();
                                        finish();
                                    }
                                }).setCanceledOnTouchOutside(false);
                        myAlertDialog.show();
                    } else {
                        //??????????????????
                        titleView.removeWatcher(userProfile);
                    }
                } else if (cmd.getCmd() == ILVLiveConstants.ILVLIVE_CMD_ENTER) {

                    titleView.addWatcher(userProfile);
                    mVipEnterView.showVipEnter(userProfile);
                } else if (cmd.getCmd() == ILVLiveConstants.ILVLIVE_CMD_SELECTED_GOODS) {
                    GoodsInfo goodsInfo=cmd.getGoodsInfo();
                    updateGoodsView(goodsInfo);
                }
            }

            @Override
            public void onRecvC2CCustomMessage(String msgID, V2TIMUserInfo sender, byte[] customData) {
                ILVCustomCmd cmd = GsonInstance.fromJson(new String(customData), ILVCustomCmd.class);
                if (cmd.getCmd() == ILVLiveConstants.ILVLIVE_CMD_SELECTED_GOODS) {
                    GoodsInfo goodsInfo=cmd.getGoodsInfo();
                    updateGoodsView(goodsInfo);
                }
            }
        });
    }

    private void updateGoodsView(GoodsInfo goodsInfo) {
        goodView.setGoodAvatar(urlGet+goodsInfo.getPhoto_path());
        goodView.setGoodPrice(goodsInfo.getPrice());
        goodView.setGoodName(goodsInfo.getName());
        Toast.makeText(PlayActivity.this, "????????????????????????--"+goodsInfo.getName()+"!", Toast.LENGTH_SHORT).show();
    }

    //???????????????

    private void startHeartBeat() {
        heartBeatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //???????????????
                if (mHeartBeatRequest == null) {
                    mHeartBeatRequest = new HeartBeatRequest();
                }
                String userId = MyApplication.getApplication().getSelfProfile().getUser_id() + "";
                HeartBeatRequest.getHeartBeatParam heartBeatParam = new HeartBeatRequest.getHeartBeatParam();
                heartBeatParam.streamId = streamId;
                heartBeatParam.userId = userId;
                mHeartBeatRequest.request(heartBeatParam);
            }
        }, 0, 4000); //4?????? ???????????????10????????????????????????
    }

    //??????????????????

    private int getRandomColor() {
        return Color.rgb(heartRandom.nextInt(255), heartRandom.nextInt(255), heartRandom.nextInt(255));
    }

    //??????????????????

    private void updateTitleView() {
//        List<String> list = new ArrayList<String>();
//        list.add(hostId+"");
        GetSingleUserInfoRequest getSingleUserInfoRequest = new GetSingleUserInfoRequest();
        GetSingleUserInfoRequest.getUserParam param = new GetSingleUserInfoRequest.getUserParam();
        param.userId = hostId + "";
        getSingleUserInfoRequest.setOnResultListener(new BaseRequest.OnResultListener<UserInfo>() {
            @Override
            public void onFail(int code, String msg) {
                Toast.makeText(mContext, "??????title?????????", Toast.LENGTH_SHORT).show();
                Log.e("abc", "??????title?????????", null);
                titleView.setHost(null);
            }

            @Override
            public void onSuccess(UserInfo userInfo) {
                Toast.makeText(mContext, "??????title?????????", Toast.LENGTH_SHORT).show();
                Log.e("abc", "??????title?????????", null);
                titleView.setHost(userInfo);
            }
        });
        getSingleUserInfoRequest.request(param);

        // ????????????????????????titleView??????
        titleView.addWatcher(appUserProfile);

        //???????????????????????????????????????
        GetWatcherRequest watcherRequest = new GetWatcherRequest();
        GetWatcherRequest.getWatcherParam watcherParam = new GetWatcherRequest.getWatcherParam();
        watcherParam.streamId = streamId;
        watcherRequest.setOnResultListener(new BaseRequest.OnResultListener<Set<String>>() {
            @Override
            public void onFail(int code, String msg) {

            }

            @Override
            public void onSuccess(Set<String> watchers) {
                if (watchers == null) {
                    return;
                }

                List<String> watcherList = new ArrayList<>(watchers);
                GetUserInfoRequest getUserInfoRequest = new GetUserInfoRequest();
                GetUserInfoRequest.getUserParam param1 = new GetUserInfoRequest.getUserParam();
                param1.userIdList = watcherList.toString();
                getUserInfoRequest.setOnResultListener(new BaseRequest.OnResultListener<List<UserInfo>>() {
                    @Override
                    public void onFail(int code, String msg) {

                    }

                    @Override
                    public void onSuccess(List<UserInfo> userInfos) {
                        titleView.addWatchers(userInfos);
                    }
                });
                getUserInfoRequest.request(param1);
            }
        });
        watcherRequest.request(watcherParam);
    }

    //????????????????????????

    private void sendEnterRoomMsg() {
        ILVCustomCmd customCmd = new ILVCustomCmd();
        customCmd.setType(ILVText.ILVTextType.eGroupMsg);
        customCmd.setCmd(ILVLiveConstants.ILVLIVE_CMD_ENTER);
        customCmd.setStreamId(streamId);
        customCmd.setUserProfile(appUserProfile);
        byte[] customData = GsonInstance.toJson(customCmd).getBytes();
        V2TIMManager.getInstance().sendGroupCustomMessage(customData, streamId, V2TIMMessage.V2TIM_PRIORITY_NORMAL, new V2TIMValueCallback<V2TIMMessage>() {
            @Override
            public void onError(int code, String desc) {
                Toast.makeText(PlayActivity.this, "????????????????????????:" + desc, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {
                Toast.makeText(PlayActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();

            }
        });
    }

    //??????????????????

    private void startHeartAnim() {
        heartTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                heartLayout.post(() -> heartLayout.addHeart(getRandomColor()));
            }
        }, 0, 1000); //1??????

    }

    @SuppressLint("WrongViewCast")
    private void assignIMViews() {
        mSizeChangeLayout = findViewById(R.id.size_change_layout);
        mSizeChangeLayout.setOnSizeChangeListener(new SizeChangeRelativeLayout.OnSizeChangeListener() {
            @Override
            public void onLarge() {
                //????????????
                mChatView.setVisibility(View.INVISIBLE);
                mControlView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSmall() {
                //????????????
            }
        });

        titleView = findViewById(R.id.title_view);

        //??????????????????
        roomId=findViewById(R.id.room_id);
        roomFlag = findViewById(R.id.room_flag);
        ViewRoundUtils.clipViewCornerByDp(roomFlag, 10);
        roomId.setText("ID:"+streamId);

        mChatView = findViewById(R.id.chat_view);
        mControlView = findViewById(R.id.control_view);
        mControlView.setIsHost(false);
        mControlView.setOnControlListener(new BottomControlView.OnControlListener() {
            @Override
            public void onChatClick() {
                //?????????????????????????????????????????????
                mChatView.setVisibility(View.VISIBLE);
                mControlView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCloseClick() {
                // ????????????????????????????????????
                MyAlertDialog myAlertDialog = new MyAlertDialog(getContext()).builder()
                        .setTitle("??????")
                        .setMsg("?????????????????????????????????")
                        .setPositiveButton("??????", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quitRoom();
                                heartTimer.cancel();
                                heartBeatTimer.cancel();
                                finish();
                            }
                        }).setNegativeButton("??????", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //????????????
                            }
                        });
                myAlertDialog.show();
            }

            @Override
            public void onGiftClick() {
                //?????????????????????
                if (giftSelectDialog == null) {
                    giftSelectDialog = new GiftSelectDialog(PlayActivity.this);

                    giftSelectDialog.setGiftSendListener(giftSendListener);
                }
                giftSelectDialog.show();
            }

            @Override
            public void onOptionClick(View view) {
                //????????????????????????????????????
            }
        });


        mChatView.setOnChatSendListener(new ChatView.OnChatSendListener() {
            @Override
            public void onChatSend(ILVCustomCmd customCmd) {
                //????????????
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
                            //??????????????????????????????????????????????????????
                            String chatContent = customCmd.getParam();
                            String userId = appUserProfile.getUser_id() + "";
                            String avatar = appUserProfile.getUser_avatar();
                            ChatMsgInfo info = ChatMsgInfo.createListInfo(chatContent, userId, avatar);
                            mChatListView.addMsgInfo(info);
                        } else if (customCmd.getCmd() == Constants.CMD_CHAT_MSG_DANMU) {
                            String chatContent = customCmd.getParam();
                            String userId = appUserProfile.getUser_id() + "";
                            String avatar = appUserProfile.getUser_avatar();
                            ChatMsgInfo info = ChatMsgInfo.createListInfo(chatContent, userId, avatar);
                            mChatListView.addMsgInfo(info);

                            String name = appUserProfile.getUser_name();
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
        //????????????
        mGoodsListView = findViewById(R.id.goods_view_list);
        mGoodsListView.setOnGoodsListener(new GoodsListView.OnGoodsListener() {
            @Override
            public void onCommonClick(int i, ArrayList<GoodsInfo> mGoodsInfos) {
                Toast.makeText(getContext(), mGoodsInfos.get(i).getGoods_id() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onHostClick(int i, ArrayList<GoodsInfo> mGoodsInfos) {
                //?????????????????????
            }
        });

        //?????????????????? goods_list_view
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

        mChatListView = findViewById(R.id.chat_list);
        mVipEnterView = findViewById(R.id.vip_enter);
        mDanmuView = findViewById(R.id.danmu_view);

        heartLayout = findViewById(R.id.heart_layout);
        giftRepeatView = findViewById(R.id.gift_repeat_view);
        giftFullView = findViewById(R.id.gift_full_view);

        //??????????????????

        goodView = findViewById(R.id.goods_view_self);
        ViewRoundUtils.clipViewCornerByDp(goodView, 10);


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
                //?????????????????????

                mGoodsNum = findViewById(R.id.goods_num);
                Log.e("goodsListValue", goodsListValue.toString(), null);

                mGoodsListView.addGoodsInfos(goodsList);
                mGoodsNum.bringToFront();
                mGoodsNum.setText(goodsList.size() + "");

                goodView.setGoodAvatar(urlGet + goodsList.get(0).getPhoto_path());
                goodView.setGoodName(goodsList.get(0).getName());
                goodView.setGoodPrice(goodsList.get(0).getPrice());
                goodView.setOnGoodListener(new GoodView.OnGoodListener() {
                    @Override
                    public void onGoodClick() {
                        //TODO:?????????????????????
                        Toast.makeText(getContext(), "?????????????????????", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        getGoodsInfoRequest.request(goodsParam);

    }


    private GiftSelectDialog.OnGiftSendListener giftSendListener = new GiftSelectDialog.OnGiftSendListener() {
        @Override
        public void onGiftSendClick(ILVCustomCmd customCmd) {
            customCmd.setStreamId(streamId);
            customCmd.setUserProfile(appUserProfile);
            byte[] customData = GsonInstance.toJson(customCmd).getBytes();
            V2TIMManager.getInstance().sendGroupCustomMessage(customData, streamId, V2TIMMessage.V2TIM_PRIORITY_NORMAL, new V2TIMValueCallback<V2TIMMessage>() {

                @Override
                public void onError(int code, String desc) {


                }

                @Override
                public void onSuccess(V2TIMMessage v2TIMMessage) {

                    if (customCmd.getCmd() == Constants.CMD_CHAT_GIFT) {
                        //???????????????????????????
                        GiftCmdInfo giftCmdInfo = new Gson().fromJson(customCmd.getParam(), GiftCmdInfo.class);
                        int giftId = giftCmdInfo.giftId;
                        String repeatId = giftCmdInfo.repeatId;
                        GiftInfo giftInfo = GiftInfo.getGiftById(giftId);
                        if (giftInfo == null) {
                            return;
                        }
                        if (giftInfo.type == GiftInfo.Type.ContinueGift) {
                            giftRepeatView.showGift(giftInfo, repeatId, MyApplication.getApplication().getSelfProfile());
                        } else if (giftInfo.type == GiftInfo.Type.FullScreenGift) {
                            //????????????
                            giftFullView.showGift(giftInfo, MyApplication.getApplication().getSelfProfile());
                        }
                    }
                }
            });
        }
    };

    private void quitRoom() {

        //??????????????????????????????

        ILVCustomCmd customCmd = new ILVCustomCmd();
        customCmd.setType(ILVText.ILVTextType.eGroupMsg);
        customCmd.setCmd(ILVLiveConstants.ILVLIVE_CMD_LEAVE);
        customCmd.setStreamId(streamId);
        customCmd.setUserProfile(appUserProfile);
        byte[] customData = GsonInstance.toJson(customCmd).getBytes();
        V2TIMManager.getInstance().sendGroupCustomMessage(customData, streamId, V2TIMMessage.V2TIM_PRIORITY_NORMAL, new V2TIMValueCallback<V2TIMMessage>() {
            @Override
            public void onError(int code, String desc) {
                Log.e("sendGroupCustomMessage", "???????????????????????????????????????????????????????????????" + code + "," + desc, null);
            }

            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {
                Log.e("sendGroupCustomMessage", "??????????????????????????????", null);
                V2TIMManager.getInstance().quitGroup(streamId, new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {
                        Toast.makeText(PlayActivity.this, "???????????????" + code + ",??????????????????????????????????????????????????????" + desc, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess() {

                        if (V2TIMManager.getInstance().getLoginStatus() == V2TIMManager.V2TIM_STATUS_LOGINED) {
                            logout();
                        }

                    }
                });
            }
        });

        QuitRoomRequest request = new QuitRoomRequest();
        QuitRoomRequest.getRoomParam param = new QuitRoomRequest.getRoomParam();
        param.userId = userId + "";
        param.streamId = streamId;
        request.setOnResultListener(new BaseRequest.OnResultListener() {
            @Override
            public void onFail(int code, String msg) {
                Log.e("QuitRoomRequest", "????????????????????????" + code + "," + msg, null);
            }

            @Override
            public void onSuccess(Object object) {
                Log.e("QuitRoomRequest", "????????????????????????", null);
            }
        });
        request.request(param);
    }

    private void logout() {
        V2TIMManager.getInstance().logout(new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                Toast.makeText(PlayActivity.this, "????????????????????????!" + code + desc, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(PlayActivity.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void login() {
        if (status == V2TIMManager.V2TIM_STATUS_LOGINED) {
            joinRoom();
        } else if (status == V2TIMManager.V2TIM_STATUS_LOGOUT) {
            V2TIMManager.getInstance().login(userId + "", UserSig, new V2TIMCallback() {
                @Override
                public void onError(int code, String desc) {
                    Toast.makeText(getApplication(), "????????????", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess() {
                    Toast.makeText(getApplication(), "????????????", Toast.LENGTH_SHORT).show();
                    joinRoom();
                }
            });
        }
    }

    /**
     * ?????????????????????
     */
    private void assignViews() {
        playSurface = getView(R.id.play_surface);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            MyAlertDialog myAlertDialog = new MyAlertDialog(this).builder()
                    .setTitle("??????")
                    .setMsg("??????????????????????????????")
                    .setPositiveButton("??????", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            quitRoom();
                            heartTimer.cancel();
                            heartBeatTimer.cancel();
                            finish();
                        }
                    }).setNegativeButton("??????", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //????????????
                        }
                    });
            myAlertDialog.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void initPresenter() {
        mPresenter.initPresenter(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void exit() {
        finish();
    }

    @Override
    public NodePlayerView getNodePlayerView() {
        return playSurface;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        addMsgListener();
    }


}
