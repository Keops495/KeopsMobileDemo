package com.ags.keopsandroidapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String user_name;
    private String user_id;
    TextView textView;

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
        user_id = intent.getStringExtra("user_id");
        
    }

    public void showAlbum(View view) {
        Intent albumsIntent = new Intent(getApplicationContext(), AlbumsActivity.class);
        albumsIntent.putExtra("user_id",user_id);
        startActivity(albumsIntent);
    }

    public void sharedWithMe(View view) {

    }


}
