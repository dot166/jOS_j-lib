package jOS.Core.ThemeEngine;

import android.content.Context;

import androidx.compose.material3.ColorScheme;

public interface values {

    public int jLibTheme();

    public int M3L();

    public int M3();

    public int M3D();

    public ColorScheme DComposeColourScheme(Context context);

    public ColorScheme LComposeColourScheme(Context context);
}
