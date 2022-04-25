package com.asoul.asasfans.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.asoul.asasfans.R
import com.asoul.asasfans.databinding.SplashDataBinding
import com.asoul.asasfans.viewmodel.SplashViewModel
import com.fairhr.module_support.base.MvvmActivity
import com.fairhr.module_support.utils.GlideUtils
import com.fairhr.module_support.utils.SPreferenceUtils
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : MvvmActivity<SplashDataBinding, SplashViewModel>() {

    companion object {
        private const val DELAY_SECOND = 3
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

        mViewModel.getBackground()

        flow {
            for (i in DELAY_SECOND downTo 1) {
                emit(i)
                delay(1000)
            }
        }.flowOn(Dispatchers.Main)
            .onEach {
                mDataBinding.skipTick.text = getString(R.string.skip_with_sec, it)
            }
            .onCompletion {
                goMainActivity()
            }
            .launchIn(lifecycleScope)

        mDataBinding.skipTick.setOnClickListener {
            goMainActivity()
        }
    }


    override fun registerLiveDataObserve() {
        super.registerLiveDataObserve()

        mViewModel.mFanArtList.observe(this) { result ->
            val imageDataBean = result.getOrNull()
            if (imageDataBean != null) {

                val num = (0 until 20).random()

                val randomImage = imageDataBean[num].pic_url[0]

                val imageWidth: Double
                val imageHeight: Double

                if (randomImage.img_width >= 480) {
                    imageWidth = 480.0
                    imageHeight = imageWidth * randomImage.img_height / randomImage.img_width
                } else {
                    imageWidth = randomImage.img_width
                    imageHeight = randomImage.img_height
                }

                val firstFanArtThumbnail =
                    "${randomImage.img_src}@${imageWidth.toInt()}w_${imageHeight.toInt()}h_1e_1c.jpg"

                GlideUtils.loadToImageView(this, firstFanArtThumbnail, iv_background)
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