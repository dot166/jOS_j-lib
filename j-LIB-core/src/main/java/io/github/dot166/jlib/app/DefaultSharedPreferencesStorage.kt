package io.github.dot166.jlib.app

import android.content.Context

class DefaultSharedPreferencesStorage(context: Context): JLibSharedPreferencesStorage(
    context = context,
    name = getDefaultSharedPreferencesName(context),
    mode = Context.MODE_PRIVATE
)