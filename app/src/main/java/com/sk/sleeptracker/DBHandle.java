package com.sk.sleeptracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by karti on 16-03-2017.
 */

public class DBHandle {
    static SQLiteDatabase sqlDB;
    public static SQLiteDatabase createDBTables(Context context){
        sqlDB=context.openOrCreateDatabase("dbKartik12#4",context.MODE_PRIVATE, null);

        //sqlDB.execSQL("DROP TABLE ToDoList");

        //sqlDB.execSQL("ALTER TABLE ToDoList ADD COLUMN DeadlineDate varchar(10)");

        sqlDB.execSQL("CREATE TABLE IF NOT EXISTS SleepTimes(Date varchar(10) PRIMARY KEY,SleepTime varchar(10),WakeupTime varchar(10))");

        return sqlDB;
    }
}
