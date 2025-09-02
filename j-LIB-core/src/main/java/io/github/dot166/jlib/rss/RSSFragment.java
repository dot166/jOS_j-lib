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
import io.github.dot166.jlib.app.jActivity;

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
        if (getActivity() instanceof jActivity) {
            ((jActivity)getActivity()).forceNotificationPermission();
        }
        String[] rssUrls = PreferenceManager.getDefaultSharedPreferences(requireContext()).getString("rssUrls", "").split(";");
        View view = inflater.inflate(R.layout.fragment_rss, container, false);
        viewModel = new ViewModelProvider(this).get(RSSViewModel.class);

        progressBar = view.findViewById(R.id.progress);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        progressBar.setVisibility(View.VISIBLE);

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
            if (mAdapter != null) {
                mAdapter.getArticleList().clear();
                mAdapter.notifyDataSetChanged();
            }
            mSwipeRefreshLayout.setRefreshing(true);
            progressBar.setVisibility(View.VISIBLE);
            if (mId == 0) {
                viewModel.fetchAllFeedsAsync(rssUrls);
            } else {
                viewModel.fetchFeedAsync(rssUrls[mId-1]);
            }
        });

        if (mId == 0) {
            viewModel.fetchAllFeedsAsync(rssUrls);
        } else {
            viewModel.fetchFeedAsync(rssUrls[mId-1]);
        }
        return view;
    }

    public int getIdRss() {
        return mId;
    }
}
