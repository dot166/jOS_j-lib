package io.github.dot166.jlib.rss;

import static io.github.dot166.jlib.utils.DateUtils.convertDateToEpochSeconds;
import static io.github.dot166.jlib.utils.DateUtils.convertFromCommonFormats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.prof18.rssparser.model.RssChannel;
import com.prof18.rssparser.model.RssItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import io.github.dot166.jlib.R;

public class RSSFragment extends Fragment {

    private int mId;

    public RSSFragment() {
        new RSSFragment(0);
    }

    public RSSFragment(int id) {
        mId = id;
    }

    private RecyclerView mRecyclerView;
    private ArticleAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private RSSViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String[] rssUrls = PreferenceManager.getDefaultSharedPreferences(requireContext()).getString("rssUrls", "https://podcasts.files.bbci.co.uk/p02pc9pj.rss").split(";");
        View view = inflater.inflate(R.layout.fragment_rss, container, false);

        viewModel = new ViewModelProvider(this).get(RSSViewModel.class);

        progressBar = view.findViewById(R.id.progress);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        viewModel.getChannel().observe(getViewLifecycleOwner(), channel -> {
            if (channel != null) {
                if (channel.getTitle() != null) {
                    requireActivity().setTitle(channel.getTitle());
                }
                mAdapter = new ArticleAdapter(channel.getItems());
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        viewModel.getSnackbar().observe(getViewLifecycleOwner(), s -> {
            if (s != null) {
                Snackbar.make(view, s, Snackbar.LENGTH_LONG).show();
                viewModel.onSnackbarShowed();
            }
        });

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setColorSchemeColors(
                requireContext().obtainStyledAttributes(new int[]{com.google.android.material.R.attr.colorPrimary}).getColor(0, 0)
        );
        mSwipeRefreshLayout.canChildScrollUp();
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mAdapter.getArticleList().clear();
            mAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(true);
            if (mId == 0) {
                List<RssItem> list = new ArrayList<>();
                for (String url : rssUrls) {
                    RssChannel channel = viewModel.fetchFeedWithoutViewModel(url, getContext());
                    for (int i = 0; i < channel.getItems().toArray().length; i++) {
                        list.add(channel.getItems().get(i));
                    }
                    if (Objects.equals(channel.getItems().get(0).getDescription(), "Something failed and to keep the app running this is displayed")) { // should only trigger on error handler
                        // stop parsing here, prevent feed being full of the same error
                        break;
                    }
                }
                list.sort(new Comparator<RssItem>() {
                    @Override
                    public int compare(RssItem o1, RssItem o2) {
                        return Long.compare(convertDateToEpochSeconds(convertFromCommonFormats(o2.getPubDate())), convertDateToEpochSeconds(convertFromCommonFormats(o1.getPubDate())));
                    }
                });
                viewModel.setChannel(new RssChannel("All Feeds", null, null, null, null, null, list, null, null));
            } else {
                viewModel.fetchFeed(rssUrls[mId-1], getContext());
            }
        });

        if (mId == 0) {
            List<RssItem> list = new ArrayList<>();
            for (String url : rssUrls) {
                RssChannel channel = viewModel.fetchFeedWithoutViewModel(url, getContext());
                for (int i = 0; i < channel.getItems().toArray().length; i++) {
                    list.add(channel.getItems().get(i));
                }
            }
            list.sort(new Comparator<RssItem>() {
                @Override
                public int compare(RssItem o1, RssItem o2) {
                    return Long.compare(convertDateToEpochSeconds(convertFromCommonFormats(o2.getPubDate())), convertDateToEpochSeconds(convertFromCommonFormats(o1.getPubDate())));
                }
            });
            viewModel.setChannel(new RssChannel("All Feeds", null, null, null, null, null, list, null, null));
        } else {
            viewModel.fetchFeed(rssUrls[mId-1], getContext());
        }
        return view;
    }

    public int getIdRss() {
        return mId;
    }
}
