package com.asoul.asasfans.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.asoul.asasfans.R
import com.asoul.asasfans.utils.localVersion
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
        val versionText = "v$localVersion"
        version.text = versionText
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
}