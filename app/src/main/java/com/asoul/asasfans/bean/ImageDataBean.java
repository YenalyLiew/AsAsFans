package com.asoul.asasfans.bean;

import java.util.List;
import java.util.Objects;

/**
 * @author: akari
 * @date: 2022/3/8
 * @description $
 */
public class ImageDataBean {

    /**
     * dy_id : 634858202928775172
     * pic_url : [{"img_src":"https://i0.hdslb.com/bfs/album/c3fb2e2b34d752803ee9f7115fa5e9ad5d9a2f91.jpg","img_size":790,"img_tags":null,"img_width":1080,"img_height":1920},{"img_src":"https://i0.hdslb.com/bfs/album/c6b4493317ab00bd346ea12efae151445b44496b.jpg","img_size":947.6699829101562,"img_tags":null,"img_width":1080,"img_height":1920}]
     * uid : 33631964
     * name : Diana牌白颜料
     * face : http://i2.hdslb.com/bfs/face/adb8e03a32cd3f88fe2a2555065b545750c081ed.jpg
     */

    private String dy_id;
    private int uid;
    private String name;
    private String face;
    private List<PicUrlBean> pic_url;

    public String getDy_id() {
        return dy_id;
    }

    public void setDy_id(String dy_id) {
        this.dy_id = dy_id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public List<PicUrlBean> getPic_url() {
        return pic_url;
    }

    public void setPic_url(List<PicUrlBean> pic_url) {
        this.pic_url = pic_url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageDataBean that = (ImageDataBean) o;
        return uid == that.uid && Objects.equals(dy_id, that.dy_id) && Objects.equals(name, that.name) && Objects.equals(face, that.face) && Objects.equals(pic_url, that.pic_url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dy_id, uid, name, face, pic_url);
    }

    public static class PicUrlBean {
        /**
         * img_src : https://i0.hdslb.com/bfs/album/c3fb2e2b34d752803ee9f7115fa5e9ad5d9a2f91.jpg
         * img_size : 790
         * img_tags : null
         * img_width : 1080
         * img_height : 1920
         */

        private String img_src;
        private double img_size;
        private Object img_tags;
        private double img_width;
        private double img_height;

        public String getImg_src() {
            return img_src;
        }

        public void setImg_src(String img_src) {
            this.img_src = img_src;
        }

        public double getImg_size() {
            return img_size;
        }

        public void setImg_size(double img_size) {
            this.img_size = img_size;
        }

        public Object getImg_tags() {
            return img_tags;
        }

        public void setImg_tags(Object img_tags) {
            this.img_tags = img_tags;
        }

        public double getImg_width() {
            return img_width;
        }

        public void setImg_width(double img_width) {
            this.img_width = img_width;
        }

        public double getImg_height() {
            return img_height;
        }

        public void setImg_height(double img_height) {
            this.img_height = img_height;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PicUrlBean that = (PicUrlBean) o;
            return Double.compare(that.img_size, img_size) == 0 && Double.compare(that.img_width, img_width) == 0 && Double.compare(that.img_height, img_height) == 0 && Objects.equals(img_src, that.img_src) && Objects.equals(img_tags, that.img_tags);
        }

        @Override
        public int hashCode() {
            return Objects.hash(img_src, img_size, img_tags, img_width, img_height);
        }
    }
}
