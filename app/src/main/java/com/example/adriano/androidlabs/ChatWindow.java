package com.example.adriano.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    ListView chatView;
    Button sendButton;
    EditText typeMessage;
    ArrayList<String> chatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatView = (ListView) findViewById(R.id.chatView);
        sendButton = (Button) findViewById(R.id.sendButton);
        typeMessage = (EditText) findViewById(R.id.typeMessage);
        chatMessages = new ArrayList<>();

        ChatAdapter messageAdapter = new ChatAdapter(this);
        chatView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(e -> {
            chatMessages.add(typeMessage.getText().toString());
            messageAdapter.notifyDataSetChanged();
            typeMessage.setText("");
        });
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

}
