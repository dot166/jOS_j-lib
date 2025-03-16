/*
 * Copyright (C) 2014 The Android Open Source Project
 * adapted to jLib in 2024 by ._______166
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

package io.github.dot166.jlib;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import io.github.dot166.jlib.app.jActivity;
import io.github.dot166.jlib.widget.BirdGame;

public class BirdGameActivity extends jActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.birdgame);
        BirdGame world = (BirdGame) findViewById(R.id.world);
        world.setScoreField((TextView) findViewById(R.id.score));
        world.setSplash(findViewById(R.id.welcome));
        Log.v(BirdGame.TAG, "focus: " + world.requestFocus());
    }
}
