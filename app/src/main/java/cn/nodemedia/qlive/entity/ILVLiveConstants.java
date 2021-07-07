package cn.nodemedia.qlive.entity;

public class ILVLiveConstants {

    public static final int ILVLIVE_CMD_NONE = 0x700;         //无效消息

    public static final int ILVLIVE_CMD_ENTER = ILVLIVE_CMD_NONE + 1;                                 //用户加入直播, Group消息
    public static final int ILVLIVE_CMD_LEAVE = ILVLIVE_CMD_ENTER + 1;                           //用户退出直播, Group消息
    public static final int ILVLIVE_CMD_INVITE = ILVLIVE_CMD_LEAVE + 1;                           //邀请上麦，C2C消息
    public static final int ILVLIVE_CMD_INVITE_CANCEL = ILVLIVE_CMD_INVITE + 1;                       //取消邀请上麦，C2C消息
    public static final int ILVLIVE_CMD_INVITE_CLOSE = ILVLIVE_CMD_INVITE_CANCEL + 1;                    //关闭上麦，C2C消息
    public static final int ILVLIVE_CMD_INTERACT_AGREE = ILVLIVE_CMD_INVITE_CLOSE + 1;                  //同意上麦，C2C消息
    public static final int ILVLIVE_CMD_INTERACT_REJECT = ILVLIVE_CMD_INTERACT_AGREE + 1;                       //拒绝上麦，C2C消息

    /**
     * 用户自定义消息段
     */
    public static final int ILVLIVE_CMD_CUSTOM_LOW_LIMIT = 0x800;          //自定义消息段下限
    public static final int ILVLIVE_CMD_CUSTOM_UP_LIMIT = 0x900;          //自定义消息段上线

    /** 信令专用标识 */
    //public static final String TCEXT_MAGIC          = "LiveNotification";

    //主播选择商品消息
    public static final int ILVLIVE_CMD_SELECTED_GOODS = ILVLIVE_CMD_CUSTOM_LOW_LIMIT + 2;

    /**
     * 命令字
     */
    public static final String CMD_KEY = "userAction";
    /**
     * 命令参数
     */
    public static final String CMD_PARAM = "actionParam";

}

