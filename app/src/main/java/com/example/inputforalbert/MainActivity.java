package com.example.inputforalbert;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.GestureDetector;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static android.view.MotionEvent.ACTION_UP;

// gary is testing his push

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    // constant for passing pin as intent to DisplayPin activity
    public static final String PIN = "com.example.inputforalbert.PIN";

    // VARIABLES FOR DIGIT MANIPULATION
    // current digit
    private static int digit;
    // last number of pointers on screen.
    private static int lastCount;
    // TextView for current digit
    private TextView digitView;

    // VARIABLES FOR PIN MANIPULATION
    // max pin
    private final int MIN_PIN = 4;
    private final int MAX_PIN = 12;
    // TextView for pin
    private TextView pinView;
    // ArrayList for pin
    private ArrayList<String> pin;
    // Toggle button for pin
    private ToggleButton pinToggle;
    // Toggle variable for pin
    private boolean showPin;

    // Variables for scrolling
    private GestureDetector gestureStuff;
    private float dx;
    private float dy;
    private boolean isScrolling;
    private static int SCROLL_THRESHHOLD = 250;
    private static final double DEADZONE_ANGLE = 0.57735026919;

    // Initializer stuff for tactile feedback systems
    private Vibrator v;
    private MediaPlayer tapSound;
    private MediaPlayer swipeSound;

    // Variable for controlling tap distance threshhold
    //private static int TAP_THRESHHOLD = 50;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Digit initialisation
        digitView = (TextView) findViewById(R.id.digitView);
        digitView.setText("");
        digit = 0;
        lastCount = 0;

        // Pin initialisation
        pinView = (TextView) findViewById(R.id.pinView);
        pin = new ArrayList<String>();
        ToggleButton pinToggle = (ToggleButton) findViewById(R.id.pinToggle);
        pinToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showPin = true;
                    digitView.setText(String.valueOf(digit));
                    pinView.setText(TextUtils.join(" ", pin));
                } else {
                    showPin = false;
                    digitView.setText("");
                    String pinConfirmed = "";
                    for (int i = pin.size(); i > 0; i--) {
                        pinConfirmed += "* ";
                    }
                    pinView.setText(pinConfirmed);
                }
            }
        });

        // Back button listener
        final Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent back = new Intent(getApplicationContext(), Startup.class);
                startActivity(back);
            }
        });

        // Scroll initialisation
        this.gestureStuff = new GestureDetector(this,this);
        isScrolling = false;

        // Tactile feedback initialisation
        v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        tapSound = MediaPlayer.create(this, R.raw.tap_sound);
        swipeSound = MediaPlayer.create(this, R.raw.swipe_sound);
    }

    @Override
    public boolean onTouchEvent (MotionEvent e) {
        
        this.gestureStuff.onTouchEvent(e);
        float absX = (dx > 0) ? dx : -dx;
        float absY = (dy > 0) ? dy : -dy;
        // If scroll distance is greater than the scroll threshhold, perform a swipe
        if(e.getAction() == ACTION_UP && isScrolling && (absX > SCROLL_THRESHHOLD || absY > SCROLL_THRESHHOLD)) {
            // Perform corresponding swipe action to scroll direction
            if(absX > absY) {
                if (dx > SCROLL_THRESHHOLD) {
                    setDigit("RIGHT");
                } else if (dx < -SCROLL_THRESHHOLD) {
                    setDigit("LEFT");
                }
            } else {
                if(dy < -SCROLL_THRESHHOLD) {
                    setDigit("UP");
                } else if(dy > SCROLL_THRESHHOLD) {
                    setDigit("DOWN");
                }
            }
            isScrolling = false;
        // If scroll distance is less than the scroll threshhold, touch event is taken as a touch.
        } else { //if(absX < TAP_THRESHHOLD && absY < TAP_THRESHHOLD) {
            // Compare current pointer count and last pointer count to determine how many fingers are on the screen
            if (e.getPointerCount() < lastCount) {
                digit += (lastCount - e.getPointerCount());
                v.vibrate(20);
                tapSound.start();
            }
            // last pointer leaves screen, increment digit by 1
            if (e.getAction() == ACTION_UP) {
                digit++;
                v.vibrate(20);
                tapSound.start();
            }
            // ensure digit doesn't pass 9
            if (digit > 9) {
                digit = 9;
            }
            
            // Change digit view accordingly
            if (showPin) {
                digitView.setText(String.valueOf(digit));
            }
            // update last pointer count
            lastCount = e.getPointerCount();
        }
        return true;
    }

    private void setDigit(String swipe) {

        int currentPin = pin.size();
        if(swipe.equals("RIGHT")) {
            // append current digit to pin and reset digit to 0
            if(currentPin >= MAX_PIN) return;
            pin.add(String.valueOf(digit));
        } else if(swipe.equals("LEFT")){
            // reset digit to 0
        } else if(swipe.equals("DOWN")){
            // reset entire pin and reset digit to 0
            pin.clear();
        } else if(swipe.equals("UP")){
            // submit current pin to DisplayPin activity as intent
            if(pin.size() < MIN_PIN) return;
            swipeSound.start();
            v.vibrate(80);
            Intent intent = new Intent(this, DisplayPin.class);
            intent.putExtra(PIN, pin);
            startActivity(intent);
        }
        // in all swipe cases, digit is reset to 0
        digit = 0;

        // reset digit and pin views
        if (showPin) {
            digitView.setText(String.valueOf(digit));
            pinView.setText(TextUtils.join(" ", pin));
        } else {
            digitView.setText("");
            String pinConfirmed = "";
            for (int i = pin.size(); i > 0; i--) {
                pinConfirmed += "* ";
            }
            pinView.setText(pinConfirmed);
        }

        // output sound and vibration tactile feedback
        swipeSound.start();
        v.vibrate(80);
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
