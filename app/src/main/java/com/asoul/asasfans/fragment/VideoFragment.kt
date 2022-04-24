package com.asoul.asasfans.fragment

import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.asoul.asasfans.adapter.VideoFragmentAdapter
import com.asoul.asasfans.viewmodel.VideoViewModel
import com.asoul.asasfans.R
import com.asoul.asasfans.activity.SettingsActivity
import com.asoul.asasfans.databinding.VideoDataBinding
import com.fairhr.module_support.base.MvvmFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_video.*

class VideoFragment : MvvmFragment<VideoDataBinding, VideoViewModel>() {


    private var videoFragmentAdapter: VideoFragmentAdapter? = null


    override fun initContentView(): Int {
        return R.layout.fragment_video
    }

    override fun initViewModel(): VideoViewModel {
        return createViewModel(this, VideoViewModel::class.java)
    }

    override fun initDataBindingVariable() {}


    override fun initView() {
        super.initView()
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(video_toolbar)
        }
        initAdapter()
    }

    private fun initAdapter() {

        videoFragmentAdapter = VideoFragmentAdapter(this)
        video_view_pager.adapter = videoFragmentAdapter

        val title = mutableMapOf<Int, String>()

        title[0] = getString(R.string.fan_video)
        title[1] = getString(R.string.hot_cut_video)
        title[2] = getString(R.string.new_release_video)
        title[3] = getString(R.string.history_recommend_video)

        TabLayoutMediator(video_tab_layout, video_view_pager) { tab, position ->
            tab.text = title[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(activity, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }
}