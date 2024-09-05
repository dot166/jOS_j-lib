package jOS.Core

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import jOS.Core.ThemeEngine.isDarkTheme
import jOS.Core.utils.ContributorRow
import jOS.Core.utils.ErrorUtils
import jOS.Core.utils.IconUtils
import jOS.Core.utils.LabelUtils.getAppLabel

open class jAboutActivity : jActivity() {

    open fun versionIntent(context : Context): Intent {
        return Intent()
    }

    interface Roles {
        fun descriptionResId(): Int
    }

    enum class Role(val descriptionResId: Int) : Roles {
        LeadDev(descriptionResId = R.string.leaddev),
        Dev(descriptionResId = R.string.dev),
        Example(descriptionResId = R.string.example_info),;

        override fun descriptionResId(): Int {
            return this.descriptionResId
        }
    }

    data class Contributor(
        val name: String,
        val role: Roles,
        val photoUrl: String,
        val socialUrl: String,
    )

    data class Link(
        @DrawableRes val iconResId: Int,
        @StringRes val labelResId: Int,
        val url: String,
    )

    open fun showOnlyContributors(context : Context) : Boolean {
        return false
    }

    open fun product() : List<Contributor> {
        return defaultProduct
    }

    private val defaultProduct = listOf(
        Contributor(
            name = "._______166",
            role = Role.LeadDev,
            photoUrl = "https://avatars.githubusercontent.com/u/62702353",
            socialUrl = "https://github.com/jf916",
        ),
        Contributor(
            name = "bh916",
            role = Role.Dev,
            photoUrl = "https://avatars.githubusercontent.com/u/138221251",
            socialUrl = "https://github.com/bh916",
        ),
        Contributor(
            name = "Put your main devs or contributors here",
            role = Role.Example,
            photoUrl = "https://upload.wikimedia.org/wikipedia/commons/9/99/Sample_User_Icon.png",
            socialUrl = "https://example.com",
        )
    )

    open fun links(): List<Link> {
        return defaultLinks;
    }

    private val defaultLinks = listOf(
        Link(
            iconResId = R.drawable.ic_github,
            labelResId = R.string.github,
            url = "https://github.com/dot166/jOS_j-LIB",
        ),
    )

    private fun versionName(context: Context): String {
        try {
            val pInfo: PackageInfo =
                context.packageManager.getPackageInfo(context.packageName, 0)
            return pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            ErrorUtils.handle(e, context)
            return ""
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    open fun About() {
        val context = LocalContext.current

        Column(
            modifier = Modifier.padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (!showOnlyContributors(context)) {
                Image(
                    painter = rememberDrawablePainter(drawable = IconUtils.getActivityIcon(context)),
                    contentDescription = null,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = getAppLabel(context),
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = versionName(context),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.combinedClickable(
                        onClick = {},
                        onLongClick = {
                            context.startActivity(versionIntent(context))
                        },
                    ),
                )
                Spacer(modifier = Modifier.requiredHeight(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    links().forEach { link ->
                        AssistChip(
                            leadingIcon = {
                                Image(
                                    painter = painterResource(id = link.iconResId),
                                    contentDescription = stringResource(id = link.labelResId),
                                    colorFilter = ColorFilter.tint(color = LocalContentColor.current)
                                )
                            },
                            label = { Text(stringResource(id = link.labelResId)) },
                            modifier = Modifier.weight(weight = 1f),
                            onClick = {
                                val webpage = Uri.parse(link.url)
                                val intent = CustomTabsIntent.Builder()
                                    .build()
                                intent.launchUrl(context, webpage)
                            }
                        )
                    }
                }
            }
            Contributors()
            if (!showOnlyContributors(context)) {
                Button(onClick = {
                    try {
                        startActivity(
                            Intent(
                                context,
                                OSSLicenceActivity::class.java
                            )
                        )
                    } catch (e : Exception) {
                        ErrorUtils.handle(e, context)
                    }
                }) {
                    Text(
                        stringResource(id = R.string.licences)
                    )
                }
            }
        }
    }

    @Composable
    fun Contributors() {
        Surface(modifier = Modifier.padding(horizontal = 16.dp).border(1.0.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),) {
            Column() {
                product().forEach {
                    ContributorRow(
                        name = it.name,
                        description = stringResource(it.role.descriptionResId()),
                        url = it.socialUrl,
                        photoUrl = it.photoUrl,
                    )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        configure(R.layout.aboutactivity, false)
        super.onCreate(savedInstanceState)
        findViewById<ComposeView>(R.id.my_composable).setContent {
            MaterialTheme(
                colorScheme = if (isDarkTheme) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        dynamicDarkColorScheme(this)
                    } else {
                        darkColorScheme()
                    }
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    dynamicLightColorScheme(this)
                } else {
                    lightColorScheme()
                }
            ) {
                Surface {
                    About()
                }
            }
        }
    }
}
