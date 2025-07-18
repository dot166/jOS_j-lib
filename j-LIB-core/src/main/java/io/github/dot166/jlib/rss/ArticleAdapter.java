package io.github.dot166.jlib.rss;

import static android.widget.Toast.LENGTH_SHORT;

import static io.github.dot166.jlib.utils.DateUtils.convertFromCommonFormats;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prof18.rssparser.model.RssItem;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.github.dot166.jlib.R;
import io.github.dot166.jlib.app.MediaPlayerActivity;
import io.github.dot166.jlib.utils.ErrorUtils;
import io.github.dot166.jlib.web.jWebIntent;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<RssItem> articles;

    public ArticleAdapter(List<RssItem> list) {
        this.articles = list;
    }

    public List<RssItem> getArticleList() {
        return articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        RssItem currentArticle = articles.get(position);

        String sourceDateString = currentArticle.getPubDate();

        String pubDateString = sourceDateString;

        if (sourceDateString != null) {
            pubDateString = convertFromCommonFormats(sourceDateString);
        }

        viewHolder.title.setText(currentArticle.getTitle());

        viewHolder.pubDate.setText(pubDateString);

        if (currentArticle.getCategories().isEmpty() || PreferenceManager.getDefaultSharedPreferences(viewHolder.category.getContext()).getBoolean("show_desc_by_default_in_rss", false)) {
            viewHolder.category.setText(currentArticle.getDescription());
        } else {
            viewHolder.category.setText(Arrays.toString(currentArticle.getCategories().toArray()));
        }

        viewHolder.itemView.setOnClickListener(view -> {
            if (currentArticle.getRawEnclosure() != null && currentArticle.getRawEnclosure().getUrl() != null && !currentArticle.getRawEnclosure().getUrl().isEmpty()) {
                if (currentArticle.getRawEnclosure().getType() != null && currentArticle.getRawEnclosure().getType().contains("audio")) {
                    String drawUrl;
                    if (currentArticle.getItunesItemData() != null && currentArticle.getItunesItemData().getImage() != null) {
                        drawUrl = currentArticle.getItunesItemData().getImage();
                    } else {
                        drawUrl = "";
                    }
                    MediaPlayerActivity.playAudio(currentArticle.getRawEnclosure().getUrl(), view.getContext(), drawUrl, currentArticle.getTitle());
                } else {
                    // do not know what to do with anything else so send it to webview
                    jWebIntent webIntent = new jWebIntent(view.getContext());
                    webIntent.setUrl(currentArticle.getRawEnclosure().getUrl());
                    webIntent.configureWebView(true, true);
                    webIntent.launch();
                }
            } else if (currentArticle.getLink() != null && !currentArticle.getLink().isEmpty()) {
                jWebIntent webIntent = new jWebIntent(view.getContext());
                webIntent.setUrl(currentArticle.getLink());
                webIntent.configureWebView(true, true);
                webIntent.launch();
            } else {
                Toast.makeText(view.getContext(), "no url available", LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles == null ? 0 : articles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView pubDate;
        TextView category;

        public ViewHolder(View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.title);
            pubDate = itemView.findViewById(R.id.pubDate);
            category = itemView.findViewById(R.id.categories);
        }
    }
}