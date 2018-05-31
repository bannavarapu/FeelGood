package com.example.anudeepthi.feelgood;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


public class MainFactsAdapter extends RecyclerView.Adapter<MainFactsAdapter.MainFactsViewHolder> {


    Context mContext;

    public MainFactsAdapter(Context context){
        mContext = context;

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

        holder.title.setText("Test Title");
        holder.desc.setText("Test Desc");

    }

    @Override
    public int getItemCount() {
        return 10;
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
