package com.example.inputforalbert;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;
import android.view.GestureDetector;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private static int counter = 0;
    private static int lastCount = 0;
    private TextView textView;
    private GestureDetector gestureStuff;
    private ArrayList<TextView> pin;
    private int currentPinPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
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
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        int speed = 3000;
        if(v > speed)
            setDigit("RIGHT");
        else if(v < -speed)
            setDigit("LEFT");
        else if(v1 > speed)
            setDigit("DOWN");
        if(v1 < -speed)
            textView.setText(String.valueOf("UP"));
        return true;
    }

    @Override
    public boolean onTouchEvent (MotionEvent e) {
        if(!this.gestureStuff.onTouchEvent(e)) {
            if (e.getPointerCount() < lastCount) {
                counter += (lastCount - e.getPointerCount());
            }
            if (e.getAction() == MotionEvent.ACTION_UP) {
                counter++;
            }
            if (counter > 9) {
                counter = 9;
            }
            textView.setText(String.valueOf(counter));

            lastCount = e.getPointerCount();
        }
        return true;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
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
            pin.get(currentPinPos).setText(String.valueOf(counter));
            counter = 0;
            textView.setText("0");
            currentPinPos++;
        } else if(swipe.equals("LEFT")){
            counter = 0;
            textView.setText("0");
        } else if(swipe.equals("DOWN")){
            for (int i = 0; i < 4; i++) {
                pin.get(i).setText("*");
            }
            currentPinPos = counter = 0;
        }
    }
    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int actionPerformed = e.getAction();
        boolean trigger = true;
        uf(false)
        switch (actionPerformed) {
            case MotionEvent.ACTION_POINTER_UP:{
                counter++;
                if (counter > 9) {
                    counter = 0;
                }
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(String.valueOf(counter));
                trigger = true;
                break;
            }
            case MotionEvent.ACTION_UP:{
                counter++;
                if (counter > 9) {
                    counter = 0;
                }
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(String.valueOf(counter));
                trigger = true;
                break;
            }
        }

        return trigger;
    }
    */
}
