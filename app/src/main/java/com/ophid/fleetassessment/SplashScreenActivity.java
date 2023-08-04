package com.ophid.fleetassessment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.PointerIconCompat;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.ResultSet;

public class SplashScreenActivity extends AppCompatActivity {

    MySQLiteHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_main);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dbHelper = new MySQLiteHelper(SplashScreenActivity.this);
                dbHelper.populateLocalVehicles();
                Intent myIntent = new Intent(SplashScreenActivity.this,LoginActivity.class);
                startActivity(myIntent);
                finish();
            }
        }, 4000);




    }


}