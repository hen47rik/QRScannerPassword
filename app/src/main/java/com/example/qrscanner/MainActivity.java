package com.example.qrscanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.security.SecureRandom;

// implements onClickListener for the onclick behaviour of button
public class MainActivity extends AppCompatActivity  {
    private static final String CHANNEL_ID = "defaultChannel";
    private static final String CHANNEL_NAME = "Default Channel";
    private static final double OTP_LENGTH = 8;
    Button qrButton, infoButton;

    TextView numberCombination, atmName, user, password, history;

    PasswordGenerator passwordGenerator = new PasswordGenerator();
    private NotificationManager notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qrButton = findViewById(R.id.notify);
        infoButton = findViewById(R.id.infobutton);
        qrButton.setBackgroundColor(Color.parseColor("#9B0E21"));
        infoButton.setBackgroundColor(Color.parseColor("#003F69"));
        numberCombination = findViewById(R.id.numberCombination);
        atmName = findViewById(R.id.atmName);
        user = findViewById(R.id.user);
        password = findViewById(R.id.password);
        history = findViewById(R.id.history);
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("Scan a QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.initiateScan();
            }
        });
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });
        this.notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

    }

    private void sendNotification(String password) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle("Password Generated")
                .setContentText(password)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(0, builder.build());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                String[] arrOfstr = intentResult.getContents().split(",");
                String numbers = arrOfstr[0];
                String atm = arrOfstr[1];
                String username = arrOfstr[2];
                numberCombination.setText(numbers);
                atmName.setText(atm);
                user.setText(username);
                String createdpassword = String.valueOf(passwordGenerator.generatePassword(numbers));
                password.setText(createdpassword);
                passwordGenerator.generatePassword(intentResult.getContents());
                history.append(intentResult.getContents());
                history.append(": " + createdpassword);
                history.append("\n");
                sendNotification(createdpassword);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}



