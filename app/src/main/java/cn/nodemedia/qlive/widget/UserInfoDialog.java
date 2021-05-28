package cn.nodemedia.qlive.widget;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

import cn.nodemedia.qlive.R;
import cn.nodemedia.qlive.entity.UserInfo;
import cn.nodemedia.qlive.utils.ImgUtils;

public class UserInfoDialog extends TransParentDialog {
    private UserInfo userInfo;

    private ImageView user_close;
    private ImageView user_avatar;
    private TextView user_name;
    private ImageView user_gender;
    private TextView user_level;
    private TextView user_id;
    private TextView user_renzhen;
    private TextView user_sign;
    private TextView user_songchu;
    private TextView user_bopiao;

    public UserInfoDialog(Activity activity, UserInfo userInfo) {
        super(activity);
        this.userInfo = userInfo;

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_userinfo, null);
        setContentView(view);

        findAllViews(view);
        bindDataToViews();

        setWidthAndHeight(activity.getWindow().getDecorView().getWidth() * 80 / 100, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    private void findAllViews(View view) {
        user_close = view.findViewById(R.id.user_close);
        user_avatar = view.findViewById(R.id.user_avatar);
        user_name = view.findViewById(R.id.user_name);
        user_gender = view.findViewById(R.id.user_gender);
        user_level = view.findViewById(R.id.user_level);
        user_id = view.findViewById(R.id.user_id);
        user_renzhen = view.findViewById(R.id.user_renzhen);
        user_sign = view.findViewById(R.id.user_sign);
        user_songchu = view.findViewById(R.id.user_songchu);
        user_bopiao = view.findViewById(R.id.user_bopiao);

        user_close.setOnClickListener(clickListener);
        user_avatar.setOnClickListener(clickListener);
    }


    private void bindDataToViews() {

        String avatarUrl = userInfo.getUser_avatar();
        if (TextUtils.isEmpty(avatarUrl)) {
            ImgUtils.loadRound(R.drawable.default_avatar, user_avatar);
        } else {
            ImgUtils.loadRound(avatarUrl, user_avatar);
        }

        String nickName = userInfo.getUser_name();
        if(TextUtils.isEmpty(nickName)){
            nickName = "用户";
        }
        user_name.setText(nickName);
        long genderValue = userInfo.getUser_gender();
        user_gender.setImageResource(genderValue == 1 ? R.drawable.ic_male : R.drawable.ic_female);

        user_id.setText("ID：" + userInfo.getUser_id());
        String sign = userInfo.getUser_sign();
        user_sign.setText(TextUtils.isEmpty(sign) ? "Ta好像忘记写签名了..." : sign);

        user_renzhen.setText(userInfo.getUser_renzheng());
        int sendNum = userInfo.getUser_songchu();
        user_songchu.setText("送出：" + formatLargNum(sendNum));
        int getNum = userInfo.getUser_bopiao();
        user_bopiao.setText("播票：" + formatLargNum(getNum));
        int level = userInfo.getUser_level();
        user_level.setText(level+"");
    }

    private String getValue(Map<String, byte[]> customInfo, String key, String defaultValue) {
        if (customInfo != null) {
            byte[] valueBytes = customInfo.get(key);
            if (valueBytes != null) {
                return new String(valueBytes);
            }
        }
        return defaultValue;
    }

    private String formatLargNum(int num) {
        float wan = num * 1.0f / 10000;
        if (wan < 1) {
            return "" + num;
        } else {
            return new java.text.DecimalFormat("#.00").format(wan) + "万";
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == user_close) {
                hideDialog();
            }
        }
    };

    private void hideDialog() {
        hide();
    }

}
