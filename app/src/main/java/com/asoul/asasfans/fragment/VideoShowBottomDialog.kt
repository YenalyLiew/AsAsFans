package com.asoul.asasfans.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.asoul.asasfans.R
import com.asoul.asasfans.utils.showShortToast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.dialog_video_show.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @ProjectName : AsAsFans
 * @Author : Yenaly Liew
 * @Time : 2022/04/22 022 20:41
 * @Description : Description...
 */
class VideoShowBottomDialog : BottomSheetDialogFragment() {

    private lateinit var videoDesc: String
    private lateinit var videoTag: String
    private lateinit var videoRealTime: String

    private val tagSet = mutableSetOf<String>()

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
        videoDesc = arguments?.getString(BUNDLE_DESC).toString()
        videoTag = arguments?.getString(BUNDLE_TAG).toString()
        val videoTime = arguments?.getString(BUNDLE_TIME)
        videoRealTime = if (videoTime != null) {
            simpleDateFormat.format(Date(videoTime.toLong() * 1000))
        } else "null"
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_video_show, null)
        dialog.setContentView(view)
        initView(view, dialog)
        return dialog
    }

    private fun initView(view: View, dialog: Dialog) {
        view.release_time.text = getString(R.string.release_time, videoRealTime)
        view.video_introduction.text = videoDesc
        videoTag.split(',').forEach { tag ->
            view.tag_chip_group.createTagChip(tag) { _, isChecked ->
                if (isChecked) tagSet.add(tag) else tagSet.remove(tag)
            }
        }
        view.no_this_tag.setOnClickListener {
            // TODO
            if (tagSet.isEmpty()) {
                "请至少选一项tag进行屏蔽".showShortToast()
            } else {
                "下次加载不会再出现含有这些tag的视频".showShortToast()
                dialog.hide()
            }
        }
        view.no_this_up.setOnClickListener {
            // TODO
            "下次加载不会再出现该up主的视频".showShortToast()
            dialog.hide()
        }
        view.no_this_video.setOnClickListener {
            // TODO
            "下次加载不会再出现该视频".showShortToast()
            dialog.hide()
        }
    }

    companion object {

        private const val BUNDLE_DESC = "video_desc"
        private const val BUNDLE_TAG = "video_tag"
        private const val BUNDLE_TIME = "video_time"

        @JvmStatic
        fun getInstance(desc: String, tag: String, time: String): VideoShowBottomDialog {
            val dialog = VideoShowBottomDialog()
            val bundle = Bundle()
            bundle.putString(BUNDLE_DESC, desc)
            bundle.putString(BUNDLE_TAG, tag)
            bundle.putString(BUNDLE_TIME, time)
            dialog.arguments = bundle
            return dialog
        }
    }

    private fun ChipGroup.createTagChip(chipName: String, onChange: (View, Boolean) -> Unit) {
        val chip =
            layoutInflater.inflate(R.layout.layout_chip_choice, tag_chip_group, false) as Chip
        chip.text = chipName
        this.addView(chip)
        chip.setOnCheckedChangeListener(onChange::invoke)
    }
}