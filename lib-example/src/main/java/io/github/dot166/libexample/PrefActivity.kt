package io.github.dot166.libexample

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.android.settingslib.preference.PreferenceFragment
import com.android.settingslib.widget.SettingsSpinnerAdapter
import com.android.settingslib.widget.SettingsSpinnerPreference2
import io.github.dot166.jlib.app.jConfigActivity

class PrefActivity: jConfigActivity() {
    override fun preferenceFragment(): PreferenceFragment {
        return PrefFragment()
    }

    class PrefFragment: PreferenceFragment() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            preferenceScreen = preferenceManager.createPreferenceScreen(requireContext())
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.removeAll()

            for (i in 0 until 24) {
                val ctx = requireContext()
                val appItemPref = SettingsSpinnerPreference2(ctx)
                val packageManager = ctx.packageManager
                appItemPref.summary = ctx.packageName
                appItemPref.title = i.toString() + getString(R.string.app_name)
                appItemPref.icon = packageManager.getApplicationIcon(ctx.packageName)
                val adapter = SettingsSpinnerAdapter<String>(ctx)
                adapter.addAll(mutableListOf<String>("Allowed", "Blocked", "5m", "10m", "15m", "30m", "1h", "1h 30m", "2h", "2h 30m", "3h", "3h 30m", "4h", "4h 30m", "5h", "5h 30m", "6h", "6h 30m", "7h", "7h 30m"))
                appItemPref.setAdapter(adapter)
                appItemPref.setSelection(0)
                appItemPref.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Nothing to implement
                    }
                })
                preferenceScreen.addPreference(appItemPref)
            }
        }
    }
}