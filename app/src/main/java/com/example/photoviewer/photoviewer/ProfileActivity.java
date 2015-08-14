package com.example.photoviewer.photoviewer;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photoviewer.photoviewer.models.InstagramUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class ProfileActivity extends Activity {
    String ACCESS_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Bundle extras = getIntent().getExtras();
        ACCESS_TOKEN = extras.getString("ACCESS_TOKEN");
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
