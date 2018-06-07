package com.example.anudeepthi.feelgood;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class FactsAdapter extends RecyclerView.Adapter<FactsAdapter.FactsViewHolder> {

    Context mContext;
    ArrayList<String> factList;


    public FactsAdapter(Context context, ArrayList<String> factList){
        this.mContext = context;
        this.factList = factList;
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

        holder.fact.setText(factList.get(position));
    }

    @Override
    public int getItemCount() {
        return factList.size();
    }

    class FactsViewHolder extends RecyclerView.ViewHolder{

        TextView fact;
        public FactsViewHolder(View view){
            super(view);

            fact = (TextView) view.findViewById(R.id.fact);
        }
    }
}
