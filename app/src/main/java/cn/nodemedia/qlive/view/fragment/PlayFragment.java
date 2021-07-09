package cn.nodemedia.qlive.view.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import cn.nodemedia.qlive.MyApplication;
import cn.nodemedia.qlive.R;
import cn.nodemedia.qlive.entity.RoomInfo;
import cn.nodemedia.qlive.entity.UserInfo;
import cn.nodemedia.qlive.utils.BaseRequest;
import cn.nodemedia.qlive.view.MyRequest.GetLiveRoomRequest;
import cn.nodemedia.qlive.view.MyRequest.GetSingleUserInfoRequest;
import cn.nodemedia.qlive.view.PlayActivity;


public class PlayFragment extends Fragment {

    private Fragment mContext;
   private static final String  YB ="yb";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.pref_play, null);
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
        //用户Id
        intent.putExtra("userId",1235);
        //主播id
        intent.putExtra("hostId",123);
        //用户昵称
        intent.putExtra("userName","123");
        //用户头像
        intent.putExtra("userAvatar","http://qsz313e9w.hn-bkt.clouddn.com/1621552044871_avatar_crop.jpg");
        //房间Id（yb+主播id  组成）
        intent.putExtra("streamId","yb123");
        Button play_btn=view.findViewById(R.id.play_btn);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                checkLiveRoom(intent);
                startActivity(intent);
            }
        });
        return view;
    }

    private void checkLiveRoom(Intent intent) {
        GetLiveRoomRequest getLiveRoomRequest=new GetLiveRoomRequest();
        GetLiveRoomRequest.getLiveRoomParam param=new GetLiveRoomRequest.getLiveRoomParam();
        param.streamId=YB+"123";
        getLiveRoomRequest.setOnResultListener(new BaseRequest.OnResultListener<RoomInfo>() {
            @Override
            public void onFail(int code, String msg) {
                Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onSuccess(RoomInfo room) {
                startActivity(intent);

            }
        });
        getLiveRoomRequest.request(param);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
