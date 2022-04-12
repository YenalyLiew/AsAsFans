package com.asoul.asasfans.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.asoul.asasfans.R
import com.asoul.asasfans.databinding.SplashDataBinding
import com.fairhr.module_support.base.BaseViewModel
import com.fairhr.module_support.base.MvvmActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity: MvvmActivity<SplashDataBinding, BaseViewModel>() {


    private val handler = Handler(Looper.getMainLooper()) { goMainActivity()
        true }

    override fun initContentView(): Int {
        return R.layout.activity_splash
    }

    override fun initViewModel(): BaseViewModel? {
       return null
    }

    override fun initDataBindingVariable() {
    }


    override fun initView() {
        super.initView()

        handler.postDelayed({ handler.sendEmptyMessage(0) }, 500)
    }


    /**
     * 跳转到主界面
     */
    private fun goMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}