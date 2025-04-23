package io.github.dot166.themeengine;

import androidx.fragment.app.FragmentActivity;

import com.android.customization.model.CustomizationManager;
import com.android.customization.picker.CustomizationPickerApplication;
import com.android.wallpaper.model.CustomizationSectionController;

public class TEApplication extends CustomizationPickerApplication {

    @Override
    public CustomizationSectionController<?> getTEController(CustomizationManager<?> themeEngineOptionsManager, CustomizationSectionController.CustomizationSectionNavigationController sectionNavigationController) {
        return new ThemeEngineSectionController((ThemeEngineManager) themeEngineOptionsManager, sectionNavigationController); // default
    }

    @Override
    public CustomizationManager<?> getTEManager(FragmentActivity activity) {
        return ThemeEngineManager.getInstance(activity); // default
    }
}
