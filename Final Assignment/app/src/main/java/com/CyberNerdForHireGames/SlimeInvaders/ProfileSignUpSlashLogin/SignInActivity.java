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
import android.widget.TextView;
import android.widget.Toast;

import com.CyberNerdForHireGames.SlimeInvaders.R;
import com.CyberNerdForHireGames.SlimeInvaders.main_menu_screen.MainMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInActivity extends AppCompatActivity {

    EditText email, password;
    Button signin;
    TextView needAccount;

    private FirebaseAuth mAuth;

    //Firestore instance
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        email = findViewById(R.id.login_email_editText);
        password = findViewById(R.id.login_password_editText);
        signin = findViewById(R.id.signInButton);
        needAccount = findViewById(R.id.need_acct);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging In...");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                    //validate & set error
                    email.setError("Invalid Email");
                    email.setFocusable(true);
                } else{
                    loginUser(userEmail, userPassword);
                }

            }
        });

        needAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, RegisterActivity.class));
            }
        });


    }

    private void loginUser(String e, String pass) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(e, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            startActivity(new Intent(SignInActivity.this, MainMenu.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SignInActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}
