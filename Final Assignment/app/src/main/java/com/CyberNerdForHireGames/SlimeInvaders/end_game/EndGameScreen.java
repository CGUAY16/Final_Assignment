package com.CyberNerdForHireGames.SlimeInvaders.end_game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.CyberNerdForHireGames.SlimeInvaders.R;
import com.CyberNerdForHireGames.SlimeInvaders.high_scores_screen.HighScores;
import com.CyberNerdForHireGames.SlimeInvaders.in_game_screen.GameView;
import com.CyberNerdForHireGames.SlimeInvaders.main_menu_screen.MainMenu;

public class EndGameScreen extends AppCompatActivity {

    private TextView endGame;
    private Button mainMenu, highScores;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_game_screen_layout);

        endGame = findViewById(R.id.final_score);
        endGame.setText("0");

        mainMenu = findViewById(R.id.return_mainmenu);
        highScores = findViewById(R.id.proceed_to_highscores);


        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(EndGameScreen.this, MainMenu.class));
            }
        });

        highScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(EndGameScreen.this, HighScores.class));
            }
        });


    }
}
