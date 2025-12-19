package io.github.dot166.jlib.app

import android.content.Context
import androidx.appcompat.app.AlertDialog
import io.github.dot166.jlib.view.ContextThemeBubble

/**
 * An extension of [AlertDialog.Builder] that forces a SettingsLib theme on the dialog (e.g.,
 * Theme.SubSettingsBase).
 *
 *
 * The type of dialog returned is still an [AlertDialog]; there is no specific SettingsLib
 * or jLib implementation of [AlertDialog].
 */
class SettingsLibAlertDialogBuilder(context: Context) : AlertDialog.Builder(
    ContextThemeBubble(context)
)
