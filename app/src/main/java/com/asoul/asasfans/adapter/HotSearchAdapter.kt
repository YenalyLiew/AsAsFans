package com.asoul.asasfans.adapter

import android.widget.TextView
import com.asoul.asasfans.R
import com.asoul.asasfans.bean.VideoBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class HotSearchAdapter: BaseQuickAdapter<VideoBean, BaseViewHolder>(R.layout.item_adapter_hotsearch) {



    override fun convert(holder: BaseViewHolder, item: VideoBean) {

        val hotsearch : TextView = holder.getView(R.id.tv_hotsearch_content)
        hotsearch.text = item.title


    }

}