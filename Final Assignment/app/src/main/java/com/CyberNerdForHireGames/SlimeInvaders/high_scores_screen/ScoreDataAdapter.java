package com.CyberNerdForHireGames.SlimeInvaders.high_scores_screen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CyberNerdForHireGames.SlimeInvaders.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ScoreDataAdapter extends FirestoreRecyclerAdapter<ScoreData,ScoreDataAdapter.ScoreHolder> {

    public ScoreDataAdapter(FirestoreRecyclerOptions<ScoreData> options) {
        super(options);
    }

    class ScoreHolder extends RecyclerView.ViewHolder{

        TextView username;
        TextView score;

        public ScoreHolder (View v){
            super(v);
            username = v.findViewById(R.id.user_id_username);
            score = v.findViewById(R.id.user_id_highscore);

        }



    }

    @Override
    protected void onBindViewHolder(ScoreHolder scoreHolder, int i, ScoreData scoreData) {
        scoreHolder.username.setText(scoreData.getHighscoreUsername());
        scoreHolder.score.setText(String.valueOf(scoreData.getScore()));
    }

    @NonNull
    @Override
    public ScoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_highscore_layout,parent,false);
        return new ScoreHolder(v);
    }




}