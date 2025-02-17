package io.github.dot166.jLib.ThemeEngine;

import android.content.Context;

import androidx.compose.material3.Shapes;
import androidx.compose.material3.Typography;

public interface values {

    public int jLibTheme();

    public int M3();

    public Typography ComposeTypography(Context context);

    public Shapes ComposeShapes(Context context);
}
