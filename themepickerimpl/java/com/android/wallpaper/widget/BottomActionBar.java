/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.wallpaper.widget;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ImageViewCompat;

import com.android.wallpaper.util.ResourceUtils;
import com.android.wallpaper.util.SizeCalculator;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.github.dot166.jlib.utils.VersionUtils;
import io.github.dot166.themeengine.R;

/** A {@code ViewGroup} which provides the specific actions for the user to interact with. */
public class BottomActionBar extends FrameLayout {
    private Context mContext;

    /**
     * Interface to be implemented by an Activity hosting a {@link BottomActionBar}
     */
    public interface BottomActionBarHost {
        /** Gets {@link BottomActionBar}. */
        BottomActionBar getBottomActionBar();
    }

    /**
     * The listener for {@link BottomActionBar} visibility change notification.
     */
    public interface VisibilityChangeListener {
        /**
         * Called when {@link BottomActionBar} visibility changes.
         *
         * @param isVisible {@code true} if it's visible; {@code false} otherwise.
         */
        void onVisibilityChange(boolean isVisible);
    }

    /** This listens to changes to an action view's selected state. */
    public interface OnActionSelectedListener {

        /**
         * This is called when an action view's selected state changes.
         * @param selected whether the action view is selected.
         */
        void onActionSelected(boolean selected);
    }

    /**
     *  A Callback to notify the registrant to change it's accessibility param when
     *  {@link BottomActionBar} state changes.
     */
    public interface AccessibilityCallback {
        /**
         * Called when {@link BottomActionBar} collapsed.
         */
        void onBottomSheetCollapsed();

        /**
         * Called when {@link BottomActionBar} expanded.
         */
        void onBottomSheetExpanded();
    }

    /**
     * Object to host content view for bottom sheet to display.
     *
     * <p> The view would be created in the constructor.
     */
    public static abstract class BottomSheetContent<T extends View> {

        private T mContentView;
        private boolean mIsVisible;

        public BottomSheetContent(Context context) {
            mContentView = createView(context);
            setVisibility(false);
        }

        /** Gets the view id to inflate. */
        @LayoutRes
        public abstract int getViewId();

        /** Gets called when the content view is created. */
        public abstract void onViewCreated(T view);

        /** Gets called when the current content view is going to recreate. */
        public void onRecreateView(T oldView) {}

        private void recreateView(Context context) {
            // Inform that the view is going to recreate.
            onRecreateView(mContentView);
            // Create a new view with the given context.
            mContentView = createView(context);
            setVisibility(mIsVisible);
        }

        private T createView(Context context) {
            T contentView = (T) LayoutInflater.from(context).inflate(getViewId(), null);
            onViewCreated(contentView);
            contentView.setFocusable(true);
            return contentView;
        }

        protected void setVisibility(boolean isVisible) {
            mIsVisible = isVisible;
            mContentView.setVisibility(mIsVisible ? VISIBLE : GONE);
        }
    }

    // TODO(b/154299462): Separate downloadable related actions from WallpaperPicker.
    /** The action items in the bottom action bar. */
    public enum BottomAction {
        ROTATION,
        DELETE,
        INFORMATION(R.string.accessibility_info_shown, R.string.accessibility_info_hidden),
        EDIT,
        CUSTOMIZE(R.string.accessibility_customize_shown, R.string.accessibility_customize_hidden),
        EFFECTS,
        DOWNLOAD,
        PROGRESS,
        APPLY,
        APPLY_TEXT;

        private final int mShownAccessibilityResId;
        private final int mHiddenAccessibilityResId;

        BottomAction() {
            this(/* shownAccessibilityLabelResId= */ 0, /* shownAccessibilityLabelResId= */ 0);
        }

        BottomAction(int shownAccessibilityLabelResId, int hiddenAccessibilityLabelResId) {
            mShownAccessibilityResId = shownAccessibilityLabelResId;
            mHiddenAccessibilityResId = hiddenAccessibilityLabelResId;
        }

        /**
         * Returns the string resource id of the currently bottom action for its shown or hidden
         * state.
         */
        public int getAccessibilityStringRes(boolean isShown) {
            return isShown ? mShownAccessibilityResId : mHiddenAccessibilityResId;
        }
    }

