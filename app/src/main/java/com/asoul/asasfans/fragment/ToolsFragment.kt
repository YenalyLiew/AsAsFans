package com.asoul.asasfans.fragment


import com.asoul.asasfans.viewmodel.ToolsViewModel
import com.asoul.asasfans.R
import com.asoul.asasfans.databinding.ToolsDataBinding
import com.fairhr.module_support.base.MvvmFragment

class ToolsFragment : MvvmFragment<ToolsDataBinding, ToolsViewModel>()  {



    override fun initContentView(): Int { return R.layout.fragment_tools }

    override fun initViewModel(): ToolsViewModel {
        return createViewModel(this, ToolsViewModel::class.java)
    }

    override fun initDataBindingVariable() {}


}