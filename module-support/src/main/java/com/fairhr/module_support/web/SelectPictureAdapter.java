package com.fairhr.module_support.web;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fairhr.module_support.R;
import com.fairhr.module_support.bean.CommonCheckedBean;


public class SelectPictureAdapter extends BaseQuickAdapter<CommonCheckedBean<String>, BaseViewHolder> {


    public SelectPictureAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommonCheckedBean<String> item) {
        TextView itemText = helper.getView(R.id.tv_select_item);
        itemText.setText(item.data);
    }
}
