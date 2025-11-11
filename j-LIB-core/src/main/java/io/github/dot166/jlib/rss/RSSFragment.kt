package io.github.dot166.jlib.rss

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.prof18.rssparser.model.RssChannel
import io.github.dot166.jlib.R
import io.github.dot166.jlib.app.jActivity
import io.github.dot166.jlib.registry.RegistryHelper
import io.github.dot166.jlib.registry.XmlHelper.writeXmlToFile
import androidx.core.content.edit
import com.prof18.rssparser.model.RssItem

class RSSFragment : Fragment {
    var idRss: Int = 0
        private set

    constructor() {
        RSSFragment(0)
    }

    constructor(id: Int) {
        this.idRss = id
    }

    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: ArticleAdapter? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var progressBar: CircularProgressIndicator? = null
    private var viewModel: RSSViewModel? = null

    @SuppressLint("NotifyDataSetChanged", "Recycle")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (activity is jActivity) {
            (activity as jActivity).forceNotificationPermission()
        }
        val feeds = RegistryHelper.getFromRegistry(requireContext())
        val rssUrls = arrayOfNulls<String>(feeds.size)
        for (i in feeds.indices) {
            rssUrls[i] = feeds[i].url
        }
        val view = inflater.inflate(R.layout.fragment_rss, container, false)
        viewModel = ViewModelProvider(this)[RSSViewModel::class.java]

        progressBar = view.findViewById(R.id.progress)
        mRecyclerView = view.findViewById(R.id.recycler_view)
        mRecyclerView!!.setLayoutManager(LinearLayoutManager(context))
        mRecyclerView!!.setItemAnimator(DefaultItemAnimator())
        mRecyclerView!!.setHasFixedSize(true)
        progressBar!!.visibility = View.VISIBLE
        val feedKey: String? = if (this.idRss == 0) {
            "ALL"
        } else {
            rssUrls[this.idRss - 1]
        }
        viewModel!!.getChannel(feedKey)
            .observe(getViewLifecycleOwner(), Observer { channel: RssChannel? ->
                if (channel != null) {
                    if (channel.title != null) {
                        requireActivity().setTitle(channel.title)
                    }
                    mAdapter = ArticleAdapter(channel.items as MutableList<RssItem>)
                    mRecyclerView!!.setAdapter(mAdapter)
                    mAdapter!!.notifyDataSetChanged()
                    progressBar!!.visibility = View.GONE
                    mSwipeRefreshLayout!!.isRefreshing = false
                }
            })

        viewModel!!.getSnackbar().observe(getViewLifecycleOwner(), Observer { s: String? ->
            if (s != null) {
                Snackbar.make(view, s, Snackbar.LENGTH_LONG).show()
                viewModel!!.onSnackbarShowed()
            }
        })

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout)
        mSwipeRefreshLayout!!.setColorSchemeColors(
            requireContext().obtainStyledAttributes(intArrayOf(androidx.appcompat.R.attr.colorPrimary))
                .getColor(0, 0)
        )
        mSwipeRefreshLayout!!.canChildScrollUp()
        mSwipeRefreshLayout!!.setOnRefreshListener {
            if (mAdapter != null) {
                mAdapter!!.articleList.clear()
                mAdapter!!.notifyDataSetChanged()
            }
            mSwipeRefreshLayout!!.isRefreshing = true
            progressBar!!.visibility = View.VISIBLE
            if (this.idRss == 0) {
                viewModel!!.fetchAllFeedsAsync(rssUrls)
            } else {
                viewModel!!.fetchFeedAsync(rssUrls[this.idRss - 1]!!)
            }
        }

        if (this.idRss == 0) {
            viewModel!!.fetchAllFeedsAsync(rssUrls)
        } else {
            viewModel!!.fetchFeedAsync(rssUrls[this.idRss - 1]!!)
        }
        return view
    }
}
