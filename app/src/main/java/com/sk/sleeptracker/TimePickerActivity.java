package com.sk.sleeptracker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimePickerActivity extends AppCompatActivity {

    private Button setTimeButton;
    private TimePicker timePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_picker_layout);

        Bundle b=getIntent().getExtras();
        String time=(String)b.get("Time");

        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm aa");
        Date date1=new Date();
        try {
            date1 = dateFormat.parse(time);
            Log.e("Time", ""+date1);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            Log.e("Error", ""+e);
        }

        setTimeButton=(Button)findViewById(R.id.setTimeBtn);
        timePicker=(TimePicker)findViewById(R.id.timePicker);

        timePicker.setHour(date1.getHours());
        timePicker.setMinute(date1.getMinutes());

        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hours=timePicker.getHour();
                String minutes=String.format("%02d",timePicker.getMinute());
                String AMPM=null;
                if(hours>=12){
                    AMPM="PM";
                }
                else{
                    AMPM="AM";
                }

                String timeDisplay=(hours%12)+":"+minutes+" "+AMPM;

                Intent i=new Intent();
                i.putExtra("Time",timeDisplay);
                setResult(Activity.RESULT_OK,i);
                finish();
            }
        });
    }
}
