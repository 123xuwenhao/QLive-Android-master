package cn.nodemedia.qlive.utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;


import java.util.LinkedList;
import java.util.List;

import cn.nodemedia.qlive.R;
import cn.nodemedia.qlive.entity.GiftInfo;
import cn.nodemedia.qlive.entity.UserInfo;

public class GiftRepeatView extends LinearLayout {

    private GiftRepeatItemView item0, item1;

    private class GiftSenderAndInfo {
        public GiftInfo giftInfo;
        public String repeatId;
        public UserInfo senderProfile;
    }

    private List<GiftSenderAndInfo> giftSenderAndInfoList = new LinkedList<GiftSenderAndInfo>();

    public GiftRepeatView(Context context) {
        super(context);
        init();
    }

    public GiftRepeatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GiftRepeatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_gift_repeat, this, true);
        findAllViews();
        item0.setVisibility(INVISIBLE);
        item1.setVisibility(INVISIBLE);
    }

    private void findAllViews() {
        item0 = findViewById(R.id.item0);
        item1 = findViewById(R.id.item1);

        item0.setOnGiftItemAvaliableListener(avaliableListener);
        item1.setOnGiftItemAvaliableListener(avaliableListener);
    }

    private GiftRepeatItemView.OnGiftItemAvaliableListener avaliableListener = () -> {
        if (giftSenderAndInfoList.size() > 0) {
            GiftSenderAndInfo info = giftSenderAndInfoList.remove(0);
            showGift(info.giftInfo, info.repeatId, info.senderProfile);
        }
    };

    public void showGift(GiftInfo giftInfo, String repeatId, UserInfo profile) {
        GiftRepeatItemView avaliableView = getAvaliableView(giftInfo, repeatId, profile);
        if (avaliableView == null) {
            GiftSenderAndInfo info = new GiftSenderAndInfo();
            info.giftInfo = giftInfo;
            info.senderProfile = profile;
            info.repeatId = repeatId;
            giftSenderAndInfoList.add(info);
        } else {
            avaliableView.showGift(giftInfo, repeatId, profile);
        }
    }

    private GiftRepeatItemView getAvaliableView(GiftInfo giftInfo, String repeatId, UserInfo profile) {

        if (item0.isAvaliable(giftInfo, repeatId, profile)) {
            return item0;
        }

        if (item1.isAvaliable(giftInfo, repeatId, profile)) {
            return item1;
        }

        if (item0.getVisibility() == INVISIBLE) {
            return item0;
        }
        if (item1.getVisibility() == INVISIBLE) {
            return item1;
        }
        return null;
    }
}
