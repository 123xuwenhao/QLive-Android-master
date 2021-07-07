package cn.nodemedia.qlive.contract;

import java.util.Map;
import cn.nodemedia.qlive.entity.Result;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface APIPost {
    @FormUrlEncoded
    @POST("v4/im_open_login_svc/account_check")
    Call<Result> request(@Field("file") byte [] image,@Field("path") String path);
}