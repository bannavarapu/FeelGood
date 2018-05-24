package com.example.anudeepthi.feelgood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

public class PostBlog extends AppCompatActivity {

    Button post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_blog);


    }

    public void Post(View view){

        post = (Button) findViewById(R.id.postBlog);
        PopupMenu popUp = new PopupMenu(PostBlog.this, post);
        popUp.getMenuInflater().inflate(R.menu.post_blog, popUp.getMenu());

        popUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(PostBlog.this, "You posted in "+ item.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

    }


}
