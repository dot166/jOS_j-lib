package io.github.dot166.jlib.app

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.compose.runtime.Composable
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
                    text = stringResource(R.string.you_forgot_to_set_the_correct_implementation_of_spaenvironment_this_is_the_default_one_for_testing_jlib_preference_objects)
                )
                SettingsBanner(model)
            }
//            Category {
//                PreferenceMainPageProvider.Entry()
//                RestrictedSwitchPreferencePageProvider.Entry()
//            }
//            Category {
//                SearchScaffoldPageProvider.Entry()
//                GlifScaffoldPageProvider.Entry()
//                ArgumentPageProvider.EntryItem(stringParam = "foo", intParam = 0)
//            }
//            Category {
//                SliderPageProvider.Entry()
//                SpinnerPageProvider.Entry()
//                PagerMainPageProvider.Entry()
//                FooterPageProvider.Entry()
//                IllustrationPageProvider.Entry()
//                CategoryPageProvider.Entry()
//                ActionButtonPageProvider.Entry()
//                ProgressBarPageProvider.Entry()
//                LoadingBarPageProvider.Entry()
//                ChartPageProvider.Entry()
//                DialogMainPageProvider.Entry()
//                EditorMainPageProvider.Entry()
//                BannerPageProvider.Entry()
//                CardPageProvider.Entry()
//                CopyablePageProvider.Entry()
//            }
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
