package com.example.inputforalbert;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
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
// victor is testing garys pushing who is testing victors pushing test

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
    SoundPool swipeSounds;
    private MediaPlayer tapSound;
    // Sounds for soundPool
    int digit_1;
    int digit_2;
    int digit_3;
    int digit_4;
    int digit_5;
    int digit_6;
    int digit_7;
    int digit_8;
    int digit_9;
    int digit_10;
    int digit_11;
    int digit_12;
    int digit_max;
    int swipe_down;
    int swipe_left;
    int swipe_up_min;
    
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
        createSoundPool();
    }

    @SuppressWarnings("deprecation")
    protected void createSoundPool(){
        swipeSounds = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        tapSound = MediaPlayer.create(MainActivity.this, R.raw.tap_sound);
        digit_1 = swipeSounds.load(MainActivity.this, R.raw.digit_1, 1);
        digit_2 = swipeSounds.load(MainActivity.this, R.raw.digit_2, 1);
        digit_3 = swipeSounds.load(MainActivity.this, R.raw.digit_3, 1);
        digit_4 = swipeSounds.load(MainActivity.this, R.raw.digit_4, 1);
        digit_5 = swipeSounds.load(MainActivity.this, R.raw.digit_5, 1);
        digit_6 = swipeSounds.load(MainActivity.this, R.raw.digit_6, 1);
        digit_7 = swipeSounds.load(MainActivity.this, R.raw.digit_7, 1);
        digit_8 = swipeSounds.load(MainActivity.this, R.raw.digit_8, 1);
        digit_9 = swipeSounds.load(MainActivity.this, R.raw.digit_9, 1);
        digit_10 = swipeSounds.load(MainActivity.this, R.raw.digit_10, 1);
        digit_11 = swipeSounds.load(MainActivity.this, R.raw.digit_11, 1);
        digit_12 = swipeSounds.load(MainActivity.this, R.raw.digit_12, 1);
        digit_max = swipeSounds.load(MainActivity.this, R.raw.digit_max, 1);
        swipe_down = swipeSounds.load(MainActivity.this, R.raw.swipe_down, 1);
        swipe_left = swipeSounds.load(MainActivity.this, R.raw.swipe_left, 1);
        swipe_up_min = swipeSounds.load(MainActivity.this, R.raw.swipe_up_min, 1);
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
            }
            // last pointer leaves screen, increment digit by 1
            if (e.getAction() == ACTION_UP) {
                if (showPin) {
                    //swipeSounds.play(tap_sound, 1, 1, 0, 0, 1);
                    tapSound.start();
                }
                digit++;
                v.vibrate(20);
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
        // output sound and vibration tactile feedback
        v.vibrate(80);
        int currentPin = pin.size();
        if(swipe.equals("RIGHT")) {
            // append current digit to pin and reset digit to 0
            if(currentPin >= MAX_PIN) {
                swipeSounds.play(digit_max, 1, 1, 1, 0, 1);
                return;
            }
            pin.add(String.valueOf(digit));
            if (pin.size() == 1) {
                swipeSounds.play(digit_1, 1, 1, 1, 0, 1);
            }
            else if (pin.size() == 2) {
                swipeSounds.play(digit_2, 1, 1, 1, 0, 1);
            }
            else if (pin.size() == 3) {
                swipeSounds.play(digit_3, 1, 1, 1, 0, 1);
            }
            else if (pin.size() == 4) {
                swipeSounds.play(digit_4, 1, 1, 1, 0, 1);
            }
            else if (pin.size() == 5) {
                swipeSounds.play(digit_5, 1, 1, 1, 0, 1);
            }
            else if (pin.size() == 6) {
                swipeSounds.play(digit_6, 1, 1, 1, 0, 1);
            }
            else if (pin.size() == 7) {
                swipeSounds.play(digit_7, 1, 1, 1, 0, 1);
            }
            else if (pin.size() == 8) {
                swipeSounds.play(digit_8, 1, 1, 1, 0, 1);
            }
            else if (pin.size() == 9) {
                swipeSounds.play(digit_9, 1, 1, 1, 0, 1);
            }
            else if (pin.size() == 10) {
                swipeSounds.play(digit_10, 1, 1, 1, 0, 1);
            }
            else if (pin.size() == 11) {
                swipeSounds.play(digit_11, 1, 1, 1, 0, 1);
            }
            else if (pin.size() == 12) {
                swipeSounds.play(digit_12, 1, 1, 1, 0, 1);
            }
        } else if(swipe.equals("LEFT")){
            // reset digit to 0
            swipeSounds.play(swipe_left, 1, 1, 1, 0, 1);
        } else if(swipe.equals("DOWN")){
            // reset entire pin and reset digit to 0
            pin.clear();
            swipeSounds.play(swipe_down, 1, 1, 1, 0, 1);
        } else if(swipe.equals("UP")){
            // submit current pin to DisplayPin activity as intent
            if(pin.size() < MIN_PIN) {
                swipeSounds.play(swipe_up_min, 1, 1, 1, 0, 1);
                return;
            }
            swipeSounds.release();
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
