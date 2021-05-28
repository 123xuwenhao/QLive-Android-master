package cn.nodemedia.qlive.utils;

import android.app.Application;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import cn.nodemedia.qlive.MyApplication;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class ImgUtils {
   public  static  void load(int resId, ImageView targetImg){
      Glide.with(MyApplication.getContext())
              .load(resId)
              .into(targetImg);
   }
   public  static  void load(String url, ImageView targetImg){
      Glide.with(MyApplication.getContext())
              .load(url)
              .into(targetImg);
   }
   public static void loadRound(String url, ImageView targetView) {
      Glide.with(MyApplication.getContext())
              .load(url)
              .apply(RequestOptions.bitmapTransform(new CircleCrop()))
              .into(targetView);
   }

   public static void loadRound(int resId, ImageView targetView) {
      Glide.with(MyApplication.getContext())
              .load(resId)
              .apply(RequestOptions.bitmapTransform(new CircleCrop()))
              .into(targetView);
   }
}
