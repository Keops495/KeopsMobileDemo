package com.ags.keopsandroidapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AlbumsActivity extends AppCompatActivity {

    private String user_id;
    ListView listViewAlbums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        listViewAlbums = findViewById(R.id.listViewAlbums);
        getAlbums();
    }

    public void getAlbums() {
        DownloadData downloadData = new DownloadData();

        try {
            String url = "https://keops-web1.herokuapp.com/Api/albums";
            downloadData.execute(url);
        } catch (Exception e) { }
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

        //[{"album_id":"1","name":"Dogs","album_user_id":"1","album_date":"2018-11-25"},{"album_id":"2","name":"Cats","album_user_id":"2","album_date":"2018-11-25"}]
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            final ArrayList<String> albumNames = new ArrayList<>();
            final ArrayList<String> albumIds = new ArrayList<>();
            final JSONArray jsonArr;
            try {
                //bir kullanici id'si olacak ve o kullanici id'ye gore album name'ler alinacak
                jsonArr = new JSONArray(s);
                for (int i = 0; i < jsonArr.length(); i++) {
                    if (jsonArr.getJSONObject(i).getString("album_user_id").equals(user_id)) {
                        albumNames.add(jsonArr.getJSONObject(i).getString("name"));
                        albumIds.add(jsonArr.getJSONObject(i).getString("album_id"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayAdapter arrayAdapter = new ArrayAdapter(AlbumsActivity.this, android.R.layout.simple_list_item_1, albumNames);
            listViewAlbums.setAdapter(arrayAdapter);


            listViewAlbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), ShowAlbumContentActivity.class);
                    intent.putExtra("album_id", albumIds.get(position));
                    intent.putExtra("name",albumNames.get(position));
                    startActivity(intent);
                }
            });

        }
    }


}
