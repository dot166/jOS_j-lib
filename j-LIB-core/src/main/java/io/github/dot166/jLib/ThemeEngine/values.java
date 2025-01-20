package io.github.dot166.jLib.ThemeEngine;

import android.content.Context;

import androidx.compose.material3.ColorScheme;
import androidx.compose.material3.Shapes;
import androidx.compose.material3.Typography;

public interface values {

    public int jLibTheme();

    /**
     * @Deprecated M3 Theme is now DayNight
     */
    @Deprecated(since = "4.0.1", forRemoval = true)
    public int M3L();

    public int M3();

    /**
     * @Deprecated M3 Theme is now DayNight
     */
    @Deprecated(since = "4.0.1", forRemoval = true)
    public int M3D();

    /**
     * @Deprecated Compose Theme now uses attributes from the selected theme resource
     */
    @Deprecated(since = "4.0.1", forRemoval = true)
    public ColorScheme DComposeColourScheme(Context context);

    /**
     * @Deprecated Compose Theme now uses attributes from the selected theme resource
     */
    @Deprecated(since = "4.0.1", forRemoval = true)
    public ColorScheme LComposeColourScheme(Context context);

    public Typography ComposeTypography(Context context);

    public Shapes ComposeShapes(Context context);
}
