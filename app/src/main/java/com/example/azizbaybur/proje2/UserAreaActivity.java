package com.example.azizbaybur.proje2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserAreaActivity extends AppCompatActivity {

    Button buttonOpenDialog;
    Button buttonUp;
    TextView textFolder;
    String KEY_TEXTPSS = "TEXTPSS";

    static final int CUSTOM_DIALOD_ID = 0;
    ListView dialog_ListView;

    File root;
    File curFolder;
    private List<String> fileList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final TextView welcomeMessage = (TextView) findViewById(R.id.tvWelcomeMsg);
        final Button bExit = (Button) findViewById(R.id.bExit);
        final Button bSend = (Button) findViewById(R.id.bSend);
        final Button bReceive = (Button) findViewById(R.id.bReceive);
        final TextView textFolder2 = (TextView) findViewById(R.id.textFolder2);
        final EditText etReceiver = (EditText) findViewById(R.id.etReceiver);
        final TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
        final TextView tvReceiveText = (TextView) findViewById(R.id.tvReceiveText);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        String message = username + "welcome to your user area.";
        welcomeMessage.setText(message);
        tvUsername.setText(username);

        buttonOpenDialog = (Button) findViewById(R.id.opendialog);

        buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(CUSTOM_DIALOD_ID);
            }
        });
        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        curFolder = root;

        bSend.setOnClickListener(new View.OnClickListener() {//text sending button triggered
            @Override
            public void onClick(View v) {
                final String username = etReceiver.getText().toString();
                final String text = textFolder2.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(UserAreaActivity.this);
                                builder.setMessage("Send Successful").show();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserAreaActivity.this);
                                builder.setMessage("Send Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
            }
        };
                SendTextRequest sendTextRequest = new SendTextRequest(username, text, responseListener);
                RequestQueue queue = Volley.newRequestQueue(UserAreaActivity.this);
                queue.add(sendTextRequest);

            }
        });
        bReceive.setOnClickListener(new View.OnClickListener() {//text sending button triggered
            @Override
            public void onClick(View v) {

                final String username = tvUsername.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Intent intent = getIntent();
                                String text = intent.getStringExtra("text");
                                tvReceiveText.setText(text);

                                AlertDialog.Builder builder = new AlertDialog.Builder(UserAreaActivity.this);
                                builder.setMessage("Receive Successful").show();

                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserAreaActivity.this);
                                builder.setMessage("Receive Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ReceiveTextRequest receiveTextRequest = new ReceiveTextRequest(username, responseListener);
                RequestQueue queue = Volley.newRequestQueue(UserAreaActivity.this);
                queue.add(receiveTextRequest);

            }
        });
        bExit.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         Intent intent = new Intent(UserAreaActivity.this, LoginActivity.class);
                                         UserAreaActivity.this.startActivity(intent);
                                     }

        });
        }

    @Override
    protected Dialog onCreateDialog(int id) {

        Dialog dialog = null;

        switch (id){
            case CUSTOM_DIALOD_ID:
                dialog = new Dialog(UserAreaActivity.this);
                dialog.setContentView(R.layout.dialoglayout);
                dialog.setTitle("Custom Dialog");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                textFolder = (TextView) dialog.findViewById(R.id.folder);
                buttonUp = (Button) dialog.findViewById(R.id.up);
                buttonUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListDir(curFolder.getParentFile());
                    }
                });
                dialog_ListView = (ListView) dialog.findViewById(R.id.dialoglist);
                dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    File selected = new File(fileList.get(position));
                        if(selected.isDirectory()){
                            ListDir(selected);
                        }
                        else {
                            Toast.makeText(UserAreaActivity.this, selected.toString()+ "selected",
                                    Toast.LENGTH_LONG).show();
                            dismissDialog(CUSTOM_DIALOD_ID);
                        }
                    }
                });
                break;
    }
        return  dialog;

    }
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id){
            case CUSTOM_DIALOD_ID:
                ListDir(curFolder);
                break;

        }
    }
    void ListDir(File f){
    if(f.equals(root)){
        buttonUp.setEnabled(false);
    }
    else {
        buttonUp.setEnabled(true);
    }
    curFolder = f;
        textFolder.setText(f.getPath());

        File[] files = f.listFiles();
        fileList.clear();
        for(File file: files){
            fileList.add(file.getPath());
        }
        ArrayAdapter<String> directoryList=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileList);

        dialog_ListView.setAdapter(directoryList);
    }
}
