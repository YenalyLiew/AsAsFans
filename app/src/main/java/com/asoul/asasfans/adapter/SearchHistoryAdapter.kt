package com.asoul.asasfans.adapter


import android.widget.ImageView
import android.widget.TextView
import com.asoul.asasfans.R
import com.asoul.asasfans.bean.LoadMoreBean
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


class SearchHistoryAdapter: BaseMultiItemQuickAdapter<LoadMoreBean, BaseViewHolder>() {

    init {
        addItemType(LoadMoreBean.LOADMORE, R.layout.item_adapter_loadmore)
        addItemType(LoadMoreBean.CANCELMORE, R.layout.item_adapter_closeload)
        addItemType(LoadMoreBean.nomal, R.layout.item_adapter_searchhistory)
    }



    override fun convert(holder: BaseViewHolder, item: LoadMoreBean) {



        when (holder.itemViewType){
            LoadMoreBean.LOADMORE ->{

            }
            LoadMoreBean.CANCELMORE ->{

            }
            LoadMoreBean.nomal ->{
                val question : TextView = holder.getView(R.id.tv_searchhistory_content)
                question.text = item.data
            }
        }

    }


}