package com.company.formationadvisor.activites;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;

import com.company.formationadvisor.R;

public class MainActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void inscription(View view) {
        intent = new Intent(this, Inscription.class);
        startActivity(intent);
    }

    public void connexion(View view) {
        intent = new Intent(this, Connexion.class);
        startActivity(intent);
    }
}
