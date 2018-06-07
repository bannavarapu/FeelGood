package com.example.anudeepthi.feelgood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class PostBlog extends AppCompatActivity {

    Button post;
    String userID = MainActivity.getmUserID();
    final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
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
                        if (id == R.id.MyBlog) {
                            addPost("private");
                            //My blog code
                        } else if (id == R.id.PublicBlog) {
                            //Public blog code
                            addPost("public");
                        } else if (id == R.id.cancel) {
                            finish();
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    protected void addPost(final String visibility) {
        EditText titleField= (EditText) findViewById(R.id.blog_title);
        final String title = titleField.getText().toString().trim();
        EditText descriptionField = (EditText) findViewById((R.id.blog_body));
        final String description = descriptionField.getText().toString().trim();
        final DatabaseReference countRef = mFirebaseDatabase.getReference().child("blog_posts_number");
        if(title.length()==0 || description.length()==0)
        {
            Toast.makeText(this, "You can't leave those fields empty", Toast.LENGTH_LONG).show();

        }
        else
        {
            countRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String post_count = dataSnapshot.getValue().toString();
                    final String post_number = Integer.parseInt(post_count)+1+"";
                    countRef.setValue(post_number);
                    if(visibility.equals("public"))
                      {
                        Blog_format blog = new Blog_format(userID,title, "blog_"+post_number, description,"public");
                        DatabaseReference postRef = mFirebaseDatabase.getReference().child("blog_posts");
                        postRef.child(post_number).setValue(blog);
                        DatabaseReference userPost = mFirebaseDatabase.getReference().child("users").child(userID).child("my_blog");
                        userPost.child(post_number).setValue(blog);

                      }
                    else if(visibility.equals("private"))
                        {
                            Blog_format blog = new Blog_format(userID,title, "blog_"+post_number, description,"private");
                            DatabaseReference userPost = mFirebaseDatabase.getReference().child("users").child(userID).child("my_blog");
                            userPost.child(post_number).setValue(blog);
                        }
                      finish();

                    }

                    @Override
                public void onCancelled(DatabaseError databaseError) {

                    }
                });

     }

    }

}
