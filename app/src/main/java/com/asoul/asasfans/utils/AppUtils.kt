package com.asoul.asasfans.utils

import androidx.core.content.pm.PackageInfoCompat
import com.fairhr.module_support.utils.ContextUtil


fun String.toVersionCode(): Int {
    val versionSplit = this.split('.')
    return versionSplit[0].toInt() * 100 + versionSplit[1].toInt() * 10 + versionSplit[2].toInt()
}

val localVersionCode: Long
    get() {
        val packageInfo =
            ContextUtil.getContext().packageManager.getPackageInfo(
                ContextUtil.getContext().packageName,
                0
            )
        return PackageInfoCompat.getLongVersionCode(packageInfo)
    }