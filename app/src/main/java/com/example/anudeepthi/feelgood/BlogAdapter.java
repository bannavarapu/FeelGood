package com.example.anudeepthi.feelgood;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    Context context;
    String[] array;
    BlogAdapter(Context context, String[] array){
        this.context = context;
        this.array = array;
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
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {

        holder.title.setText(array[0]);
        holder.username.setText(array[1]);
        holder.description.setText(array[2]);

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class BlogViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView username;
        TextView description;
        public BlogViewHolder(View view){
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            username = (TextView) view.findViewById(R.id.username);
            description = (TextView) view.findViewById(R.id.description);
        }
    }
}
