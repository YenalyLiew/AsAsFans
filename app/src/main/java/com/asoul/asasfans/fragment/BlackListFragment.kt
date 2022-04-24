package com.asoul.asasfans.fragment

import android.view.MenuItem
import com.asoul.asasfans.R
import com.asoul.asasfans.adapter.BlackListFragmentAdapter
import com.asoul.asasfans.databinding.BlackListDataBinding
import com.asoul.asasfans.viewmodel.BlackListViewModel
import com.fairhr.module_support.base.MvvmFragment
import com.google.android.material.tabs.TabLayoutMediator

/**
 * @ProjectName : AsAsFans
 * @Author : Yenaly Liew
 * @Time : 2022/04/23 023 20:51
 * @Description : Description...
 */
class BlackListFragment : MvvmFragment<BlackListDataBinding, BlackListViewModel>() {
    override fun initContentView(): Int {
        return R.layout.fragment_black_list
    }

    override fun initViewModel(): BlackListViewModel {
        return createViewModel(this, BlackListViewModel::class.java)
    }

    override fun initDataBindingVariable() {
    }

    override fun initView() {
        initViewPager()
    }

    private fun initViewPager() {
        mDataBinding.fabBlackList.setOnClickListener {
            // TODO
        }
        mDataBinding.viewPagerBlackList.adapter = BlackListFragmentAdapter(this)
        TabLayoutMediator(
            mDataBinding.tabLayoutBlackList,
            mDataBinding.viewPagerBlackList
        ) { tab, position ->
            when (position) {
                0 -> tab.setText(R.string.tag)
                1 -> tab.setText(R.string.up)
                2 -> tab.setText(R.string.video)
            }
        }.attach()
    }
}