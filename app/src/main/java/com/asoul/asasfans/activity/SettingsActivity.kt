package com.asoul.asasfans.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.asoul.asasfans.R
import com.asoul.asasfans.databinding.SettingsDataBinding
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

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar = findViewById(R.id.settings_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_nav_settings) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf()
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun isWindowFullMode(): Boolean {
        return false
    }

    override fun isStatusIconDarkMode(): Boolean {
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fcv_nav_settings).navigateUp() || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val currentFragmentId = navController.currentDestination?.id
        if (currentFragmentId != null && currentFragmentId == R.id.settingsFragment) {
            when (item.itemId) {
                android.R.id.home -> finish()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
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