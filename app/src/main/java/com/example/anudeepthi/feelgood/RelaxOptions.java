package com.example.anudeepthi.feelgood;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class RelaxOptions extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RelaxationAdapter mAdapter;
    RecyclerView mRecyclerView;
    private DatabaseReference mDatabaseReference;
    ArrayList<Relax_option_format> suggestionsToDisplay;
    FirebaseDatabase mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relax_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.relax_opts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        suggestionsToDisplay = new ArrayList<Relax_option_format>();
        mAdapter = new RelaxationAdapter(this,suggestionsToDisplay);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        fillDataSet();
    }

    public void onResume(){
        super.onResume();
        fillDataSet();
    }

    protected void fillDataSet()
    {
        String link="relax_options/";

        mDatabaseReference = mFirebaseDatabase.getReference().child(link);
        ValueEventListener toFillSuggestions = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Relax_option_format option ;
               String index1 = 1+"";
               String index2 = 2+"";
               suggestionsToDisplay.clear();
               option = dataSnapshot.child("music").child(index1).getValue(Relax_option_format.class);
               suggestionsToDisplay.add(option);
               option = dataSnapshot.child("music").child(index2).getValue(Relax_option_format.class);
               suggestionsToDisplay.add(option);
                option = (Relax_option_format)dataSnapshot.child("dance").child(index1).getValue(Relax_option_format.class);
                suggestionsToDisplay.add(option);
                option = (Relax_option_format)dataSnapshot.child("dance").child(index2).getValue(Relax_option_format.class);
                suggestionsToDisplay.add(option);
                option = (Relax_option_format)dataSnapshot.child("yoga").child(index1).getValue(Relax_option_format.class);
                suggestionsToDisplay.add(option);
                option = (Relax_option_format)dataSnapshot.child("yoga").child(index2).getValue(Relax_option_format.class);
                suggestionsToDisplay.add(option);
                option = (Relax_option_format)dataSnapshot.child("meditation").child(index1).getValue(Relax_option_format.class);
                suggestionsToDisplay.add(option);
                option = (Relax_option_format)dataSnapshot.child("meditation").child(index2).getValue(Relax_option_format.class);
                suggestionsToDisplay.add(option);
                option = (Relax_option_format)dataSnapshot.child("comedy").child(index1).getValue(Relax_option_format.class);
                suggestionsToDisplay.add(option);
                option = (Relax_option_format)dataSnapshot.child("comedy").child(index2).getValue(Relax_option_format.class);
                suggestionsToDisplay.add(option);
                mRecyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabaseReference.addValueEventListener(toFillSuggestions);

}

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.relax_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_dashboard) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Yoga) {
            // Handle the camera action
        } else if (id == R.id.nav_Meditation) {

        } else if (id == R.id.nav_Dance) {

        } else if (id == R.id.nav_Music) {

        } else if (id == R.id.nav_Comedy) {

        } else if (id == R.id.nav_TedTalks) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void relax_like(View view){
        //add code to what happens when user likes their relaxation suggestions
    }

    public void relax_dislike(View view){
        //add code to what happens when user does not like their relaxation suggestions
    }
}
