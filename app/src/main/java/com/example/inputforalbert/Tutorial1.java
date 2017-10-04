package com.example.inputforalbert;

import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.IOException;

public class Tutorial1 extends AppCompatActivity {

    private ImageButton nextButton;
    private ImageButton backButton;

    private ImageButton playSound;
    private MediaPlayer tutorialScript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial1);

        tutorialScript = MediaPlayer.create(this, R.raw.step_1);

        nextButton = (ImageButton) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextTutorial = new Intent(getApplicationContext(), Tutorial2.class);
                startActivity(nextTutorial);
            }
        });

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent prevTutorial = new Intent(getApplicationContext(), Startup.class);
                startActivity(prevTutorial);
            }
        });

        playSound = (ImageButton) findViewById(R.id.soundButton);
        playSound.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tutorialScript.start();
            }
        });
    }
}
