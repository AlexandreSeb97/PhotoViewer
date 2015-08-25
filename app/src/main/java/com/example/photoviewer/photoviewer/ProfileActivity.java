package com.example.photoviewer.photoviewer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photoviewer.photoviewer.adapters.InstagramProfileAdapter;
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


public class ProfileActivity extends Activity {
    String ACCESS_TOKEN;
    private ArrayList<InstagramPhoto> photos;
    private InstagramProfileAdapter aProfiles;
    private int resultOffset = 0;
    GridView gvProfileView;
    int USER_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Bundle extras = getIntent().getExtras();
        ACCESS_TOKEN = extras.getString("ACCESS_TOKEN");
        USER_ID = extras.getInt("user_id");
        photos = new ArrayList<>();
        aProfiles = new InstagramProfileAdapter(this, photos);
        gvProfileView = (GridView) findViewById(R.id.gvProfileView);
        gvProfileView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Launch the full image display activity
                // Creating an intent
                Intent i = new Intent(ProfileActivity.this, FullViewActivity.class);
                // get the image result to display
                InstagramPhoto photo = photos.get(position);
                // Pass image result into the intent
                i.putExtra("url", photo.imageURL);
                // Launch the  new activity
                startActivity(i);
            }
        });
        gvProfileView.setAdapter(aProfiles);
        if (USER_ID != 0){
            fetchUserInfoID();
            profUserSetup();
        } else {
            fetchUserInfo();
            profSetup();
        }
    }

    private void profSetup(){
        if (photos != null) {
            photos.clear();
        }
        String urlprof2 = "https://api.instagram.com/v1/users/self/media/recent/?access_token=" + ACCESS_TOKEN;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlprof2, null, new JsonHttpResponseHandler() {

            //onSuccess (worked)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray profilesJSON = null;
                try {
                    profilesJSON = response.getJSONArray("data"); //array of posts
                    //iterate array of posts
                    for (int i = 0; i < profilesJSON.length(); i++) {
                        //get the JSON object at that positiion
                        JSONObject profileJSON = profilesJSON.getJSONObject(i);
                        //decode the attributes of the JSON into a data model
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = profileJSON.getJSONObject("user").getString("username");
                        photo.imageURL = profileJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        // Height
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                aProfiles.notifyDataSetChanged();
            }
        });
    }

    private void profUserSetup(){
        if (photos != null) {
            photos.clear();
        }
        String urlprof2 = "https://api.instagram.com/v1/users/" + USER_ID +"/media/recent/?access_token=" + ACCESS_TOKEN;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlprof2, null, new JsonHttpResponseHandler() {

            //onSuccess (worked)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray profilesJSON = null;
                try {
                    profilesJSON = response.getJSONArray("data"); //array of posts
                    //iterate array of posts
                    for (int i = 0; i < profilesJSON.length(); i++) {
                        //get the JSON object at that positiion
                        JSONObject profileJSON = profilesJSON.getJSONObject(i);
                        //decode the attributes of the JSON into a data model
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = profileJSON.getJSONObject("user").getString("username");
                        photo.imageURL = profileJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        // Height
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                aProfiles.notifyDataSetChanged();
            }
        });
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
                    TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
                    tvUsername.setText("@" + user.username);
                    user.bio = userJSON.getString("bio");
                    TextView tvBio = (TextView) findViewById(R.id.tvBio);
                    tvBio.setText(user.bio);
                    user.full_name = userJSON.getString("full_name");
                    TextView tvFullname = (TextView) findViewById(R.id.tvFullName);
                    tvFullname.setText(user.full_name);
                    user.profile_picture = userJSON.getString("profile_picture");
                    ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfilePicture);
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

    public void fetchUserInfoID() {
        String urlprof = "https://api.instagram.com/v1/users/" + USER_ID + "/?access_token=" + ACCESS_TOKEN;
        Toast.makeText(this, urlprof, Toast.LENGTH_SHORT).show();
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
                    TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
                    tvUsername.setText("@" + user.username);
                    user.bio = userJSON.getString("bio");
                    TextView tvBio = (TextView) findViewById(R.id.tvBio);
                    tvBio.setText(user.bio);
                    user.full_name = userJSON.getString("full_name");
                    TextView tvFullname = (TextView) findViewById(R.id.tvFullName);
                    tvFullname.setText(user.full_name);
                    user.profile_picture = userJSON.getString("profile_picture");
                    ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfilePicture);
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
    private void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter

        resultOffset = resultOffset + 8;

        if (resultOffset == 64) {
            return;
        }
        // only load more if not at the end
        //getData();
        profSetup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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

