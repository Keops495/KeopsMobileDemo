package com.ags.keopsandroidapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_photo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu item'nin secilmesi kontrolu
        if (item.getItemId() == R.id.add_photo) {
            Intent intent = new Intent(getApplicationContext(), AddPhotoActivity.class);
            intent.putExtra("album_id", albumId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

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

        //[{"photo_id":"20","photo_url":"https:\/\/iysr.tmgrup.com.tr\/2010\/01\/21\/555510626250.jpg","photo_date":"2018-11-24","photo_album_id":"1","likes":[{"like_id":"9","like_user_id":"1","like_photo_id":"20","name":"yalim","surname":"bilgin"}],"key":{"thekey":"#daDog1"}},
        // {"photo_id":"23","photo_url":"https:\/\/static.independent.co.uk\/s3fs-public\/thumbnails\/image\/2017\/02\/22\/09\/paris.jpg?w968h681","photo_date":"2018-11-25","photo_album_id":"2","likes":[{"like_id":"7","like_user_id":"1","like_photo_id":"23","name":"yalim","surname":"bilgin"}],"key":{"thekey":"#key"}},
        // {"photo_id":"24","photo_url":"https:\/\/upload.wikimedia.org\/wikipedia\/commons\/thumb\/e\/ee\/Sagrada_Familia_01.jpg\/250px-Sagrada_Familia_01.jpg","photo_date":"2018-11-28","photo_album_id":"4","likes":[{"like_id":"8","like_user_id":"1","like_photo_id":"24","name":"yalim","surname":"bilgin"}],"key":{"thekey":"#key"}},
        // {"photo_id":"27","photo_url":"https:\/\/encrypted-tbn0.gstatic.com\/images?q=tbn:ANd9GcQ-9SxuP7GTqQT8Wj0Yj_ilFTJN_gpKpZzAQHcY5GINKWsQfXClCw","photo_date":"2018-11-28","photo_album_id":"1","likes":[{"like_id":"10","like_user_id":"1","like_photo_id":"27","name":"yalim","surname":"bilgin"}],"key":{"thekey":"#key"}}]
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            final ArrayList<String> tagsFromDB = new ArrayList<String>();
            final ArrayList<String> likesFromDB = new ArrayList<>();
            final ArrayList<String> photosFromDB = new ArrayList<String>();
            final JSONArray jsonArr;
            try {

                jsonArr = new JSONArray(s);
                for (int i = 0; i < jsonArr.length(); i++) {
                    if (jsonArr.getJSONObject(i).getString("photo_album_id").equals(albumId)) {
                        photosFromDB.add(jsonArr.getJSONObject(i).getString("photo_url"));

                        if (!(jsonArr.getJSONObject(i).isNull("key"))) {
                            tagsFromDB.add(jsonArr.getJSONObject(i).getJSONObject("key").getString("thekey"));
                        } else {
                            tagsFromDB.add("");
                        }

                        if (jsonArr.getJSONObject(i).optJSONArray("likes") != null) {
                            if (jsonArr.getJSONObject(i).getJSONArray("likes").length() != 0) {
                                likesFromDB.add(jsonArr.getJSONObject(i).getJSONArray("likes").getJSONObject(0).getString("name") + jsonArr.getJSONObject(i).getJSONArray("likes").getJSONObject(0).getString("surname"));
                            } else {
                                likesFromDB.add("");
                            }
                        }
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
