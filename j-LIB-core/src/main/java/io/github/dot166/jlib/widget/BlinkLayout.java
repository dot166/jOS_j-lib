package io.github.dot166.jlib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import io.github.dot166.jlib.app.jLibConfig;

public class BlinkLayout extends ConstraintLayout {
    private static final int MESSAGE_BLINK = 0x42;

    private boolean mBlink;
    private boolean mBlinkState;
    private final Handler mHandler;

    public BlinkLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHandler = new Handler(msg -> {
            if (msg.what == MESSAGE_BLINK) {
                if (mBlink && jLibConfig.get_Blink_enabled()) {
                    mBlinkState = !mBlinkState;
                    makeBlink();
                }
                invalidate();
                return true;
            }
            return false;
        });
    }

    private void makeBlink() {
        if (!isInEditMode()) {
            Message message = mHandler.obtainMessage(MESSAGE_BLINK);
            mHandler.sendMessageDelayed(message, jLibConfig.get_Blink_speed());
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mBlink = true;
        mBlinkState = true;

        makeBlink();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mBlink = false;
        mBlinkState = true;

        mHandler.removeMessages(MESSAGE_BLINK);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mBlinkState) {
            super.dispatchDraw(canvas);
        }
    }
}
