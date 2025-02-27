package com.CyberNerdForHireGames.SlimeInvaders.in_game_screen;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import com.CyberNerdForHireGames.SlimeInvaders.settings_screen.Settings;


public class SlimePlaysTheGame extends Activity {

    private static final String TAG = "SlimePlaysTheGame";
    GameView gameView;
    Settings settings;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        gameView = new GameView(this, size.x, size.y);
        setContentView(gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        gameView.pause();
    }


}




