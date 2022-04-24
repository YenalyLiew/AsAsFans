package com.asoul.asasfans.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.asoul.asasfans.fragment.BlackListFragment

/**
 * @ProjectName : AsAsFans
 * @Author : Yenaly Liew
 * @Time : 2022/04/23 023 21:30
 * @Description : Description...
 */
class BlackListFragmentAdapter(fragment: BlackListFragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return Fragment()
    }
}