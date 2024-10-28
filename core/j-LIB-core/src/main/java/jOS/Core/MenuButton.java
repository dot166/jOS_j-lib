package jOS.Core;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuButton extends LinearLayout {

    public MenuButton(Context context) {
        this(context, null);
    }

    public MenuButton(Context context, AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.buttonStyle);
    }

    public MenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.menubutton, this, true);
        if (attrs != null) {
            TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.MenuButton, defStyleAttr, android.R.style.Widget_Holo_Button);

            String textValue = styledAttributes.getString(R.styleable.MenuButton_android_text);
            Drawable icon = styledAttributes.getDrawable(R.styleable.MenuButton_android_icon);
            int iconTint = styledAttributes.getColor(R.styleable.MenuButton_tint, context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.textColorPrimary}).getColor(0, R.color.j_primary_text_holo_dark));

            if (icon != null) {
                icon.setTint(iconTint);
            }
            setIcon(icon);
            setText(textValue);

            styledAttributes.recycle();
        }
    }

    private void setIcon(Drawable icon) {
        ImageView imageView = findViewById(R.id.image);
        imageView.setImageDrawable(icon);
    }

    public void setText(String value) {
        TextView textView = findViewById(R.id.text);
        textView.setText(value);
    }

}

