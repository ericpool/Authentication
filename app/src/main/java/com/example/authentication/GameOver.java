package com.example.authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {

    private TextView HighScoreTV;
    public int highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        this.HighScoreTV = this.findViewById(R.id.HighScoreTV);

        Intent i = getIntent();
        int highscore = i.getIntExtra("highscore", 0);

        HighScoreTV.setText(highscore + "");

    }

    public void onStartOverbuttonPressed(View v)
    {
        Intent i = new Intent(this, MainActivity.class);
        this.startActivity(i);
    }

}
