@file:JvmName("SpUtils")
@file:JvmMultifileClass

package com.dede.basic


import android.content.Context
import android.content.SharedPreferences

/**
 * SharedPreferences Utils
 *
 * @author hsh
 * @since 2020/10/20 3:01 PM
 */

private val Context.sp: SharedPreferences
    get() = this.getSharedPreferences(this.packageName, Context.MODE_PRIVATE)

fun Context.getLong(key: String, default: Long): Long {
    return sp.getLong(key, default)
}

fun Context.putLong(key: String, value: Long) {
    sp.edit().putLong(key, value).apply()
}