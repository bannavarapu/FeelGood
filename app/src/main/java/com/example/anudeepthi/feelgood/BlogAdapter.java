package com.example.anudeepthi.feelgood;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    Context context;
    ArrayList<Blog_format> array;
    boolean flag;
    BlogAdapter(Context context, ArrayList<Blog_format> array, boolean flag){
        this.context = context;
        this.array = array;
        this.flag = flag;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutid = R.layout.blog_rv;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutid, parent, false);
        BlogViewHolder viewHolder = new BlogViewHolder(view);

        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BlogViewHolder holder, final int position) {
        holder.title.setText(array.get(position).title);
        holder.description.setText(array.get(position).description);
        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference toaddfav = FirebaseDatabase.getInstance().getReference().child("users").child(MainActivity.getmUserID()).child("fav_blog");
                String postID=array.get(position).postID;
                toaddfav.child(postID.split("_")[1]).setValue(array.get(position));
                Toast.makeText(context,"Well, you have a nice taste.....",Toast.LENGTH_LONG).show();
            }
        });
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(array.get(position).userId).child("userName");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.username.setText(dataSnapshot.getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(flag==true)
        {
            holder.heart.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    class BlogViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView username;
        TextView description;
        ImageView heart;
        public BlogViewHolder(View view){
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            username = (TextView) view.findViewById(R.id.username);
            description = (TextView) view.findViewById(R.id.description);
            heart=(ImageView) view.findViewById(R.id.heart);
        }
    }
}
