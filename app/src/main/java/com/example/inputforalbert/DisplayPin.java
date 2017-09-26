package com.example.inputforalbert;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import android.view.View.OnClickListener;

public class DisplayPin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pin);

        // Get pin from Intent that started this activity
        Intent intent = getIntent();
        ArrayList<String> pin = intent.getStringArrayListExtra(MainActivity.PIN);

        // Set pinView as pin
        TextView pinView = (TextView) findViewById(R.id.pinView);
        pinView.setText(TextUtils.join(" ", pin));

        // set OnClickListener for back button
        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(back);
    }

    // onClickListener for back button
    private OnClickListener back = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            startActivity(intent);
        }
    };

}
