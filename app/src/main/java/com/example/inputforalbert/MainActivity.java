package com.example.inputforalbert;

import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;
import android.view.GestureDetector;

import java.util.ArrayList;

import static android.view.MotionEvent.ACTION_UP;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    // current digit
    private static int digit = 0;
    // last number of pointers on screen.
    private static int lastCount = 0;
    // TextView for current digit
    private TextView digitView;
    // ArrayList for pin
    private ArrayList<TextView> pin;
    // Current Position of pin
    private int currentPinPos;

    //Initializer stuff for methods
    private GestureDetector gestureStuff;
    private Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        digitView = (TextView) findViewById(R.id.digitView);
        this.gestureStuff = new GestureDetector(this,this);
        pin = new ArrayList<TextView>();
        pin.add((TextView) findViewById(R.id.digit1));
        pin.add((TextView) findViewById(R.id.digit2));
        pin.add((TextView) findViewById(R.id.digit3));
        pin.add((TextView) findViewById(R.id.digit4));
        for (int i = 0; i < 4; i++) {
            pin.get(i).setText("*");
        }
        currentPinPos = 0;

        v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
    }

    @Override
    public boolean onTouchEvent (MotionEvent e) {
        if(!this.gestureStuff.onTouchEvent(e)) {
            if (e.getPointerCount() < lastCount) {
                digit += (lastCount - e.getPointerCount());
                v.vibrate(10);
            }
            if (e.getAction() == ACTION_UP) {
                digit++;
                v.vibrate(20);
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
        return false;
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
        float dx = motionEvent1.getX(0) - motionEvent.getX(0);
        float dy = motionEvent1.getY(0) - motionEvent.getY(0);

        if(motionEvent1.getAction() == ACTION_UP) {
            if(dx > dy) {
                return true;
            }
        }
//        if(v > 100)
//            setDigit("LEFT");
//        else if(v < -100)
//            setDigit("RIGHT");
//        else if(v1 > 100)
//            textView.setText(String.valueOf("UP"));
//        if(v1 < -100)
//            setDigit("DOWN");
        return true;
    }

    private void setDigit(String swipe) {

        if(swipe.equals("RIGHT")) {
            if(currentPinPos == 4) return;
            pin.get(currentPinPos).setText(String.valueOf(digit));
            digit = 0;
            currentPinPos++;
        } else if(swipe.equals("LEFT")){
            digit = 0;
        } else if(swipe.equals("DOWN")){
            for (int i = 0; i < 4; i++) {
                pin.get(i).setText("*");
            }
            currentPinPos = digit = 0;
        }
        digitView.setText("0");
        v.vibrate(80);
    }
    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }
}
