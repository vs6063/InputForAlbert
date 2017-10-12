package com.example.inputforalbert;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import static android.view.MotionEvent.ACTION_UP;

public class TutorialEnd extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private Button nextButton;
    private ImageButton backButton;

    private MediaPlayer tutorialScript;

    // Variables for scrolling
    private GestureDetector gestureStuff;
    private float dx;
    private float dy;
    private static int SCROLL_THRESHHOLD = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_end);

        tutorialScript = MediaPlayer.create(TutorialEnd.this,R.raw.conclusion_tutorial);
        tutorialScript.start();

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tutorialScript.release();
                Intent nextTutorial = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(nextTutorial);
            }
        });

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tutorialScript.release();
                Intent prevTutorial = new Intent(getApplicationContext(), Tutorial6.class);
                startActivity(prevTutorial);
            }
        });
        // Scroll initialisation
        this.gestureStuff = new GestureDetector(this,this);

    }

    @Override
    public boolean onTouchEvent (MotionEvent e) {

        this.gestureStuff.onTouchEvent(e);
        float absX = (dx > 0) ? dx : -dx;
        float absY = (dy > 0) ? dy : -dy;
        // If scroll distance is greater than the scroll threshhold, perform a swipe
        if(e.getAction() == ACTION_UP &&(absX > SCROLL_THRESHHOLD || absY > SCROLL_THRESHHOLD)) {
            // Perform corresponding swipe action to scroll direction
            if (absX > absY) {
                if (dx > SCROLL_THRESHHOLD) {
                    tutorialScript.release();
                    Intent prevTutorial = new Intent(getApplicationContext(), Tutorial6.class);
                    startActivity(prevTutorial);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

        dx = motionEvent1.getX(0) - motionEvent.getX(0);
        dy = motionEvent1.getY(0) - motionEvent.getY(0);
        if(motionEvent.getPointerCount() == 1) {
            return true;
        }
        return false;
    }

    // ----- REQUIRED BLANK FUNCTIONS FOR SCROLLING ----- //

    @Override
    public void onLongPress(MotionEvent motionEvent) { }

    @Override
    public void onShowPress(MotionEvent motionEvent) { }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { return false; }

    @Override
    public boolean onDown(MotionEvent motionEvent) { return false; }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) { return false; }
}