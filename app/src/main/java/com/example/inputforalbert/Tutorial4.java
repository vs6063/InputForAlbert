package com.example.inputforalbert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Tutorial4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial4);

        final ImageButton nextButton = (ImageButton) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextTutorial = new Intent(getApplicationContext(), Tutorial5.class);
                startActivity(nextTutorial);
            }
        });

        final ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent prevTutorial = new Intent(getApplicationContext(), Tutorial3.class);
                startActivity(prevTutorial);
            }
        });
    }
}
