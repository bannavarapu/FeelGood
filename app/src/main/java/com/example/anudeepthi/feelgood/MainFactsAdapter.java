package com.example.anudeepthi.feelgood;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.ArrayList;


public class MainFactsAdapter extends RecyclerView.Adapter<MainFactsAdapter.MainFactsViewHolder> {


    Context mContext;
    ArrayList<main_fact> mfactstodisplay;

    public MainFactsAdapter(Context context, ArrayList<main_fact> factstodisplay){
        mContext = context;
        mfactstodisplay = factstodisplay;
    }

    @NonNull
    @Override
    public MainFactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutid = R.layout.main_facts_rv;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutid, parent, false);
        MainFactsViewHolder viewHolder = new MainFactsViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainFactsViewHolder holder, int position) {
        holder.title.setText(mfactstodisplay.get(position).title);
        holder.desc.setText(mfactstodisplay.get(position).desc);

    }

    @Override
    public int getItemCount() {
       return mfactstodisplay.size();
    }

    class MainFactsViewHolder extends RecyclerView.ViewHolder{


        TextView title;
        TextView desc;


        public MainFactsViewHolder(View view){
            super(view);

            title = (TextView) view.findViewById(R.id.Title);
            desc = (TextView) view.findViewById(R.id.description);

        }

    }
}
