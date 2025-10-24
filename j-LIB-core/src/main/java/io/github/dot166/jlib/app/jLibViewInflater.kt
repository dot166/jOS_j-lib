package io.github.dot166.jlib.app

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.theme.MaterialComponentsViewInflater
import org.jetbrains.annotations.Contract

open class jLibViewInflater : MaterialComponentsViewInflater() {
    override fun createView(context: Context, name: String, attrs: AttributeSet?): View? {
        var view: View? = null

        // We need to 'inject' our Views in place of the standard framework versions and appcompat versions and materialcomponents versions
        when (name) {
            "com.google.android.material.switchmaterial.SwitchMaterial" -> {
                view = createSwitch(context, attrs)
                verifyNotNull(view, name)
            }

            "androidx.appcompat.widget.SwitchCompat" -> {
                view = createSwitch(context, attrs)
                verifyNotNull(view, name)
            }

            "androidx.appcompat.widget.Toolbar" -> {
                view = createToolbar(context, attrs)
                verifyNotNull(view, name)
            }

            "androidx.cardview.widget.CardView" -> {
                view = createCardView(context, attrs)
                verifyNotNull(view, name)
            }

            else -> return super.createView(context, name, attrs)
        }
        Log.i(this.javaClass.simpleName, view.javaClass.name)
        return view
    }

    @Contract("_, _ -> new")
    protected fun createToolbar(context: Context, attrs: AttributeSet?): Toolbar {
        return MaterialToolbar(context, attrs)
    }

    @Contract("_, _ -> new")
    protected fun createSwitch(context: Context, attrs: AttributeSet?): SwitchCompat {
        return MaterialSwitch(context, attrs)
    }

    @Contract("_, _ -> new")
    protected fun createCardView(context: Context?, attrs: AttributeSet?): CardView {
        return MaterialCardView(context, attrs)
    }

    private fun verifyNotNull(view: View, name: String?) {
        checkNotNull(view) {
            (this.javaClass.getName()
                    + " asked to inflate view for <" + name + ">, but returned null")
        }
    }
}