    private final Map<BottomAction, View> mActionMap = new EnumMap<>(BottomAction.class);
    private final Map<BottomAction, BottomSheetContent<?>> mContentViewMap =
            new EnumMap<>(BottomAction.class);
    private final Map<BottomAction, OnActionSelectedListener> mActionSelectedListeners =
            new EnumMap<>(BottomAction.class);

    private final ViewGroup mBottomSheetView;
    private final QueueStateBottomSheetBehavior<ViewGroup> mBottomSheetBehavior;
    private final Set<VisibilityChangeListener> mVisibilityChangeListeners = new HashSet<>();

    // The current selected action in the BottomActionBar, can be null when no action is selected.
    @Nullable private BottomAction mSelectedAction;
    // The last selected action in the BottomActionBar.
    @Nullable private BottomAction mLastSelectedAction;
    @Nullable private AccessibilityCallback mAccessibilityCallback;

    public BottomActionBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.bottom_actions_layout, this, true);

        mActionMap.put(BottomAction.ROTATION, findViewById(R.id.action_rotation));
        mActionMap.put(BottomAction.DELETE, findViewById(R.id.action_delete));
        mActionMap.put(BottomAction.INFORMATION, findViewById(R.id.action_information));
        mActionMap.put(BottomAction.EDIT, findViewById(R.id.action_edit));
        mActionMap.put(BottomAction.CUSTOMIZE, findViewById(R.id.action_customize));
        mActionMap.put(BottomAction.EFFECTS, findViewById(R.id.action_effects));
        mActionMap.put(BottomAction.DOWNLOAD, findViewById(R.id.action_download));
        mActionMap.put(BottomAction.PROGRESS, findViewById(R.id.action_progress));
        mActionMap.put(BottomAction.APPLY, findViewById(R.id.action_apply));
        mActionMap.put(BottomAction.APPLY_TEXT, findViewById(R.id.action_apply_text_button));

        mBottomSheetView = findViewById(R.id.action_bottom_sheet);
        SizeCalculator.adjustBackgroundCornerRadius(mBottomSheetView);
        setColor(context);

        mBottomSheetBehavior = (QueueStateBottomSheetBehavior<ViewGroup>) BottomSheetBehavior.from(
                mBottomSheetView);
        mBottomSheetBehavior.setState(STATE_COLLAPSED);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (mBottomSheetBehavior.isQueueProcessing()) {
                    // Avoid button and bottom sheet mismatching from quick tapping buttons when
                    // bottom sheet is changing state.
                    disableActions();
                    // If bottom sheet is going with expanded-collapsed-expanded, the new content
                    // will be updated in collapsed state. The first state change from expanded to
                    // collapsed should still show the previous content view.
                    if (mSelectedAction != null && newState == STATE_COLLAPSED) {
                        updateContentViewFor(mSelectedAction);
                    }
                    return;
                }

                notifyAccessibilityCallback(newState);

                // Enable all buttons when queue is not processing.
                enableActions();
                if (!isExpandable(mSelectedAction)) {
                    return;
                }
                // Ensure the button state is the same as bottom sheet state to catch up the state
                // change from dragging or some unexpected bottom sheet state changes.
                if (newState == STATE_COLLAPSED) {
                    updateSelectedState(mSelectedAction, /* selected= */ false);
                } else if (newState == STATE_EXPANDED) {
                    updateSelectedState(mSelectedAction, /* selected= */ true);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) { }
        });

        setOnApplyWindowInsetsListener((v, windowInsets) -> {
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(),
                    windowInsets.getSystemWindowInsetBottom());
            return windowInsets;
        });

        // Skip "info selected" and "customize selected" Talkback while double tapping on info and
        // customize action.
        skipAccessibilityEvent(new BottomAction[]{BottomAction.INFORMATION, BottomAction.CUSTOMIZE},
                new int[]{AccessibilityEvent.TYPE_VIEW_CLICKED,
                        AccessibilityEvent.TYPE_VIEW_SELECTED});
    }

    @Override
    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);
        mVisibilityChangeListeners.forEach(listener -> listener.onVisibilityChange(isVisible));
    }

    /**
     * Binds the {@code bottomSheetContent} with the {@code action}, the {@code action} button
     * would be able to expand/collapse the bottom sheet to show the content.
     *
     * @param bottomSheetContent the content object with view being added to the bottom sheet
     * @param action the action to be bound to expand / collapse the bottom sheet
     */
    public void bindBottomSheetContentWithAction(BottomSheetContent<?> bottomSheetContent,
            BottomAction action) {
        mContentViewMap.put(action, bottomSheetContent);
        mBottomSheetView.addView(bottomSheetContent.mContentView);
        setActionClickListener(action, actionView -> {
            if (mBottomSheetBehavior.getState() == STATE_COLLAPSED) {
                updateContentViewFor(action);
            }
            mBottomSheetView.setAccessibilityTraversalAfter(actionView.getId());
        });
    }

    /** Collapses the bottom sheet. */
    public void collapseBottomSheetIfExpanded() {
        hideBottomSheetAndDeselectButtonIfExpanded();
    }

    /** Enables or disables action buttons that show the bottom sheet. */
    public void enableActionButtonsWithBottomSheet(boolean enabled) {
        if (enabled) {
            enableActions(mContentViewMap.keySet().toArray(new BottomAction[0]));
        } else {
            disableActions(mContentViewMap.keySet().toArray(new BottomAction[0]));
        }
    }

    /**
     * Sets a click listener to a specific action.
     *
     * @param bottomAction the specific action
     * @param actionClickListener the click listener for the action
     */
    public void setActionClickListener(
            BottomAction bottomAction, OnClickListener actionClickListener) {
        View buttonView = mActionMap.get(bottomAction);
        if (buttonView.hasOnClickListeners()) {
            throw new IllegalStateException(
                    "Had already set a click listener to button: " + bottomAction);
        }
        buttonView.setOnClickListener(view -> {
            if (mSelectedAction != null && isActionSelected(mSelectedAction)) {
                updateSelectedState(mSelectedAction, /* selected= */ false);
                if (isExpandable(mSelectedAction)) {
                    mBottomSheetBehavior.enqueue(STATE_COLLAPSED);
                }
            } else {
                // Error handling, set to null if the action is not selected.
                mSelectedAction = null;
            }

            if (bottomAction == mSelectedAction) {
                // Deselect the selected action.
                mSelectedAction = null;
            } else {
                // Select a different action from the current selected action.
                // Also keep the same action for unselected case for a11y.
                mLastSelectedAction = mSelectedAction = bottomAction;
                updateSelectedState(mSelectedAction, /* selected= */ true);
                if (isExpandable(mSelectedAction)) {
                    mBottomSheetBehavior.enqueue(STATE_EXPANDED);
                }
            }
            actionClickListener.onClick(view);
            mBottomSheetBehavior.processQueueForStateChange();
        });
    }

    /**
     * Sets a selected listener to a specific action. This is triggered each time the bottom
     * action's selected state changes.
     *
     * @param bottomAction the specific action
     * @param actionSelectedListener the selected listener for the action
     */
    public void setActionSelectedListener(
            BottomAction bottomAction, OnActionSelectedListener actionSelectedListener) {
        if (mActionSelectedListeners.containsKey(bottomAction)) {
            throw new IllegalStateException(
                    "Had already set a selected listener to button: " + bottomAction);
        }
        mActionSelectedListeners.put(bottomAction, actionSelectedListener);
    }

    /** Set back button visibility. */
    public void setBackButtonVisibility(int visibility) {
        findViewById(R.id.action_back).setVisibility(visibility);
    }

    /** Binds the cancel button to back key. */
    public void bindBackButtonToSystemBackKey(Activity activity) {
        findViewById(R.id.action_back).setOnClickListener(v -> activity.onBackPressed());
    }

    /** Returns {@code true} if visible. */
    public boolean isVisible() {
        return getVisibility() == VISIBLE;
    }

    /** Shows {@link BottomActionBar}. */
    public void show() {
        setVisibility(VISIBLE);
    }

    /** Hides {@link BottomActionBar}. */
    public void hide() {
        setVisibility(GONE);
    }

    /**
     * Adds the visibility change listener.
     *
     * @param visibilityChangeListener the listener to be notified.
     */
    public void addVisibilityChangeListener(VisibilityChangeListener visibilityChangeListener) {
        if (visibilityChangeListener == null) {
            return;
        }
        mVisibilityChangeListeners.add(visibilityChangeListener);
        visibilityChangeListener.onVisibilityChange(isVisible());
    }

    /**
     * Sets a AccessibilityCallback.
     *
     * @param accessibilityCallback the callback to be notified.
     */
    public void setAccessibilityCallback(@Nullable AccessibilityCallback accessibilityCallback) {
        mAccessibilityCallback = accessibilityCallback;
    }

    /**
     * Shows the specific actions.
     *
     * @param actions the specific actions
     */
    public void showActions(BottomAction... actions) {
        for (BottomAction action : actions) {
            mActionMap.get(action).setVisibility(VISIBLE);
        }
    }

    /**
     * Hides the specific actions.
     *
     * @param actions the specific actions
     */
    public void hideActions(BottomAction... actions) {
        for (BottomAction action : actions) {
            mActionMap.get(action).setVisibility(GONE);

            if (isExpandable(action) && mSelectedAction == action) {
                hideBottomSheetAndDeselectButtonIfExpanded();
            }
        }
    }

    /**
     * Focus the specific action.
     *
     * @param action the specific action
     */
    public void focusAccessibilityAction(BottomAction action) {
        mActionMap.get(action).sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
    }

    /**
     * Shows the specific actions only. In other words, the other actions will be hidden.
     *
     * @param actions the specific actions which will be shown. Others will be hidden.
     */
    public void showActionsOnly(BottomAction... actions) {
        final Set<BottomAction> actionsSet = new HashSet<>(Arrays.asList(actions));

        mActionMap.keySet().forEach(action -> {
            if (actionsSet.contains(action)) {
                showActions(action);
            } else {
                hideActions(action);
            }
        });
    }

    /**
     * Checks if the specific actions are shown.
     *
     * @param actions the specific actions to be verified
     * @return {@code true} if the actions are shown; {@code false} otherwise
     */
    public boolean areActionsShown(BottomAction... actions) {
        final Set<BottomAction> actionsSet = new HashSet<>(Arrays.asList(actions));
        return actionsSet.stream().allMatch(bottomAction -> {
            View view = mActionMap.get(bottomAction);
            return view != null && view.getVisibility() == VISIBLE;
        });
    }

    /**
     * All actions will be hidden.
     */
    public void hideAllActions() {
        showActionsOnly(/* No actions to show */);
    }

    /** Enables all the actions' {@link View}. */
    public void enableActions() {
        enableActions(BottomAction.values());
    }

    /** Disables all the actions' {@link View}. */
    public void disableActions() {
        disableActions(BottomAction.values());
    }

    /**
     * Enables specified actions' {@link View}.
     *
     * @param actions the specified actions to enable their views
     */
    public void enableActions(BottomAction... actions) {
        for (BottomAction action : actions) {
            mActionMap.get(action).setEnabled(true);
        }
    }

    /**
     * Disables specified actions' {@link View}.
     *
     * @param actions the specified actions to disable their views
     */
    public void disableActions(BottomAction... actions) {
        for (BottomAction action : actions) {
            mActionMap.get(action).setEnabled(false);
        }
    }

    /** Sets a default selected action button. */
    public void setDefaultSelectedButton(BottomAction action) {
        if (mSelectedAction == null) {
            mSelectedAction = action;
            updateSelectedState(mSelectedAction, /* selected= */ true);
        }
    }

    /** Deselects an action button. */
    public void deselectAction(BottomAction action) {
        if (isExpandable(action)) {
            mBottomSheetBehavior.setState(STATE_COLLAPSED);
        }
        updateSelectedState(action, /* selected= */ false);
        if (action == mSelectedAction) {
            mSelectedAction = null;
        }
    }

    public boolean isActionSelected(BottomAction action) {
        return mActionMap.get(action).isSelected();
    }

    /** Returns {@code true} if the state of bottom sheet is collapsed. */
    public boolean isBottomSheetCollapsed() {
        return mBottomSheetBehavior.getState() == STATE_COLLAPSED;
    }

    /** Resets {@link BottomActionBar} to initial state. */
    public void reset() {
        // Not visible by default, see res/layout/bottom_action_bar.xml
        hide();
        // All actions are hide and enabled by default, see res/layout/bottom_action_bar.xml
        hideAllActions();
        enableActions();
        // Clears all the actions' click listeners
        mActionMap.values().forEach(v -> v.setOnClickListener(null));
        findViewById(R.id.action_back).setOnClickListener(null);
        // Deselect all buttons.
        mActionMap.keySet().forEach(a -> updateSelectedState(a, /* selected= */ false));
        // Clear values.
        mContentViewMap.clear();
        mActionSelectedListeners.clear();
        mBottomSheetView.removeAllViews();
        mBottomSheetBehavior.reset();
        mSelectedAction = null;
    }

    /** Dynamic update color with {@code Context}. */
    public void setColor(Context context) {
        // Set bottom sheet background.
        mBottomSheetView.setBackground(context.getDrawable(R.drawable.bottom_sheet_background));
        if (mBottomSheetView.getChildCount() > 0) {
            // Update the bottom sheet content view if any.
            mBottomSheetView.removeAllViews();
            mContentViewMap.values().forEach(bottomSheetContent -> {
                bottomSheetContent.recreateView(context);
                mBottomSheetView.addView(bottomSheetContent.mContentView);
            });
        }

        // Set the bar background and action buttons.
        ViewGroup actionTabs = findViewById(R.id.action_tabs);
        actionTabs.setBackgroundColor(
                ResourceUtils.getColorAttr(context, android.R.attr.colorBackground));
        ColorStateList colorStateList = context.getColorStateList(
                R.color.bottom_action_button_color_tint);
        for (int i = 0; i < actionTabs.getChildCount(); i++) {
            View v = actionTabs.getChildAt(i);
            if (v instanceof ImageView) {
                v.setBackground(context.getDrawable(R.drawable.bottom_action_button_background));
                ImageViewCompat.setImageTintList((ImageView) v, colorStateList);
            } else if (v instanceof ProgressBar) {
                ((ProgressBar) v).setIndeterminateTintList(colorStateList);
            }
        }
    }

    /** Sets action button accessibility traversal after. */
    public void setActionAccessibilityTraversalAfter(BottomAction action, int afterId) {
        View bottomActionView = mActionMap.get(action);
        bottomActionView.setAccessibilityTraversalAfter(afterId);
    }

    /** Sets action button accessibility traversal before. */
    public void setActionAccessibilityTraversalBefore(BottomAction action, int beforeId) {
        View bottomActionView = mActionMap.get(action);
        bottomActionView.setAccessibilityTraversalBefore(beforeId);
    }

    private void updateSelectedState(BottomAction bottomAction, boolean selected) {
        View bottomActionView = mActionMap.get(bottomAction);
        if (bottomActionView.isSelected() == selected) {
            return;
        }

        OnActionSelectedListener listener = mActionSelectedListeners.get(bottomAction);
        if (listener != null) {
            listener.onActionSelected(selected);
        }
        bottomActionView.setSelected(selected);
    }

    private void hideBottomSheetAndDeselectButtonIfExpanded() {
        if (isExpandable(mSelectedAction) && mBottomSheetBehavior.getState() == STATE_EXPANDED) {
            mBottomSheetBehavior.setState(STATE_COLLAPSED);
            updateSelectedState(mSelectedAction, /* selected= */ false);
            mSelectedAction = null;
        }
    }

    private void updateContentViewFor(BottomAction action) {
        mContentViewMap.forEach((a, content) -> content.setVisibility(a.equals(action)));
    }

    private boolean isExpandable(BottomAction action) {
        return action != null && mContentViewMap.containsKey(action);
    }

    private void notifyAccessibilityCallback(int state) {
        if (mAccessibilityCallback == null) {
            return;
        }

        if (state == STATE_COLLAPSED) {
            CharSequence text = getAccessibilityText(mLastSelectedAction, /* isShown= */ false);
            if (!TextUtils.isEmpty(text)) {
                if (VersionUtils.isAtLeastP()) {
                    setAccessibilityPaneTitle(text);
                }
            }
            mAccessibilityCallback.onBottomSheetCollapsed();
        } else if (state == STATE_EXPANDED) {
            CharSequence text = getAccessibilityText(mSelectedAction, /* isShown= */ true);
            if (!TextUtils.isEmpty(text)) {
                if (VersionUtils.isAtLeastP()) {
                    setAccessibilityPaneTitle(text);
                }
            }
            mAccessibilityCallback.onBottomSheetExpanded();
        }
    }

    private CharSequence getAccessibilityText(BottomAction action, boolean isShown) {
        if (action == null) {
            return null;
        }
        int resId = action.getAccessibilityStringRes(isShown);
        if (resId != 0) {
            return mContext.getText(resId);
        }
        return null;
    }

    /**
     * Skip bottom action's Accessibility event.
     *
     * @param actions the {@link BottomAction} actions to be skipped.
     * @param eventTypes the {@link AccessibilityEvent} event types to be skipped.
     */
    private void skipAccessibilityEvent(BottomAction[] actions, int[] eventTypes) {
        for (BottomAction action : actions) {
            View view = mActionMap.get(action);
            view.setAccessibilityDelegate(new AccessibilityDelegate() {
                @Override
                public void sendAccessibilityEvent(View host, int eventType) {
                    if (!contains(eventTypes, eventType)) {
                        super.sendAccessibilityEvent(host, eventType);
                    }
                }
            });
        }
    }

    /** A {@link BottomSheetBehavior} that can process a queue of bottom sheet states.*/
    public static class QueueStateBottomSheetBehavior<V extends View>
            extends BottomSheetBehavior<V> {

        private final Deque<Integer> mStateQueue = new ArrayDeque<>();
        private boolean mIsQueueProcessing;

        public QueueStateBottomSheetBehavior(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            // Binds the default callback for processing queue.
            setBottomSheetCallback(null);
        }

        /** Enqueues the bottom sheet states. */
        public void enqueue(int state) {
            if (!mStateQueue.isEmpty() && mStateQueue.getLast() == state) {
                return;
            }
            mStateQueue.add(state);
        }

        /** Processes the queue of bottom sheet state that was set via {@link #enqueue}. */
        public void processQueueForStateChange() {
            if (mStateQueue.isEmpty()) {
                return;
            }
            setState(mStateQueue.getFirst());
            mIsQueueProcessing = true;
        }

        /**
         * Returns {@code true} if the queue is processing. For example, if the bottom sheet is
         * going with expanded-collapsed-expanded, it would return {@code true} until last expanded
         * state is finished.
         */
        public boolean isQueueProcessing() {
            return mIsQueueProcessing;
        }

        /** Resets the queue state. */
        public void reset() {
            mStateQueue.clear();
            mIsQueueProcessing = false;
        }

        @Override
        public void setBottomSheetCallback(BottomSheetCallback callback) {
            super.setBottomSheetCallback(new BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (!mStateQueue.isEmpty()) {
                        if (newState == mStateQueue.getFirst()) {
                            mStateQueue.removeFirst();
                            if (mStateQueue.isEmpty()) {
                                mIsQueueProcessing = false;
                            } else {
                                setState(mStateQueue.getFirst());
                            }
                        } else {
                            setState(mStateQueue.getFirst());
                        }
                    }

                    if (callback != null) {
                        callback.onStateChanged(bottomSheet, newState);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    if (callback != null) {
                        callback.onSlide(bottomSheet, slideOffset);
                    }
                }
            });
        }
    }
    /**
     * Checks that value is present as at least one of the elements of the array.
     * @param array the array to check in
     * @param value the value to check for
     * @return true if the value is present in the array
     */
    public static boolean contains(@Nullable int[] array, int value) {
        return indexOf(array, value) != -1;
    }

    /**
     * Return first index of {@code value} in {@code array}, or {@code -1} if
     * not found.
     */
    public static int indexOf(@Nullable int[] array, int value) {
        if (array == null) return -1;
        for (int i = 0; i < array.length; i++) {
            if (Objects.equals(array[i], value)) return i;
        }
        return -1;
    }
}
