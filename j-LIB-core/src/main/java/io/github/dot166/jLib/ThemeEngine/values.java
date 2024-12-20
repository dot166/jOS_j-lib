package io.github.dot166.jLib.ThemeEngine;

import android.content.Context;

import androidx.compose.material3.ColorScheme;
import androidx.compose.material3.Shapes;
import androidx.compose.material3.Typography;

public interface values {

    public int jLibTheme();

    public int M3L();

    public int M3();

    public int M3D();

    public ColorScheme DComposeColourScheme(Context context);

    public ColorScheme LComposeColourScheme(Context context);

    public Typography ComposeTypography(Context context);

    public Shapes ComposeShapes(Context context);
}
