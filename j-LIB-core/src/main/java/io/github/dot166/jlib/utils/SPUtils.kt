package io.github.dot166.jlib.utils

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.android.settingslib.datastore.SharedPreferencesStorage
import eu.geekplace.iesp.ImportExportSharedPreferences.exportToFile
import eu.geekplace.iesp.ImportExportSharedPreferences.importFromFile
import io.github.dot166.jlib.utils.FileUtils.getFileFromContentUri
import java.io.File
import java.io.FileInputStream


object SPUtils {
    fun importSharedPrefsFromSAF(fileUri: Uri?, ctx: Context, sp: SharedPreferencesStorage) {
        if (fileUri != null) {
            val file = getFileFromContentUri(ctx, fileUri)
            importFromFile(sp.sharedPreferences, file)
            file!!.delete()
        }
    }

    fun exportSharedPrefsToSAF(fileUri: Uri?, ctx: Context, sp: SharedPreferencesStorage, doNotExport: Set<String>?) {
        if (fileUri != null) {
            try {
                val tempFile = File(ctx.filesDir, "temp_export.xml")
                exportToFile(sp.sharedPreferences, tempFile, doNotExport)

                FileInputStream(tempFile).use { `in` ->
                    ctx.contentResolver.openOutputStream(fileUri).use { out ->
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
                tempFile.delete()

                Toast.makeText(ctx, "Export completed successfully!", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: Exception) {
                ErrorUtils.handle(e, ctx)
            }
        } else {
            Toast.makeText(ctx, "No file selected.", Toast.LENGTH_SHORT).show()
        }
    }

}