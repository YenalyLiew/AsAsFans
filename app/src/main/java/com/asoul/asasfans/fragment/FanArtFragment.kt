package com.asoul.asasfans.fragment


import com.asoul.asasfans.viewmodel.FanArtViewModel

import com.asoul.asasfans.R
import com.asoul.asasfans.databinding.FanArtDataBinding
import com.fairhr.module_support.base.MvvmFragment

class FanArtFragment : MvvmFragment<FanArtDataBinding, FanArtViewModel>()  {



    override fun initContentView(): Int { return R.layout.fragment_fanart }

    override fun initViewModel(): FanArtViewModel {
        return createViewModel(this, FanArtViewModel::class.java)
    }

    override fun initDataBindingVariable() {}




}