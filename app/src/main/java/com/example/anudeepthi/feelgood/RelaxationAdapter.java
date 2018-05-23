package com.example.anudeepthi.feelgood;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RelaxationAdapter extends RecyclerView.Adapter<RelaxationAdapter.RelaxationViewHolder>{

    Context mContext;
    ArrayList<String> factList;

    public RelaxationAdapter(Context context,ArrayList<String> factList){
        mContext = context;
        this.factList=factList;
    }

    @NonNull
    @Override
    public RelaxationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutid = R.layout.relax_opts_main;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutid, parent, false);
        RelaxationViewHolder viewHolder = new RelaxationViewHolder(view);

        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RelaxationViewHolder holder, int position) {

        holder.title.setText("title");
        holder.desc.setText(factList.get(position));

    }

    @Override
    public int getItemCount() {
        return factList.size();
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
