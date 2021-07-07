package cn.nodemedia.qlive.utils.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nodemedia.qlive.R;
import cn.nodemedia.qlive.entity.RoomInfo;
import cn.nodemedia.qlive.entity.UserInfo;
import cn.nodemedia.qlive.utils.BaseRequest;
import cn.nodemedia.qlive.utils.ImgUtils;
import cn.nodemedia.qlive.view.MyRequest.GetLiveRoomRequest;
import cn.nodemedia.qlive.view.MyRequest.GetUserInfoRequest;
import cn.nodemedia.qlive.widget.UserInfoDialog;

public class TitleView extends LinearLayout {

    private ImageView hostAvatarImgView;//主播头像
    private TextView watchersNumView;//观看人数。
    private int watcherNum = 0;

    private RecyclerView watcherListView;//观众列表
    private WatcherAdapter watcherAdapter;

    private String hostId; //主播id

    public TitleView(Context context) {
        super(context);
        init();
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater.from(getContext()).inflate(R.layout.view_title, this, true);
        findAllViews();
        initWatcherNum();
    }
    private void initWatcherNum() {
        GetLiveRoomRequest getLiveRoomRequest = new GetLiveRoomRequest();
        GetLiveRoomRequest.getLiveRoomParam liveRoomParam = new GetLiveRoomRequest.getLiveRoomParam();
        liveRoomParam.streamId = hostId;
        getLiveRoomRequest.setOnResultListener(new BaseRequest.OnResultListener<RoomInfo>() {
            @Override
            public void onFail(int code, String msg) {
                Toast.makeText(TitleView.this.getContext(), "请求房间信息失败:" + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(RoomInfo roomInfo) {
                watcherNum = roomInfo.watcher_nums < 0 ? 0 : roomInfo.watcher_nums;
            }
        });
        getLiveRoomRequest.request(liveRoomParam);
    }



    private void findAllViews() {
        hostAvatarImgView = findViewById(R.id.host_avatar);
        watchersNumView = findViewById(R.id.watchers_num);
        hostAvatarImgView.setOnClickListener(v -> {
            //TODO 点击头像，显示详情对话框
            showUserInfoDialog(hostId);
        });

        watcherListView = findViewById(R.id.watch_list);
        watcherListView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        watcherListView.setLayoutManager(layoutManager);
        //设置adapter
        watcherAdapter = new WatcherAdapter(getContext());
        watcherListView.setAdapter(watcherAdapter);
    }



    private void showUserInfoDialog(String senderId) {
        List<String> ids = new ArrayList<String>();
        ids.add(senderId);
        GetUserInfoRequest getUserInfoRequest=new GetUserInfoRequest();
        GetUserInfoRequest.getUserParam param=new GetUserInfoRequest.getUserParam();
        param.userIdList=ids.toString();
        getUserInfoRequest.setOnResultListener(new BaseRequest.OnResultListener<List<UserInfo>>() {
            @Override
            public void onFail(int code, String msg) {
                Toast.makeText(TitleView.this.getContext(), "请求用户信息失败:"+msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(List<UserInfo> userInfoList) {
                Context context = TitleView.this.getContext();
                if(context instanceof Activity) {
                    UserInfoDialog userInfoDialog = new UserInfoDialog((Activity) context, userInfoList.get(0));
                    userInfoDialog.show();
                }
            }
        });
        getUserInfoRequest.request(param);
    }

    public void setHost(UserInfo userProfile) {
        if(userProfile == null){
            ImgUtils.loadRound(R.drawable.default_avatar, hostAvatarImgView);
        }else {
            hostId = userProfile.getUser_id()+"";
            String avatarUrl = userProfile.getUser_avatar();
            if (TextUtils.isEmpty(avatarUrl)) {
                ImgUtils.loadRound(R.drawable.default_avatar, hostAvatarImgView);
            } else {
                ImgUtils.loadRound(avatarUrl, hostAvatarImgView);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void addWatcher(UserInfo userProfile) {
        if (userProfile != null) {
            watcherAdapter.addWatcher(userProfile);
            watcherNum++;
            watchersNumView.setText("观众:" + watcherNum);
        }
    }

    @SuppressLint("SetTextI18n")
    public void addWatchers(List<UserInfo> userProfileList){
        if(userProfileList != null){
            watcherAdapter.addWatchers(userProfileList);
            watcherNum+= userProfileList.size();
            watchersNumView.setText("观众:" + watcherNum);
        }
    }

    public void removeWatcher(UserInfo userProfile) {
        if (userProfile != null) {
            watcherAdapter.removeWatcher(userProfile);
            watcherNum--;
            watchersNumView.setText("观众:" +  (Math.max(watcherNum, 0)));
        }
    }


    public class WatcherAdapter extends RecyclerView.Adapter {

        private Context mContext;
        private List<UserInfo> watcherList = new ArrayList<UserInfo>();
        private Map<String , UserInfo> watcherMap = new HashMap<String , UserInfo>();


        WatcherAdapter(Context context) {
            mContext = context;
        }

        public void addWatchers(List<UserInfo> userProfileList){
            if(userProfileList == null){
                return;
            }

            for(UserInfo userProfile : userProfileList){
                if (userProfile != null) {
                    boolean inWatcher = watcherMap.containsKey(userProfile.getUser_id()+"");
                    if(!inWatcher) {
                        watcherList.add(userProfile);
                        watcherMap.put(userProfile.getUser_id()+"", userProfile);
                    }
                }
            }

            notifyDataSetChanged();
        }

        public void addWatcher(UserInfo userProfile) {
            if (userProfile != null) {
                boolean inWatcher = watcherMap.containsKey(userProfile.getUser_id()+"");
                if(!inWatcher) {
                    watcherList.add(userProfile);
                    watcherMap.put(userProfile.getUser_id()+"", userProfile);
                    notifyDataSetChanged();
                }
            }
        }

        public void removeWatcher(UserInfo userProfile) {
            if (userProfile == null) {
                return;
            }

            UserInfo targetUser = watcherMap.get(userProfile.getUser_id()+"");
            if(targetUser != null) {
                watcherList.remove(targetUser);
                watcherMap.remove(targetUser.getUser_id()+"");
                notifyDataSetChanged();
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_watcher, parent, false);
            return new WatcherHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof WatcherHolder) {
                ((WatcherHolder) holder).bindData(watcherList.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return watcherList.size();
        }

        private class WatcherHolder extends RecyclerView.ViewHolder {

            private ImageView avatarImg;

            public WatcherHolder(View itemView) {
                super(itemView);
                avatarImg = itemView.findViewById(R.id.user_avatar);
            }

            public void bindData(final UserInfo userProfile) {
                String avatarUrl = userProfile.getUser_avatar();
                if (TextUtils.isEmpty(avatarUrl)) {
                    ImgUtils.loadRound(R.drawable.default_avatar, avatarImg);
                } else {
                    ImgUtils.loadRound(avatarUrl, avatarImg);
                }
                avatarImg.setOnClickListener(v -> showUserInfoDialog(userProfile.getUser_id()+""));
            }
        }
    }

}

