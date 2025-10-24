package io.github.dot166.jlib.internal

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.CircularProgressIndicator
import androidx.core.net.toUri

class ContributorRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyleAttr) {
    var mNameView: TextView
    var mDescriptionView: TextView
    var mUrl: String? = null
    var mPhotoView: ImageView
    var mLoadingSpinner: CircularProgressIndicator

    init {
        LayoutInflater.from(context)
            .inflate(io.github.dot166.jlib.R.layout.contributor_row, this, true)
        mNameView = findViewById<TextView>(io.github.dot166.jlib.R.id.name)
        mDescriptionView = findViewById<TextView>(io.github.dot166.jlib.R.id.description)
        mPhotoView = findViewById<ImageView>(io.github.dot166.jlib.R.id.imageView)
        mLoadingSpinner =
            findViewById<CircularProgressIndicator>(io.github.dot166.jlib.R.id.progress)
        setOnClickListener { v: View? ->
            if (mUrl != null && !mUrl!!.isEmpty()) {
                val intent = CustomTabsIntent.Builder()
                    .build()
                intent.launchUrl(context, mUrl!!.toUri())
            }
        }
    }

    fun setName(name: String?) {
        mNameView.text = name
    }

    fun setDescription(description: String?) {
        mDescriptionView.text = description
    }

    fun setUrl(url: String?) {
        mUrl = url
    }

    fun setPhotoUrl(photoUrl: String?) {
        mLoadingSpinner.visibility = VISIBLE
        Glide.with(this)
            .load(photoUrl)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    mLoadingSpinner.visibility = INVISIBLE
                    return false
                }
            })
            .into(mPhotoView)
    }
}
