package com.CyberNerdForHireGames.SlimeInvaders.main_menu_screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.CyberNerdForHireGames.SlimeInvaders.R;
import com.CyberNerdForHireGames.SlimeInvaders.high_scores_screen.HighScores;
import com.CyberNerdForHireGames.SlimeInvaders.in_game_screen.SlimePlaysTheGame;
import com.CyberNerdForHireGames.SlimeInvaders.settings_screen.Settings;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class MainMenu extends AppCompatActivity {

    private static final String TAG = "MainMenu";

    public Button button1, button2, button3;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_screen_layout);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Start up the game window
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, SlimePlaysTheGame.class));
            }
        });

        // start up highscores activity
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, HighScores.class));
            }
        });

        // Start up the settings activity
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, Settings.class));
            }
        });
    }






}
