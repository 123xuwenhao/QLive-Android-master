package cn.nodemedia.qlive.utils.view;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.nodemedia.qlive.R;
import cn.nodemedia.qlive.entity.GoodsInfo;
import cn.nodemedia.qlive.utils.ImgUtils;

public class GoodsListView extends RelativeLayout {

    private ListView mGoodsListView;
    private GoodsAdapter mGoodsAdapter;
    private static final String urlGet = "http://47.99.171.180:8082/streaming/";

    private Button buttonItem;

    private boolean isHostItem = false;

    public GoodsListView(Context context) {
        super(context);
        init();
    }

    public GoodsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GoodsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_goods_list, this, true);

        findAllViews();
    }

    private void findAllViews() {
        mGoodsListView = findViewById(R.id.goods_list);
        mGoodsAdapter = new GoodsAdapter();
        mGoodsListView.setAdapter(mGoodsAdapter);
        mGoodsListView.setOnItemClickListener((parent, view, position, id) -> {
            GoodsInfo goodsInfo = mGoodsAdapter.getItem(position);
//            showUserInfoDialog(goodsInfo.getSenderId());
            //跳转

        });
    }

    public void addGoodsInfo(GoodsInfo info) {
        if (info != null) {
            mGoodsAdapter.addGoodsInfo(info);
            mGoodsListView.smoothScrollToPosition(mGoodsAdapter.getCount());
        }
    }

    public void addGoodsInfos(ArrayList<GoodsInfo> infos) {
        if (infos != null) {
            mGoodsAdapter.addGoodsInfos(infos);
            mGoodsListView.smoothScrollToPosition(mGoodsAdapter.getCount());
        }
    }

    public void setIsHost(boolean isHost) {
        isHostItem = isHost;
    }

    private class GoodsAdapter extends BaseAdapter {

        private ArrayList<GoodsInfo> mGoodsInfos = new ArrayList<>();

        public void addGoodsInfo(GoodsInfo info) {
            if (info != null) {
                mGoodsInfos.add(info);
                notifyDataSetChanged();
            }
        }

        public void addGoodsInfos(ArrayList<GoodsInfo> infos) {
            if (infos != null) {
                mGoodsInfos.addAll(infos);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return mGoodsInfos.size();
        }

        @Override
        public GoodsInfo getItem(int i) {
            return mGoodsInfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            GoodsHolder holder = null;

            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(isHostItem ? R.layout.view_goods_list_item_host : R.layout.view_goods_list_item, viewGroup, false);
                holder = new GoodsHolder(view);
                view.setTag(holder);
            } else {

                holder = (GoodsHolder) view.getTag();
            }

            holder.bindData(mGoodsInfos.get(i));
            buttonItem = view.findViewById(isHostItem ? R.id.goods_selected_item : R.id.goods_shop_item);
            buttonItem.setFocusable(false);
            buttonItem.setTag(i);
            buttonItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:跳转到购物页面
                    if (isHostItem) {
//                        Toast.makeText(getContext(), mGoodsInfos.get(i).getGoods_id() + "", Toast.LENGTH_SHORT).show();
//                        mGoodsListView.setVisibility(View.INVISIBLE);
                        if (mOnGoodsListener != null) {
                            mOnGoodsListener.onHostClick(i,mGoodsInfos);
                        }
                    } else {
//                        Toast.makeText(getContext(), mGoodsInfos.get(i).getGoods_id() + "", Toast.LENGTH_SHORT).show();
                        if (mOnGoodsListener != null) {
                            mOnGoodsListener.onCommonClick(i,mGoodsInfos);
                        }
                    }
                }
            });

            return view;
        }
    }

    private class GoodsHolder {

        private ImageView goodsAvatar;
        private TextView goodsName;
        private TextView goodsPrice;

        private GoodsInfo goodsInfo;

        public GoodsHolder(View itemView) {
            goodsAvatar = itemView.findViewById(R.id.goods_avatar_item);
            goodsName = itemView.findViewById(R.id.goods_name_item);
            goodsPrice = itemView.findViewById(R.id.goods_price_item);
        }

        public void bindData(GoodsInfo info) {
            goodsInfo = info;
            goodsName.setText(info.getName());
            goodsPrice.setText(info.getPrice() + "");
            String avatarUrl = info.getPhoto_path();
            if (TextUtils.isEmpty(avatarUrl)) {
                ImgUtils.loadRound(R.drawable.default_avatar, goodsAvatar);
            } else {
                ImgUtils.loadRound(urlGet + avatarUrl, goodsAvatar);
            }

        }
    }

    private GoodsListView.OnGoodsListener mOnGoodsListener;

    public void setOnControlListener(GoodsListView.OnGoodsListener l) {
        mOnGoodsListener = l;
    }

    public interface OnGoodsListener {
        public void onCommonClick(int i,ArrayList<GoodsInfo> mGoodsInfos);

        public void onHostClick(int i,ArrayList<GoodsInfo> mGoodsInfos);
    }

}

