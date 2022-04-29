package com.asoul.asasfans.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.asoul.asasfans.R
import com.asoul.asasfans.bean.SearchVideoBean
import com.asoul.asasfans.utils.dp
import com.asoul.asasfans.utils.setMargin
import com.asoul.asasfans_concept.utils.toViewsCase
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fairhr.module_support.utils.ContextUtil
import com.fairhr.module_support.utils.GlideUtils

class SearchVideoListAdapter :
    BaseQuickAdapter<SearchVideoBean, BaseViewHolder>(R.layout.item_video) {


    override fun convert(holder: BaseViewHolder, item: SearchVideoBean) {

        val pic: ImageView = holder.getView(R.id.video_cover)
        val time: TextView = holder.getView(R.id.video_duration)
        val title: TextView = holder.getView(R.id.video_title)
        val up: TextView = holder.getView(R.id.video_up)
        val views: TextView = holder.getView(R.id.video_views)
        val comments: TextView = holder.getView(R.id.video_comments)
        val partition: TextView = holder.getView(R.id.video_partition)

        title.setMargin(0, 0, 4.dp, 0)

        time.text = item.duration

        var tl = item.title.replace("<em class=\"keyword\">", "")
        tl = tl.replace("</em>", "")

        title.text = tl
        up.text = item.author
        views.text = item.play.toViewsCase()
        partition.text = item.typename
        comments.text = item.like.toViewsCase()

        GlideUtils.loadToImageView(ContextUtil.getContext(), "http:" + item.pic, pic)

        holder.getView<View>(R.id.video_more_button).visibility = View.GONE

    }


}