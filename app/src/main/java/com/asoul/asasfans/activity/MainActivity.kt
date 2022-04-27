package com.asoul.asasfans.activity


import android.content.Intent
import android.net.Uri
import androidx.annotation.IntRange
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.asoul.asasfans.R
import com.asoul.asasfans.bean.GithubVersionBean
import com.asoul.asasfans.databinding.MainDataBinding
import com.asoul.asasfans.fragment.FanArtFragment
import com.asoul.asasfans.fragment.RecordFragment
import com.asoul.asasfans.fragment.ToolsFragment
import com.asoul.asasfans.fragment.VideoFragment
import com.asoul.asasfans.utils.localVersionCode
import com.asoul.asasfans.utils.toVersionCode
import com.asoul.asasfans.viewmodel.MainViewModel
import com.fairhr.module_support.KtxActivityManger
import com.fairhr.module_support.base.MvvmActivity
import com.fairhr.module_support.utils.SPreferenceUtils
import com.fairhr.module_support.utils.ToastUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : MvvmActivity<MainDataBinding, MainViewModel>() {


    var clickBackTime = 0L
    var navHostFragment: NavHostFragment? = null
    var navController: NavController? = null

    val fragments = ArrayList<Fragment>()
    var lastShowFragment = 0

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
        initFragments()
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
        //navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_nav) as NavHostFragment
        //navController = navHostFragment!!.navController
        //bn_nav.setupWithNavController(navController!!)
        mDataBinding.bnNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.video_fragment -> switchFragment(0)
                R.id.fan_art_fragment -> switchFragment(1)
                R.id.record_fragment -> switchFragment(2)
                R.id.tools_fragment -> switchFragment(3)
            }
            true
        }
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
            .setPositiveButton("去下载") { _, _ ->
                val uri = Uri.parse("https://app.asf.ink/")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
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

    private fun switchFragment(
        @IntRange(from = 0, to = 3) index: Int
    ) {
        val transaction = supportFragmentManager.beginTransaction()
        fragments.forEachIndexed { position, fragment ->
            if (position != index) {
                if (fragment.isAdded)
                    transaction.hide(fragment)
            }
        }
        if (!fragments[index].isAdded) {
            transaction.add(R.id.fcv_nav, fragments[index])
        }
        transaction.show(fragments[index]).commit()
    }

    private fun initFragments() {
        fragments.add(VideoFragment())
        fragments.add(FanArtFragment())
        fragments.add(RecordFragment())
        fragments.add(ToolsFragment())
        supportFragmentManager.beginTransaction().apply {
            if (!fragments[0].isAdded) {
                add(R.id.fcv_nav, fragments[0])
            }
            replace(R.id.fcv_nav, fragments[0])
            commit()
        }
    }
}