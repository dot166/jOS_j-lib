package io.github.dot166.jlib.rss;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

import io.github.dot166.jlib.R;
import io.github.dot166.jlib.app.jActivity;
import io.github.dot166.jlib.time.ReminderItem;

public class RSSActivity extends jActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);
        Toolbar toolbar = findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, new RSSFragment()).commit();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        ReminderItem reminderItem = new ReminderItem(cal.getTimeInMillis(), 1);
        new RSSAlarmScheduler(this).schedule(reminderItem);
    }
}