package io.github.dot166.jlib.app

import android.content.Context
import com.android.settingslib.spa.debug.DebugLogger
import com.android.settingslib.spa.framework.common.SettingsPageProviderRepository
import com.android.settingslib.spa.framework.common.SpaEnvironment
import com.android.settingslib.spa.framework.common.createSettingsPage
import com.android.settingslib.widget.theme.flags.Flags


class JLibSpaEnvironment(context: Context) : SpaEnvironment(context) {
    override val pageProviderRepository = lazy {
        SettingsPageProviderRepository(
            allPageProviders =
                listOf(
                    HomePageProvider,
                ),
            rootPages = listOf(HomePageProvider.createSettingsPage()),
        )
    }

    override val logger = DebugLogger()

    override val browseActivityClass = PreferenceMainActivity::class.java

    //override val searchProviderAuthorities = "com.android.spa.gallery.search.provider"

    override val isSpaExpressiveEnabled = Flags.isExpressiveDesignEnabled()

    //override fun getRestrictedRepository(context: Context) = GalleryRestrictedRepository(context)
}