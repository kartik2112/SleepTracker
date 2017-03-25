package com.sk.sleeptracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.sleeptracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SleepTimesView extends AppCompatActivity {
    private TextView sleepTxtClck,wakeupTxtClck;
    private SQLiteDatabase sqlDB=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_times_view);


        sleepTxtClck=(TextView)findViewById(R.id.sleepTextClock);
        wakeupTxtClck=(TextView)findViewById(R.id.wakeupTextClock);

        Calendar c=Calendar.getInstance();

        sqlDB=DBHandle.createDBTables(getApplicationContext());
        Cursor findTimes=sqlDB.rawQuery("SELECT * FROM SleepTimes where Date='"+new SimpleDateFormat("yyyy-MM-dd").format(c.getTime())+"'",null);

        try {
            String dateHandle=null;
            String sleepTime=null;
            String wakeUpTime=null;

            Log.d("ABCABC",findTimes.getCount()+" entries found for "+new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()));
            if(findTimes!=null){
                if(findTimes.moveToFirst()){
                    do{
                        dateHandle=findTimes.getString( findTimes.getColumnIndex("Date"));
                        sleepTime=findTimes.getString( findTimes.getColumnIndex("SleepTime"));
                        wakeUpTime=findTimes.getString( findTimes.getColumnIndex("WakeupTime"));

                    }while (findTimes.moveToNext());
                }
                sleepTxtClck.setText(sleepTime);
                wakeupTxtClck.setText(wakeUpTime);
            }
        }
        finally {
            findTimes.close();
            sqlDB.close();
        }


        CalendarView calendarView=(CalendarView)findViewById(R.id.cal);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                Calendar c=Calendar.getInstance();
                c.set(year,month,day);

                sqlDB=DBHandle.createDBTables(getApplicationContext());
                Cursor findTimes=sqlDB.rawQuery("SELECT * FROM SleepTimes where Date='"+new SimpleDateFormat("yyyy-MM-dd").format(c.getTime())+"'",null);

                try {
                    String dateHandle=null;
                    String sleepTime=null;
                    String wakeUpTime=null;

                    Log.d("ABCABC",findTimes.getCount()+" entries found for "+new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()));
                    if(findTimes!=null){
                        if(findTimes.moveToFirst()){
                            do{
                                dateHandle=findTimes.getString( findTimes.getColumnIndex("Date"));
                                sleepTime=findTimes.getString( findTimes.getColumnIndex("SleepTime"));
                                wakeUpTime=findTimes.getString( findTimes.getColumnIndex("WakeupTime"));

                            }while (findTimes.moveToNext());
                        }
                        sleepTxtClck.setText(sleepTime);
                        wakeupTxtClck.setText(wakeUpTime);
                    }
                }
                finally {
                    findTimes.close();
                    sqlDB.close();
                }

            }
        });
    }

}
