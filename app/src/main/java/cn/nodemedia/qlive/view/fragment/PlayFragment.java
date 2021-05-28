package cn.nodemedia.qlive.view.fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;


import cn.nodemedia.qlive.MyApplication;
import cn.nodemedia.qlive.R;
import cn.nodemedia.qlive.adapter.MyAdapter;
import cn.nodemedia.qlive.entity.LiveView;
import cn.nodemedia.qlive.entity.UserInfo;
import cn.nodemedia.qlive.utils.BaseRequest;
import cn.nodemedia.qlive.view.MyRequest.GetSingleUserInfoRequest;
import cn.nodemedia.qlive.view.PlayActivity;


public class PlayFragment extends Fragment{

    private Fragment mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pref_play, null);
        GetSingleUserInfoRequest getSingleUserInfoRequest=new GetSingleUserInfoRequest();
        GetSingleUserInfoRequest.getUserParam param=new GetSingleUserInfoRequest.getUserParam();
        param.userId="1235";
        getSingleUserInfoRequest.setOnResultListener(new BaseRequest.OnResultListener<UserInfo>() {
            @Override
            public void onFail(int code, String msg) {

            }

            @Override
            public void onSuccess(UserInfo userInfo) {
                MyApplication.getApplication().setSelfProfile(userInfo);

            }
        });
        getSingleUserInfoRequest.request(param);
        Intent intent=new Intent(getActivity(), PlayActivity.class);
        intent.putExtra("userId",1235);
        intent.putExtra("hostId",123);
        intent.putExtra("userName","123");
        intent.putExtra("userAvatar","http://qsz313e9w.hn-bkt.clouddn.com/1621552044871_avatar_crop.jpg");
        intent.putExtra("streamId","yb123");
        Button play_btn=view.findViewById(R.id.play_btn);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
