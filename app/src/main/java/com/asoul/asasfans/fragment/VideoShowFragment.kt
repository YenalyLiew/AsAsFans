package com.asoul.asasfans.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.asoul.asasfans.adapter.VideoListAdapter
import com.asoul.asasfans.bean.VideoBean
import com.asoul.asasfans.viewmodel.VideoShowViewModel
import com.asoul.asasfans.R
import com.asoul.asasfans.databinding.VideoShowDataBinding
import com.fairhr.module_support.base.MvvmFragment
import com.fairhr.module_support.constants.ServiceConstants
import com.fairhr.module_support.router.RouteUtils
import com.fairhr.module_support.utils.ContextUtil
import kotlinx.android.synthetic.main.fragment_video_show.*

class VideoShowFragment : MvvmFragment<VideoShowDataBinding, VideoShowViewModel>() {



    var PAGE_SIZE = 1
    var type = 0
    var videoListAdapter: VideoListAdapter?=null
    val tempData= mutableListOf<VideoBean>()

    override fun initContentView(): Int { return R.layout.fragment_video_show }

    override fun initViewModel(): VideoShowViewModel { return createViewModel(this, VideoShowViewModel::class.java)}

    override fun initDataBindingVariable() {}




    override fun initView() {
        super.initView()

        val bundle = arguments
        type = bundle!!.getInt("type")
        mViewModel.getVideoList(type,PAGE_SIZE)


        initAdapter()
        initEvent()
    }

    private fun initEvent() {

        sl_video.setEnableRefresh(false)

        sl_video.setOnLoadMoreListener {
            PAGE_SIZE += 1
            mViewModel.getVideoList(type,PAGE_SIZE)
        }


        videoListAdapter!!.setOnItemClickListener { adapter, view, position ->
            val bean = adapter.getItem(position) as VideoBean
            val bvid = bean.bvid
            RouteUtils.openWebview(ServiceConstants.BILIBILITITLE + bvid)
        }
    }

    private fun initAdapter() {

        rv_video.layoutManager = LinearLayoutManager(ContextUtil.getContext(), LinearLayoutManager.VERTICAL,false)
        videoListAdapter = VideoListAdapter()
        rv_video.adapter = videoListAdapter
    }




    override fun registerLiveDateObserve() {
        super.registerLiveDateObserve()
        mViewModel.videoListData.observe(this) { VideoListBean ->

            tempData.addAll(VideoListBean.result)

            sl_video.finishLoadMore(true)

            videoListAdapter!!.setList(tempData)
        }
    }





    fun newInstance(type: Int): VideoShowFragment {
        val fragment = VideoShowFragment()
        val bundle = Bundle()
        bundle.putInt("type",type)
        fragment.arguments = bundle
        return fragment
    }

}