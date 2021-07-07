package cn.nodemedia.qlive.entity;


public class GoodsInfo {
    private Long goods_id;                            //商品id，主键
    private Long vendor_id;                             //发布该商品的商家id,是user表中userId的外键
    private String name;                               //商品名
    private String price;                          //商品价格
    private Long sold_count;                            //商品已售个数
    private Long max_quantity;                           //商品库存数量
    private String photo_path;                          //商品照片存放路径
    private String spec_path;                           //商品参数存放路径
    private String delivery_type;                        //商品支持的配送方式
    private String display_type;                        //商品展示方式
    private Long subject_id;                         //商品所属类别Id
    private Long subject_parentId;                    //商品所属父类别Id，若无父类别则为空
    private Long view_count;                            //商品浏览次数
    private String status;

    public Long getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Long goods_id) {
        this.goods_id = goods_id;
    }

    public Long getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(Long vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getSold_count() {
        return sold_count;
    }

    public void setSold_count(Long sold_count) {
        this.sold_count = sold_count;
    }

    public Long getMax_quantity() {
        return max_quantity;
    }

    public void setMax_quantity(Long max_quantity) {
        this.max_quantity = max_quantity;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getSpec_path() {
        return spec_path;
    }

    public void setSpec_path(String spec_path) {
        this.spec_path = spec_path;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getDisplay_type() {
        return display_type;
    }

    public void setDisplay_type(String display_type) {
        this.display_type = display_type;
    }

    public Long getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(Long subject_id) {
        this.subject_id = subject_id;
    }

    public Long getSubject_parentId() {
        return subject_parentId;
    }

    public void setSubject_parentId(Long subject_parentId) {
        this.subject_parentId = subject_parentId;
    }

    public Long getView_count() {
        return view_count;
    }

    public void setView_count(Long view_count) {
        this.view_count = view_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "goods_id=" + goods_id +
                ", vendor_id=" + vendor_id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", sold_count=" + sold_count +
                ", max_quantity=" + max_quantity +
                ", photo_path='" + photo_path + '\'' +
                ", spec_path='" + spec_path + '\'' +
                ", delivery_type='" + delivery_type + '\'' +
                ", display_type='" + display_type + '\'' +
                ", subject_id=" + subject_id +
                ", subject_parentId=" + subject_parentId +
                ", view_count=" + view_count +
                ", status='" + status + '\'' +
                '}';
    }
}
