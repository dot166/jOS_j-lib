package jOS.Core;

import static com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.MaterialToolbar;

import jOS.Core.utils.ToolbarUtils;
import jOS.Core.utils.IconUtils;

/**
 * {@code ActionBar2} is a {@link MaterialToolbar} that implements certain features and fixes.
 *
 *
 * <p>To get started with the {@code ActionBar2} component, use {@code
 * jOS.Core.ActionBar2} in your layout XML instead of {@code
 * androidx.appcompat.widget.Toolbar} or {@code Toolbar} or {@code
 * com.google.android.material.appbar.MaterialToolbar} or {@code MaterialToolbar}. E.g.,:
 *
 * <pre>
 * &lt;jOS.Core.ActionBar2
 *         android:layout_width=&quot;match_parent&quot;
 *         android:layout_height=&quot;wrap_content&quot;/&gt;
 * </pre>
 */
public class ActionBar2 extends MaterialToolbar {

    private static final int DEF_STYLE_RES = R.style.j_ActionBar;
    private static final String TAG = "ActionBar2";
    private static boolean working;
    private static boolean halt = false;
    private static int tries;
    private static boolean iconAsLogo;

    public ActionBar2(@NonNull Context context) {
        this(context, null);
    }

    public ActionBar2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.toolbarStyle);
    }

    public ActionBar2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(wrap(context, attrs, defStyleAttr, DEF_STYLE_RES), attrs, defStyleAttr);
        // Ensure we are using the correctly themed context rather than the context that was passed in.
        context = getContext();

        final TypedArray a =
                context.obtainStyledAttributes(attrs, R.styleable.jToolbar, defStyleAttr, DEF_STYLE_RES);

        iconAsLogo = a.getBoolean(R.styleable.jToolbar_iconAsLogo, true);
        //subtitleCentered = a.getBoolean(R.styleable.jToolbar_jsubtitleCentered, false);

        a.recycle();

        if (isInEditMode()) {
            iconAsLogo = false;
            setLogo(R.drawable.ic_launcher_j);
            setTitle("PREVIEW OF LAYOUT");
        }

        useActivityIconAsLogo(false);
        Log.i(TAG, "init complete!!");
    }

    public void useActivityIconAsLogo(boolean override) {
        if (iconAsLogo || override) {
            setLogo(IconUtils.getActivityIcon(getContext()));
        } else {
            Log.i(TAG, "icon disabled in config, Please set icon manually");
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        TextView titleTextView = ToolbarUtils.getTitleTextView(this);
        if (titleTextView != null) {
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17.50F);
        }
        TextView subtitleTextView = ToolbarUtils.getSubtitleTextView(this);
        if (subtitleTextView != null) {
            subtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        }
        fixParameters();

    }

    @Override
    public void requestLayout(){
        if (working) {
            Log.i(TAG, "give it a rest!");
        } else {
            Log.i(TAG, "reloading layout");
            super.requestLayout();
        }
    }

    private void fixParameters(){
        Log.i(TAG, String.valueOf(tries));
        Log.i(TAG, String.valueOf(working));
        Log.i(TAG, String.valueOf(halt));
        if (!halt) {
            working = true;
            int ABHeight = this.getHeight();
            ImageButton nav = ToolbarUtils.getNavImageView(this);
            ImageView logoImageView = ToolbarUtils.getLogoImageView(this);
            TextView titleTextView = ToolbarUtils.getTitleTextView(this);
            TextView subtitleTextView = ToolbarUtils.getSubtitleTextView(this);
            Resources r = getContext().getResources();
            int pxl = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    0,
                    r.getDisplayMetrics()
            );
            int pxr = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    8,
                    r.getDisplayMetrics()
            );
            if (logoImageView != null) {
                LayoutParams params = (LayoutParams) logoImageView.getLayoutParams();
                params.setMargins(pxl, (int) r.getDimension(R.dimen.j_action_bar_icon_vertical_padding), pxr, (int) r.getDimension(R.dimen.j_action_bar_icon_vertical_padding));
                params.height = (int) (ABHeight - (15 * getContext().getResources().getDisplayMetrics().density));
                params.width = (int) (ABHeight - (15 * getContext().getResources().getDisplayMetrics().density));
                logoImageView.setLayoutParams(params);
                logoImageView.setPadding(0, 0, 0, 0);
            } else {
                Log.e(TAG, "logoImageView is disabled!!!");
            }
            if (titleTextView != null) {
                LayoutParams params = (LayoutParams) titleTextView.getLayoutParams();
                params.setMargins(pxl, pxl, pxl, pxl);
                params.height = LayoutParams.WRAP_CONTENT;
                params.width = LayoutParams.WRAP_CONTENT;
                titleTextView.setLayoutParams(params);
                titleTextView.setPadding(0, 0, 0, 0);
            } else {
                Log.e(TAG, "titleTextView is disabled!!!");
            }
            if (subtitleTextView != null) {
                LayoutParams params = (LayoutParams) subtitleTextView.getLayoutParams();
                params.setMargins(pxl, pxl, pxl, pxl);
                params.height = LayoutParams.WRAP_CONTENT;
                params.width = LayoutParams.WRAP_CONTENT;
                subtitleTextView.setLayoutParams(params);
                subtitleTextView.setPadding(0, 0, 0, 0);
            } else {
                Log.e(TAG, "subtitleTextView is disabled!!!");
            }
            if (nav != null) {
                LayoutParams params = (LayoutParams) nav.getLayoutParams();
                params.setMargins(pxl, pxl, pxl, pxl);
                params.height = ABHeight;
                params.width = ABHeight;
                nav.setLayoutParams(params);
                nav.setPadding(0, 0, 0, 0);
            } else {
                Log.e(TAG, "nav is disabled!!!");
            }
            working = false;
            requestLayout();
            halt = true;
        } else {
            if (tries < 1) {
                tries++;
                Log.i(TAG, String.valueOf(tries));
            } else {
                tries = 0;
                halt = false;
                Log.i(TAG, "it has finally shut up");
            }
        }
    }

}