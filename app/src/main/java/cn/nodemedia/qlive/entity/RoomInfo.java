package cn.nodemedia.qlive.entity;

public class RoomInfo {
    public int user_id;
    public String stream_id;
    public String stream_title;
    public String stream_cover;
    public int watcher_nums;
    public String goods_selected;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getStream_title() {
        return stream_title;
    }

    public void setStream_title(String stream_title) {
        this.stream_title = stream_title;
    }

    public String getStream_cover() {
        return stream_cover;
    }

    public void setStream_cover(String stream_cover) {
        this.stream_cover = stream_cover;
    }

    public int getWatcher_nums() {
        return watcher_nums;
    }

    public void setWatcher_nums(int watcher_nums) {
        this.watcher_nums = watcher_nums;
    }

    public String getGoods_selected() {
        return goods_selected;
    }

    public void setGoods_selected(String goods_selected) {
        this.goods_selected = goods_selected;
    }

    @Override
    public String toString() {
        return "RoomInfo{" +
                "user_id=" + user_id +
                ", stream_id='" + stream_id + '\'' +
                ", stream_title='" + stream_title + '\'' +
                ", stream_cover='" + stream_cover + '\'' +
                ", watcher_nums=" + watcher_nums +
                ", goods_selected='" + goods_selected + '\'' +
                '}';
    }
}
