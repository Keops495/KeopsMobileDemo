package com.ags.keopsandroidapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SignUpActivity extends AppCompatActivity {

    private Button signUpButton;
    private TextView content;
    private EditText nameText, surnameText, usernameText, passwordText;
    private String name, surname, user_name, password;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        content = (TextView) findViewById(R.id.content);
        nameText = findViewById(R.id.nameText);
        surnameText = findViewById(R.id.surnameText);
        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);

        signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // CALL GetText method to make post method call
                    GetText();
                } catch (Exception ex) {
                    content.setText(" url exeption! ");
                    System.out.print(content.getText().toString());
                }
            }
        });
    }

    // Create GetText Metod
    public void GetText() throws UnsupportedEncodingException {
        // Get user defined values
        name = nameText.getText().toString();
        surname = surnameText.getText().toString();
        user_name = usernameText.getText().toString();
        password = passwordText.getText().toString();

        // Create data variable for sent values to server

        String data = URLEncoder.encode("name", "UTF-8")
                + "=" + URLEncoder.encode(name, "UTF-8");

        data += "&" + URLEncoder.encode("surname", "UTF-8") + "="
                + URLEncoder.encode(surname, "UTF-8");

        data += "&" + URLEncoder.encode("user_name", "UTF-8")
                + "=" + URLEncoder.encode(user_name, "UTF-8");

        data += "&" + URLEncoder.encode("password", "UTF-8")
                + "=" + URLEncoder.encode(password, "UTF-8");

        String text = "";
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("https://keops-web1.herokuapp.com/Api/signup");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();
        } catch (Exception ex) {

        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
            }
        }

        // Show response on activity
        content.setText(text);
    }

}
