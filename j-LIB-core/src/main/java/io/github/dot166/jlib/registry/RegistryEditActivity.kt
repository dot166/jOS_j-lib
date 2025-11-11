package io.github.dot166.jlib.registry

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import io.github.dot166.jlib.R
import io.github.dot166.jlib.app.jActivity
import io.github.dot166.jlib.registry.XmlHelper.readXmlFromFile
import io.github.dot166.jlib.registry.XmlHelper.writeXmlToFile
import io.github.dot166.jlib.utils.ErrorUtils.handle
import java.io.File
import java.io.FileInputStream

open class RegistryEditActivity : jActivity() {
    var stations: MutableList<RegistryHelper.Object>? = null

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regedit)
        setSupportActionBar(findViewById(R.id.action_bar))

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val tabLayout: TabLayout = findViewById(R.id.tabs)
        buildTabs(tabLayout)
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val station = tab.tag as RegistryHelper.Object?
                findViewById<AppCompatEditText>(R.id.name_input).setText(
                    station!!.name
                )
                findViewById<AppCompatEditText>(R.id.url_input).setText(
                    station.url
                )
                findViewById<AppCompatEditText>(R.id.logo_url_input).setText(
                    station.logoUrl
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.i("Registry", "No logic needed for onTabUnselected()")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.i("Registry", "What are you doing???")
            }
        })
        findViewById<MaterialButton>(R.id.save_button).setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(v: View) {
                val tab = tabLayout.getTabAt(tabLayout.selectedTabPosition)
                val attrs = HashMap<String, String>()
                val name: String = findViewById<AppCompatEditText>(R.id.name_input).getText()
                    .toString()
                val url: String = findViewById<AppCompatEditText>(R.id.url_input).getText()
                    .toString()
                val logoUrl = findViewById<AppCompatEditText>(R.id.logo_url_input).getText()
                    .toString()
                if (tab!!.tag != null) {
                    if (stations!!.contains(tab.tag) && findViewById<AppCompatEditText>(R.id.url_input).getText()
                            .toString().isEmpty()
                    ) {
                        stations!!.remove(tab.tag)
                        writeXmlToFile(v.context, "Registry.xml", stations!!)
                        rebuildTabs(tabLayout)
                        return
                    } else if (stations!!.contains(tab.tag) && !findViewById<AppCompatEditText>(R.id.url_input).getText()
                            .toString().isEmpty()
                    ) {
                        val station = tab.tag as RegistryHelper.Object?
                        station!!.updateAttributes(name, url, logoUrl)
                        stations!!.remove(tab.tag)
                        stations!!.add(station)
                        writeXmlToFile(v.context, "Registry.xml", stations!!)
                        rebuildTabs(tabLayout)
                        return
                    }
                }
                val station = RegistryHelper.Object(name, url, logoUrl)
                stations!!.add(station)
                writeXmlToFile(v.context, "Registry.xml", stations!!)
                rebuildTabs(tabLayout)
            }
        })
    }

    protected fun rebuildTabs(tabLayout: TabLayout) {
        tabLayout.removeAllTabs()
        buildTabs(tabLayout)
    }

    protected fun buildTabs(tabLayout: TabLayout) {
        val newTab = tabLayout.newTab()
        newTab.setText(R.string.add_new_station_to_registry)
        newTab.tag = RegistryHelper.Object("", "", "")
        tabLayout.addTab(newTab)
        newTab.select()
        stations = readXmlFromFile(this, "Registry.xml")
        for (i in stations!!.indices) {
            val tab = tabLayout.newTab()
            val station = stations!![i]
            tab.tag = station
            tab.text = station.name
            tabLayout.addTab(tab)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
            R.id.import_xml -> {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.setType("text/xml") // Or any other MIME type you need
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                startActivityForResult(intent, 61016)
                return true
            }
            R.id.export_xml -> {
                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.setType("text/xml")
                intent.putExtra(Intent.EXTRA_TITLE, "Registry.xml") // default filename
                startActivityForResult(intent, 888)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 61016 && resultCode == RESULT_OK) {
            val fileUri = data!!.data
            stations = readXmlFromFile(this, "", fileUri)
            writeXmlToFile(this, "Registry.xml", stations!!)
            rebuildTabs(findViewById(R.id.tabs)!!)
        } else if (requestCode == 888 && resultCode == RESULT_OK && data != null) {
            val fileUri = data.data
            if (fileUri != null) {
                try {
                    // Grab current stations from local registry
                    val currentStations =
                        readXmlFromFile(this, "Registry.xml")

                    val tempName = "temp_export.xml"
                    writeXmlToFile(this, tempName, currentStations)

                    val tempFile = File(filesDir, tempName)

                    FileInputStream(tempFile).use { `in` ->
                        contentResolver.openOutputStream(fileUri).use { out ->
                            if (out != null) {
                                val buffer = ByteArray(8192)
                                var len: Int
                                while ((`in`.read(buffer).also { len = it }) != -1) {
                                    out.write(buffer, 0, len)
                                }
                                out.flush()
                            }
                        }
                    }
                    // Delete temp file after use
                    tempFile.delete()

                    Toast.makeText(this, "Registry exported successfully!", Toast.LENGTH_SHORT)
                        .show()
                } catch (e: Exception) {
                    handle(e, this)
                }
            } else {
                Toast.makeText(this, "No file selected.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.import_menu, menu)
        return true
    }
}
