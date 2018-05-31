package com.example.anudeepthi.feelgood;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RelaxationAdapter extends RecyclerView.Adapter<RelaxationAdapter.RelaxationViewHolder> implements View.OnClickListener{

    Context mContext;
    ArrayList<Relax_option_format> suggestionList;
    static String suggClick;
//    private StorageReference mStorageRef;




    public RelaxationAdapter(Context context,ArrayList<Relax_option_format> suggestionList){
        mContext = context;
        this.suggestionList=suggestionList;
    }

    @NonNull
    @Override
    public RelaxationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutid = R.layout.relax_opts_rv;
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
        final String imgUrl = suggestionList.get(position).image;
        Glide.with(mContext).load(imgUrl).thumbnail(0.5f).into(holder.mainImg);
        holder.mainImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,RelaxOptsContent.class);
                suggClick = "suggClick";
                intent.putExtra(suggClick,suggestionList.get(position).id+";"+imgUrl);
                mContext.startActivity(intent);
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"You liked this post!",Toast.LENGTH_LONG).show();
                updateNumber(suggestionList.get(position).id, true);

            }
        });
        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"hm.. we'll try getting better ones!",Toast.LENGTH_LONG).show();
                updateNumber(suggestionList.get(position).id, false);

            }
        });


    }

    public void updateNumber(String id, boolean flag)
    {
        String [] details = id.split("_");
        final DatabaseReference refToUpdate = FirebaseDatabase.getInstance().getReference().child("dashboard").child(details[0]).child(details[1]);
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
