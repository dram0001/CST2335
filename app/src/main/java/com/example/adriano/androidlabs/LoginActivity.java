package com.example.adriano.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

    protected static final String ACTIVITY_NAME = "LoginActivity";
    private Button loginButton;
    private EditText loginName;
    private SharedPreferences.Editor edit;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.i(ACTIVITY_NAME, "In OnCreate()");

        loginButton = (Button) findViewById(R.id.loginButton);
        loginName = (EditText) findViewById(R.id.loginName);

        prefs = getSharedPreferences("email", Context.MODE_PRIVATE);
        String userEmail = prefs.getString("UserEmail","login name");
        loginName.setText(userEmail);

        loginButton.setOnClickListener(e-> {
            String input = loginName.getText().toString();
            edit = prefs.edit();
            edit.putString("UserEmail", input);
            edit.commit();

            Intent startActivity = new Intent(LoginActivity.this, StartActivity.class);
            startActivity(startActivity);
        });


    }

    @Override
    public void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In OnStart()");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In OnResume()");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In OnPause()");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In OnStop()");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In OnDestroy()");
    }
}
