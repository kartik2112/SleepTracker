package com.sk.helloworldproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;

public class MainActivity extends AppCompatActivity {
    private TextClock sleepTxtClck,wakeupTxtClck;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sleepTxtClck=(TextClock)findViewById(R.id.sleepTextClock);
        wakeupTxtClck=(TextClock)findViewById(R.id.wakeUpTextClock);

        addButton=(Button)findViewById(R.id.setTimeBtn);

        sleepTxtClck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
