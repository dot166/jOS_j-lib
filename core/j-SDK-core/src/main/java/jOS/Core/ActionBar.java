package jOS.Core;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ActionBar {

    public static void actionBarConfig(String name, int logo, boolean home, AppCompatActivity context, String Action){
        Toolbar actionbar = (Toolbar) context.findViewById(R.id.toolbar);
        TextView Title = (TextView)context.findViewById(R.id.Title);
        ImageButton Back = (ImageButton)context.findViewById(R.id.back);
        ImageView icon = (ImageView)context.findViewById(R.id.icon);
        LinearLayout Back_content = context.findViewById(R.id.back_layout);
        context.setSupportActionBar(actionbar);
        if (context.getSupportActionBar() == null) {
            Log.i("ActionBar", "You forgot to import the action bar in the layout file");
        }
        context.getSupportActionBar().setDisplayShowTitleEnabled(false);
        context.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Title.setText(name);
        icon.setImageResource(logo);
        Back.setImageResource(R.drawable.j_ic_ab_back_holo_dark_am);
        if (!home) {
            Back.setVisibility(View.VISIBLE);
            Back_content.setClickable(true);
        } else if (home) {
            Back.setVisibility(View.GONE);
            Back_content.setClickable(false);
        }
        Back_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.onBackPressed();
            }
        });
    }
}
