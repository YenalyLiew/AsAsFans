package com.asoul.asasfans.activity


import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.asoul.asasfans.HistoryDataRepository
import com.asoul.asasfans.R
import com.asoul.asasfans.adapter.HotSearchAdapter
import com.asoul.asasfans.adapter.SearchHistoryAdapter
import com.asoul.asasfans.bean.LoadMoreBean
import com.asoul.asasfans.databinding.SearchDataBinding
import com.asoul.asasfans.entity.HistorySearchEntity
import com.asoul.asasfans.viewmodel.SearchViewModel
import com.fairhr.module_support.base.MvvmActivity
import com.fairhr.module_support.constants.ServiceConstants
import com.fairhr.module_support.router.RouteUtils
import com.fairhr.module_support.utils.SPreferenceUtils
import com.fairhr.module_support.utils.SystemStatusUtil
import com.fairhr.module_support.utils.UrlUtils
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.item_search.*

class SearchActivity: MvvmActivity<SearchDataBinding, SearchViewModel>() {

    var HotSearchAdapter: HotSearchAdapter? = null
    var SearchHistoryAdapter: SearchHistoryAdapter? = null


    override fun initContentView(): Int {
        return R.layout.activity_search
    }

    override fun initViewModel(): SearchViewModel? {
        return createViewModel(this, SearchViewModel::class.java)
    }

    override fun initDataBindingVariable() {}

    override fun setTheme() {
        val theme = SPreferenceUtils.readInt(this, "theme", R.style.Theme_AsAsFans_Ava)
        setTheme(theme)
    }


    override fun initView() {
        super.initView()


        common_title_tv_title.text = getString(R.string.search)
        initData()
        initAdapter()
        initEvent()
    }


    private fun initData() {
        mViewModel.gethotSearch()
    }

    private fun initAdapter() {

        rc_hotSearch.layoutManager = FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP)
        HotSearchAdapter = HotSearchAdapter()
        rc_hotSearch.adapter = HotSearchAdapter



        rv_searchhistory.layoutManager = GridLayoutManager(this,4)
        SearchHistoryAdapter = SearchHistoryAdapter()
        rv_searchhistory.adapter = SearchHistoryAdapter

    }



    override fun registerLiveDataObserve() {
        super.registerLiveDataObserve()

        HistoryDataRepository.getInstance().allHistoryLiveData.observe(this) { HistorySearchEntity ->
            val list1 = ArrayList<LoadMoreBean>()
            if (HistorySearchEntity != null && HistorySearchEntity.isNotEmpty()) {
                if (HistorySearchEntity.size <= 10) {
                    for (i in 0 until HistorySearchEntity.size) {
                        list1.add(LoadMoreBean(HistorySearchEntity[i]))
                    }
                } else {
                    for (i in 0..9) {
                        list1.add(LoadMoreBean(HistorySearchEntity[i]))
                    }
                    list1.add(LoadMoreBean(0, "查看更多"))
                }

            }
            val tempData = mutableListOf<LoadMoreBean>()
            tempData.addAll(list1)
            SearchHistoryAdapter?.setNewInstance(tempData)

        }
    }



    private fun initEvent() {

        common_title_tiv_left.setOnClickListener {
            finish()
        }

        tv_canel.setOnClickListener {
            tv_canel.visibility = View.GONE
            SystemStatusUtil.showIme(window, false)
            et_search.setText("")
            et_search.clearFocus()
        }

        et_search.addTextChangedListener {
            tv_canel.isGone = it.toString().isEmpty()
        }


        et_search.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                if (et_search.text.toString().isNotBlank()) {

                    val text = HistorySearchEntity()
                    text.hotWordName = et_search.text.toString()
                    HistoryDataRepository.getInstance().insertHistoryEntity(text)

                    search(et_search.text.toString())
                }
            }
            true
        }


        iv_searchclean.setOnClickListener {
            HistoryDataRepository.getInstance().deleteAllHistoryEntity()
            SearchHistoryAdapter!!.removeAllFooterView()
        }


        SearchHistoryAdapter!!.setOnItemClickListener { adapter, view, position ->

            when (adapter.getItemViewType(position)){
                LoadMoreBean.LOADMORE ->{

                    HistoryDataRepository.getInstance().allHistoryLiveData.observe(this, Observer<List<HistorySearchEntity>> { HistorySearchEntity ->
                        if (HistorySearchEntity != null ) {
                            var list1 = ArrayList<LoadMoreBean>()
                            for (element in HistorySearchEntity){
                                list1.add(LoadMoreBean(element))
                            }
                            list1.add(LoadMoreBean(1,"收起"))
                            var tempData= mutableListOf<LoadMoreBean>()
                            tempData.addAll(list1)
                            SearchHistoryAdapter?.setNewInstance(tempData)
                        }

                    })
                }
                LoadMoreBean.CANCELMORE ->{

                    HistoryDataRepository.getInstance().allHistoryLiveData.observe(this, Observer<List<HistorySearchEntity>> { HistorySearchEntity ->
                        if (HistorySearchEntity != null ) {

                            var list1 = ArrayList<LoadMoreBean>()
                            for (i in 0..9){
                                list1.add(LoadMoreBean(HistorySearchEntity[i]))
                            }
                            list1.add(LoadMoreBean(0,"查看更多"))
                            var tempData= mutableListOf<LoadMoreBean>()
                            tempData.addAll(list1)
                            SearchHistoryAdapter?.setNewInstance(tempData)
                        }

                    })
                }
                LoadMoreBean.nomal ->{
                    val data: List<*> = adapter.data
                    val bean = data[position] as LoadMoreBean
                    val name = bean.data
                    search(name)
                }
            }


        }
    }

    private fun search(keyword: String) {
        val params: MutableMap<String, Any> = HashMap()

        params["keyword"] = keyword.trim()

        val url = UrlUtils.formatUrl(
            ServiceConstants.BILIBILI_SEARCH,
            ServiceConstants.BILIBILI_ALL,
            params
        )
        RouteUtils.openWebview(url, "搜索结果")
        SystemStatusUtil.showIme(window, false)
        finish()
    }
}