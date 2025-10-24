package io.github.dot166.jlib.app

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.google.android.material.chip.Chip
import com.google.android.material.color.MaterialColors
import io.github.dot166.jlib.R
import io.github.dot166.jlib.internal.ContributorRow
import io.github.dot166.jlib.utils.AppUtils
import io.github.dot166.jlib.utils.ErrorUtils.handle

open class jAboutActivity : jActivity() {
    open fun versionIntent(context: Context): Intent {
        return Intent()
    }

    open fun getAppIcon(context: Context): Drawable? {
        return AppUtils.getAppIcon(context)
    }

    open fun getAppLabel(context: Context): String {
        return AppUtils.getAppLabel(context)
    }

    interface Roles {
        fun descriptionResId(): Int
    }

    enum class Role(private val descriptionResId: Int) : Roles {
        Maintainer(R.string.maintainer),
        Contributor(R.string.contributor),
        Example(R.string.example_info);

        override fun descriptionResId(): Int {
            return this.descriptionResId
        }
    }

    @JvmRecord
    data class Contributor(
        val name: String?,
        val role: Roles?,
        val photoUrl: String?,
        val socialUrl: String?
    )

    @JvmRecord
    data class Link(
        @field:DrawableRes val iconResId: Int,
        @field:StringRes val labelResId: Int,
        val url: String?
    )

    fun showOnlyContributors(): Boolean {
        return false
    }

    open fun product(): List<Contributor> {
        return defaultProduct
    }

    private val defaultProduct: List<Contributor> = listOf<Contributor>(
        Contributor(
            "._______166",
            Role.Maintainer,
            "https://avatars.githubusercontent.com/u/62702353",
            "https://github.com/jf916"
        ),
        Contributor(
            "bh916",
            Role.Contributor,
            "https://avatars.githubusercontent.com/u/138221251",
            "https://github.com/bh916"
        ),
        Contributor(
            "Put your main devs or contributors here",
            Role.Example,
            "https://upload.wikimedia.org/wikipedia/commons/9/99/Sample_User_Icon.png",
            "https://example.com"
        )
    )

    fun links(): List<Link> {
        return defaultLinks
    }

    private val defaultLinks: List<Link> = listOf<Link>(
        Link(
            R.drawable.ic_github,
            R.string.github,
            "https://github.com/dot166/jOS_j-LIB"
        )
    )

    open fun versionName(context: Context): String {
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return (if (pInfo.versionName != null) pInfo.versionName else "")!!
        } catch (e: PackageManager.NameNotFoundException) {
            handle(e, context)
            return ""
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aboutactivity)
        val context: Context = this

        if (!showOnlyContributors()) {
            (findViewById<View?>(R.id.app_icon) as ImageView).setImageDrawable(getAppIcon(context))
            (findViewById<View?>(R.id.app_name) as TextView).text = getAppLabel(context)
            val versionView = findViewById<TextView>(R.id.app_version)
            versionView.text = versionName(this)
            versionView.setOnLongClickListener {
                try {
                    startActivity(versionIntent(context))
                } catch (_: Exception) {
                    Log.i("LibTest", "Test Activity Disabled")
                    Log.i(
                        "GLaDOS",
                        "you have completed all available tests, you will now receive cake"
                    )
                }
                true
            }

            val linkContainer = findViewById<LinearLayout>(R.id.link_container)
            linkContainer.removeAllViews()
            for (link in links()) {
                val button = Chip(context)
                button.chipIcon = AppCompatResources.getDrawable(context, link.iconResId)
                button.text = context.getString(link.labelResId)
                button.chipIconTint = ColorStateList.valueOf(
                    MaterialColors.getColor(
                        button,
                        com.google.android.material.R.attr.colorOnSurface
                    )
                )
                val params = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                params.setMarginStart(4)
                params.setMarginEnd(4)
                button.setLayoutParams(params)
                button.setOnClickListener {
                    if (link.url != null) {
                        val webpage = link.url.toUri()
                        val intent = CustomTabsIntent.Builder().build()
                        intent.launchUrl(context, webpage)
                    }
                }
                linkContainer.addView(button)
            }
        }

        val contributorsContainer = findViewById<LinearLayout>(R.id.contributors_container)
        for (contributor in product()) {
            val row = ContributorRow(context)
            row.setName(contributor.name)
            row.setDescription(context.getString(contributor.role!!.descriptionResId()))
            row.setUrl(contributor.socialUrl)
            row.setPhotoUrl(contributor.photoUrl)
            contributorsContainer.addView(row)
        }

        if (!showOnlyContributors()) {
            val licencesButton = findViewById<Button>(R.id.licences_button)
            licencesButton.setOnClickListener {
                try {
                    startActivity(Intent(context, OSSLicenceActivity::class.java))
                } catch (e: Exception) {
                    handle(e, context)
                }
            }
        }
        setSupportActionBar(findViewById(R.id.actionbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeActionContentDescription(androidx.appcompat.R.string.abc_action_bar_up_description)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}
