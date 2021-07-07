package cn.nodemedia.qlive.view.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tencent.imsdk.v2.V2TIMSDKConfig;
//import com.tencent.qcloud.tim.uikit.TUIKit;
//import com.tencent.qcloud.tim.uikit.config.CustomFaceConfig;
//import com.tencent.qcloud.tim.uikit.config.GeneralConfig;
//import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
//import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;

import cn.nodemedia.qlive.R;

/**
 * Created by aliang on 2017/12/4.
 */

public class AboutFragment extends Fragment implements View.OnClickListener {
    public static final int SDKAPPID = 1400514968; // 您的 SDKAppID
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //         配置 Config，请按需配置
//        TUIKitConfigs configs = TUIKit.getConfigs();
//        configs.setSdkConfig(new V2TIMSDKConfig());
//        configs.setCustomFaceConfig(new CustomFaceConfig());
//        configs.setGeneralConfig(new GeneralConfig());
//        TUIKit.init(getContext(), SDKAPPID, configs);
////        // 从布局文件中获取会话列表面板
//        ConversationLayout conversationLayout = getActivity().findViewById(R.id.conversation_layout);
////        // 初始化聊天面板
//        conversationLayout.initDefault();

    }

    @Override
    public void onClick(View view) {

    }
}
