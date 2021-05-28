package cn.nodemedia.qlive.entity;

public class ILVCustomCmd extends ILVText<ILVCustomCmd>{
    private int cmd=-1;
    private UserInfo userProfile;
    public ILVCustomCmd(){};

    public ILVCustomCmd(ILVTextType type, String streamId, String text, int cmd, UserInfo userProfile ) {
        super(type, streamId, text);
        this.cmd = cmd;
        this.userProfile=userProfile;
    }
    public String getStreamId(){
        return super.getStreamId();
    }

    public ILVCustomCmd setStreamId(String streamId){
        super.setStreamId(streamId);
        return this;
    }

    public UserInfo getUserProfile() {
        return userProfile;
    }

    public ILVCustomCmd setUserProfile(UserInfo userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public int getCmd() {
        return cmd;
    }

    public ILVCustomCmd setCmd(int cmd) {
        this.cmd = cmd;
        return this;
    }

    public String getParam(){
        return super.getText();
    }

    public ILVCustomCmd setParam(String param){
        super.setText(param);
        return this;
    }
}
