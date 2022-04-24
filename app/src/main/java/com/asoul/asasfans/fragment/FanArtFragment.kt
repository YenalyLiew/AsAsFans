package com.asoul.asasfans.fragment

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.asoul.asasfans.R
import com.asoul.asasfans.adapter.FanArtAdapter
import com.asoul.asasfans.bean.ImageDataBean
import com.asoul.asasfans.databinding.FanArtDataBinding
import com.asoul.asasfans.utils.showShortToast
import com.asoul.asasfans.viewmodel.FanArtViewModel
import com.fairhr.module_support.base.MvvmFragment
import com.fairhr.module_support.constants.ServiceConstants
import com.fairhr.module_support.router.RouteUtils
import com.fairhr.module_support.utils.UrlUtils
import kotlinx.android.synthetic.main.fragment_fan_art.*
import kotlinx.android.synthetic.main.item_search.*
import java.util.HashMap

class FanArtFragment : MvvmFragment<FanArtDataBinding, FanArtViewModel>() {

    private val fanArtAdapter by lazy { FanArtAdapter(this) }

    companion object {
        private var CURRENT_PAGE = 1
    }

    override fun initContentView(): Int {
        return R.layout.fragment_fan_art
    }

    override fun initViewModel(): FanArtViewModel {
        return createViewModel(this, FanArtViewModel::class.java)
    }

    override fun initDataBindingVariable() {
    }

    override fun initView() {
        super.initView()
        initAdapter()
        initChip()
        initSpinner()
        initEvent()

        mViewModel.getFanArtList(
            CURRENT_PAGE,
            sort = mViewModel.fanArtOrder,
            part = mViewModel.fanArtPart,
            rank = mViewModel.fanArtDate
        )
    }

    override fun registerLiveDataObserve() {
        super.registerLiveDataObserve()

        var sort = mViewModel.fanArtOrder
        var part = mViewModel.fanArtPart
        var rank = mViewModel.fanArtDate

        mViewModel.mFanArtList.observe(this) { result ->
            val imageDataBean = result.getOrNull()
            if (imageDataBean != null) {

                srl_fan_art.resetNoMoreData()

                if (
                    sort != mViewModel.fanArtOrder ||
                    part != mViewModel.fanArtPart ||
                    rank != mViewModel.fanArtDate
                ) {
                    mViewModel.tempData.clear()
                    mViewModel.tempData.addAll(imageDataBean)
                    sort = mViewModel.fanArtOrder
                    part = mViewModel.fanArtPart
                    rank = mViewModel.fanArtDate
                }

                if (imageDataBean.isNotEmpty()) {
                    if (imageDataBean[0] !in mViewModel.tempData) {
                        mViewModel.tempData.addAll(imageDataBean)
                    }
                }

                srl_fan_art.finishLoadMore(true)

                fanArtAdapter.setList(mViewModel.tempData)
            } else {
                result.exceptionOrNull()?.printStackTrace()
                if (result.exceptionOrNull()?.message == "没有更多数据") {
                    fanArtAdapter.setList(mViewModel.tempData)
                    srl_fan_art.finishLoadMoreWithNoMoreData()
                } else {
                    "加载失败了捏".showShortToast()
                }
            }
        }
    }

    private fun initEvent() {
        srl_fan_art.setEnableRefresh(false)

        srl_fan_art.setOnLoadMoreListener {
            CURRENT_PAGE += 1
            mViewModel.getFanArtList(
                CURRENT_PAGE,
                sort = mViewModel.fanArtOrder,
                part = mViewModel.fanArtPart,
                rank = mViewModel.fanArtDate
            )
        }


        fanArtAdapter.setOnItemClickListener { adapter, view, position ->
            val bean = adapter.getItem(position) as ImageDataBean
            val id = bean.dy_id
            val title = bean.name + "的二创图片"
            val params: MutableMap<String, Any> = HashMap()
            params["tab"] = 2

            RouteUtils.openWebview(UrlUtils.formatUrl(ServiceConstants.BILIBILI_ART , id, params),title)
        }
    }

    private fun initAdapter() {
        fan_art_rv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        fan_art_rv.adapter = fanArtAdapter
    }

