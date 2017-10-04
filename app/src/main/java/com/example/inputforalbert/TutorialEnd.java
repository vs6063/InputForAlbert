package com.example.inputforalbert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class TutorialEnd extends AppCompatActivity {

    private ImageButton nextButton;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_end);

        nextButton = (ImageButton) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextTutorial = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(nextTutorial);
            }
        });

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent prevTutorial = new Intent(getApplicationContext(), Tutorial5.class);
                startActivity(prevTutorial);
            }
        });
    }
}
