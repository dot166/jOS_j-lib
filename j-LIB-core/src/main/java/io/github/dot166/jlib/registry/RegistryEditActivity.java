package io.github.dot166.jlib.registry;

import static io.github.dot166.jlib.registry.XmlHelper.readXmlFromFile;
import static io.github.dot166.jlib.registry.XmlHelper.writeXmlToFile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import io.github.dot166.jlib.R;
import io.github.dot166.jlib.app.jActivity;
import io.github.dot166.jlib.utils.ErrorUtils;

public class RegistryEditActivity extends jActivity {

    List<RegistryHelper.Object> stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regedit);
        setSupportActionBar(findViewById(R.id.action_bar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TabLayout tabLayout = findViewById(R.id.tabs);
        buildTabs(tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                RegistryHelper.Object station = (RegistryHelper.Object) tab.getTag();
                ((AppCompatEditText)findViewById(R.id.name_input)).setText(station.getName());
                ((AppCompatEditText)findViewById(R.id.url_input)).setText(station.getUrl());
                ((AppCompatEditText)findViewById(R.id.logo_url_input)).setText(station.getLogoUrl());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.i("Registry", "No logic needed for onTabUnselected()");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.i("Registry", "What are you doing???");
            }
        });
        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout.Tab tab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
                HashMap<String, String> attrs = new HashMap<>();
                attrs.put("objectName", ((AppCompatEditText)findViewById(R.id.name_input)).getText().toString());
                attrs.put("objectUrl", ((AppCompatEditText)findViewById(R.id.url_input)).getText().toString());
                attrs.put("objectLogoUrl", ((AppCompatEditText)findViewById(R.id.logo_url_input)).getText().toString());
                if (tab.getTag() != null) {
                    if (stations.contains(tab.getTag()) && ((AppCompatEditText)findViewById(R.id.url_input)).getText().toString().isEmpty()) {
                        stations.remove(tab.getTag());
                        writeXmlToFile(v.getContext(), "Registry.xml", stations);
                        rebuildTabs(tabLayout);
                        return;
                    } else if (stations.contains(tab.getTag()) && !((AppCompatEditText)findViewById(R.id.url_input)).getText().toString().isEmpty()) {
                        RegistryHelper.Object station = (RegistryHelper.Object) tab.getTag();
                        station.updateAttributes(attrs);
                        stations.remove(tab.getTag());
                        stations.add(station);
                        writeXmlToFile(v.getContext(), "Registry.xml", stations);
                        rebuildTabs(tabLayout);
                        return;
                    }
                }
                RegistryHelper.Object station = new RegistryHelper.Object(attrs);
                stations.add(station);
                writeXmlToFile(v.getContext(), "Registry.xml", stations);
                rebuildTabs(tabLayout);
            }
        });
    }

    protected void rebuildTabs(TabLayout tabLayout) {
        tabLayout.removeAllTabs();
        buildTabs(tabLayout);
    }

    protected void buildTabs(TabLayout tabLayout) {
        TabLayout.Tab newTab = tabLayout.newTab();
        newTab.setText(R.string.add_new_station_to_registry);
        newTab.setTag(new RegistryHelper.Object(new HashMap<>()));
        tabLayout.addTab(newTab);
        newTab.select();
        stations = readXmlFromFile(this, "Registry.xml");
        for (int i = 0; i < stations.size(); i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            RegistryHelper.Object station = stations.get(i);
            tab.setTag(station);
            tab.setText(station.getName());
            tabLayout.addTab(tab);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        } else if (itemId == R.id.import_xml) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("text/xml"); // Or any other MIME type you need
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 61016);
            return true;
        } else if (itemId == R.id.export_xml) {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/xml");
            intent.putExtra(Intent.EXTRA_TITLE, "Registry.xml"); // default filename
            startActivityForResult(intent, 888);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 61016 && resultCode == RESULT_OK) {
            Uri fileUri = data.getData();
            stations = readXmlFromFile(this, "", fileUri);
            writeXmlToFile(this, "Registry.xml", stations);
            rebuildTabs(findViewById(R.id.tabs));
        } else if (requestCode == 888 && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                try {
                    // Grab current stations from local registry
                    List<RegistryHelper.Object> currentStations =
                            XmlHelper.readXmlFromFile(this, "Registry.xml");

                    String tempName = "temp_export.xml";
                    XmlHelper.writeXmlToFile(this, tempName, currentStations);

                    File tempFile = new File(getFilesDir(), tempName);

                    // Copy temp file into SAF-chosen destination
                    try (InputStream in = new FileInputStream(tempFile);
                         OutputStream out = getContentResolver().openOutputStream(fileUri)) {

                        if (out != null) {
                            byte[] buffer = new byte[8192];
                            int len;
                            while ((len = in.read(buffer)) != -1) {
                                out.write(buffer, 0, len);
                            }
                            out.flush();
                        }
                    }

                    // Delete temp file after use
                    tempFile.delete();

                    Toast.makeText(this, "Registry exported successfully!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    ErrorUtils.handle(e, this);
                }
            } else {
                Toast.makeText(this, "No file selected.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.import_menu, menu);
        return true;
    }

}
