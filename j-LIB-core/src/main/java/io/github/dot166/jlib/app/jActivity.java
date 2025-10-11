package io.github.dot166.jlib.app;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ContentView;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import io.github.dot166.jlib.R;
import io.github.dot166.jlib.utils.VersionUtils;

public class jActivity extends AppCompatActivity {
    @Deprecated
    String currentTheme = null;
    private ActivityResultLauncher<String> notificationPermissionLauncher;

    /**
     * Default constructor for jActivity. All Activities must have a default constructor
     * for API 27 and lower devices or when using the default
     * {@link android.app.AppComponentFactory}.
     */
    public jActivity() {
        super();
    }

    /**
     * Alternate constructor that can be used to provide a default layout
     * that will be inflated as part of <code>super.onCreate(savedInstanceState)</code>.
     *
     * <p>This should generally be called from your constructor that takes no parameters,
     * as is required for API 27 and lower or when using the default
     * {@link android.app.AppComponentFactory}.
     *
     * @see #jActivity()
     */
    @ContentView
    public jActivity(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notificationPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (!isGranted) {
                if (VersionUtils.isAtLeastT()) {
                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                        showNotificationPermissionRationale();
                    } else {
                        showSettingDialog();
                    }
                }
            }
        });

        if (VersionUtils.isAtLeastV()) {
            // Fix A15 EdgeToEdge
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, windowInsets) -> {
                Insets insets = windowInsets.getInsets(
                        WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime()
                                | WindowInsetsCompat.Type.displayCutout());
                int statusBarHeight = getWindow().getDecorView().getRootWindowInsets()
                        .getInsets(WindowInsetsCompat.Type.statusBars()).top;
                // Apply the insets paddings to the view.
                v.setPadding(insets.left, statusBarHeight, insets.right, insets.bottom);
                // Return CONSUMED if you don't want the window insets to keep being
                // passed down to descendant views.
                return WindowInsetsCompat.CONSUMED;
            });
        }
    }

    private void showSettingDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.notification_permission)
                .setMessage(R.string.settings_notif_dialog)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void showNotificationPermissionRationale() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.notification_permission)
                .setMessage(R.string.notif_dialog)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    if (VersionUtils.isAtLeastT()) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    public void forceNotificationPermission() {
        if (VersionUtils.isAtLeastT()) {
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
        }
    }
}

