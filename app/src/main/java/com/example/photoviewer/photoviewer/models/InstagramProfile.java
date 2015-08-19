package com.example.photoviewer.photoviewer.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Isaac on 8/17/2015.
 */
public class InstagramProfile {
    public String fullUrl;
    public String thumbUrl;
    public String title;

    // new ImageResult(.. raw item json...)
    public InstagramProfile(JSONObject json){
        try{
            this.fullUrl = json.getString("url");
            this.thumbUrl = json.getString("tbUrl");
            this.title = json.getString("title");

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    // Take an array of json images and return arraylist of image results
    // ImgResult.fromJSONArray([...,...])
    public  static ArrayList<InstagramProfile> fromJSONArray(JSONArray array){
        ArrayList<InstagramProfile> results = new ArrayList<InstagramProfile>();
        for (int i= 0; i < array.length(); i++){
            try{
                results.add(new InstagramProfile(array.getJSONObject(i)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return  results;
    }
}
