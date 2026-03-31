package io.github.dot166.libexample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.preference.Preference
import androidx.preference.SwitchPreferenceCompat
import com.android.settingslib.collapsingtoolbar.widget.ScrollableToolbarItemLayout
import com.android.settingslib.preference.PreferenceFragment
import com.android.settingslib.widget.SettingsSpinnerAdapter
import com.android.settingslib.widget.SettingsSpinnerPreference2
import io.github.dot166.jlib.app.SettingsLibAlertDialogBuilder
import io.github.dot166.jlib.app.jConfigActivity

class PrefActivity: jConfigActivity() {
    override fun preferenceFragment(): PreferenceFragment {
        return PrefFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFloatingToolbarVisibility(true)
        val list = mutableListOf<ScrollableToolbarItemLayout.ToolbarItem>()
        list.add(
            ScrollableToolbarItemLayout.ToolbarItem(
                com.android.settingslib.widget.preference.banner.R.drawable.ic_warning,
                "バカ"
            )
        )
        list.add(
            ScrollableToolbarItemLayout.ToolbarItem(
                com.android.settingslib.widget.preference.banner.R.drawable.ic_warning,
                "バカ"
            )
        )
        list.add(
            ScrollableToolbarItemLayout.ToolbarItem(
                com.android.settingslib.widget.preference.banner.R.drawable.ic_warning,
                "バカ"
            )
        )
        list.add(
            ScrollableToolbarItemLayout.ToolbarItem(
                com.android.settingslib.widget.preference.banner.R.drawable.ic_warning,
                "バカ"
            )
        )
        list.add(
            ScrollableToolbarItemLayout.ToolbarItem(
                com.android.settingslib.widget.preference.banner.R.drawable.ic_warning,
                "バカ"
            )
        )
        setToolbarItems(list)
        setOnItemSelectedListener(object : ScrollableToolbarItemLayout.OnItemSelectedListener {
            override fun onItemSelected(
                position: Int,
                toolbarItem: ScrollableToolbarItemLayout.ToolbarItem
            ) {
                Log.i(position.toString(), toolbarItem.text)
                SettingsLibAlertDialogBuilder(this@PrefActivity)
                    .setMessage(position.toString() + toolbarItem.text)
                    .setIcon(toolbarItem.iconResId!!)
                    .setCancelable(false)
                    .setNegativeButton(
                        android.R.string.ok
                    ) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }

        })
    }

    class TestPrefFrag: PreferenceFragment() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            preferenceScreen = preferenceManager.createPreferenceScreen(requireContext())
            val pref = SwitchPreferenceCompat(requireContext())
            pref.title = "testPref_useless"
            preferenceScreen.addPreference(pref)
        }

        override fun onResume() {
            super.onResume()
            val activity: jConfigActivity = activity as jConfigActivity
            activity.setFloatingToolbarVisibility(false)
        }
    }

    class PrefFragment: PreferenceFragment() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            preferenceScreen = preferenceManager.createPreferenceScreen(requireContext())
            val pref = Preference(requireContext())
            pref.title = "testFrag"
            pref.fragment = TestPrefFrag::class.java.name
            preferenceScreen.addPreference(pref)

            for (i in 0 until 24) {
                val ctx = requireContext()
                val appItemPref = SettingsSpinnerPreference2(ctx)
                val packageManager = ctx.packageManager
                appItemPref.summary = ctx.packageName
                appItemPref.title = i.toString() + getString(R.string.app_name)
                appItemPref.icon = packageManager.getApplicationIcon(ctx.packageName)
                val adapter = SettingsSpinnerAdapter<String>(ctx)
                adapter.addAll(
                    mutableListOf<String>(
                        "Allowed",
                        "Blocked",
                        "5m",
                        "10m",
                        "15m",
                        "30m",
                        "1h",
                        "1h 30m",
                        "2h",
                        "2h 30m",
                        "3h",
                        "3h 30m",
                        "4h",
                        "4h 30m",
                        "5h",
                        "5h 30m",
                        "6h",
                        "6h 30m",
                        "7h",
                        "7h 30m"
                    )
                )
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

        override fun onResume() {
            super.onResume()
            val activity: jConfigActivity = activity as jConfigActivity
            activity.setFloatingToolbarVisibility(true)
        }
    }
}