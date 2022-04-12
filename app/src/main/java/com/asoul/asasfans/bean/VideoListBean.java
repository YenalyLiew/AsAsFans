package com.asoul.asasfans.bean;


import java.util.List;

public class VideoListBean {

    private int page;
    private int numResults;
    private List<VideoBean>  result;

    public List<VideoBean> getResult() {
        return result;
    }

    public void setResult(List<VideoBean> result) {
        this.result = result;
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getNumResults() {
        return numResults;
    }

    public void setNumResults(int numResults) {
        this.numResults = numResults;
    }





}
