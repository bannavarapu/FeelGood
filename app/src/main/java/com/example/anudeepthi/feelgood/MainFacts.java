package com.example.anudeepthi.feelgood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class MainFacts extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MainFactsAdapter mAdapter;
    private HashMap<String,Integer> forsize = new HashMap<>();
    ArrayList<main_fact> factstodisplay = new ArrayList<>();

    public void filldata()
    {
        final String tag [] = MainActivity.muserTag().split("_");
        final int l = tag.length;
        final DatabaseReference factref = FirebaseDatabase.getInstance().getReference().child("main_facts");
        factref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int j=1;j<6;j++)
                {
                    factstodisplay.add(dataSnapshot.child("S").child(j+"").getValue(main_fact.class));
                }

                for(int i=2;i<l;i++)
                {
                    for(int j=1;j<forsize.get(tag[i]);j++)
                    {
                        factstodisplay.add(dataSnapshot.child(tag[i]).child(j+"").getValue(main_fact.class));
                    }
                }

                Collections.shuffle(factstodisplay);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_facts);
        mRecyclerView = (RecyclerView) findViewById(R.id.main_facts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MainFactsAdapter(this,factstodisplay);
        forsize.put("A",5);
        forsize.put("C",2);
        forsize.put("E",3);
        forsize.put("G",3);
        forsize.put("M",1);
        forsize.put("N",0);
        forsize.put("P",0);
        filldata();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_dashboard){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
