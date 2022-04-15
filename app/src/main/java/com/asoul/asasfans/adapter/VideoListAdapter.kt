package com.asoul.asasfans.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.asoul.asasfans.R
import com.asoul.asasfans.bean.VideoBean
import com.asoul.asasfans_concept.utils.toDurationCase
import com.asoul.asasfans_concept.utils.toViewsCase
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class VideoListAdapter(private val fragment: Fragment) :
    BaseQuickAdapter<VideoBean, BaseViewHolder>(R.layout.item_video) {

    private lateinit var pic: ImageView
    private lateinit var time: TextView
    private lateinit var title: TextView
    private lateinit var up: TextView
    private lateinit var views: TextView
    private lateinit var comments: TextView
    private lateinit var partition: TextView

    private val glideOptions = RequestOptions()
        .placeholder(R.drawable.loading)
        .error(R.drawable.load_failure)
        .diskCacheStrategy(DiskCacheStrategy.NONE)

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        pic = viewHolder.getView(R.id.video_cover)
        time = viewHolder.getView(R.id.video_duration)
        title = viewHolder.getView(R.id.video_title)
        up = viewHolder.getView(R.id.video_up)
        views = viewHolder.getView(R.id.video_views)
        comments = viewHolder.getView(R.id.video_comments)
        partition = viewHolder.getView(R.id.video_partition)
    }

    override fun convert(holder: BaseViewHolder, item: VideoBean) {

        time.text = item.duration.toInt().toDurationCase()
        title.text = item.title
        up.text = item.name
        views.text = item.view.toViewsCase()
        partition.text = item.tname
        comments.text = item.favorite.toInt().toViewsCase()
        Glide.with(fragment).load(item.pic)
            .apply(glideOptions)
            .into(pic)

        when (item.name) {
            "嘉然今天吃什么" -> {
                up.paint.isFakeBoldText = true
                up.setTextColor(
                    ContextCompat.getColor(
                        fragment.requireContext(),
                        R.color.diana_pink_200
                    )
                )
            }
            "向晚大魔王" -> {
                up.paint.isFakeBoldText = true
                up.setTextColor(
                    ContextCompat.getColor(
                        fragment.requireContext(),
                        R.color.ava_blue_500
                    )
                )
            }
            "珈乐Carol" -> {
                up.paint.isFakeBoldText = true
                up.setTextColor(
                    ContextCompat.getColor(
                        fragment.requireContext(),
                        R.color.carol_purple_200
                    )
                )
            }
            "乃琳Queen" -> {
                up.paint.isFakeBoldText = true
                up.setTextColor(
                    ContextCompat.getColor(
                        fragment.requireContext(),
                        R.color.eileen_blue_200
                    )
                )
            }
            "贝拉kira" -> {
                up.paint.isFakeBoldText = true
                up.setTextColor(
                    ContextCompat.getColor(
                        fragment.requireContext(),
                        R.color.bella_orange_200
                    )
                )
            }
            else -> {
                up.paint.isFakeBoldText = false
                up.setTextColor(
                    ContextCompat.getColor(
                        fragment.requireContext(),
                        R.color.text_color
                    )
                )
            }
        }

    }
}