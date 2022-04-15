package com.asoul.asasfans.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.asoul.asasfans.R
import com.asoul.asasfans.databinding.SettingsDataBinding
import com.asoul.asasfans.fragment.SettingsFragment
import com.asoul.asasfans.viewmodel.SettingsViewModel
import com.fairhr.module_support.base.MvvmActivity
import com.fairhr.module_support.utils.SPreferenceUtils

/**
 * @ProjectName : AsAsFans
 * @Author : Yenaly Liew
 * @Time : 2022/04/12 012 23:18
 * @Description : Description...
 */
class SettingsActivity : MvvmActivity<SettingsDataBinding, SettingsViewModel>() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar = findViewById(R.id.settings_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
    }

    override fun isWindowFullMode(): Boolean {
        return false
    }

    override fun isStatusIconDarkMode(): Boolean {
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun initContentView(): Int {
        return R.layout.activity_settings
    }

    override fun initViewModel(): SettingsViewModel {
        return createViewModel(this, SettingsViewModel::class.java)
    }

    override fun initDataBindingVariable() {
    }

    override fun setTheme() {
        val theme = SPreferenceUtils.readInt(this, "theme", R.style.Theme_AsAsFans_Ava)
        setTheme(theme)
    }
}