package cn.nodemedia.qlive.view.MyRequest;

import java.io.IOException;

import cn.nodemedia.qlive.utils.BaseRequest;

public class HeartBeatRequest extends BaseRequest {
    private static final String Action = "http://47.99.171.180:8080/zhibo/servlet/RoomServlet?action=heartBeat";

    private static final String RequestParamKey_StreamId = "streamId";
    private static final String RequestParamKey_UserId = "userId";

    public static class getHeartBeatParam {
        public String  userId;
        public String streamId;

        @Override
        public String toString() {
            return RequestParamKey_StreamId + "=" + streamId+"&"+RequestParamKey_UserId+"="+userId;
        }
    }

    public void request(HeartBeatRequest.getHeartBeatParam param) {
        String url = Action + param.toString();
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

    }
}
