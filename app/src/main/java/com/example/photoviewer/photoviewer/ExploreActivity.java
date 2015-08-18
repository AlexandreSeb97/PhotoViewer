package com.example.photoviewer.photoviewer;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.photoviewer.photoviewer.adapters.InstagramMediaAdapter;
import com.example.photoviewer.photoviewer.adapters.InstagramPhotosAdapter;
import com.example.photoviewer.photoviewer.adapters.InstagramResultsAdapter;
import com.example.photoviewer.photoviewer.models.InstagramPhoto;
import com.example.photoviewer.photoviewer.models.InstagramUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ExploreActivity extends Activity {
    private ArrayList<InstagramPhoto> photos;
    private InstagramMediaAdapter aPhotos;
    private ArrayList<InstagramUser> results;
    private InstagramResultsAdapter aResults;
    String ACCESS_TOKEN;
    private GridView gvPhotos;
    private GridView gvResults;
    private EditText etQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        Bundle extras = getIntent().getExtras();
        ACCESS_TOKEN = extras.getString("ACCESS_TOKEN");
        results = new ArrayList<>();
        aResults = new InstagramResultsAdapter(this, results);
        //Send out API request to Popular Photos
        photos = new ArrayList<>();
        // Create adapter linking it to the source
        aPhotos = new InstagramMediaAdapter(this, photos);
        // find the ListView from the layout
        GridView gvPhotos = (GridView) findViewById(R.id.gvPhotos);
        GridView gvResults = (GridView) findViewById(R.id.gvResults);
        // Link the adapter to the adapter view (gridview)
        gvPhotos.setAdapter(aPhotos);
        gvResults.setAdapter(aResults);

        fetchMediaPopular();
        setupViews();
    }

    private void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvPhotos = (GridView) findViewById(R.id.gvPhotos);
        gvResults = (GridView) findViewById(R.id.gvResults);
        }

    public void fetchMediaPopular() {
        String url = "https://api.instagram.com/v1/media/popular?access_token=" + ACCESS_TOKEN;
        //Create the client
        AsyncHttpClient client = new AsyncHttpClient();
        //  Trigger the GET request :D
        client.get(url, null, new JsonHttpResponseHandler() {
            //onSuccess(worked)
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
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        photo.imageURL = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        // Height
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
                fetchMediaPopular();
            }
        });
    }

    //This method will run anytime the "search button" is clicked. This is possible thanks to the onClick attribute in activity_search.xml
    public void onImageSearch(View v) {
        //Get the string from the EditText
        if (photos != null) {
            photos.clear();
        }
        if (aResults != null) {
            aResults.clear();
        }
        // Merci Orlson! :)
        String query = etQuery.getText().toString();
        //Print the Text on the screen
        Toast.makeText(this, "Search for: " + query, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        //http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android&rsz=8
        String searchUrl = "https://api.instagram.com/v1/users/search?q=" + query + "&access_token=" + ACCESS_TOKEN + "&count=15";
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray resultsJSON = null;
                try {
                    resultsJSON = response.getJSONArray("data");
                    //iterate array of posts
                    for (int i = 0; i < resultsJSON.length(); i++) {
                        //get the JSON object at that positiion
                        JSONObject resultJSON = resultsJSON.getJSONObject(i);
//                        Toast.makeText(getApplicationContext(), i, Toast.LENGTH_SHORT).show();
                        InstagramUser user = new InstagramUser();
                        user.username = resultJSON.getString("username");
                        user.profile_picture = resultJSON.getString("profile_picture");
                        Toast.makeText(getApplicationContext(), "Found Ya!", Toast.LENGTH_SHORT).show();
                            aResults.add(user);
                        //clear the existig images in case there is a new search
                        // When you make to the adapter, it does modify the underliying data auto
                    }
                } catch (JSONException e) {
                    //TODO catch block
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_explore, menu);
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
