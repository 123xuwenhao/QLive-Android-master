package cn.nodemedia.qlive.view.MyRequest;

import java.io.IOException;

import cn.nodemedia.qlive.entity.GetGoodsInfoResponseObj;
import cn.nodemedia.qlive.entity.ResponseObj;
import cn.nodemedia.qlive.utils.BaseRequest;

public class GetGoodsInfoRequest extends BaseRequest {
    public static final String host = "http://47.99.171.180:8080/zhibo/servlet/RoomServlet?action=getGoods&";
    private static final String RequestParamKey_GoodsIdList = "goodsIdList";

    public static class getGoodsParam {
        public String  goodsIdList;

        @Override
        public String toString() {
            return RequestParamKey_GoodsIdList + "=" + goodsIdList;
        }
    }

    public void request(getGoodsParam param) {
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
        GetGoodsInfoResponseObj responseObject = gson.fromJson(body, GetGoodsInfoResponseObj.class);
        if (responseObject == null) {
            sendFailMsg(-101, "数据格式错误");
            return;
        }


        if (responseObject.code.equals(ResponseObj.CODE_SUC)) {
            sendSuccMsg(responseObject.data);
        } else if (responseObject.code.equals(ResponseObj.CODE_FAIL)) {
            sendFailMsg(Integer.valueOf(responseObject.errCode), responseObject.errMsg);
        }
    }

}

