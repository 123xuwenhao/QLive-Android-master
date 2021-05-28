package cn.nodemedia.qlive.entity;

public class LiveView {
    private String avatarUrl;
    private String backgroundUrl;
    private String avatarName;
    private String avatarPwd;

    public LiveView() {
    }

    public LiveView(String avatarUrl, String backgroundUrl, String avatarName,String avatarPwd) {
        this.avatarUrl = avatarUrl;
        this.backgroundUrl = backgroundUrl;
        this.avatarName = avatarName;
        this.avatarPwd=avatarPwd;
    }

    public LiveView(int iv_icon_1, String 图标1) {
    }


    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public String getAvatarPwd() {
        return avatarPwd;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public  void setAvatarPwd(String avatarPwd){
        this.avatarPwd=avatarPwd;
    }
}