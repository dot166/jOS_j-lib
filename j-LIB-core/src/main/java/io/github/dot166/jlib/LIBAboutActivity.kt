package io.github.dot166.jlib

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import io.github.dot166.jlib.app.jAboutActivity
import io.github.dot166.jlib.utils.VersionUtils.libVersion
import java.util.Objects

class LIBAboutActivity : jAboutActivity() {
    override fun versionIntent(context: Context): Intent {
        return Intent(context, LIBTestActivity::class.java)
    }

    override fun getAppIcon(context: Context): Drawable {
        return Objects.requireNonNull<Drawable>(
            AppCompatResources.getDrawable(
                context,
                R.mipmap.ic_launcher_j
            )
        )
    }

    override fun getAppLabel(context: Context): String {
        return getString(R.string.jlib)
    }

    override fun versionName(context: Context): String {
        return libVersion
    }

    override fun product(): List<Contributor> {
        return object : ArrayList<Contributor>() {
            init {
                add(
                    Contributor(
                        "._______166",
                        Role.Maintainer,
                        "https://avatars.githubusercontent.com/u/62702353",
                        "https://github.com/dot166"
                    )
                )
                add(
                    Contributor(
                        "bh916",
                        Role.Contributor,
                        "https://avatars.githubusercontent.com/u/138221251",
                        "https://github.com/bh196"
                    )
                )
            }
        }
    }

    enum class Role(val descriptionResId: Int) : Roles {
        Maintainer(R.string.maintainer),
        Contributor(R.string.contributor);

        override fun descriptionResId(): Int {
            return this.descriptionResId
        }
    }
}
