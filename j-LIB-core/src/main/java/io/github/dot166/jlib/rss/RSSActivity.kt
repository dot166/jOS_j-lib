package io.github.dot166.jlib.rss

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import io.github.dot166.jlib.R
import io.github.dot166.jlib.app.jActivity
import io.github.dot166.jlib.registry.RegistryEditActivity
import io.github.dot166.jlib.time.ReminderItem
import java.util.Calendar

class RSSActivity : jActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rss)
        val toolbar = findViewById<Toolbar?>(R.id.actionbar)
        setSupportActionBar(toolbar)
        supportFragmentManager.beginTransaction().add(R.id.fragment, RSSFragment()).commit()
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val reminderItem = ReminderItem(cal.getTimeInMillis(), 1)
        RSSAlarmScheduler(this).schedule(reminderItem)
    }
}