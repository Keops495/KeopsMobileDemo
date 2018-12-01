package com.ags.keopsandroidapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends AppCompatActivity {

    private String user_name;
    private String user_id;
    private ArrayList<String> photo_urls, photo_ids;
    private LinearLayout layout;
    private String searchText;
    private int count = 1;

    //Menu olusturuldu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_albums, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu item'nin secilmesi kontrolu
        if (item.getItemId() == R.id.add_albums) {
            //MainActivit2'nin calismasi saglaniyor
            Intent intent = new Intent(getApplicationContext(), AddPhotoActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_homepage);

        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        Toast.makeText(this, "Hoşgeldin: " + user_name,
                Toast.LENGTH_SHORT).show();

        user_id = intent.getStringExtra("user_id");
        photo_urls = intent.getStringArrayListExtra("photo_urls");
        photo_ids = intent.getStringArrayListExtra("photo_ids");

        createImages();

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    GetText();
                } catch (Exception e) {
                    System.out.print("e.message: " + e.getMessage());
                }
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void createImages() {

        layout = findViewById(R.id.imageLayout);
        for (String url : photo_urls) {
            ImageView image = new ImageView(this);
            image.setMinimumHeight(500);
            image.setMaxHeight(500);
            image.setBackgroundColor(Color.rgb(106, 208, 240));

            image.setLeft(0);
            // Adds the view to the layout
            new DownloadImageTask(image).execute(url);

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new android.view.ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            linearLayout.setPadding(160, 0, 0, 0);

            final Button likeButton = new Button(this);
            likeButton.setLayoutParams(new android.view.ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            likeButton.setBackgroundColor(Color.rgb(255, 255, 255));

            likeButton.setText("like");
            linearLayout.addView(likeButton);


            likeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {
                        if(count%2==0){
                            likeButton.setTextColor(Color.rgb(255, 0, 0));
                            count=1;
                        }
                        else
                        {
                            likeButton.setTextColor(Color.rgb(0, 0, 0));
                            count = count*2;
                        }


                    } catch (Exception ex) {
                        System.out.print(ex.getMessage());
                    }
                }
            });

            layout.addView(image);
            layout.addView(linearLayout);
        }
    }

    public void showAlbum(View view) {
        Intent albumsIntent = new Intent(getApplicationContext(), AlbumsActivity.class);
        albumsIntent.putExtra("user_id", user_id);
        startActivity(albumsIntent);
    }

    public void sharedWithMe(View view) {

    }

    public void GetText() throws UnsupportedEncodingException {
        EditText searchEditText = findViewById(R.id.searchText);

        for (String photo_id : photo_ids) {
            final String data = "{" + "\"photo_id\" : " + "\"" + photo_id + "\"" + "}";

            String respond = "";
            BufferedReader reader = null;

            // Send data
            try {

                // Defined URL  where to send data
                URL url = new URL("https://keops-web1.herokuapp.com/Api/get_photo");

                System.out.println("data " + data);

                // Send POST data request

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
                    sb.append(line + "\n");
                }
                reader.close();

                respond = sb.toString();

                System.out.print("respond: " + respond);


            } catch (Exception ex) {
                System.out.print(ex.getMessage());
            } finally {
                try {
                    reader.close();
                } catch (Exception ex) {
                }
            }

        }

    }

}
