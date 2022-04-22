package com.asoul.asasfans.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.asoul.asasfans.R
import com.asoul.asasfans.databinding.SplashDataBinding
import com.asoul.asasfans.utils.showShortToast
import com.asoul.asasfans.viewmodel.SplashViewModel
import com.fairhr.module_support.base.MvvmActivity
import com.fairhr.module_support.utils.GlideUtils
import com.fairhr.module_support.utils.SPreferenceUtils
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.fragment_fan_art.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : MvvmActivity<SplashDataBinding, SplashViewModel>() {


    private val handler = Handler(Looper.getMainLooper()) {
        goMainActivity()
        true
    }

    override fun initContentView(): Int {
        return R.layout.activity_splash
    }

    override fun initViewModel(): SplashViewModel? {
        return createViewModel(this, SplashViewModel::class.java)
    }

    override fun initDataBindingVariable() {
    }


    override fun initView() {
        super.initView()

        mViewModel.getbackground()

        handler.postDelayed({ handler.sendEmptyMessage(0) }, 4000)
    }


    override fun registerLiveDataObserve() {
        super.registerLiveDataObserve()

        mViewModel.mFanArtList.observe(this) { result ->
            val imageDataBean = result.getOrNull()
            if (imageDataBean != null) {
                val num = (0..19).random()

                GlideUtils.loadToImageView(this,imageDataBean[num].pic_url[0].img_src,iv_background)
                tv_name.text = "UP主 : " + imageDataBean[num].name
            }
        }
    }






    /**
     * 跳转到主界面
     */
    private fun goMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun setTheme() {
        val theme = SPreferenceUtils.readInt(this, "theme", R.style.Theme_AsAsFans_Ava)
        setTheme(theme)
    }
}