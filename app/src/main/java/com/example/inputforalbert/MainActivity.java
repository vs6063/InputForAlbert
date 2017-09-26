package com.example.inputforalbert;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.TextView;
import android.view.GestureDetector;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static android.view.MotionEvent.ACTION_UP;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    public static final String PIN = "com.example.inputforalbert.PIN";
    private final int pinLimit = 12;

    // current digit
    private static int digit = 0;
    // last number of pointers on screen.
    private static int lastCount = 0;
    // TextView for current digit
    private TextView digitView;
    // TextView for pin
    private TextView pinView;
    // ArrayList for pin
    private ArrayList<String> pin;

    //Initializer stuff for methods
    private GestureDetector gestureStuff;
    private Vibrator v;
    private MediaPlayer tapSound;
    private MediaPlayer swipeSound;

    //variables for scrolling
    private float dx;
    private float dy;
    private boolean isScrolling;
    private static int scrollThreshhold = 500;
    private static int tapThreshhold = 50;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        digitView = (TextView) findViewById(R.id.digitView);
        pinView = (TextView) findViewById(R.id.pinView);
        this.gestureStuff = new GestureDetector(this,this);
        pin = new ArrayList<String>();
        v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        isScrolling = false;

        tapSound = MediaPlayer.create(this, R.raw.tap);
        swipeSound = MediaPlayer.create(this, R.raw.swipe);
    }

    @Override
    public boolean onTouchEvent (MotionEvent e) {
        
        this.gestureStuff.onTouchEvent(e);
        float absX = (dx > 0) ? dx : -dx;
        float absY = (dy > 0) ? dy : -dy;
        if(e.getAction() == ACTION_UP && isScrolling && (absX > scrollThreshhold || absY > scrollThreshhold)) {
            if(absX > absY) {
                if (dx > scrollThreshhold) {
                    setDigit("RIGHT");
                } else if (dx < -scrollThreshhold) {
                    setDigit("LEFT");
                }
            } else {
                if(dy < -scrollThreshhold) {
                    setDigit("UP");
                } else if(dy > scrollThreshhold) {
                    setDigit("DOWN");
                }
            }
            isScrolling = false;
        } else { //if(absX < tapThreshhold && absY < tapThreshhold) {
            if (e.getPointerCount() < lastCount) {
                digit += (lastCount - e.getPointerCount());
                v.vibrate(20);
                tapSound.start();
            }
            if (e.getAction() == ACTION_UP) {
                digit++;
                v.vibrate(20);
                tapSound.start();
            }
            if (digit > 9) {
                digit = 9;
            }
            digitView.setText(String.valueOf(digit));
            lastCount = e.getPointerCount();
        }
        return true;
    }

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
        /*
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        setDigit("RIGHT");
                    } else {
                        setDigit("LEFT");
                    }
                    result = true;
                }
            }
            else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    setDigit("DOWN");
                } else {
                    digitView.setText("UP");
                }
                result = true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
        */
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    private static final double DEADZONE_ANGLE = 0.57735026919;

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        dx = motionEvent1.getX(0) - motionEvent.getX(0);
        dy = motionEvent1.getY(0) - motionEvent.getY(0);
        isScrolling = true;
        return true;
    }

    private void setDigit(String swipe) {

        int currentPin = pin.size();
        if(swipe.equals("RIGHT")) {
            if(currentPin >= pinLimit) return;
            pin.add(String.valueOf(digit));
            digit = 0;
        } else if(swipe.equals("LEFT")){
            digit = 0;
        } else if(swipe.equals("DOWN")){
            digit = 0;
            pin.clear();
        } else if(swipe.equals("UP")){
            Intent intent = new Intent(this, DisplayPin.class);
            intent.putExtra(PIN, pin);
            startActivity(intent);
        }
        digitView.setText(String.valueOf(digit));
        pinView.setText(TextUtils.join(" ", pin));

        swipeSound.start();
        v.vibrate(80);
    }
    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }
}
