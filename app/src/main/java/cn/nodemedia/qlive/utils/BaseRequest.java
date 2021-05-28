package cn.nodemedia.qlive.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.textclassifier.ConversationActions;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tencent.openqq.protocol.imsdk.msg;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.nodemedia.qlive.entity.ResponseObj;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class BaseRequest {

    protected final int WHAT_FAIL = 0;
    protected final int WHAT_SUCC = 1;

    private final static OkHttpClient okClient = new OkHttpClient();
    protected final static Gson gson = new Gson();

    public interface OnResultListener<T> {
        void onFail(int code, String msg);

        void onSuccess(T object);
    }

    private OnResultListener listener;

    private Handler uiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == WHAT_FAIL) {
                if (listener != null) {
                    listener.onFail(msg.arg1, (String) msg.obj);
                }
            } else if (what == WHAT_SUCC) {
                if (listener != null) {
                    listener.onSuccess(msg.obj);
                }
            }
        }
    };

    protected void sendFailMsg(int code, String reason) {
        Message msg = uiHandler.obtainMessage(WHAT_FAIL);
        msg.arg1 = code;
        msg.obj = reason;
        uiHandler.sendMessage(msg);
    }

    protected <T> void sendSuccMsg(T data) {
        Message msg = uiHandler.obtainMessage(WHAT_SUCC);
        msg.obj = data;
        uiHandler.sendMessage(msg);
    }

    public void setOnResultListener(OnResultListener l) {
        listener = l;
    }

    public void request(String url) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        okClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //不是UI线程
                if (response.isSuccessful()) {
                    onResponseSuccess(response.body().string());
                } else {
                    onResponseFail(response.code());
                }
            }
        });
    }

    protected abstract void onFail(IOException e);

    protected abstract void onResponseFail(int code);

    protected abstract void onResponseSuccess(String body);

}
