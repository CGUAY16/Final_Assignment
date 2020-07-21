package com.CyberNerdForHireGames.SlimeInvaders.ProfileSignUpSlashLogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.CyberNerdForHireGames.SlimeInvaders.R;

public class FirstLoginScreen extends AppCompatActivity {

    private Button loginButton, createButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen_layout);

        createButton = findViewById(R.id.connect_create_prof_page);
        loginButton = findViewById(R.id.connect_login_page);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstLoginScreen.this, RegisterActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstLoginScreen.this, SignInActivity.class));
            }
        });
    }
}