    private fun initSpinner() {
        val orderSpinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_custom_spinner,
            resources.getStringArray(R.array.fan_art_order)
        )
        val dateSpinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_custom_spinner,
            resources.getStringArray(R.array.fan_art_date)
        )

        spinner_order.adapter = orderSpinnerAdapter
        spinner_date.adapter = dateSpinnerAdapter

        // 防止一上来就触发Spinner的listener.
        spinner_order.setSelection(-1, true)
        spinner_date.setSelection(-1, true)

        spinner_order.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    Sort.PUB_DATE_IMAGE.ordinal -> {
                        mViewModel.fanArtOrder = Sort.PUB_DATE_IMAGE.value.toString()
                        Log.d("FAN_ART", "已选择最新发布")
                    }
                    Sort.BILI_HOT_IMAGE.ordinal -> {
                        mViewModel.fanArtOrder = Sort.BILI_HOT_IMAGE.value.toString()
                        Log.d("FAN_ART", "已选择B站热门")
                    }
                }
                mViewModel.getFanArtList(
                    CURRENT_PAGE,
                    sort = mViewModel.fanArtOrder,
                    part = mViewModel.fanArtPart,
                    rank = mViewModel.fanArtDate
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
        spinner_date.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        mViewModel.fanArtDate = 0
                        Log.d("FAN_ART", "已选择无榜单")
                    }
                    1 -> {
                        mViewModel.fanArtDate = 1
                        Log.d("FAN_ART", "已选择日榜")
                    }
                    2 -> {
                        mViewModel.fanArtDate = 2
                        Log.d("FAN_ART", "已选择周榜")
                    }
                    3 -> {
                        mViewModel.fanArtDate = 3
                        Log.d("FAN_ART", "已选择月榜")
                    }
                }
                mViewModel.getFanArtList(
                    CURRENT_PAGE,
                    sort = mViewModel.fanArtOrder,
                    part = mViewModel.fanArtPart,
                    rank = mViewModel.fanArtDate
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

    private fun initChip() {
        fan_art_chip_group.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_asoul -> {
                    mViewModel.fanArtPart = Part.ASOUL.value
                }
                R.id.chip_ava -> {
                    mViewModel.fanArtPart = Part.AVA.value
                }
                R.id.chip_bella -> {
                    mViewModel.fanArtPart = Part.BELLA.value
                }
                R.id.chip_carol -> {
                    mViewModel.fanArtPart = Part.CAROL.value
                }
                R.id.chip_diana -> {
                    mViewModel.fanArtPart = Part.DIANA.value
                }
                R.id.chip_eileen -> {
                    mViewModel.fanArtPart = Part.EILEEN.value
                }
                R.id.chip_bella_carol -> {
                    mViewModel.fanArtPart = Part.BELLA_CAROL.value
                }
                R.id.chip_eileen_bella -> {
                    mViewModel.fanArtPart = Part.EILEEN_BELLA.value
                }
                R.id.chip_diana_ava -> {
                    mViewModel.fanArtPart = Part.DIANA_AVA.value
                }
                R.id.chip_eileen_carol -> {
                    mViewModel.fanArtPart = Part.EILEEN_CAROL.value
                }
                R.id.chip_carol_eileen -> {
                    mViewModel.fanArtPart = Part.CAROL_EILEEN.value
                }
                R.id.chip_eileen_ava -> {
                    mViewModel.fanArtPart = Part.EILEEN_AVA.value
                }
                R.id.chip_eileen_diana -> {
                    mViewModel.fanArtPart = Part.EILEEN_DIANA.value
                }
                else -> {
                    mViewModel.fanArtPart = Part.ALL_TAG.value
                }
            }
            mViewModel.getFanArtList(
                CURRENT_PAGE,
                sort = mViewModel.fanArtOrder,
                part = mViewModel.fanArtPart,
                rank = mViewModel.fanArtDate
            )
        }
    }

    private enum class Sort(val value: Int) {
        PUB_DATE_IMAGE(3),
        BILI_HOT_IMAGE(4)
    }

    private enum class Part(val value: Int) {
        ALL_TAG(0),
        ASOUL(1712619),
        AVA(9221368),
        BELLA(195579),
        CAROL(17872743),
        DIANA(17520266),
        EILEEN(17839311),
        BELLA_CAROL(18207897),
        EILEEN_BELLA(18843054),
        DIANA_AVA(17895874),
        EILEEN_CAROL(21134102),
        CAROL_EILEEN(18579605),
        EILEEN_AVA(1058727),
        EILEEN_DIANA(20064249)
    }
}