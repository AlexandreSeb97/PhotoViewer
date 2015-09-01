package com.example.photoviewer.photoviewer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
    public String request_token;
    RelativeLayout relativeLayout;
    int images[] = {R.drawable.img, R.drawable.img2, R.drawable.img4, R.drawable.img5,  R.drawable.img6,  R.drawable.img7};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        relativeLayout = (RelativeLayout) findViewById(R.id.mylayout);

    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                while(true) {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            relativeLayout.setBackgroundResource(R.drawable.img);

                        }
                    });
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            relativeLayout.setBackgroundResource(R.drawable.img2);

                        }
                    });
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            relativeLayout.setBackgroundResource(R.drawable.img4);

                        }
                    });
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            relativeLayout.setBackgroundResource(R.drawable.img5);

                        }
                    });
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            relativeLayout.setBackgroundResource(R.drawable.img6);

                        }
                    });
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            relativeLayout.setBackgroundResource(R.drawable.img7);

                        }
                    });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            relativeLayout.setBackgroundResource(R.drawable.img3);

                        }
                    });

                }
            }
        };
        new Thread(runnable).start();
    }

    public void AuthIG() {
        String url = "https://api.instagram.com/oauth/authorize/?client_id=29ee23ba2c8441fa821455660f3c94a3&redirect_uri=http://codepath.com&response_type=token";
        WebView webView = (WebView) findViewById(R.id.webView);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        webView.setVisibility(View.VISIBLE);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new AuthWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        //progressBar.setVisibility(View.INVISIBLE);
    }

    public void goToSo (View view) {
        goToUrl ( "http://9gag.com/");
    }

    public void goToSu (View view) {
        AuthIG();
    }


    public class AuthWebViewClient extends WebViewClient
    {
        //@Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            if (url.startsWith("http://codepath.com"))
            {

                //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
                String parts[] = url.split("=");
                request_token = parts[1];  //This is your request token.
                //InstagramLoginDialog.this.dismiss();
                launchTimeline();
                return true;
            }
            return false;
        }
    }

    public void launchTimeline() {
        Intent i = new Intent(this, TabsActivity.class);
        //Toast.makeText(getApplicationContext(), request_token, Toast.LENGTH_SHORT).show();
        i.putExtra("request_token", request_token);
        startActivity(i);
    }

    //access token: 402526745.3a73893.9c0054ab116f443d9ea0e3820e5f8e2f

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

}
