package cn.nodemedia.qlive.utils.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebHistoryItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import cn.nodemedia.qlive.R;
import cn.nodemedia.qlive.utils.ImgUtils;

public class GoodView extends LinearLayout {
    private ImageView goodAvatar;
    private TextView goodsName;
    private TextView goodsPrice;
    private ImageView goodsShop;
    private int goodsId;

    public void setShopVis() {
        goodsShop.setVisibility(View.INVISIBLE);
    }

    public ImageView getGoodAvatar() {
        return goodAvatar;
    }

    public TextView getGoodName() {
        return goodsName;
    }

    public void setGoodName(TextView goodsName) {
        this.goodsName = goodsName;
    }

    public TextView getGoodPrice() {
        return goodsPrice;
    }

    public void setGoodPrice(TextView goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public LinearLayout getGoodView() {
        return goodsView;
    }

    public void setGoodView(LinearLayout goodsView) {
        this.goodsView = goodsView;
    }

    private LinearLayout goodsView;

    public GoodView(Context context) {
        super(context);
        init();
    }

    public GoodView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GoodView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GoodView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.view_good, this, true);
        findAllViews();
    }

    private void findAllViews() {
        goodAvatar = findViewById(R.id.goods_avatar);
        goodsName = findViewById(R.id.goods_name);
        goodsPrice = findViewById(R.id.goods_price);
        goodsView=findViewById(R.id.goods_view);
        goodsShop=findViewById(R.id.goods_shop);
        goodsShop.setOnClickListener(clickListener);
    }

    public void setGoodAvatar(String url) {
        ImgUtils.load(url,goodAvatar);
    }

    @SuppressLint("ResourceAsColor")
    public void setGoodName(String name) {
        goodsName.setText(name);
        goodsName.setTextColor(R.color.navigationBarColor);
    }

    @SuppressLint("ResourceAsColor")
    public void setGoodPrice(String price) {
        goodsPrice.setText("RMB "+price);
        goodsPrice.setTextColor(R.color.navigationBarColor);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.goods_shop) {
                // 跳转到购物界面
                if (mOnGoodListener != null) {
                    mOnGoodListener.onGoodClick();
                }
            }

        }
    };


    private GoodView.OnGoodListener mOnGoodListener;

    public void setOnGoodListener(GoodView.OnGoodListener l) {
        mOnGoodListener = l;
    }

    public interface OnGoodListener {
        public void onGoodClick();
    }
    
}
