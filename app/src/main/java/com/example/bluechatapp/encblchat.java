package com.example.bluechatapp;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.SecureRandom;
import android.util.Base64;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

public class enc extends AppCompatActivity {
    EditText enc,dec;
    byte[] iv = new byte[12];
    byte[] key = new byte[16];
    String plain1,key1,iv1;
    Button decr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enc);
        //AES-GCM
        Bundle intent = getIntent().getExtras();
        plain1 = (String) intent.getString("sms");
        plain1.replaceAll(" ",",");
        enc = (EditText) findViewById(R.id.enc);
        Button decr = (Button) findViewById(R.id.decr);
        byte[] plainText = plain1.getBytes();
        String cipher1 = null;
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        //NEVER REUSE THIS IV WITH SAME KEY
        secureRandom.nextBytes(iv);
        final Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv); //128 bit auth tag length
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            byte[] cipherText = cipher.doFinal(plainText);
            ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + cipherText.length);
            byteBuffer.putInt(iv.length);
            byteBuffer.put(iv);
            byteBuffer.put(cipherText);
            byte[] cipherMessage = byteBuffer.array();
            cipher1 = cipherMessage.toString();
            enc.setText(cipher1);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        decr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c1 = enc.getText().toString();
                byte[] cipherText = c1.getBytes();
                byte[] cipherMessage = Base64.decode(cipherText, Base64.DEFAULT);
                Cipher cipher = null;
                try {
                    cipher = Cipher.getInstance("AES/GCM/NoPadding");
                    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, iv));
                    byte[] plainText= cipher.doFinal(cipherText);
                    String plain2 = plainText.toString();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
                dec = (EditText) findViewById(R.id.dec);
                dec.setText(plain1);
            }

        });
    }
}
