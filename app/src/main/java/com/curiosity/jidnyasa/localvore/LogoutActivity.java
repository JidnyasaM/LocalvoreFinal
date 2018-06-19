package com.curiosity.jidnyasa.localvore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class LogoutActivity extends AppCompatActivity {

    public static final int LOGIN_AGAIN=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Button
        final Button loginAgain = findViewById(R.id.loginAgain);
        loginAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(LogoutActivity.this,WelcomePage.class);
                startActivityForResult(login,LOGIN_AGAIN);
            }
        });

    }

}
