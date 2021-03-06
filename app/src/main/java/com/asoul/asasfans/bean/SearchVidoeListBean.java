package com.asoul.asasfans.bean;

import java.lang.reflect.Array;
import java.util.List;

public class SearchVidoeListBean {

    private Object cost_time;
    private Object exp_list;
    private Object pageinfo;
    private int  egg_hit;
    private int  numPages;
    private int  numResults;
    private int  page;
    private int  pagesize;
    private List<SearchVideoBean> result;
    private  String rqt_type;
    private  String seid;
    private  int  show_column;
    private  String suggest_keyword;




    public Object getCost_time() {
        return cost_time;
    }

    public void setCost_time(Object cost_time) {
        this.cost_time = cost_time;
    }

    public Object getExp_list() {
        return exp_list;
    }

    public void setExp_list(Object exp_list) {
        this.exp_list = exp_list;
    }

    public Object getPageinfo() {
        return pageinfo;
    }

    public void setPageinfo(Object pageinfo) {
        this.pageinfo = pageinfo;
    }

    public int getEgg_hit() {
        return egg_hit;
    }

    public void setEgg_hit(int egg_hit) {
        this.egg_hit = egg_hit;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public int getNumResults() {
        return numResults;
    }

    public void setNumResults(int numResults) {
        this.numResults = numResults;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public List<SearchVideoBean> getResult() {
        return result;
    }

    public void setResult(List<SearchVideoBean> result) {
        this.result = result;
    }

    public String getRqt_type() {
        return rqt_type;
    }

    public void setRqt_type(String rqt_type) {
        this.rqt_type = rqt_type;
    }

    public String getSeid() {
        return seid;
    }

    public void setSeid(String seid) {
        this.seid = seid;
    }

    public int getShow_column() {
        return show_column;
    }

    public void setShow_column(int show_column) {
        this.show_column = show_column;
    }

    public String getSuggest_keyword() {
        return suggest_keyword;
    }

    public void setSuggest_keyword(String suggest_keyword) {
        this.suggest_keyword = suggest_keyword;
    }


}
