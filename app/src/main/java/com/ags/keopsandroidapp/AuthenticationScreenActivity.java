package com.ags.keopsandroidapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AuthenticationScreenActivity extends AppCompatActivity {

    private String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_screen);

        Button signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RetrieveFeedTask().execute();
            }
        });

        Button signUpNavigationButton = (Button) findViewById(R.id.signUpNavigationButton);
        signUpNavigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });

    }

    public void SignIn() {
        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
        loginIntent.putExtra("userId",user_id);
        startActivity(loginIntent);
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        static final String API_URL = "https://keops-web1.herokuapp.com/Api/users";
        EditText userName = (EditText) findViewById(R.id.userName);
        EditText password = (EditText) findViewById(R.id.password);

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here

            try {
                URL url = new URL(API_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", response);

            try {

                System.out.println(response);
                response = response.substring(1, response.length());

                while (true) {

                    JSONObject object = new JSONObject(response);
                    String user_name = object.getString("user_name");
                    String pass_word = object.getString("password");
                    user_id = object.getString("user_id");

                    if (userName.getText().toString().equals(user_name) && password.getText().toString().equals(pass_word)) {
                        System.out.println("Kullanıcı eslesti ");
                        SignIn();
                        break;
                    } else
                        System.out.println("Kullanıcı bulunamadı ");

                    if (response.contains(",{")) {
                        response = response.substring(response.indexOf(",{") + 1);
                    } else
                        break;
                }
            } catch (JSONException e) {
                // Appropriate error handling code
                System.out.println(e.getMessage());
            }
        }

    }

}
