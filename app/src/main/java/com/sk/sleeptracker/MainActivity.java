package com.sk.sleeptracker;


import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView sleepTxtClck,wakeupTxtClck;
    private Calendar sleepTime,wakeupTime;
    private Button addButton,checkTimesButton,setAlarmButton;
    public static final int REQUEST_TYPE_FOR_SLEEP=2;
    public static final int REQUEST_TYPE_FOR_WAKEUP=4;
    private SQLiteDatabase sqlDB=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sleepTime=Calendar.getInstance();
        wakeupTime=Calendar.getInstance();

        sleepTxtClck=(TextView)findViewById(R.id.sleepTextClock1);
        wakeupTxtClck=(TextView)findViewById(R.id.wakeUpTextClock1);

        /**
         * SharedPreferences are checked to see if this particular preference has been added to it.
         * If it has been then, this is the time set for wakeuptime, else standard 6 am time will be set.
         */
        SharedPreferences sp=this.getSharedPreferences(getString(R.string.alarm_name),MODE_PRIVATE);
        String timeFromSP=sp.getString(getString(R.string.alarm_name),"None");

        boolean wakeUpTimeSet=false;
        if(timeFromSP.equals("None")){
            /**
             * This will set sleep and wakeup times to standard ones
             */
            sleepTime.set(Calendar.HOUR_OF_DAY,23);
            sleepTime.set(Calendar.MINUTE,30);

            wakeupTime.set(Calendar.HOUR_OF_DAY,6);
            wakeupTime.set(Calendar.MINUTE,0);

            setTextClockTime(sleepTxtClck,sleepTime);
            setTextClockTime(wakeupTxtClck,wakeupTime);
        }
        else{
            wakeUpTimeSet=true;

            /**
             * This will set sleep and wakeup times to standard ones
             */
            sleepTime.set(Calendar.HOUR_OF_DAY,23);
            sleepTime.set(Calendar.MINUTE,30);
            setTextClockTime(sleepTxtClck,sleepTime);

            wakeupTime=getTimeAsCalendar(timeFromSP);
            setTextClockTime(wakeupTxtClck,wakeupTime);
        }


        sqlDB=DBHandle.createDBTables(this);
        Cursor findTimes=sqlDB.rawQuery("SELECT * FROM SleepTimes where Date='"+new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())+"'",null);

        try{
            String dateHandle=null;
            String sleepTimeS=null;
            String wakeUpTimeS=null;

            if(findTimes!=null){
                if(findTimes.moveToFirst()){
                    do{
                        dateHandle=findTimes.getString( findTimes.getColumnIndex("Date"));
                        sleepTimeS=findTimes.getString( findTimes.getColumnIndex("SleepTime"));
                        wakeUpTimeS=findTimes.getString( findTimes.getColumnIndex("WakeupTime"));

                    }while (findTimes.moveToNext());
                }
                if(sleepTimeS!=null){
                    sleepTxtClck.setText(sleepTimeS);
                    sleepTime=getTimeAsCalendar(sleepTimeS);
                }
                if(wakeUpTimeS!=null){
                    wakeupTxtClck.setText(wakeUpTimeS);
                    wakeupTime=getTimeAsCalendar(wakeUpTimeS);
                }
            }
        }
        catch(Exception e){
            Log.d("ABCABC",e.toString());
        }
        finally {
            findTimes.close();
            sqlDB.close();
        }



        addButton=(Button)findViewById(R.id.setTimeBtn);
        checkTimesButton=(Button)findViewById(R.id.checkTimes);
        setAlarmButton=(Button)findViewById(R.id.setAlarm);

        sleepTxtClck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(view.getContext(),TimePickerActivity.class);
                i.putExtra("Time",getTimeAsString(sleepTime));
                startActivityForResult(i,REQUEST_TYPE_FOR_SLEEP);
            }
        });

        wakeupTxtClck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(view.getContext(),TimePickerActivity.class);
                i.putExtra("Time",getTimeAsString(wakeupTime));
                startActivityForResult(i,REQUEST_TYPE_FOR_WAKEUP);
            }
        });

        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(view.getContext(),AlarmSetter.class);
                startActivity(i);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ABCABC","Button clicked");
                sqlDB=DBHandle.createDBTables(view.getContext());
                Cursor findTimes=sqlDB.rawQuery("SELECT * FROM SleepTimes where Date='"+new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())+"'",null);
                try{


                    if(findTimes!=null && findTimes.getCount()!=0){
                        String stmt="UPDATE SleepTimes SET SleepTime='"+getTimeAsString(sleepTime)+"', WakeupTime='"+getTimeAsString(wakeupTime)+"' WHERE Date='"+new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())+"'";
                        sqlDB.execSQL(stmt);
                        Log.d("ABCABC",stmt);
                        Log.d("ABCABC","Times updated");
                        Toast.makeText(getApplicationContext(),"Sleep and wakeup times updated",Toast.LENGTH_LONG).show();
                    }
                    else{
                        ContentValues c=new ContentValues();
                        c.put("Date",new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
                        c.put("SleepTime",getTimeAsString(sleepTime));
                        c.put("WakeupTime",getTimeAsString(wakeupTime));

                        String stmt="INSERT INTO SleepTimes VALUES('"+new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())+"','"+getTimeAsString(sleepTime)+"','"+getTimeAsString(wakeupTime)+"')";
                        sqlDB.execSQL(stmt);
                        Log.d("ABCABC",stmt);
                        Log.d("ABCABC","Times added");
                        Toast.makeText(getApplicationContext(),"Sleep and wakeup times added",Toast.LENGTH_LONG).show();
                    }

                    /*Intent i=new Intent(view.getContext(),SleepTimesView.class);
                    startActivity(i);*/
                }
                catch (Exception e){
                    Log.d("ABCABC",e.toString());
                }
                finally {
                    findTimes.close();
                    sqlDB.close();
                }

            }
        });

        checkTimesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent i=new Intent(view.getContext(),SleepTimesView.class);
                    startActivity(i);
                }
                catch (Exception e){
                    Log.d("ABCABC",e.toString());
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_TYPE_FOR_SLEEP){
            if(resultCode==RESULT_OK){
                sleepTime=getTimeAsCalendar(data.getStringExtra("Time"));
                String timeDisplay=data.getStringExtra("Time");

                Log.d("ABCABC",sleepTime.get(Calendar.HOUR_OF_DAY)+"");
                Log.d("ABCABC",sleepTime.get(Calendar.MINUTE)+"");

                setTextClockTimeFromString(sleepTxtClck,timeDisplay);
            }
        }
        else if(requestCode==REQUEST_TYPE_FOR_WAKEUP){
            if(resultCode==RESULT_OK){
                wakeupTime=getTimeAsCalendar(data.getStringExtra("Time"));
                String timeDisplay=data.getStringExtra("Time");

                setTextClockTimeFromString(wakeupTxtClck,timeDisplay);
            }
        }
    }

    void setTextClockTime(TextView txtClk, Calendar cal){
        txtClk.setText(getTimeAsString(cal));
        Log.d("ABCABC",getTimeAsString(cal));
        Toast.makeText(this.getApplicationContext(),getTimeAsString(cal),Toast.LENGTH_LONG).show();
    }

    void setTextClockTimeFromString(TextView txtClk, String timeDisplay){
        txtClk.setText(timeDisplay);
        Log.d("ABCABC",timeDisplay);
        Toast.makeText(this.getApplicationContext(),timeDisplay,Toast.LENGTH_LONG).show();
    }

    String getTimeAsString(Calendar cal){
        int hours=cal.get(Calendar.HOUR_OF_DAY);
        String minutes=String.format("%02d",cal.get(Calendar.MINUTE));
        String AMPM=null;
        if(hours>=12){
            AMPM="PM";
        }
        else{
            AMPM="AM";
        }
        hours=hours%12;
        if(hours==0){
            hours=12;
        }

        return (hours)+":"+minutes+" "+AMPM;
    }

    Calendar getTimeAsCalendar(String timeDisplay){
        Calendar c=Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm aa");
        Date date1=new Date();
        try {
            date1 = dateFormat.parse(timeDisplay);
            Log.e("Time", ""+date1);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            Log.e("Error", ""+e);
        }
        c.set(Calendar.HOUR_OF_DAY,date1.getHours());
        c.set(Calendar.MINUTE,date1.getMinutes());

        return c;

    }
}
