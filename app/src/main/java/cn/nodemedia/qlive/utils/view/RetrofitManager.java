package cn.nodemedia.qlive.utils.view;

import java.io.IOException;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static Retrofit retrofit = null;
    private static String url = "";

    public static Retrofit getInstance(String baseUrl) {
        url = baseUrl;
        if (retrofit == null) {
            return create();
        } else {
            return retrofit;
        }
    }

    public static Retrofit create() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder.addInterceptor(logging);
//        httpClientBuilder.addInterceptor(chain -> {
//            Request mRequest = chain.request().newBuilder()
//                    .header("Content-Type","multipart/form-data")
//                    .build();
//            return chain.proceed(mRequest);
//        });
        //       创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)//设置网络请求url，后面一段写在网络请求接口里面
                .addConverterFactory(GsonConverterFactory.create())//添加Gson支持，然后Retrofit就会使用Gson将响应体（api接口的Take）转换我们想要的类型。
                .client(httpClientBuilder.build())
                .build();
        return retrofit;

    }
}
