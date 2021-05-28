package cn.nodemedia.qlive.entity;

public class UserInfo {
    public String  user_avatar;
    public String user_name;
    public int user_gender;
    public int user_level;
    public int user_id;
    public String  user_renzheng;
    public String user_sign;
    public int user_songchu;
    public int user_bopiao;
    public String stream_id;

    public UserInfo(String user_avatar, String user_name, int user_gender, int user_level, int user_id, String user_renzheng,
                String user_sign, int user_songchu, int user_bopiao,String stream_id) {
        this.user_avatar = user_avatar;
        this.user_name = user_name;
        this.user_gender = user_gender;
        this.user_level = user_level;
        this.user_id = user_id;
        this.user_renzheng = user_renzheng;
        this.user_sign = user_sign;
        this.user_songchu = user_songchu;
        this.user_bopiao = user_bopiao;
        this.stream_id=stream_id;
    }


    public UserInfo() {

    }


    public String getStream_id() {
        return stream_id;
    }
    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }
    public String getUser_avatar() {
        return user_avatar;
    }
    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public int getUser_gender() {
        return user_gender;
    }
    public void setUser_gender(int user_gender) {
        this.user_gender = user_gender;
    }
    public int getUser_level() {
        return user_level;
    }
    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public String getUser_renzheng() {
        return user_renzheng;
    }
    public void setUser_renzheng(String user_renzheng) {
        this.user_renzheng = user_renzheng;
    }
    public String getUser_sign() {
        return user_sign;
    }
    public void setUser_sign(String user_sign) {
        this.user_sign = user_sign;
    }
    public int getUser_songchu() {
        return user_songchu;
    }
    public void setUser_songchu(int user_songchu) {
        this.user_songchu = user_songchu;
    }
    public int getUser_bopiao() {
        return user_bopiao;
    }
    public void setUser_bopiao(int user_bopiao) {
        this.user_bopiao = user_bopiao;
    }


}
