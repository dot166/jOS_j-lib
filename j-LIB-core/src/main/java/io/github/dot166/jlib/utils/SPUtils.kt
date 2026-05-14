package io.github.dot166.jlib.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import eu.geekplace.iesp.ImportExportSharedPreferences.exportToFile
import eu.geekplace.iesp.ImportExportSharedPreferences.importFromFile
import io.github.dot166.jlib.utils.FileUtils.getFileFromContentUri
import java.io.File
import java.io.FileInputStream


object SPUtils {
    fun importSharedPrefsFromSAF(data: Intent?, ctx: Context, sp: SharedPreferences) {
        if (data == null) {
            return
        }
        val fileUri = data.data ?: return
        val file = getFileFromContentUri(ctx, fileUri)
        importFromFile(sp, file)
        file!!.delete()
    }

    fun exportSharedPrefsToSAF(data: Intent?, ctx: Context, sp: SharedPreferences, doNotExport: Set<String>?) {
        if (data == null) {
            return
        }
        val fileUri = data.data
        if (fileUri != null) {
            try {
                val tempFile = File(ctx.filesDir, "temp_export.xml")
                exportToFile(sp, tempFile, doNotExport)

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