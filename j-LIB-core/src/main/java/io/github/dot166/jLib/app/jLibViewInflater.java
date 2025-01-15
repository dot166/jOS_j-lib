package io.github.dot166.jLib.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.theme.MaterialComponentsViewInflater;

import org.jetbrains.annotations.Contract;

import io.github.dot166.jLib.widget.ActionBar2;

public class jLibViewInflater extends MaterialComponentsViewInflater {

    @Override
    @Nullable
    protected View createView(Context context, @NonNull String name, AttributeSet attrs) {
        View view = null;

        // We need to 'inject' our Views in place of the standard framework versions and appcompat versions and materialcomponents versions
        switch (name) {
            case "com.google.android.material.materialswitch.MaterialSwitch": // does not theme properly so replace with the older one
                view = createSwitch(context, attrs);
                verifyNotNull(view, name);
                break;
            case "androidx.appcompat.widget.SwitchCompat":
                view = createSwitch(context, attrs);
                verifyNotNull(view, name);
                break;
            case "Switch":
                view = createSwitch(context, attrs);
                verifyNotNull(view, name);
                break;
            case "Toolbar":
                view = createToolbar(context, attrs);
                verifyNotNull(view, name);
                break;
            case "androidx.appcompat.widget.Toolbar":
                view = createToolbar(context, attrs);
                verifyNotNull(view, name);
                break;
            case "com.google.android.material.appbar.MaterialToolbar":
                view = createToolbar(context, attrs);
                verifyNotNull(view, name);
                break;
            default:
                return super.createView(context, name, attrs);
        }
        return view;
    }

    @NonNull
    @Contract("_, _ -> new")
    private Toolbar createToolbar(Context context, AttributeSet attrs) {
        return new ActionBar2(context, attrs);
    }

    @NonNull
    @Contract("_, _ -> new")
    protected SwitchCompat createSwitch(Context context, AttributeSet attrs) {
        return new SwitchMaterial(context, attrs);
    }

    private void verifyNotNull(View view, String name) {
        if (view == null) {
            throw new IllegalStateException(this.getClass().getName()
                    + " asked to inflate view for <" + name + ">, but returned null");
        }
    }
}
