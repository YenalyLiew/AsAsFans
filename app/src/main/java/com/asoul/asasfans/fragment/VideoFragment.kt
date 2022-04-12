package com.asoul.asasfans.fragment

import com.asoul.asasfans.adapter.VideoFragmentAdapter
import com.asoul.asasfans.viewmodel.VideoViewModel
import com.asoul.asasfans.R
import com.asoul.asasfans.databinding.VideoDataBinding
import com.fairhr.module_support.base.MvvmFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_video.*

class VideoFragment: MvvmFragment<VideoDataBinding, VideoViewModel>()  {


    var videoFragmentAdapter: VideoFragmentAdapter? = null


    override fun initContentView(): Int { return R.layout.fragment_video }

    override fun initViewModel(): VideoViewModel {
        return createViewModel(this, VideoViewModel::class.java)
    }

    override fun initDataBindingVariable() {}


    override fun initView() {
        super.initView()

        initAdapter()
    }

    private fun initAdapter() {

        videoFragmentAdapter = VideoFragmentAdapter(this)
        video_view_pager.adapter = videoFragmentAdapter

        val title = mutableMapOf<Int, String>()

        title[0] = "热门二创"
        title[1] = "热门切片"
        title[2] = "最新发布"
        title[3] = "历史推荐"

        TabLayoutMediator(video_tab_layout, video_view_pager) { tab, position ->
            tab.text = title[position]
        }.attach()
    }

}