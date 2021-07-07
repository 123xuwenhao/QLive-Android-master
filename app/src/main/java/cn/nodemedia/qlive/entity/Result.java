package cn.nodemedia.qlive.entity;

import com.google.gson.annotations.SerializedName;

public class Result {

    /**
     * status
     */
    @SerializedName("status")
    private String status;
    /**
     * errCode
     */
    @SerializedName("errCode")
    private String errCode;
    /**
     * errMsg
     */
    @SerializedName("errMsg")
    private String errMsg;
    /**
     * data
     */
    @SerializedName("data")
    private String data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
