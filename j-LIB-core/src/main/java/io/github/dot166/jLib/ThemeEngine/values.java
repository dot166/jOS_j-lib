package io.github.dot166.jLib.ThemeEngine;

import android.content.Context;

import androidx.compose.material3.Shapes;
import androidx.compose.material3.Typography;
import androidx.compose.runtime.Composable;

public interface values {

    public int jLibTheme();

    public int M3();

    @Composable
    public Typography composeTypography(Context context);

    @Composable
    public Shapes composeShapes(Context context);
}
