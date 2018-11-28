package com.ags.keopsandroidapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ShowAlbumContentActivity extends AppCompatActivity {

    ListView listView;
    PostClass adaptor;
    /*
    ArrayList<String> tagsFromDB;
    ArrayList<String> likesFromDB;
    ArrayList<String> photosFromDB;
    */
    String albumName;
    String albumId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_album_content);

        Intent intent = getIntent();
        albumName = intent.getStringExtra("name");
        albumId = intent.getStringExtra("album_id");
        listView = findViewById(R.id.listViewPhotoTagLike);

        getPhotos();

        /*
        listView = findViewById(R.id.fotograf_liste);

        tagsFromDB = new ArrayList<String>();
        likesFromDB = new ArrayList<String>();
        photosFromDB = new ArrayList<String>();

        adaptor = new PostClass(tagsFromDB, likesFromDB, photosFromDB, this);
        listView.setAdapter(adaptor);
        */
    }

    public void getPhotos() {
        DownloadData downloadData = new DownloadData();

        try {
            String url = "https://keops-web1.herokuapp.com/Api/all";
            downloadData.execute(url);
        } catch (Exception e) {

        }
    }


    private class DownloadData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;
            try {

                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data > 0) {
                    char character = (char) data;
                    result += character;

                    data = inputStreamReader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        //{"photo_id":"20","photo_url":"https:\/\/vignette.wikia.nocookie.net\/southpark\/images\/9\/95\/Kyle-broflovski.png\/revision\/latest?cb=20170725131924","photo_date":"2018-11-24","photo_album_id":"1"}
        //[{"photo_id":"20","photo_url":"http:\/\/www.petmd.com\/sites\/default\/files\/small-dog-breeds.jpg","photo_date":"2018-11-24","photo_album_id":"1"},
        //{"photo_id":"22","photo_url":"http:\/\/www3.canisius.edu\/~grandem\/butterflylifecycle\/Butterfly.jpg","photo_date":"2018-11-24","photo_album_id":"1"},
        //{"photo_id":"23","photo_url":"https:\/\/images.unsplash.com\/photo-1518791841217-8f162f1e1131?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=2d7b1bd980752bb3ea0a259f528eae78&w=1000&q=80","photo_date":"2018-11-25","photo_album_id":"2"}]
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            final ArrayList<String> tagsFromDB = new ArrayList<String>();
            final ArrayList<String> likesFromDB = new ArrayList<String>();
            final ArrayList<String> photosFromDB = new ArrayList<String>();
            final JSONArray jsonArr;
            try {

                jsonArr = new JSONArray(s);
                for (int i = 0; i < jsonArr.length(); i++) {
                    if (jsonArr.getJSONObject(i).getString("photo_album_id").equals(albumId)) {
                        photosFromDB.add(jsonArr.getJSONObject(i).getString("photo_url"));
                        tagsFromDB.add("api yazilmadigi icin serverdan data gelmiyor!");
                        likesFromDB.add("api yazilmadigi icin serverdan data gelmiyor!");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adaptor = new PostClass(tagsFromDB, likesFromDB, photosFromDB, ShowAlbumContentActivity.this);
            listView.setAdapter(adaptor);

        }
    }

}
