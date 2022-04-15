package com.asoul.asasfans.bean;

public class VideoBean {

    private Long aid;
    private String  bvid;
    private int  coin;
    private int  copyright;
    private int  danmaku;
    private String  desc;
    private String  duration;
    private String  face;
    private String  favorite;
    private String  like;
    private String  mid;
    private String  name;
    private String  pic;
    private String  pubdate;
    private int  reply;
    private int  score;
    private int  share;
    private String  tag;
    private int  tid;
    private String  title;
    private String  tname;
    private int  view;

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public String getBvid() {
        return bvid;
    }

    public void setBvid(String bvid) {
        this.bvid = bvid;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getCopyright() {
        return copyright;
    }

    public void setCopyright(int copyright) {
        this.copyright = copyright;
    }

    public int getDanmaku() {
        return danmaku;
    }

    public void setDanmaku(int danmaku) {
        this.danmaku = danmaku;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }


    @Override
    public String toString() {
        return "VideoBean{" +
                "bvid='" + bvid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
