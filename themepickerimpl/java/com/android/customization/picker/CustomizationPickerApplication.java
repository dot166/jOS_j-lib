package com.android.customization.picker;

import androidx.fragment.app.FragmentActivity;

import com.android.customization.model.CustomizationManager;
import com.android.wallpaper.model.CustomizationSectionController;

import io.github.dot166.jlib.app.jLIBCoreApp;

public class CustomizationPickerApplication extends jLIBCoreApp {

    public CustomizationSectionController<?> getTEController(CustomizationManager<?> themeEngineOptionsManager, CustomizationSectionController.CustomizationSectionNavigationController sectionNavigationController) {
        return null; // default
    }

    public CustomizationManager<?> getTEManager(FragmentActivity activity) {
        return null; // default
    }
}
