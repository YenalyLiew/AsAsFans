package com.asoul.asasfans.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.asoul.asasfans.R
import com.asoul.asasfans.activity.AboutActivity
import com.asoul.asasfans.utils.*
import com.asoul.asasfans.viewmodel.SettingsViewModel
import com.bumptech.glide.Glide
import com.fairhr.module_support.KtxActivityManger
import com.fairhr.module_support.utils.ContextUtil
import com.fairhr.module_support.utils.SPreferenceUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @ProjectName : AsAsFans
 * @Author : Yenaly Liew
 * @Time : 2022/04/13 013 18:47
 * @Description : Description...
 */
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel by activityViewModels<SettingsViewModel>()

    private var blackList: Preference? = null
    private var changeTheme: ListPreference? = null
    private var clearPhotoCache: Preference? = null
    private var clearWebCache: Preference? = null
    private var checkUpdate: Preference? = null
    private var feedback: Preference? = null
    private var aboutThis: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        blackList = findPreference("black_list")
        changeTheme = findPreference("change_theme")
        clearPhotoCache = findPreference("clear_photo_cache")
        clearWebCache = findPreference("clear_web_cache")
        checkUpdate = findPreference("check_update")
        feedback = findPreference("feedback")
        aboutThis = findPreference("about_this")

        val versionText = "当前版本：$localVersion"
        checkUpdate?.summary = versionText

        viewModel.getVersion()
        checkUpdate()

        blackList?.setOnPreferenceClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_settingsFragment_to_blackListFragment)
            true
        }

        clearPhotoCache?.setOnPreferenceClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    Glide.get(ContextUtil.getContext()).clearDiskCache()
                }
                "清理完成了捏".showShortToast()
            }
            true
        }

        clearWebCache?.setOnPreferenceClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.pay_attention)
                .setMessage("请谨慎使用，一般情况无需清理。\n若清理可能会引起软件无响应。")
                .setPositiveButton(R.string.no_problem) { _, _ ->
                    clearWebViewCache() // 耗时，可能会有阻塞，但放在Dispatchers.IO里会引起软件重启。
                    "清理完成了捏".showShortToast()
                }
                .setNegativeButton(R.string.let_it_go, null)
                .show()
            true
        }

        changeTheme?.setOnPreferenceChangeListener { _, newValue ->
            val oldTheme = SPreferenceUtils.readInt(context, "theme", R.style.Theme_AsAsFans_Ava)
            when (newValue.toString()) {
                "0" -> {
                    SPreferenceUtils.write(context, "theme", R.style.Theme_AsAsFans_Ava)
                }
                "1" -> {
                    SPreferenceUtils.write(context, "theme", R.style.Theme_AsAsFans_Bella)
                }
                "2" -> {
                    SPreferenceUtils.write(context, "theme", R.style.Theme_AsAsFans_Carol)
                }
                "3" -> {
                    SPreferenceUtils.write(context, "theme", R.style.Theme_AsAsFans_Diana)
                }
                "4" -> {
                    SPreferenceUtils.write(context, "theme", R.style.Theme_AsAsFans_Eileen)
                }
            }
            val newTheme = SPreferenceUtils.readInt(context, "theme", R.style.Theme_AsAsFans_Ava)
            if (oldTheme != newTheme) {
                KtxActivityManger.recreateAllActivity()
            }
            true
        }
        feedback?.setOnPreferenceClickListener {
            val feedbackUri = Uri.parse("http://asf.ink/appbug")
            val intent = Intent(Intent.ACTION_VIEW, feedbackUri)
            startActivity(intent)
            true
        }
        aboutThis?.setOnPreferenceClickListener {
            val intent = Intent(activity, AboutActivity::class.java)
            startActivity(intent)
            true
        }
    }

    private fun checkUpdate() {
        viewModel.mVersion.observe(this) { result ->
            val versionString = result.getOrNull()
            if (versionString != null) {
                val latestVersion = versionString.name.substringAfter('v')
                if (latestVersion.toVersionCode() > localVersionCode) {
                    val updateText = "当前版本：$localVersion (检测到新版本：$latestVersion)"
                    checkUpdate?.summary = updateText
                    checkUpdate?.setOnPreferenceClickListener {
                        val uri = Uri.parse("https://app.asf.ink/")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                        true
                    }
                } else {
                    val updateText = "当前已经是最新版本：$localVersion"
                    checkUpdate?.summary = updateText
                }
            } else {
                result.exceptionOrNull()?.printStackTrace()
                val updateText = "当前版本：$localVersion (无法获取最新版本信息)"
                checkUpdate?.summary = updateText
            }
        }
    }
}