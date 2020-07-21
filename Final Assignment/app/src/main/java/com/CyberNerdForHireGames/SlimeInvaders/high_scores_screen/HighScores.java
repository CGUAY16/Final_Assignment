package com.CyberNerdForHireGames.SlimeInvaders.high_scores_screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CyberNerdForHireGames.SlimeInvaders.R;
import com.CyberNerdForHireGames.SlimeInvaders.main_menu_screen.MainMenu;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HighScores extends AppCompatActivity {

    private static final String TAG = "HighScores";

    private RecyclerView recyclerView;
    private Button button;

    private ScoreDataAdapter adapter;
    
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores_screen_layout);

        //setUpRecyclerView();

        button = findViewById(R.id.mainmenu_return);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HighScores.this, MainMenu.class));
            }
        });

    }

    private void setUpRecyclerView(){
        Query query = userRef.orderBy("score",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ScoreData> options = new FirestoreRecyclerOptions.Builder<ScoreData>()
                .setQuery(query, ScoreData.class)
                .build();

        adapter = new ScoreDataAdapter(options);

        recyclerView = findViewById(R.id.highscore_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



    }


    @Override
    protected void onStart(){
        super.onStart();
        //adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //adapter.stopListening();
    }
}
