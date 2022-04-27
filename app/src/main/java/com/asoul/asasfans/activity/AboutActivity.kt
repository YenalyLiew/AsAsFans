package com.asoul.asasfans.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.asoul.asasfans.R
import com.asoul.asasfans.utils.localVersion
import com.bumptech.glide.Glide
import com.drakeet.about.AbsAboutActivity
import com.drakeet.about.Card
import com.drakeet.about.Category
import com.drakeet.about.Contributor
import com.fairhr.module_support.utils.SPreferenceUtils

/**
 * @ProjectName : AsAsFans
 * @Author : Yenaly Liew
 * @Time : 2022/04/13 013 23:26
 * @Description : Description...
 */
class AboutActivity : AbsAboutActivity() {

    private var npk48 = 0
    // private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        when (SPreferenceUtils.readInt(this, "theme", R.style.Theme_AsAsFans_Ava)) {
            R.style.Theme_AsAsFans_Ava -> setTheme(R.style.Theme_AsAsFans_About_Ava)
            R.style.Theme_AsAsFans_Bella -> setTheme(R.style.Theme_AsAsFans_About_Bella)
            R.style.Theme_AsAsFans_Carol -> setTheme(R.style.Theme_AsAsFans_About_Carol)
            R.style.Theme_AsAsFans_Diana -> setTheme(R.style.Theme_AsAsFans_About_Diana)
            R.style.Theme_AsAsFans_Eileen -> setTheme(R.style.Theme_AsAsFans_About_Eileen)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateHeader(icon: ImageView, slogan: TextView, version: TextView) {
        icon.setImageResource(R.drawable.icon_asasf_colorful_logo)
        slogan.setText(R.string.app_name)
        val versionText = "version: $localVersion"
        version.text = versionText

        icon.setOnClickListener {
            npk48++
            if (npk48 == 10) {
                Glide.with(this).load(R.drawable.diana_scratch).into(icon)
                val whatHappenedText = "怎么会是呢？"
                slogan.text = whatHappenedText
            }
            if (npk48 == 20) {
                Glide.with(this).load(R.drawable.san_pi_he_yi).into(icon)
                val niuText = "??????"
                val attentionText = "???"
                slogan.text = niuText
                version.text = attentionText
                // mediaPlayer = MediaPlayer.create(this, R.raw.yong_gan_niu_niu)
                // mediaPlayer.start()
            }
        }
    }

    override fun onItemsCreated(items: MutableList<Any>) {
        items.add(Category(getString(R.string.introduce)))
        items.add(Card(getString(R.string.asasfans_slogan)))

        items.add(Category(getString(R.string.developer)))
        items.add(
            Contributor(
                R.drawable.akari,
                "Akari",
                getString(R.string.developer),
                "https://github.com/akarinini"
            )
        )
        items.add(
            Contributor(
                R.drawable.frozen,
                "Frozen",
                getString(R.string.developer),
                "https://github.com/Frozeniceking"
            )
        )
        items.add(
            Contributor(
                R.drawable.yenaly_liew,
                "Yenaly Liew",
                getString(R.string.developer),
                "https://github.com/YenalyLiew"
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        // if (::mediaPlayer.isInitialized) {
        //     mediaPlayer.release()
        // }
    }
}