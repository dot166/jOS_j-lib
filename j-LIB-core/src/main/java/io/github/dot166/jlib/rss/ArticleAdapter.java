package io.github.dot166.jlib.rss;

import static android.widget.Toast.LENGTH_SHORT;

import static io.github.dot166.jlib.utils.DateUtils.convertFromCommonFormats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prof18.rssparser.model.RssItem;

import java.util.List;

import io.github.dot166.jlib.R;
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

        viewHolder.category.setText(currentArticle.getDescription());

        viewHolder.itemView.setOnClickListener(view -> {
            if (currentArticle.getLink() != null && !currentArticle.getLink().isEmpty()) {
                jWebIntent webIntent = new jWebIntent(view.getContext());
                webIntent.setUrl(currentArticle.getLink());
                webIntent.configureWebView(true, true);
                webIntent.launch();
            } else {
                Toast.makeText(view.getContext(), "RSS Reader is in fallback mode", LENGTH_SHORT).show();
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