package io.github.dot166.themeengine;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import io.github.dot166.jlib.R;

public class NotifierActivity extends ThemeEngineActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(io.github.dot166.themeengine.R.string.your_device_was_bundled_with_themeengine_so_you_will_need_to_wait_for_an_os_update)
                .setTitle(R.string.text_te_label)
                .setCancelable(false)
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
