package jOS.Core.ThemeEngine;

import androidx.compose.material3.ColorScheme;

public interface values {

    public int jOSTheme();

    public int M3L();

    public int M3();

    public int M2L();

    public int M2();

    /**
     * WARNING!! Is only used for Android R and older because android S and later uses Material You (M3)
     * @return Legacy Dark Colour Scheme
     */
    public ColorScheme DColourScheme();

    /**
     * WARNING!! Is only used for Android R and older because android S and later uses Material You (M3)
     * @return Legacy Light Colour Scheme
     */
    public ColorScheme LColourScheme();
}
