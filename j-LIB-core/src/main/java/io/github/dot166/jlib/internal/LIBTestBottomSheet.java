package io.github.dot166.jlib.internal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RestrictTo;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import io.github.dot166.jlib.BirdGameActivity;
import io.github.dot166.jlib.ObstacleGameActivity;
import io.github.dot166.jlib.R;
import io.github.dot166.jlib.rss.RSSActivity;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class LIBTestBottomSheet extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_bottom_sheet_content, container, false);
        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Exiting...", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        });
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Starting BirdGame");
                startActivity(new Intent(v.getContext(), BirdGameActivity.class));
            }
        });
        view.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Starting ObstacleGame");
                startActivity(new Intent(v.getContext(), ObstacleGameActivity.class));
            }
        });
        view.findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Starting RSSActivity");
                startActivity(new Intent(v.getContext(), RSSActivity.class));
            }
        });
        return view;
    }

    public static final String TAG = "LIBTestBottomSheet";
}
