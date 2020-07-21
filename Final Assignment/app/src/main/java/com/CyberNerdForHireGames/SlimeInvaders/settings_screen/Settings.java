package com.CyberNerdForHireGames.SlimeInvaders.settings_screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.CyberNerdForHireGames.SlimeInvaders.ProfileSignUpSlashLogin.SignInActivity;
import com.CyberNerdForHireGames.SlimeInvaders.R;
import com.CyberNerdForHireGames.SlimeInvaders.main_menu_screen.MainMenu;
import com.google.firebase.firestore.FirebaseFirestore;

public class Settings extends AppCompatActivity {

    private static final String TAG = "Settings";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RadioGroup radioGroup;
    private RadioButton easy, normal, hard;
    private Button changeProfiles, button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen_layout);

        radioGroup = findViewById(R.id.radioGroupButtons);

        easy = findViewById(R.id.RbuttonEasy);
        normal = findViewById(R.id.RbuttonNormal);
        hard = findViewById(R.id.RbuttonHard);

        changeProfiles = findViewById(R.id.change_profiles);

        changeProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, SignInActivity.class));
            }
        });

        button2 = findViewById(R.id.mainmenu_return);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, MainMenu.class));
            }
        });




    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.RbuttonEasy:
                if (checked){

                    break;
                }
            case R.id.RbuttonNormal:
                if (checked){

                    break;
                }
            case R.id.RbuttonHard:
                if(checked){

                    break;
                }

        }
    }
}
