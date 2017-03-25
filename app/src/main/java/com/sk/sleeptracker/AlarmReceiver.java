package com.sk.sleeptracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

/**
 * Created by karti on 23-03-2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MediaPlayer mp=MediaPlayer.create(context, R.raw.alarm   );
        mp.start();
        Toast.makeText(context,"Alarm ringing!",Toast.LENGTH_LONG).show();
        Toast.makeText(context,"Alarm will stop ringing in some time",Toast.LENGTH_LONG).show();
        Toast.makeText(context,"That's it!",Toast.LENGTH_LONG).show();
    }
}
