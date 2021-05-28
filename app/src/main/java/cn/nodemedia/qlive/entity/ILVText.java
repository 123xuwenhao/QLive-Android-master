package cn.nodemedia.qlive.entity;

import com.tencent.imsdk.TIMMessagePriority;

public class ILVText<Self extends ILVText<Self>> {

    public enum ILVTextType{
        eGroupMsg,      // 群组消息
        eC2CMsg         // 点对点(C2C)消息
    }

    String text = "";
    String streamId = "";
    ILVTextType type = ILVTextType.eC2CMsg;
    TIMMessagePriority priority = TIMMessagePriority.Normal;

    public ILVText(){}

    public ILVText(ILVTextType type, String streamId, String text) {
        this.text = text;
        this.streamId = streamId;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public Self setText(String text) {
        this.text = text;
        return (Self)this;
    }

    public String getStreamId() {
        return streamId;
    }

    public Self setStreamId(String streamId) {
        this.streamId = streamId;
        return (Self)this;
    }

    /**
     * 设置消息类型
     */
    public ILVTextType getType() {
        return type;
    }

    public Self setType(ILVTextType type) {
        this.type = type;
        return (Self)this;
    }

    /**
     * 设置消息优先级
     */
    public Self setPriority(TIMMessagePriority priority){
        this.priority = priority;
        return (Self)this;
    }

    public TIMMessagePriority getPriority() {
        return priority;
    }
}
