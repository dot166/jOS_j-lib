package io.github.dot166.jlib.app

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import com.android.settingslib.spa.framework.common.SettingsPageProvider
import com.android.settingslib.spa.framework.common.SpaEnvironmentFactory
import com.android.settingslib.spa.framework.theme.SettingsTheme
import com.android.settingslib.spa.widget.banner.BannerModel
import com.android.settingslib.spa.widget.banner.SettingsBanner
import com.android.settingslib.spa.widget.preference.SwitchPreference
import com.android.settingslib.spa.widget.preference.SwitchPreferenceModel
import com.android.settingslib.spa.widget.scaffold.HomeScaffold
import com.android.settingslib.spa.widget.ui.Category
import io.github.dot166.jlib.R

object DefaultHomePageProvider : SettingsPageProvider {
    override val name = "jLib Preference2"
    override val displayName = "Home"

    override fun getTitle(arguments: Bundle?): String {
        return SpaEnvironmentFactory.instance.appContext.getString(R.string.jlib)
    }

    @Composable
    override fun Page(arguments: Bundle?) {
        val title = remember { getTitle(arguments) }
        HomeScaffold(title) {
            Category() {
                val model = BannerModel(
                    title = stringResource(R.string.default_impl),
                    text = stringResource(R.string.default_impl_message)
                )
                SettingsBanner(model)
            }
            Category() {
                val ctx = LocalContext.current
                val testPrefEn = JLibPrefs.testBool.flow(ctx).collectAsState().value
                val title = JLibPrefs.test.flow(ctx).collectAsState().value ?: "null"
                SwitchPreference(object : SwitchPreferenceModel {
                    override val title: String = title
                    override val checked: () -> Boolean = { testPrefEn }
                    override val onCheckedChange: ((newChecked: Boolean) -> Unit)
                        get() = {
                            JLibPrefs.testBool.put(ctx, it)
                            if (it) {
                                JLibPrefs.test.put(ctx, "TestPreference")
                            } else {
                                JLibPrefs.test.put(ctx, null)
                            }
                        }
                })
            }
        }
    }
}

@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Composable
private fun HomeScreenPreview() {
    SpaEnvironmentFactory.resetForPreview2()
    SettingsTheme {
        DefaultHomePageProvider.Page(null)
    }
}

@SuppressLint("ComposableNaming")
@Composable
private fun SpaEnvironmentFactory.resetForPreview2() {
    val context = LocalContext.current
    reset(JLibSpaEnvironmentStub(context))
}
