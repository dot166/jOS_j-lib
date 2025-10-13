package io.github.dot166.jlib.internal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RestrictTo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.dot166.jlib.BirdGameActivity
import io.github.dot166.jlib.ObstacleGameActivity
import io.github.dot166.jlib.R
import io.github.dot166.jlib.rss.RSSActivity

@RestrictTo(RestrictTo.Scope.LIBRARY)
class LIBTestBottomSheet : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.lib_test_bottom_sheet_content, container, false)
        view.findViewById<View?>(R.id.button1)?.setOnClickListener { v ->
            Toast.makeText(v.context, "Exiting...", Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        }
        view.findViewById<View?>(R.id.button2)?.setOnClickListener { v ->
            Log.i(TAG, "Starting BirdGame")
            startActivity(Intent(v.context, BirdGameActivity::class.java))
        }
        view.findViewById<View?>(R.id.button3)?.setOnClickListener { v ->
            Log.i(TAG, "Starting ObstacleGame")
            startActivity(Intent(v.context, ObstacleGameActivity::class.java))
        }
        view.findViewById<View?>(R.id.button4)?.setOnClickListener { v ->
            Log.i(TAG, "Starting RSSActivity")
            startActivity(Intent(v.context, RSSActivity::class.java))
        }
        return view
    }

    companion object {
        const val TAG: String = "LIBTestBottomSheet"
    }
}
