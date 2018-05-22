package com.example.anudeepthi.feelgood;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FactsAdapter extends RecyclerView.Adapter<FactsAdapter.FactsViewHolder> {

    Context mContext;


    public FactsAdapter(Context context){
        this.mContext = context;

    }

    @NonNull
    @Override
    public FactsAdapter.FactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutid = R.layout.facts_rv;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutid, parent, false);
        FactsViewHolder viewHolder = new FactsViewHolder(view);

        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FactsAdapter.FactsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class FactsViewHolder extends RecyclerView.ViewHolder{

        TextView fact;
        public FactsViewHolder(View view){
            super(view);

            fact = (TextView) view.findViewById(R.id.main_fact);
        }
    }
}
