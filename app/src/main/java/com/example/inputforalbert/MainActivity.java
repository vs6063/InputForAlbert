package com.example.inputforalbert;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;
import android.view.GestureDetector;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private static int counter = 0;
    private static int lastCount = 0;
    private TextView textView;
    private GestureDetector gestureStuff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        this.gestureStuff = new GestureDetector(this,this);

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
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
                counter = 0;
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
        if(v > 100)
            textView.setText(String.valueOf("LEFT"));
        else if(v < -100)
            textView.setText(String.valueOf("RIGHT"));
        else if(v1 > 100)
            textView.setText(String.valueOf("UP"));
        if(v1 < -100)
            textView.setText(String.valueOf("DOWN"));
        return true;
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
