package com.asoul.asasfans.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.asoul.asasfans.R
import com.asoul.asasfans.adapter.VideoListAdapter
import com.asoul.asasfans.bean.VideoBean
import com.asoul.asasfans.databinding.VideoShowDataBinding
import com.asoul.asasfans.utils.showShortToast
import com.asoul.asasfans.viewmodel.VideoShowViewModel
import com.fairhr.module_support.base.MvvmFragment
import com.fairhr.module_support.constants.ServiceConstants
import com.fairhr.module_support.router.RouteUtils
import com.fairhr.module_support.utils.ContextUtil
import kotlinx.android.synthetic.main.fragment_video_show.*

class VideoShowFragment : MvvmFragment<VideoShowDataBinding, VideoShowViewModel>() {


    var PAGE_SIZE = 1
    var type = 0
    var videoListAdapter: VideoListAdapter? = null

    override fun initContentView(): Int {
        return R.layout.fragment_video_show
    }

    override fun initViewModel(): VideoShowViewModel {
        return createViewModel(this, VideoShowViewModel::class.java)
    }

    override fun initDataBindingVariable() {
    }


    override fun initView() {
        super.initView()

        val bundle = arguments
        type = bundle!!.getInt("type")
        if (mViewModel.tempData.isEmpty()) {
            mViewModel.getVideoList(type, PAGE_SIZE)
        }

        initAdapter()
        initEvent()
    }

    private fun initEvent() {

        sl_video.setEnableRefresh(false)

        sl_video.setOnLoadMoreListener {
            PAGE_SIZE += 1
            mViewModel.getVideoList(type, PAGE_SIZE)
        }


        videoListAdapter!!.setOnItemClickListener { adapter, _, position ->
            val bean = adapter.getItem(position) as VideoBean
            val bvid = bean.bvid
            RouteUtils.openWebview(ServiceConstants.BILIBILI_TITLE + bvid)
        }

        videoListAdapter!!.setOnItemLongClickListener { adapter, _, position ->
            val videoItem = adapter.getItem(position) as VideoBean
            val clipBoardManager =
                ContextUtil.getContext()
                    .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipDataSet = ClipData.newPlainText(null, videoItem.bvid)
            clipBoardManager.setPrimaryClip(clipDataSet)
            "BV号复制成功！".showShortToast()
            true
        }
    }

    private fun initAdapter() {

        rv_video.layoutManager =
            LinearLayoutManager(ContextUtil.getContext(), LinearLayoutManager.VERTICAL, false)
        videoListAdapter = VideoListAdapter(this)
        rv_video.adapter = videoListAdapter
    }


    override fun registerLiveDataObserve() {
        super.registerLiveDataObserve()
        mViewModel.videoListData.observe(this) { videoListBean ->

            if (videoListBean.result.isNotEmpty()) {
                if (videoListBean.result[0] !in mViewModel.tempData) {
                    mViewModel.tempData.addAll(videoListBean.result)
                }
                sl_video.finishLoadMore(true)
            } else {
                sl_video.finishLoadMoreWithNoMoreData()
            }
            videoListAdapter!!.setList(mViewModel.tempData)
        }
    }


    fun newInstance(type: Int): VideoShowFragment {
        val fragment = VideoShowFragment()
        val bundle = Bundle()
        bundle.putInt("type", type)
        fragment.arguments = bundle
        return fragment
    }

}