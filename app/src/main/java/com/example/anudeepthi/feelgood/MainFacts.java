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
import java.util.HashMap;
import java.util.Random;

public class MainFacts extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MainFactsAdapter mAdapter;
    private HashMap<Integer,String> genrandom = new HashMap<>();
    ArrayList<main_fact> factstodisplay = new ArrayList<>();

    public void filldata()
    {
//        factstodisplay.clear();
        final String tag [] = MainActivity.muserTag().split("_");
        final int l = tag.length;
        final DatabaseReference factref = FirebaseDatabase.getInstance().getReference().child("main_facts");
        factref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int i=2;i<l;i++)
                {
                    Log.e("checkpoint","3");
                    for(int j=1;j<6;j++)
                    {
                        factstodisplay.add(dataSnapshot.child(tag[i]).child(j+"").getValue(main_fact.class));
                    }
                }
                while (factstodisplay.size()<15)
                    {
                        Log.e("checkpoint","4");
                    Random rand = new Random();
                    int i = rand.nextInt(7)+1;
                    int j = rand.nextInt(5)+1;
                    factstodisplay.add(dataSnapshot.child(genrandom.get(i)).child(j+"").getValue(main_fact.class));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("checkpoint","1");
        setContentView(R.layout.activity_main_facts);
        mRecyclerView = (RecyclerView) findViewById(R.id.main_facts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MainFactsAdapter(this,factstodisplay);
        genrandom.put(1,"P");
        genrandom.put(2,"E");
        genrandom.put(3,"N");
        genrandom.put(4,"M");
        genrandom.put(5,"A");
        genrandom.put(6,"C");
        genrandom.put(7,"G");
        Log.e("checkpoint","2");
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
