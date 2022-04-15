package com.asoul.asasfans.activity


import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.asoul.asasfans.bean.GithubVersionBean
import com.asoul.asasfans.utils.localVersionCode
import com.asoul.asasfans.utils.toVersionCode
import com.asoul.asasfans.viewmodel.MainViewModel
import com.asoul.asasfans.R
import com.asoul.asasfans.databinding.MainDataBinding
import com.fairhr.module_support.KtxActivityManger
import com.fairhr.module_support.base.MvvmActivity
import com.fairhr.module_support.utils.SPreferenceUtils
import com.fairhr.module_support.utils.SystemStatusUtil
import com.fairhr.module_support.utils.ToastUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvvmActivity<MainDataBinding, MainViewModel>() {


    var clickBackTime = 0L
    var navHostFragment: NavHostFragment? = null
    var navController: NavController? = null

    override fun isWindowFullMode(): Boolean {
        return false
    }

    override fun isStatusIconDarkMode(): Boolean {
        return false
    }

    override fun initContentView(): Int {
        return R.layout.activity_main
    }

    override fun initViewModel(): MainViewModel? {
        return createViewModel(this, MainViewModel::class.java)
    }

    override fun initDataBindingVariable() {}


    override fun initView() {
        super.initView()

        initData()
        initBottomNav()
    }

    override fun onBackPressed() {
        doubleClickToExitApp()
    }


    private fun initData() {
        mViewModel.getVersion()

    }

    private fun initBottomNav() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_nav) as NavHostFragment
        navController = navHostFragment!!.navController
        bn_nav.setupWithNavController(navController!!)
    }


    override fun registerLiveDataObserve() {
        super.registerLiveDataObserve()
        mViewModel.getmVersion().observe(this, Observer<GithubVersionBean> { GithubVersionBean ->
            if (GithubVersionBean != null) {

                val latestVersion = GithubVersionBean.name.substringAfter('v')
                if (latestVersion.toVersionCode() > localVersionCode) {
                    dialogShow(GithubVersionBean)
                }
            }
        })
    }

    private fun dialogShow(bean: GithubVersionBean) {
        MaterialAlertDialogBuilder(this@MainActivity)
            .setTitle("新版本提醒")
            .setMessage(bean.body)
            .setPositiveButton("去下载", null)
            .setNegativeButton("忽略", null)
            .show()
    }


    /**
     * 双击退出APP
     */
    private fun doubleClickToExitApp() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - clickBackTime > 2000) {
            ToastUtils.showNomal("再按一次退出程序")
            clickBackTime = currentTime
        } else {
            KtxActivityManger.exitAppWithoutKillingProcess()
        }
    }

    override fun setTheme() {
        val theme = SPreferenceUtils.readInt(this, "theme", R.style.Theme_AsAsFans_Ava)
        setTheme(theme)
    }
}