package com.sk.sleeptracker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

public class AlarmSetter extends AppCompatActivity {

    private Button setTimeButton;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setter);

        setTimeButton=(Button)findViewById(R.id.setTimeBtn);
        timePicker=(TimePicker)findViewById(R.id.timePicker);


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

                /**
                 * This part will set wakeup time to this value
                 */
                SharedPreferences sp=getApplication().getSharedPreferences(getString(R.string.alarm_name),MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString(getString(R.string.alarm_name),timeDisplay);
                editor.commit();

                Calendar c=Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY,hours);
                c.set(Calendar.MINUTE,Integer.parseInt(minutes));

                AlarmManager alrm=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
                Intent i=new Intent(getBaseContext(),AlarmReceiver.class);
                PendingIntent pin=PendingIntent.getBroadcast(getApplicationContext(),0,i,0);
                alrm.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pin);

                setResult(Activity.RESULT_OK,i);

                finish();
            }
        });
    }
}
