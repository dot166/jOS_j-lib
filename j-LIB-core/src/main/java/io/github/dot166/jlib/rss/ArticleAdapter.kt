package io.github.dot166.jlib.rss

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.prof18.rssparser.model.RssItem
import io.github.dot166.jlib.R
import io.github.dot166.jlib.app.MediaPlayerActivity
import io.github.dot166.jlib.utils.DateUtils.convertFromCommonFormats
import androidx.core.net.toUri

class ArticleAdapter(val articleList: MutableList<RssItem>?) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.row, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentArticle = articleList!![position]

        val sourceDateString = currentArticle.pubDate

        var pubDateString = sourceDateString

        if (sourceDateString != null) {
            pubDateString = convertFromCommonFormats(sourceDateString)
        }

        viewHolder.title.text = currentArticle.title

        viewHolder.pubDate.text = pubDateString

        if (currentArticle.categories.isEmpty() || PreferenceManager.getDefaultSharedPreferences(
                viewHolder.category.context
            ).getBoolean("show_desc_by_default_in_rss", false)
        ) {
            viewHolder.category.text = currentArticle.description
        } else {
            viewHolder.category.text = currentArticle.categories.toTypedArray().contentToString()
        }

        viewHolder.itemView.setOnClickListener { view: View? ->
            if (currentArticle.rawEnclosure != null && currentArticle.rawEnclosure!!.url != null && !currentArticle.rawEnclosure!!.url!!.isEmpty()) {
                if (currentArticle.rawEnclosure!!.type != null && currentArticle.rawEnclosure!!.type!!.contains(
                        "audio"
                    )
                ) {
                    val drawUrl: String? =
                        if (currentArticle.itunesItemData != null && currentArticle.itunesItemData!!.image != null) {
                            currentArticle.itunesItemData!!.image
                        } else {
                            ""
                        }
                    MediaPlayerActivity.playAudio(
                        currentArticle.rawEnclosure!!.url,
                        view!!.context,
                        drawUrl,
                        currentArticle.title
                    )
                } else {
                    // do not know what to do with anything else so send it to webview
                    val webpage = currentArticle.rawEnclosure!!.url!!.toUri()
                    val intent = CustomTabsIntent.Builder()
                        .build()
                    intent.launchUrl(view!!.context, webpage)
                }
            } else if (currentArticle.link != null && !currentArticle.link!!.isEmpty()) {
                val webpage = currentArticle.link!!.toUri()
                val intent = CustomTabsIntent.Builder()
                    .build()
                intent.launchUrl(view!!.context, webpage)
            } else if (currentArticle.content != null && !currentArticle.content!!.isEmpty()) {
                val builder2 = MaterialAlertDialogBuilder(view!!.context)

                builder2.setMessage(currentArticle.content)
                    .setTitle("Rss Content")
                    .setIcon(R.drawable.outline_rss_feed_24)
                    .setCancelable(false)
                    .setNeutralButton(
                        R.string.ok
                    ) { dialog, id -> dialog.dismiss() }
                //Creating dialog box
                builder2.show()
            } else {
                Toast.makeText(
                    view!!.context,
                    "no content or url available",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewHolder.itemView.setOnLongClickListener { view: View? ->
            if (currentArticle.rawEnclosure != null && currentArticle.rawEnclosure!!.url != null && !currentArticle.rawEnclosure!!.url!!.isEmpty()) {
                val webpage = currentArticle.rawEnclosure!!.url!!.toUri()
                val intent = CustomTabsIntent.Builder()
                    .build()
                intent.launchUrl(view!!.context, webpage)
            } else if (currentArticle.link != null && !currentArticle.link!!.isEmpty()) {
                val webpage = currentArticle.link!!.toUri()
                val intent = CustomTabsIntent.Builder()
                    .build()
                intent.launchUrl(view!!.context, webpage)
            } else if (currentArticle.content != null && !currentArticle.content!!.isEmpty()) {
                val builder2 = MaterialAlertDialogBuilder(view!!.context)

                builder2.setMessage(currentArticle.content)
                    .setTitle("Rss Content")
                    .setIcon(R.drawable.outline_rss_feed_24)
                    .setCancelable(false)
                    .setNeutralButton(R.string.ok
                    ) { dialog, id -> dialog.dismiss() }
                //Creating dialog box
                builder2.show()
            } else {
                Toast.makeText(
                    view!!.context,
                    "no content or url available",
                    Toast.LENGTH_SHORT
                ).show()
            }
            false
        }
    }

    override fun getItemCount(): Int {
        return if (this.articleList == null) 0 else articleList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var pubDate: TextView = itemView.findViewById(R.id.pubDate)
        var category: TextView = itemView.findViewById(R.id.categories)
    }
}