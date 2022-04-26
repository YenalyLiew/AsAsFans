package com.asoul.asasfans.adapter

import android.widget.ImageView
import android.widget.TextView
import com.asoul.asasfans.R
import com.asoul.asasfans.bean.SearchVideoBean
import com.asoul.asasfans_concept.utils.toDurationCase
import com.asoul.asasfans_concept.utils.toViewsCase
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fairhr.module_support.utils.ContextUtil
import com.fairhr.module_support.utils.GlideUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SearchVidoeListAdapter : BaseQuickAdapter<SearchVideoBean, BaseViewHolder>(R.layout.item_video) {




    override fun convert(holder: BaseViewHolder, item: SearchVideoBean) {

        val  pic: ImageView = holder.getView(R.id.video_cover)
        val  time: TextView = holder.getView(R.id.video_duration)
        val  title: TextView = holder.getView(R.id.video_title)
        val  up : TextView= holder.getView(R.id.video_up)
        val  views: TextView = holder.getView(R.id.video_views)
        val  comments: TextView = holder.getView(R.id.video_comments)
        val  partition : TextView= holder.getView(R.id.video_partition)


        time.text = item.duration

        var tl = item.title.replace("<em class=\"keyword\">","")
        tl = tl.replace("</em>","")

        title.text = tl
        up.text = item.author
        views.text = item.play.toViewsCase()
        partition.text = item.typename
        comments.text = item.like.toViewsCase()

        GlideUtils.loadToImageView(ContextUtil.getContext(),"http:"+ item.pic, pic)

    }


}