package com.example.anudeepthi.feelgood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Blog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.public_blog:
                Intent p_intent = new Intent(this, PublicBlog.class);
                startActivity(p_intent);
                return true;

            case R.id.postIt:
                Intent m_intent = new Intent(this, PostBlog.class);
                startActivity(m_intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
