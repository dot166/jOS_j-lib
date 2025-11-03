package io.github.dot166.jlib.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textview.MaterialTextView

class ContributorRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyleAttr) {
    var mNameView: MaterialTextView
    var mDescriptionView: MaterialTextView
    var mUrl: String? = null
    var mPhotoView: AppCompatImageView
    var mLoadingSpinner: CircularProgressIndicator

    init {
        LayoutInflater.from(context)
            .inflate(io.github.dot166.jlib.R.layout.contributor_row, this, true)
        mNameView = findViewById<MaterialTextView>(io.github.dot166.jlib.R.id.name)
        mDescriptionView = findViewById<MaterialTextView>(io.github.dot166.jlib.R.id.description)
        mPhotoView = findViewById<AppCompatImageView>(io.github.dot166.jlib.R.id.imageView)
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