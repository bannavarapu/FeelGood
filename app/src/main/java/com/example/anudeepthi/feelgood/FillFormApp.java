package com.example.anudeepthi.feelgood;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FillFormApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_form_app);

        WebView webView = (WebView) findViewById(R.id.fillFormWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webView.loadUrl("https://feelgood-69e68.appspot.com/");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:(function() { document.getElementById('username').value = '"+MainActivity.getmUserID()+"'; })()");
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        DatabaseReference checkTag = FirebaseDatabase.getInstance().getReference().child("users").child(MainActivity.mUserID).child("userTag");
        checkTag.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.getValue().toString().equals("Tag")){
                    MainActivity.facts_rv.setVisibility(View.VISIBLE);
                    MainActivity.images.setVisibility(View.VISIBLE);
                    MainActivity.feelingQuery.setVisibility(View.VISIBLE);
                    MainActivity.buttonLayout.setVisibility(View.GONE);
                    MainActivity.fillFormButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_dashboard){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
