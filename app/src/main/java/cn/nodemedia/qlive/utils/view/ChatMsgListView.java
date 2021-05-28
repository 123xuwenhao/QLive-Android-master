package cn.nodemedia.qlive.utils.view;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.nodemedia.qlive.R;
import cn.nodemedia.qlive.entity.ChatMsgInfo;
import cn.nodemedia.qlive.entity.UserInfo;
import cn.nodemedia.qlive.utils.BaseRequest;
import cn.nodemedia.qlive.utils.ImgUtils;
import cn.nodemedia.qlive.view.MyRequest.GetUserInfoRequest;
import cn.nodemedia.qlive.widget.UserInfoDialog;

public class ChatMsgListView extends RelativeLayout {

    private ListView mChatMsgListView;
    private ChatMsgAdapter mChatMsgAdapter;

    public ChatMsgListView(Context context) {
        super(context);
        init();
    }

    public ChatMsgListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatMsgListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_chat_msg_list, this, true);

        findAllViews();
    }

    private void findAllViews() {
        mChatMsgListView = findViewById(R.id.chat_msg_list);
        mChatMsgAdapter = new ChatMsgAdapter();
        mChatMsgListView.setAdapter(mChatMsgAdapter);
        mChatMsgListView.setOnItemClickListener((parent, view, position, id) -> {
            ChatMsgInfo msgInfo = mChatMsgAdapter.getItem(position);
            showUserInfoDialog(msgInfo.getSenderId());
        });
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

            }

            @Override
            public void onSuccess(List<UserInfo> userInfoList) {
                Context context = ChatMsgListView.this.getContext();
                if(context instanceof Activity) {
                    UserInfoDialog userInfoDialog = new UserInfoDialog((Activity) context, userInfoList.get(0));
                    userInfoDialog.show();
                }

            }
        });
        getUserInfoRequest.request(param);

    }

    public void addMsgInfo(ChatMsgInfo info) {
        if (info != null) {
            mChatMsgAdapter.addMsgInfo(info);
            mChatMsgListView.smoothScrollToPosition(mChatMsgAdapter.getCount());
        }
    }

    public void addMsgInfos(List<ChatMsgInfo> infos) {
        if (infos != null) {
            mChatMsgAdapter.addMsgInfos(infos);
            mChatMsgListView.smoothScrollToPosition(mChatMsgAdapter.getCount());
        }
    }

    private class ChatMsgAdapter extends BaseAdapter {

        private List<ChatMsgInfo> mChatMsgInfos = new ArrayList<ChatMsgInfo>();

        public void addMsgInfo(ChatMsgInfo info) {
            if (info != null) {
                mChatMsgInfos.add(info);
                notifyDataSetChanged();
            }
        }

        public void addMsgInfos(List<ChatMsgInfo> infos) {
            if (infos != null) {
                mChatMsgInfos.addAll(infos);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return mChatMsgInfos.size();
        }

        @Override
        public ChatMsgInfo getItem(int i) {
            return mChatMsgInfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ChatMsgHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.view_chat_msg_list_item, null);
                holder = new ChatMsgHolder(view);
                view.setTag(holder);
            } else {
                holder = (ChatMsgHolder) view.getTag();
            }

            holder.bindData(mChatMsgInfos.get(i));

            return view;
        }
    }

    private class ChatMsgHolder {

        private ImageView avatar;
        private TextView content;

        private ChatMsgInfo chatMsgInfo;

        public ChatMsgHolder(View itemView) {

            avatar = itemView.findViewById(R.id.sender_avatar);
            content = itemView.findViewById(R.id.chat_content);
        }

        public void bindData(ChatMsgInfo info) {
            chatMsgInfo = info;

            String avatarUrl = info.getAvatar();
            if (TextUtils.isEmpty(avatarUrl)) {
                ImgUtils.loadRound(R.drawable.default_avatar, avatar);
            } else {
                ImgUtils.loadRound(avatarUrl, avatar);
            }
            content.setText(info.getContent());
        }
    }

}

