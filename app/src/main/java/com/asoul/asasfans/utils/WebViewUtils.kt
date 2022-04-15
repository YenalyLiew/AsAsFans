package com.asoul.asasfans.utils

import android.webkit.CookieManager
import android.webkit.WebStorage
import android.webkit.WebView
import com.fairhr.module_support.utils.ContextUtil

fun clearWebViewCache() {

    // Clear all the Application Cache, Web SQL Database and
    // the HTML5 Web Storage.
    WebStorage.getInstance().deleteAllData()

    WebView(ContextUtil.getContext()).apply {
        clearCache(true)
        clearFormData()
        clearHistory()
        clearSslPreferences()
    }

    // Clear all the cookies.
    CookieManager.getInstance().removeAllCookies(null)
    CookieManager.getInstance().flush()
}