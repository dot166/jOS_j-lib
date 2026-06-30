package io.github.dot166.jlib.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

class MinusOneComposeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LifecycleOwner, ViewModelStoreOwner,
    SavedStateRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    override val lifecycle: Lifecycle get() = lifecycleRegistry

    private val privateViewModelStore = ViewModelStore()
    override val viewModelStore: ViewModelStore get() = privateViewModelStore

    private val savedStateController = SavedStateRegistryController.create(this)
    override val savedStateRegistry: SavedStateRegistry get() = savedStateController.savedStateRegistry

    private val composeView = ComposeView(context)

    init {
        savedStateController.performRestore(null)

        setViewTreeLifecycleOwner(this)
        setViewTreeSavedStateRegistryOwner(this)
        setViewTreeViewModelStoreOwner(this)

        composeView.apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        }

        addView(composeView)
    }
    /**
     * Set the Jetpack Compose UI content for this view. Initial composition will occur when the
     * view becomes attached to a window or when [androidx.compose.ui.platform.AbstractComposeView.createComposition] is called, whichever comes
     * first.
     */
    fun setContent(content: @Composable () -> Unit) {
        composeView.setContent {
            content()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        privateViewModelStore.clear()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (visibility == VISIBLE) {
            lifecycleRegistry.currentState = Lifecycle.State.RESUMED
        } else {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }
    }
}