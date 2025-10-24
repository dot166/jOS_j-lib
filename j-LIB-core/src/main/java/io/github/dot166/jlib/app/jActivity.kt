package io.github.dot166.jlib.app

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.annotation.ContentView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.dot166.jlib.utils.VersionUtils.isAtLeastT
import io.github.dot166.jlib.utils.VersionUtils.isAtLeastV

open class jActivity : AppCompatActivity {
    private var notificationPermissionLauncher: ActivityResultLauncher<String>? = null

    /**
     * Default constructor for jActivity. All Activities must have a default constructor
     * for API 27 and lower devices or when using the default
     * [android.app.AppComponentFactory].
     */
    constructor() : super()

    /**
     * Alternate constructor that can be used to provide a default layout
     * that will be inflated as part of `super.onCreate(savedInstanceState)`.
     *
     *
     * This should generally be called from your constructor that takes no parameters,
     * as is required for API 27 and lower or when using the default
     * [android.app.AppComponentFactory].
     *
     * @see .jActivity
     */
    @ContentView
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notificationPermissionLauncher = registerForActivityResult(
            RequestPermission()
        ) { isGranted: Boolean? ->
            if (!isGranted!!) {
                if (isAtLeastT) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                        showNotificationPermissionRationale()
                    } else {
                        showSettingDialog()
                    }
                }
            }
        }

        if (isAtLeastV) {
            // Fix A15 EdgeToEdge
            ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(android.R.id.content)
            ) { v: View?, windowInsets: WindowInsetsCompat? ->
                val insets = windowInsets!!.getInsets(
                    (WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
                            or WindowInsetsCompat.Type.displayCutout())
                )
                val statusBarHeight = window.decorView.getRootWindowInsets()
                    .getInsets(WindowInsetsCompat.Type.statusBars()).top
                // Apply the insets paddings to the view.
                v!!.setPadding(insets.left, statusBarHeight, insets.right, insets.bottom)
                WindowInsetsCompat.CONSUMED
            }
        }
    }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(io.github.dot166.jlib.R.string.notification_permission)
            .setMessage(io.github.dot166.jlib.R.string.settings_notif_dialog)
            .setPositiveButton(
                io.github.dot166.jlib.R.string.ok
            ) { dialog: DialogInterface?, which: Int ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.setData(("package:$packageName").toUri())
                startActivity(intent)
            }
            .setNegativeButton(io.github.dot166.jlib.R.string.cancel, null)
            .show()
    }

    private fun showNotificationPermissionRationale() {
        MaterialAlertDialogBuilder(this)
            .setTitle(io.github.dot166.jlib.R.string.notification_permission)
            .setMessage(io.github.dot166.jlib.R.string.notif_dialog)
            .setPositiveButton(
                io.github.dot166.jlib.R.string.yes
            ) { dialog: DialogInterface?, which: Int ->
                if (isAtLeastT) {
                    notificationPermissionLauncher!!.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton(io.github.dot166.jlib.R.string.no, null)
            .show()
    }

    fun forceNotificationPermission() {
        if (isAtLeastT) {
            notificationPermissionLauncher!!.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

