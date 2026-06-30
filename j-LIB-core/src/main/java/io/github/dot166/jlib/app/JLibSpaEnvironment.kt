package io.github.dot166.jlib.app

import android.content.Context
import com.android.settingslib.spa.framework.BrowseActivity
import com.android.settingslib.spa.framework.common.SettingsPageProviderRepository
import com.android.settingslib.spa.framework.common.SpaEnvironment
import com.android.settingslib.spa.framework.common.createSettingsPage
import com.android.settingslib.widget.theme.flags.Flags


abstract class JLibSpaEnvironment(context: Context) : SpaEnvironment(context) {
    override val logger = InfoLogger()

    override val browseActivityClass: Class<out BrowseActivity> = BrowseActivity::class.java

    //override val searchProviderAuthorities = "com.android.spa.gallery.search.provider"

    override val isSpaExpressiveEnabled = Flags.isExpressiveDesignEnabled()

    //override fun getRestrictedRepository(context: Context) = GalleryRestrictedRepository(context)
}

class JLibSpaEnvironmentStub(context: Context): JLibSpaEnvironment(context) {
    override val pageProviderRepository = lazy {
        SettingsPageProviderRepository(
            allPageProviders =
                listOf(
                    DefaultHomePageProvider,
                ),
            rootPages = listOf(DefaultHomePageProvider.createSettingsPage()),
        )
    }
}