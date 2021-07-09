package cn.nodemedia.qlive.view.MyRequest;

import androidx.annotation.NonNull;

import java.io.IOException;

import cn.nodemedia.qlive.entity.GetSingleUserInfoResponseObj;
import cn.nodemedia.qlive.entity.ResponseObj;
import cn.nodemedia.qlive.utils.BaseRequest;

public class DeleteSelectedGoodsRequest extends BaseRequest {
    public static final String host = "http://47.99.171.180:8080/zhibo/servlet/RoomServlet?action=deleteSelectedGoods&";
    private static final String RequestParamKey_StreamId = "streamId";

    public static class deleteSelectedGoodsParam {
        public String streamId;

        @NonNull
        @Override
        public String toString() {
            return RequestParamKey_StreamId+"="+streamId;
        }
    }

    public void request(deleteSelectedGoodsParam param) {
        String url = host + param.toString();
        request(url);
    }


    @Override
    protected void onFail(IOException e) {
        sendFailMsg(-100, e.getMessage());
    }


    @Override
    protected void onResponseFail(int code) {
        sendFailMsg(code, "服务出现异常");
    }

    @Override
    protected void onResponseSuccess(String body) {
        ResponseObj responseObject = gson.fromJson(body, ResponseObj.class);
        if (responseObject == null) {
            sendFailMsg(-101, "数据格式错误");
            return;
        }


        if (responseObject.code.equals(ResponseObj.CODE_SUC)) {
            sendSuccMsg("");
        } else if (responseObject.code.equals(ResponseObj.CODE_FAIL)) {
            sendFailMsg(Integer.parseInt(responseObject.errCode), responseObject.errMsg);
        }
    }

}

