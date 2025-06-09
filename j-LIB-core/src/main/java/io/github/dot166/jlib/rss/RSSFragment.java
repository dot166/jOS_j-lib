package io.github.dot166.jlib.rss;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import io.github.dot166.jlib.R;

public class RSSFragment extends Fragment {

    private int mId;

    RSSFragment() {
        new RSSFragment(-1);
    }

    RSSFragment(int id) {
        mId = id;
    }

    private RecyclerView mRecyclerView;
    private ArticleAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private RSSViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String[] rssUrls = PreferenceManager.getDefaultSharedPreferences(requireContext()).getString("rssUrls", "").split(";");
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
                for (String url : rssUrls) {
                    viewModel.fetchFeed(url, getContext());
                }
            } else {
                viewModel.fetchFeed(rssUrls[mId], getContext());
            }
        });

        if (mId == -1) {
            for (String url : rssUrls) {
                viewModel.fetchFeed(url, getContext());
            }
        } else {
            viewModel.fetchFeed(rssUrls[mId], getContext());
        }
        return view;
    }

    public int getIdRss() {
        return mId;
    }
}
