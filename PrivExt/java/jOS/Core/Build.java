/*
 * Copyright (C) 2024 ._______166
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

package jOS.Core;

import android.os.SystemProperties;
import java.util.Objects;


/**
 * Information about the current jOS build, extracted from system properties.
 */
public class Build {
    /** Value used for when a build property is unknown. */
    private static final String UNKNOWN = "unknown";

    /** A Version String utilized to distinguish jOS versions */
    private static final String jOS_RELEASE_INTERNAL = getString("ro.j.osversion");

    private static String getinternalint() {
        if (jOS_RELEASE != UNKNOWN) {
            return jOS_RELEASE + android.os.Build.DISPLAY;
        }
        return "0";
    }

    /** An Integer utilized to distinguish jOS versions */
    public static final int jOS_RELEASE = Integer.parseInt(getinternalint());

    private static String getString(String property) {
        return SystemProperties.get(property, UNKNOWN);
    }
}
