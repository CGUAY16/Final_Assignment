package com.CyberNerdForHireGames.SlimeInvaders.ProfileSignUpSlashLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.CyberNerdForHireGames.SlimeInvaders.R;
import com.CyberNerdForHireGames.SlimeInvaders.main_menu_screen.MainMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final int RC_SIGN_IN = 123;

    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_SCORE = "score";

    private Button haveAccount;
    private EditText email, password, username;
    private Button createProf;

    ProgressDialog progressDialog;

    // Firebase authentication
    private FirebaseAuth mAuth;

    //Firestore instance
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        username = findViewById(R.id.edit_text_username);
        email = findViewById(R.id.edit_text_email);
        password = findViewById(R.id.edit_text_password);
        createProf = findViewById(R.id.create_profile_button);
        haveAccount = findViewById(R.id.have_acct);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");

        createProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userUsername = username.getText().toString().trim();
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                    //validate & set error
                    email.setError("Invalid Email");
                    email.setFocusable(true);
                } else if (password.length() < 6){
                    password.setError("Password length must be at least 6 characters.");
                    password.setFocusable(true);
                } else{
                    registerUser(userEmail, userPassword);
                }
            }
        });

        // login text view on click listener
        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, SignInActivity.class));
            }
        });

    }

    private void registerUser(String e, String pass) {
        progressDialog.show();

        // creates a user
        mAuth.createUserWithEmailAndPassword(e, pass)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // Sign in success, display message to user, go to profile page

                            FirebaseUser user = mAuth.getCurrentUser();
                            progressDialog.dismiss();

                            String score = "0";
                            String userUsername = username.getText().toString();
                            String userEmail = email.getText().toString();
                            String userPassword = password.getText().toString();

                            Map<String, Object> userID = new HashMap<>();
                            userID.put(KEY_USERNAME, userUsername);
                            userID.put(KEY_EMAIL,userEmail);
                            userID.put(KEY_PASSWORD,userPassword);
                            userID.put(KEY_SCORE, score);

                            db.collection("users").document(userUsername).set(userID)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(RegisterActivity.this,"Registration Successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegisterActivity.this, MainMenu.class));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, "error!", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null)
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


}
