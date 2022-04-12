package com.asoul.asasfans.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.asoul.asasfans.fragment.VideoFragment
import com.asoul.asasfans.fragment.VideoShowFragment

class VideoFragmentAdapter(fragment: VideoFragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {

        val fragment= mutableMapOf<Int, Fragment>()
        fragment[0] = VideoShowFragment().newInstance(0)
        fragment[1] = VideoShowFragment().newInstance(1)
        fragment[2] = VideoShowFragment().newInstance(2)
        fragment[3] = VideoShowFragment().newInstance(3)

        return fragment[position]!!


    }
}