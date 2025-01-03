package io.github.dot166.jLib.internal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RestrictTo;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import io.github.dot166.jLib.BirdGameActivity;
import io.github.dot166.jLib.R;

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
        return view;
    }

    public static final String TAG = "LIBTestBottomSheet";
}