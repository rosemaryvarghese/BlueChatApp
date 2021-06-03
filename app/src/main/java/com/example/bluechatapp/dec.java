package com.example.bluechatapp;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

public class dec extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dec);
        ListView lv = (ListView) findViewById(R.id.messageList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,fetchdec());
        lv.setAdapter(adapter);
        if (ContextCompat.checkSelfPermission(dec.this,
                Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(dec.this, Manifest.permission.READ_SMS)) {
                ActivityCompat.requestPermissions(dec.this, new String[]{Manifest.permission.READ_SMS}, 1);
            } else {
                ActivityCompat.requestPermissions(dec.this, new String[]{Manifest.permission.READ_SMS}, 1);
            }
        }

    }
    public  ArrayList<String> fetchdec(){
        ArrayList<String> sms = new ArrayList<String>();

        Uri uri = Uri.parse("content://sms/dec");
        Cursor cursor = getContentResolver().query(uri,new String[]{"address","body"},null,null,null);
        if(cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                String address = cursor.getString(0);
                String body = cursor.getString(1);
                sms.add(address + ":" + body);
            }
        }
        return sms;
    }
}