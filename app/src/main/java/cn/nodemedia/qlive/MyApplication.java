package cn.nodemedia.qlive;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.nodemedia.qlive.entity.UserInfo;
import cn.nodemedia.qlive.utils.QnUploadHelper;
import cn.nodemedia.qlive.widget.HintDialog;
import xyz.tanwb.airship.App;
import xyz.tanwb.airship.BaseApplication;
import xyz.tanwb.airship.rxjava.RxBus;
import xyz.tanwb.airship.utils.Log;
import xyz.tanwb.airship.utils.NetUtils;

public class MyApplication extends BaseApplication {

    private ConcurrentHashMap<String, Dialog> dialogs;
    private BroadcastReceiver broadcastReceiver;
    private static Context appContext;
    private static MyApplication app;

    private UserInfo mSelfProfile;

    @Override
    public void onCreate() {
        super.onCreate();
        initQn();
        app = this;
        appContext=getApplicationContext();
        // 1. 从 IM 控制台获取应用 SDKAppID，详情请参考 SDKAppID。
        // 2. 初始化 config 对象
        V2TIMSDKConfig config = new V2TIMSDKConfig();
        // 3. 指定 log 输出级别，详情请参考 SDKConfig。
        config.setLogLevel(V2TIMSDKConfig.V2TIM_LOG_INFO);
        // 4. 初始化 SDK 并设置 V2TIMSDKListener 的监听对象。
        // initSDK 后 SDK 会自动连接网络，网络连接状态可以在 V2TIMSDKListener 回调里面监听。
        V2TIMManager.getInstance().initSDK(this, 1400514968, config, new V2TIMSDKListener() {
            // 5. 监听 V2TIMSDKListener 回调
            @Override
            public void onConnecting() {
                // 正在连接到腾讯云服务器
                Toast.makeText(getContext(),"正在连接到腾讯云服务器",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onConnectSuccess() {
                // 已经成功连接到腾讯云服务器
                Toast.makeText(getContext(),"已经成功连接到腾讯云服务器",Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onConnectFailed(int code, String error) {
                // 连接腾讯云服务器失败


            }
        });

    }


    private void initQn() {
        QnUploadHelper.init("C1eaBALkpd3h2K8gY6Y4VHMMrdGV6ZPcESEA_vPK",
                "qi4ro7gtUgKa03vcWXS-XKF1xPysKrGu0hNxsAKR","http://qsz313e9w.hn-bkt.clouddn.com/","xuwenhaolive");
    }

    public static Context getContext() {
        return appContext;
    }

    /**
     * 注册网络监听广播
     */
    private void receiveNetwork() {
        if (broadcastReceiver != null) {
            return;
        }
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("My intent.getAction():" + intent.getAction());
                switch (intent.getAction()) {
                    case ConnectivityManager.CONNECTIVITY_ACTION:
                        boolean isconnect = NetUtils.isConnected();
                        if (isconnect) {
                            // BaseMQTTService.startAndConnect(context);
                        }
                        managerNetworkDialog(NetUtils.isConnected());
                        break;
                    case Intent.ACTION_TIME_TICK:
                        RxBus.getInstance().post(Intent.ACTION_TIME_TICK);
                        break;
                    case Intent.ACTION_SCREEN_OFF:
                        break;
                    case Intent.ACTION_SCREEN_ON:
                        break;
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    Log.e("NetworkCallback onAvailable:" + NetUtils.isConnected());
                    // BaseMQTTService.startAndConnect(getApplicationContext());
                    managerNetworkDialog(true);
                }

                @Override
                public void onLost(Network network) {
                    super.onLost(network);
                    Log.e("NetworkCallback onLost:" + NetUtils.isConnected());
                    managerNetworkDialog(false);
                }
            });
        } else {
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);// 网络改变广播,每分钟发出一次
        }
        intentFilter.addAction(Intent.ACTION_TIME_TICK);// 时间改变广播,每分钟发出一次
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);// 手机熄屏广播
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);// 手机亮屏广播
        registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * 管理网络提示弹框
     */
    private void managerNetworkDialog(boolean isConnect) {
        if (isConnect) {
            if (dialogs != null && dialogs.size() > 0) {
                for (Map.Entry<String, Dialog> dialog : dialogs.entrySet()) {
                    if (dialog.getValue().isShowing()) {
                        dialog.getValue().dismiss();
                    }
                }
                dialogs.clear();
            }
        } else {
            if (dialogs == null) {
                dialogs = new ConcurrentHashMap<>();
            }
            Activity activity = getLastActivity();
            if (activity != null && !dialogs.containsKey(activity.getClass().getName())) {
                Dialog dialog = new HintDialog.Builder(activity).
                        setTitle("网络提示").
                        setMessage("糟糕，您与地球网络暂时失联啦！请重新建立连接。").
                        setCancelable(false).
                        setLeftButton("退出使用").
                        setRightButton("设置网络").
                        setOnClickListener(new HintDialog.OnClickListener() {
                            @Override
                            public void onClick(HintDialog.Builder builder, View view, int position, Object param) {
                                dialogs.remove(builder.getDialog());
                                switch (position) {
                                    case HintDialog.LEFT:
                                        exit();
                                        break;
                                    case HintDialog.RIGHT:
                                        App.openNetSetting(getApplicationContext());
                                        break;
                                }
                            }
                        }).show();
                dialogs.put(activity.getClass().getName(), dialog);
            }
        }
    }

    @Override
    public void exit() {
//        if (rxBusManage != null) {
//            rxBusManage.clear();
//            rxBusManage = null;
//        }
        super.exit();
    }

    public static MyApplication getApplication() {
        return app;
    }

    public void setSelfProfile(UserInfo userProfile) {
        mSelfProfile = userProfile;
    }

    public UserInfo getSelfProfile() {
        return mSelfProfile;
    }


}
