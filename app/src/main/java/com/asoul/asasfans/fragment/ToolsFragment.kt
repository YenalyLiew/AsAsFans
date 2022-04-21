package com.asoul.asasfans.fragment


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.asoul.asasfans.viewmodel.ToolsViewModel
import com.asoul.asasfans.R
import com.asoul.asasfans.databinding.ToolsDataBinding
import com.asoul.asasfans.utils.dp
import com.asoul.asasfans.utils.showShortToast
import com.fairhr.module_support.base.MvvmFragment
import com.fairhr.module_support.constants.ServiceConstants
import com.fairhr.module_support.constants.ServiceConstants.*
import com.fairhr.module_support.router.RouteUtils
import com.fairhr.module_support.utils.ContextUtil
import com.fairhr.module_support.webview.WebActivity
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_tools.*

class ToolsFragment : MvvmFragment<ToolsDataBinding, ToolsViewModel>() {


    override fun initContentView(): Int {
        return R.layout.fragment_tools
    }

    override fun initViewModel(): ToolsViewModel {
        return createViewModel(this, ToolsViewModel::class.java)
    }

    override fun initDataBindingVariable() {
    }

    override fun initView() {
        super.initView()
        initToolsItem()
    }

    private fun initToolsItem() {
        val linearViewGroup = tools_list
        addToolsItem(
            linearViewGroup,
            icon = R.drawable.icon_zwcc,
            text = R.string.zhi_net_check_duplicate,
            moreAction = { it, _, _, _ ->
                it.click(ASOUL_CNKI,"枝网查重")
                it.longClick(ASOUL_CNKI)
            }
        )
        addToolsItem(
            linearViewGroup,
            icon = R.drawable.icon_zhijiang_book,
            text = R.string.dialect_dict,
            moreAction = { it, _, _, _->
                it.click(DIALECT_DICT,"方言词典")
                it.longClick(DIALECT_DICT)
            }

        )
        addToolsItem(
            linearViewGroup,
            icon = R.drawable.icon_long_comment,
            text = R.string.little_composition,
            moreAction = { it, _, _,_ ->
                it.click(LITTLE_COMPOSITION,"小作文")
                it.longClick(LITTLE_COMPOSITION)
            }
        )
        addToolsItem(
            linearViewGroup,
            icon = R.drawable.icon_asoul,
            text = R.string.group_chat_history_public,
            moreAction = { it, _, _,_ ->
                it.click(CHAT_RECORD_PUBLIC,"论坛管理群聊天记录公示")
                it.longClick(CHAT_RECORD_PUBLIC)
            }
        )
        addToolsItem(
            linearViewGroup,
            icon = R.drawable.icon_asoul,
            text = R.string.asoul_wiki,
            moreAction = { it, _, _, _ ->
                it.click(ASOUL_WIKI,"一个魂维基百科 A-SOUL WIKI")
                it.longClick(ASOUL_WIKI)
            }
        )
        addToolsItem(
            linearViewGroup,
            icon = R.drawable.icon_asoul,
            text = R.string.composition_generator,
            moreAction = { it, _, _,_ ->
                it.click(COMPOSITION_GENERATOR,"小作文生成器")
                it.longClick(COMPOSITION_GENERATOR)
            }
        )
        addToolsItem(
            linearViewGroup,
            icon = R.drawable.icon_asoul,
            text = R.string.art_and_record_site,
            moreAction = { it, _, _,_->
                it.click(ART_AND_RECORD,"A-SOUL原画录播站")
                it.longClick(ART_AND_RECORD)
            }
        )
        addToolsItem(
            linearViewGroup,
            icon = R.drawable.icon_asf_bak,
            text = R.string.status_search,
            moreAction = { it, _, _,_ ->
                it.click(STATUS_CHECK,"成分姬")
                it.longClick(STATUS_CHECK)
            }
        )
        addToolsItem(
            linearViewGroup,
            icon = R.drawable.icon_asf_bak,
            text = R.string.QA_search,
            moreAction = { it, _, _,_->
                it.click(QA_SEARCH,"QA查询（羊驼打过的太极）")
                it.longClick(QA_SEARCH)
            }
        )
    }

    /**
     * 动态添加`ToolsFragment`里的工具列表。
     *
     * @param linearViewGroup 根LinearLayout，用于存放Item.
     * @param position 添加View的位置，默认为-1，即从上到下依次排列。
     * @param icon 图标，需要Drawable.
     * @param text 文字，需要引用`string.xml`的文字。
     * @param moreAction 引用了所用的所有View，如需要更多Action可以使用该函数。
     *
     * @author Yenaly Liew
     */
    private fun Fragment.addToolsItem(
        linearViewGroup: LinearLayout,
        position: Int = -1,
        @DrawableRes icon: Int,
        @StringRes text: Int,
        moreAction: ((
            cardView: MaterialCardView,
            linearLayout: LinearLayout,
            imageView: ImageView,
            textView: TextView
        ) -> Unit)? = null
    ) {
        val materialCardView = MaterialCardView(requireContext())
        materialCardView.isHapticFeedbackEnabled = true
        materialCardView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 0, 0, 8.dp)
        }

        val linearLayout = LinearLayout(requireContext())
        linearLayout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.gravity = Gravity.CENTER_VERTICAL

        val imageView = ImageView(requireContext())
        imageView.setImageDrawable(ResourcesCompat.getDrawable(resources, icon, null))
        imageView.layoutParams = LinearLayout.LayoutParams(40.dp, 40.dp).apply {
            setMargins(0, 16.dp, 0, 16.dp)
            marginStart = 16.dp
            marginEnd = 16.dp
        }

        val textView = TextView(requireContext())
        textView.setText(text)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
        textView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        linearLayout.addView(imageView, 0)
        linearLayout.addView(textView, 1)
        materialCardView.addView(linearLayout)
        linearViewGroup.addView(materialCardView, position)

        moreAction?.invoke(materialCardView, linearLayout, imageView, textView)
    }

    private fun View.click(url: String,title:String) {
        this.setOnClickListener {
            RouteUtils.openWebview(url,title)
        }
    }

    private fun View.longClick(url: String) {
        this.setOnLongClickListener {
            val clipBoardManager =
                ContextUtil.getContext()
                    .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipDataSet = ClipData.newPlainText(null, url)
            clipBoardManager.setPrimaryClip(clipDataSet)
            "地址复制成功！".showShortToast()
            // true代表事件分发到此结束，不会传递到短按事件。
            true
        }
    }
}