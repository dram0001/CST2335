package com.example.adriano.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    private ListView chatView;
    private Button sendButton;
    private EditText typeMessage;
    private ArrayList<String> chatMessages;
    private ChatAdapter messageAdapter;
    private SQLiteDatabase db;
    private final String ACTIVITY_NAME = "ChatWindow";
    private ChatDatabaseHelper tempCDH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatView = (ListView) findViewById(R.id.chatView);
        sendButton = (Button) findViewById(R.id.sendButton);
        typeMessage = (EditText) findViewById(R.id.typeMessage);
        chatMessages = new ArrayList<>();

        messageAdapter = new ChatAdapter(this);
        chatView.setAdapter(messageAdapter);

        tempCDH = new ChatDatabaseHelper(getApplicationContext());
        db = tempCDH.getWritableDatabase();
        Cursor cursor = db.query(false, ChatDatabaseHelper.TABLE_NAME, new String[]{ChatDatabaseHelper.KEY_MESSAGE}, null, null, null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {
            String message = cursor.getString( cursor.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE));
            chatMessages.add(message);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + message);
            cursor.moveToNext();
        }

        Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getColumnCount());

        for(int i = 0; i < cursor.getColumnCount(); i++)
        {
            Log.i(ACTIVITY_NAME, cursor.getColumnName(i));
        }

        sendButton.setOnClickListener(this::sendButton);

    }

    private void sendButton(View v){
        String newMessage = typeMessage.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(ChatDatabaseHelper.KEY_MESSAGE, newMessage);
        db.insert(ChatDatabaseHelper.TABLE_NAME, "NullColumnName", cv);

        chatMessages.add(newMessage);
        messageAdapter.notifyDataSetChanged();
        typeMessage.setText("");
    }

    private class ChatAdapter extends ArrayAdapter<String>{

        public ChatAdapter(Context ctx){super(ctx, 0);}
        public int getCount(){return chatMessages.size();}
        public String getItem(int position){return chatMessages.get(position);}
        public long getItemId(int position){return position;}

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;

            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView) result.findViewById(R.id.messageText);
            message.setText(getItem(position));

            return result;
        }
    }



    @Override
    public void onDestroy(){
      super.onDestroy();
      tempCDH.close();
    }

}
