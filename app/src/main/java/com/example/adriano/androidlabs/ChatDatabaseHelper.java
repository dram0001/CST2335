package com.example.adriano.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Messages.db";
    public static final String TABLE_NAME = "messages";
    public static int VERSION_NUM = 3;
    public static final String KEY_ID = "_id";
    public static final String KEY_MESSAGE = "message";
    private static final String ACTIVITY_NAME = "ChatDatabaseHelper";

  public ChatDatabaseHelper(Context ctx){
      super(ctx, DATABASE_NAME, null, VERSION_NUM);
  }
  @Override
  public void onCreate(SQLiteDatabase db){
      Log.i(ACTIVITY_NAME, "Calling onCreate");
      db.execSQL("create table " + TABLE_NAME + " (" + KEY_ID + " integer primary key autoincrement, "
                 + KEY_MESSAGE + " text);");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldV, int newV){
      Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion = " + oldV + " newVersion = " + newV);
      db.execSQL("drop table if exists " + TABLE_NAME);
      onCreate(db);
  }

}