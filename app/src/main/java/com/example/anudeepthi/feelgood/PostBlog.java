package com.example.anudeepthi.feelgood;

import android.content.Intent;
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

        post = (Button) findViewById(R.id.postBlog);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(PostBlog.this, post);
                popup.getMenuInflater()
                        .inflate(R.menu.post_blog, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();

                        if (id == R.id.MyBlog){
                            //My blog code
                        }else if(id == R.id.PublicBlog){
                            //Public blog code
                        }else if(id == R.id.cancel){
                            finish();
                        }

                        return true;
                    }
                });

                popup.show();
            }
        });



    }



}
