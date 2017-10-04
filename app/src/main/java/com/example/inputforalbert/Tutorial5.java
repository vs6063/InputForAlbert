package com.example.inputforalbert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Tutorial5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial5);

        final ImageButton nextButton = (ImageButton) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextTutorial = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(nextTutorial);
            }
        });

        final ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent prevTutorial = new Intent(getApplicationContext(), Tutorial4.class);
                startActivity(prevTutorial);
            }
        });
    }
}
