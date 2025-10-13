package io.github.dot166.jlib.internal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import io.github.dot166.jlib.R;

public class ContributorRow extends CardView {
    TextView mNameView;
    TextView mDescriptionView;
    String mUrl;
    ImageView mPhotoView;
    CircularProgressIndicator mLoadingSpinner;

    public ContributorRow(@NonNull Context context) {
        this(context, null);
    }

    public ContributorRow(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, androidx.cardview.R.attr.cardViewStyle);
    }

    public ContributorRow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.contributor_row, this, true);
        mNameView = findViewById(R.id.name);
        mDescriptionView = findViewById(R.id.description);
        mPhotoView = findViewById(R.id.imageView);
        mLoadingSpinner = findViewById(R.id.progress);
        setOnClickListener(v -> {
            CustomTabsIntent intent = new CustomTabsIntent.Builder()
                    .build();
            intent.launchUrl(context, Uri.parse(mUrl));
        });
    }

    public void setName(String name) {
        mNameView.setText(name);
    }

    public void setDescription(String description) {
        mDescriptionView.setText(description);
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public void setPhotoUrl(String photoUrl) {
        mLoadingSpinner.setVisibility(VISIBLE);
        Glide.with(this)
                .load(photoUrl)
                .listener(new RequestListener<>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mLoadingSpinner.setVisibility(INVISIBLE);
                        return false;
                    }
                })
                .into(mPhotoView);
    }
}
