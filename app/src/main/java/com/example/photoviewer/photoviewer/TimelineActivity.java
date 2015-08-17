package com.example.photoviewer.photoviewer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.photoviewer.photoviewer.adapters.InstagramPhotosAdapter;
import com.example.photoviewer.photoviewer.models.InstagramPhoto;
import com.example.photoviewer.photoviewer.models.InstagramUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class    TimelineActivity extends Activity {
     ///**emergency token**/ public static final String ACCESS_TOKEN = "402526745.3a73893.9c0054ab116f443d9ea0e3820e5f8e2f";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    InstagramUser user;
    String ACCESS_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Bundle extras = getIntent().getExtras();
        ACCESS_TOKEN = extras.getString("request_token");
        //Send out API request to Popular Photos
        photos = new ArrayList<>();
        // Create adapter linking it to the source
        aPhotos = new InstagramPhotosAdapter(this, photos);
        // find the ListView from the layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        //set the adapter binding it to ListView
        lvPhotos.setAdapter(aPhotos);
        // fetch the popular photos
        fetchHomeTimeline();
        fetchUserInfo();
    }
    public void fetchUserInfo() {
        String urlprof = "https://api.instagram.com/v1/users/self/?access_token=" + ACCESS_TOKEN;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlprof, null, new JsonHttpResponseHandler() {
            //onSuccess (worked)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONObject userJSON = null;
                try {
                    userJSON = response.getJSONObject("data");
                    InstagramUser user = new InstagramUser();
                    //decode the attributes of the JSON into a data model
                    user.username = userJSON.getString("username");
                    TextView tvUsername = (TextView) findViewById(R.id.tvSelfUsername);
                    tvUsername.setText("@" + user.username);
                    user.full_name = userJSON.getString("full_name");
                    TextView tvFullname = (TextView) findViewById(R.id.tvSelfFullName);
                    tvFullname.setText(user.full_name);
                    user.profile_picture = userJSON.getString("profile_picture");
                    ImageView ivProfileImage = (ImageView) findViewById(R.id.UserImage);
                    // Rounded image
                    Transformation transformation = new RoundedTransformationBuilder()
                            .borderColor(Color.WHITE)
                            .borderWidthDp(1)
                            .cornerRadiusDp(50)
                            .oval(false)
                            .build();
                    Picasso.with(getApplicationContext())
                            .load(user.profile_picture)
                            .transform(transformation)
                            .into(ivProfileImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //onFailed (failed)
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //Do something
            }
        });
    }

    //Trigger the API requesthttps://api.instagram.com/v1/users/self/feed?access_token=ACCESS-TOKEN
    public void fetchHomeTimeline() {
        /*
        CLient ID: 3a738930c86844a8b505115a3c2427c0
        -Popular Media: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
        -Self Feed : https://api.instagram.com/v1/users/self/feed?access_token=ACCESS-TOKEN
        -Recent Comment :
*/
        String url = "https://api.instagram.com/v1/users/self/feed?access_token=" + ACCESS_TOKEN;
        //Create the client
        AsyncHttpClient client = new AsyncHttpClient();
        //  Trigger the GET request :D
        client.get(url, null, new JsonHttpResponseHandler() {
            //onSucces (worked)

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Expecting a JSON object
                //-Type: { "data" => [set] => "type" } ("image or video")
                // Iterate each of the photo items and decode the item into a java object
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data"); //array of posts
                    //iterate array of posts
                    for (int i = 0; i < photosJSON.length(); i++) {
                        //get the JSON object at that positiion
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        //decode the attributes of the JSON into a data model
                        InstagramPhoto photo = new InstagramPhoto();
                        //-Author Name: { "data" => [set] => "user" => "username" }
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        //-Caption: { "data" => [set] => "caption" => "text" }
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        //-URL: { "data" => [set] => "images" => "standard_resolution" => "url" }
                        photo.imageURL = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        // Height
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        // Likes Count
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        // Comment Count
                        photo.commentsCount = photoJSON.getJSONObject("comments").getInt("count");
                        // Profile Picture
                        photo.profilePicture = photoJSON.getJSONObject("user").getString("profile_picture");
                        //Add decoded objects to the photos Array
                        photos.clear();
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //callback
                aPhotos.notifyDataSetChanged();
            }

            //onFailed (failed)

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //Do something
            }
        });
    }
    public void onProfileView(MenuItem mi){
        // Launch the profile view
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("ACCESS_TOKEN", ACCESS_TOKEN);
        startActivity(i);
    }

    public void onExploreView(MenuItem mi){
        // Launch the profile view
        Intent i = new Intent(this, ExploreActivity.class);
        i.putExtra("ACCESS_TOKEN", ACCESS_TOKEN);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}