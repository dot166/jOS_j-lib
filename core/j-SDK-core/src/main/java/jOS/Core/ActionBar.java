package jOS.Core;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ActionBar {

    public static void actionBarConfig(int name, int logo, boolean home, AppCompatActivity context, String Action){
        Toolbar actionbar = (Toolbar) context.findViewById(R.id.toolbar);
        TextView Title = (TextView)context.findViewById(R.id.Title);
        ImageButton Back = (ImageButton)context.findViewById(R.id.back);
        ImageButton Menu = (ImageButton)context.findViewById(R.id.menu);
        ImageView icon = (ImageView)context.findViewById(R.id.icon);
        context.setSupportActionBar(actionbar);
        context.getSupportActionBar().setDisplayShowTitleEnabled(false);
        context.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Title.setText(name);
        icon.setImageResource(logo);
        Back.setImageResource(R.drawable.j_ic_ab_back_holo_dark_am);
        if (!home) {
            Back.setVisibility(View.VISIBLE);
            Menu.setVisibility(View.GONE);
        } else if (home) {
            Back.setVisibility(View.GONE);
            Menu.setVisibility(View.VISIBLE);
        }
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.onBackPressed();
            }
        });
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ConfigIntent = new Intent(Action);
                context.startActivity(ConfigIntent);
            }
        });
    }
}
