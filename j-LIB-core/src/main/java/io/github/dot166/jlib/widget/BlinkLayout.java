package io.github.dot166.jlib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import io.github.dot166.jlib.app.jLibConfig;

/// Blink feature has been removed in 4.2.1, this is now a deprecated shortcut to {@link ConstraintLayout}
@Deprecated(since = "4.2.1", forRemoval = true)
public class BlinkLayout extends ConstraintLayout {

    public BlinkLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }
}
