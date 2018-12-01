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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends AppCompatActivity {

    private String user_name;
    private String user_id;
    private ArrayList<String> photo_urls;
    private LinearLayout layout;

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

        createImages();
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
                        likeButton.setTextColor(Color.rgb(255, 0, 0));
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
}
