package com.example.photoviewer.photoviewer;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class TabsActivity extends TabActivity {
    String ACCESS_TOKEN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        Bundle extras = getIntent().getExtras();
        ACCESS_TOKEN = extras.getString("request_token");
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Third Tab");

        tab2.setIndicator("", ContextCompat.getDrawable(this, R.drawable.ic_search));
        tab2.setContent(new Intent(this, ExploreActivity.class).putExtra("ACCESS_TOKEN", ACCESS_TOKEN));

        tab1.setIndicator("", ContextCompat.getDrawable(this, R.drawable.ic_home));
        tab1.setContent(new Intent(this, TimelineActivity.class).putExtra("ACCESS_TOKEN", ACCESS_TOKEN));

        tab3.setIndicator("", ContextCompat.getDrawable(this, R.drawable.ic_profile));
        tab3.setContent(new Intent(this, ProfileActivity.class).putExtra("ACCESS_TOKEN", ACCESS_TOKEN));

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabs, menu);
        return true;
    }

    public void onHaitiView(MenuItem mi){
        // Launch the profile view
        Intent i = new Intent(this, HaitiActivity.class);
        i.putExtra("ACCESS_TOKEN", ACCESS_TOKEN);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
