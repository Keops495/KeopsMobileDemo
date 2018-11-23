package com.ags.keopsandroidapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AuthenticationScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_screen);
    }

    public void SignIn(View view) {
        Intent loginIntent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(loginIntent);
    }

}
