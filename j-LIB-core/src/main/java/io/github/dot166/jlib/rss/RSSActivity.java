package io.github.dot166.jlib.rss;

import static android.widget.Toast.LENGTH_LONG;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import io.github.dot166.jlib.R;
import io.github.dot166.jlib.app.jActivity;
import io.github.dot166.jlib.time.ReminderItem;
import kotlin.jvm.internal.PackageReference;

public class RSSActivity extends jActivity {

    private RecyclerView mRecyclerView;
    private ArticleAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private RSSViewModel viewModel;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String rssUrl = PreferenceManager.getDefaultSharedPreferences(this).getString("rssUrl", "");
        setContentView(R.layout.activity_rss);
        Toolbar toolbar = findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);

        viewModel = new ViewModelProvider(this).get(RSSViewModel.class);

        progressBar = findViewById(R.id.progress);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        relativeLayout = findViewById(R.id.root_layout);

        viewModel.getChannel().observe(this, channel -> {
            if (channel != null) {
                if (channel.getTitle() != null) {
                    setTitle(channel.getTitle());
                }
                mAdapter = new ArticleAdapter(channel.getItems());
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        viewModel.getSnackbar().observe(this, s -> {
            if (s != null) {
                Snackbar.make(relativeLayout, s, Snackbar.LENGTH_LONG).show();
                viewModel.onSnackbarShowed();
            }
        });

        mSwipeRefreshLayout = findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setColorSchemeColors(
                obtainStyledAttributes(new int[]{com.google.android.material.R.attr.colorPrimary}).getColor(0, 0)
        );
        mSwipeRefreshLayout.canChildScrollUp();
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mAdapter.getArticleList().clear();
            mAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(true);
            viewModel.fetchFeed(rssUrl, this);
        });

        viewModel.fetchFeed(rssUrl, this);
        Toast.makeText(this, String.valueOf(((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).areNotificationsEnabled()), LENGTH_LONG).show();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 0);
        ReminderItem reminderItem = new ReminderItem(cal.getTimeInMillis(), 1);
        new RSSAlarmScheduler(this).schedule(reminderItem);
    }
}