package com.ags.keopsandroidapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AddPhotoActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addphoto);

        //Fotografın galeriden istendigi kisim
        Button btnSelectImage = (Button) findViewById(R.id.button);
        btnSelectImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        });
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);

            ImageView im = (ImageView) findViewById(R.id.imageView);
            im.setImageBitmap(bitmap);




            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
            byte[] byte_arr = stream.toByteArray();
            String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);


            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("image", image_str));


            JSONObject jsonString = new JSONObject();
            try {
                jsonString.put("img", image_str);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            new uploadImageToPhp().execute(jsonString);


        }


    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    public class uploadImageToPhp extends AsyncTask<JSONObject, Void, Void> {
        String dataToSend = null;

        public static final String prefix = "http://";                                                        //prefix of the urls
        public static final String server_ip = "172.16.26.155";                                                   //the ip address where the php server is located

        public static final String completeServerAddress = prefix + server_ip + "/test_upload/upload_image.php";                  //Exact location of the php files

        @Override
        protected Void doInBackground(JSONObject... params) {

            dataToSend = "image=" + params[0];
            communicator(completeServerAddress, dataToSend);

            return null;
        }

        public void communicator(String urlString, String dataToSend2) {
            String result = null;

            try {
                URL url = new URL(urlString);
                URLConnection conn = url.openConnection();

                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setRequestProperty("Accept", "application/json");
                httpConn.setRequestProperty("accept-charset", "UTF-8");
                httpConn.setRequestMethod("POST");
                httpConn.connect();

                //Create an output stream to send data to the server
                OutputStreamWriter out = new OutputStreamWriter(httpConn.getOutputStream());
                out.write(dataToSend2);
                out.flush();

                int httpStatus = httpConn.getResponseCode();
                System.out.println("Http status :" + httpStatus);

                if (httpStatus == HttpURLConnection.HTTP_OK) {
                    Log.d("HTTP STatus", "http connection successful");

                    BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println(inputLine);
                        sb.append(inputLine + "\n");
                    }
                    in.close();
                    result = sb.toString();

                    try {

                        //jsonResult = new JSONObject(result);
                    } catch (Exception e) {
                        Log.e("JSON Parser", "Error parsing data " + e.toString());
                    }


                } else {
                    System.out.println("Somthing went wrong");
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }


    }
}
