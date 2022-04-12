package com.asoul.asasfans.adapter

import android.widget.ImageView
import android.widget.TextView
import com.asoul.asasfans.bean.VideoBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.asoul.asasfans.R
import com.fairhr.module_support.utils.ContextUtil
import com.fairhr.module_support.utils.GlideUtils

class VideoListAdapter : BaseQuickAdapter<VideoBean, BaseViewHolder>(R.layout.item_video) {



    override fun convert(holder: BaseViewHolder, item: VideoBean) {

        var pic: ImageView = holder.getView(R.id.video_cover)

        var time: TextView = holder.getView(R.id.video_duration)

        var title: TextView = holder.getView(R.id.video_title)

        var up: TextView = holder.getView(R.id.video_up)

        var views: TextView = holder.getView(R.id.video_views)

        var comments: TextView = holder.getView(R.id.video_comments)

        var partition: TextView = holder.getView(R.id.video_partition)


        GlideUtils.loadToImageView(ContextUtil.getContext(),item.pic,pic)
        time.text = item.duration.toInt().toDurationCase()
        title.text = item.title
        up.text = item.name
        views.text = item.view.toString()
        partition.text = item.tname
        comments.text = item.favorite


    }

    /**
     * 把一个分钟数转化为时间格式。
     *
     * 例如：123 -> 02:03
     *
     * @author Yenaly Liew
     */
    private fun Int.toDurationCase(): String {
        val second: Int = this % 60
        var minute: Int = this / 60
        var hour = 0
        if (minute >= 60) {
            hour = minute / 60
            minute %= 60
        }
        val secondString = if (second < 10) "0$second" else second.toString()
        val minuteString = if (minute < 10) "0$minute" else minute.toString()
        val hourString = if (hour < 10) "0$hour" else hour.toString()
        return if (hour != 0) "$hourString:$minuteString:$secondString" else "$minuteString:$secondString"
    }


}