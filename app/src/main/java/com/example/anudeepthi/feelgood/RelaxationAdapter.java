package com.example.anudeepthi.feelgood;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RelaxationAdapter extends RecyclerView.Adapter<RelaxationAdapter.RelaxationViewHolder> implements View.OnClickListener{

    Context mContext;
    ArrayList<Relax_option_format> suggestionList;

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    public RelaxationAdapter(Context context,ArrayList<Relax_option_format> suggestionList){
        mContext = context;
        this.suggestionList=suggestionList;
    }

    @NonNull
    @Override
    public RelaxationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutid = R.layout.relax_opts_main;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        View view = inflater.inflate(layoutid, parent, false);
        RelaxationViewHolder viewHolder = new RelaxationViewHolder(view);

        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RelaxationViewHolder holder, final int position) {

        holder.title.setText(suggestionList.get(position).title);
        holder.desc.setText(suggestionList.get(position).desc);
        holder.mainImg.setImageBitmap(getBitmapFromURL(suggestionList.get(position).image));
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNumber(suggestionList.get(position).id, true);

            }
        });
        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNumber(suggestionList.get(position).id, false);

            }
        });


    }

    public void updateNumber(String id, boolean flag)
    {
        String [] details = id.split("_");
        final DatabaseReference refToUpdate = FirebaseDatabase.getInstance().getReference().child("relax_options").child(details[0]).child(details[1]);
        if(flag == true)
        {
            refToUpdate.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long prev = (Long)dataSnapshot.child("likes").getValue();
                    refToUpdate.child("likes").setValue(prev+1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
            refToUpdate.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long prev = (Long)dataSnapshot.child("dislikes").getValue();
                    refToUpdate.child("dislikes").setValue(prev+1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return suggestionList.size();
    }

    @Override
    public void onClick(View v) {

    }

    class RelaxationViewHolder extends RecyclerView.ViewHolder{

        ImageView mainImg;
        TextView title;
        TextView desc;
        ImageView like;
        ImageView dislike;

        public RelaxationViewHolder(View view){
            super(view);

            mainImg = (ImageView) view.findViewById(R.id.mainImage);
            title = (TextView) view.findViewById(R.id.Title);
            desc = (TextView) view.findViewById(R.id.description);
            like = (ImageView) view.findViewById(R.id.like);
            dislike = (ImageView) view.findViewById(R.id.dislike);



        }


    }
}
