/*
 * Copyright (C) 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.dot166.jlib.dagger

import android.content.Context
import android.view.LayoutInflater
import io.github.dot166.jlib.app.RestorableSettingsApplication

/**
 * Utility class to extract JLibAppComponent from a context.
 *
 * If the context doesn't provide JLibAppComponent by default, it creates a new one and
 * associate it with that context
 */
object JLibComponentProvider {

    @JvmStatic
    fun get(c: Context): JLibAppComponent {
        val app = c.applicationContext
        if (app is RestorableSettingsApplication) return app.appComponent

        val inflater = LayoutInflater.from(app)
        val existingFilter = inflater.filter
        if (existingFilter is Holder) return existingFilter.component

        // Create a new component
        return Holder(
                DaggerJLibAppComponent.builder()
                    .appContext(app)
                    .build() as JLibAppComponent,
                existingFilter,
            )
            .apply { inflater.filter = this }
            .component
    }

    /** Extension method easily access JLibAppComponent */
    val Context.appComponent: JLibAppComponent
        get() = get(this)

    private data class Holder(
        val component: JLibAppComponent,
        private val filter: LayoutInflater.Filter?,
    ) : LayoutInflater.Filter {

        override fun onLoadClass(clazz: Class<*>?) = filter?.onLoadClass(clazz) ?: true
    }
}
