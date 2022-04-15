package com.asoul.asasfans.adapter

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.asoul.asasfans.R
import com.asoul.asasfans.bean.ImageDataBean
import com.asoul.asasfans.utils.dp
import com.asoul.asasfans.utils.screenWidth
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @ProjectName : AsAsFans
 * @Author : Yenaly Liew
 * @Time : 2022/04/14 014 22:18
 * @Description : Description...
 */
class FanArtAdapter(private val fragment: Fragment) :
    BaseQuickAdapter<ImageDataBean, BaseViewHolder>(R.layout.item_fan_art) {

    private lateinit var fanArt: ImageView
    private lateinit var fanArtUp: TextView
    private lateinit var fanArtNum: TextView

    private val glideOptions = RequestOptions()
        .placeholder(R.drawable.loading)
        .error(R.drawable.load_failure)
        .diskCacheStrategy(DiskCacheStrategy.NONE)

    override fun convert(holder: BaseViewHolder, item: ImageDataBean) {

        fanArt = holder.getView(R.id.fan_art)
        fanArtUp = holder.getView(R.id.fan_art_up)
        fanArtNum = holder.getView(R.id.fan_art_num)

        fanArtUp.text = item.name
        fanArtNum.text = item.pic_url.size.toString()
        val firstFanArtUrl = item.pic_url[0]
        val layoutParams = fanArt.layoutParams as LinearLayout.LayoutParams
        val imageWidth: Double
        val imageHeight: Double
        if (firstFanArtUrl != null) {
            if (firstFanArtUrl.img_width >= 480) {
                imageWidth = 480.0
                imageHeight = imageWidth * firstFanArtUrl.img_height / firstFanArtUrl.img_width
            } else {
                imageWidth = firstFanArtUrl.img_width
                imageHeight = firstFanArtUrl.img_height
            }

            // 瀑布流自适应高度设置
            val itemWidth = screenWidth / 2 - 8.dp * 4
            layoutParams.width = itemWidth
            val scale = itemWidth / imageWidth
            layoutParams.height = (imageHeight * scale).toInt()
            fanArt.layoutParams = layoutParams

            val firstFanArtThumbnail =
                "${firstFanArtUrl.img_src}@${imageWidth.toInt()}w_${imageHeight.toInt()}h_1e_1c.jpg"
            Glide.with(fragment).load(firstFanArtThumbnail)
                .apply(glideOptions)
                .override(layoutParams.width, layoutParams.height)
                .into(fanArt)
        }
    }
}