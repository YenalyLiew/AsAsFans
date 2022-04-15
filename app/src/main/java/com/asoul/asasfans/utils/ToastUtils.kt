package com.asoul.asasfans.utils

import android.widget.Toast
import com.fairhr.module_support.utils.ContextUtil

fun String.showShortToast() {
    Toast.makeText(ContextUtil.getContext(), this, Toast.LENGTH_SHORT).show()
}

fun String.showLongToast() {
    Toast.makeText(ContextUtil.getContext(), this, Toast.LENGTH_LONG).show()
}