package io.github.dot166.jlib.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources

object AppUtils {
    @JvmStatic
    fun getAppIcon(context: Context): Drawable? {
        val applicationInfo = context.applicationInfo
        val iconId = applicationInfo.icon
        return if (iconId == 0) AppCompatResources.getDrawable(
            context,
            android.R.drawable.sym_def_app_icon
        ) else AppCompatResources.getDrawable(context, iconId)
    }

    @JvmStatic
    fun getAppLabel(context: Context): String {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
            stringId
        )
    }
}
