package com.asoul.asasfans.fragment


import com.asoul.asasfans.viewmodel.RecordViewModel
import com.asoul.asasfans.R
import com.asoul.asasfans.databinding.RecordDataBinding
import com.fairhr.module_support.base.MvvmFragment

class RecordFragment : MvvmFragment<RecordDataBinding, RecordViewModel>()  {



    override fun initContentView(): Int { return R.layout.fragment_record }

    override fun initViewModel(): RecordViewModel {
        return createViewModel(this, RecordViewModel::class.java)
    }

    override fun initDataBindingVariable() {}


}