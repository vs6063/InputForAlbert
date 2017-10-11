package com.example.inputforalbert;

import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.IOException;

import static android.view.MotionEvent.ACTION_UP;

public class Tutorial1 extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private ImageButton nextButton;
    private ImageButton backButton;

    private ImageButton playSound;

    private GestureDetector gestureStuff;
    private float dx;
    private float dy;
    private boolean isScrolling;
    private static int SCROLL_THRESHHOLD = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial1);

        final MediaPlayer tutorialScript = MediaPlayer.create(Tutorial1.this,R.raw.step_1);

        nextButton = (ImageButton) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tutorialScript.release();
                Intent nextTutorial = new Intent(getApplicationContext(), Tutorial2.class);
                startActivity(nextTutorial);
            }
        });

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tutorialScript.release();
                Intent prevTutorial = new Intent(getApplicationContext(), TutorialStart.class);
                startActivity(prevTutorial);
            }
        });

        playSound = (ImageButton) findViewById(R.id.soundButton);
        playSound.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tutorialScript.start();
            }
        });

        // Scroll initialisation
        this.gestureStuff = new GestureDetector(this,this);
        isScrolling = false;
    }

    public boolean onTouchEvent (MotionEvent e) {

        this.gestureStuff.onTouchEvent(e);
        float absX = (dx > 0) ? dx : -dx;
        float absY = (dy > 0) ? dy : -dy;
        // If scroll distance is greater than the scroll threshhold, perform a swipe
        if (e.getAction() == ACTION_UP && isScrolling && (absX > SCROLL_THRESHHOLD || absY > SCROLL_THRESHHOLD)) {
            // Perform corresponding swipe action to scroll direction
            if (absX > absY) {
                if (dx > SCROLL_THRESHHOLD) {
                    Intent prevTutorial = new Intent(getApplicationContext(), TutorialStart.class);
                    startActivity(prevTutorial);
                } else if (dx < -SCROLL_THRESHHOLD) {
                    Intent nextTutorial = new Intent(getApplicationContext(), Tutorial2.class);
                    startActivity(nextTutorial);
                }
            }
            isScrolling = false;
            return true;
            // If scroll distance is less than the scroll threshhold, touch event is taken as a touch.
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

        dx = motionEvent1.getX(0) - motionEvent.getX(0);
        dy = motionEvent1.getY(0) - motionEvent.getY(0);
        if(motionEvent.getPointerCount() == 1) {
            isScrolling = true;
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