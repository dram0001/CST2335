package com.example.adriano.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity {

    protected static final String ACTIVITY_NAME = "StartActivity";
    private Button button;
    private Button startChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i(ACTIVITY_NAME, "In OnCreate()");

        button = (Button) findViewById(R.id.button2);
        startChat = (Button) findViewById(R.id.startChat);
        Intent i = new Intent(this, ListItemsActivity.class);
        Intent chat = new Intent(this, ChatWindow.class);

        button.setOnClickListener(e->{
            startActivityForResult(i, 50);
            setResult(50, i);
        });

        startChat.setOnClickListener(e->startActivity(chat));


    }

    @Override
    public void onActivityResult(int request, int result, Intent data){
       if(result == 50){
           Log.i("StartActivity", "Returned to StartActivity.onActivityResult");
       }

        if(result == Activity.RESULT_OK){
            String messagePassed = data.getStringExtra("Response");
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(getApplicationContext(), messagePassed, duration);
            toast.show();
        }
    }



    @Override
    public void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
