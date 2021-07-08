package cn.nodemedia.qlive.view.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.util.ArrayList;

import cn.nodemedia.qlive.MyApplication;
import cn.nodemedia.qlive.R;

import cn.nodemedia.qlive.entity.UserInfo;
import cn.nodemedia.qlive.utils.BaseRequest;
import cn.nodemedia.qlive.view.CreateRoomActivity;
import cn.nodemedia.qlive.view.MyRequest.GetSingleUserInfoRequest;


public class PushFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.pref_push, null);
        GetSingleUserInfoRequest getSingleUserInfoRequest = new GetSingleUserInfoRequest();
        GetSingleUserInfoRequest.getUserParam param = new GetSingleUserInfoRequest.getUserParam();
        param.userId = "123";
        getSingleUserInfoRequest.setOnResultListener(new BaseRequest.OnResultListener<UserInfo>() {
            @Override
            public void onFail(int code, String msg) {
                Toast.makeText(getActivity(), "获取用户信息失败！" + msg, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(UserInfo userInfo) {
                Toast.makeText(getActivity(), "获取用户信息成功！", Toast.LENGTH_SHORT).show();
                MyApplication.getApplication().setSelfProfile(userInfo);

            }
        });
        getSingleUserInfoRequest.request(param);
        ArrayList<Integer> goods=new ArrayList<>();
        goods.add(1);
        goods.add(2);
        goods.add(3);
        Intent intent = new Intent(getActivity(), CreateRoomActivity.class);
        intent.putIntegerArrayListExtra("selectedGoods",goods);
        intent.putExtra("userId", 123);
        intent.putExtra("userName", "哈哈哈浩");
        intent.putExtra("userAvatar", "http://qsz313e9w.hn-bkt.clouddn.com/image/user.png");
        Button push_btn = view.findViewById(R.id.push_btn);
        push_btn.setOnClickListener(v -> startActivity(intent));
        return view;
    }
}