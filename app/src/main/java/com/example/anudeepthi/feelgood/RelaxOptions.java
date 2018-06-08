package com.example.anudeepthi.feelgood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class RelaxOptions extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RelaxationAdapter mAdapter;
    RecyclerView mRecyclerView;
    private DatabaseReference mDatabaseReference;
    private ArrayList<Relax_option_format> suggestionsToDisplay;
    private FirebaseDatabase mFirebaseDatabase;
    private final int REQUEST_CODE = 1001;
    static String activity;
    private HashMap<Integer,String> GenerateRandom = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relax_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        GenerateRandom.put(1,"music");
        GenerateRandom.put(2,"tedtalk");
        GenerateRandom.put(3,"comedy");
        GenerateRandom.put(4,"yoga");
        GenerateRandom.put(5,"dance");
        GenerateRandom.put(6,"meditation");
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
                ArrayList<String> list = MainActivity.getStressReliefOptions();
                Relax_option_format option ;
                Random rand = new Random();
                String index1 = rand.nextInt(8)+1+"";
                String index2 = rand.nextInt(8)+1+"";
                suggestionsToDisplay.clear();
                System.out.println("List: "+list);
                for(String single: list)
                {
                    option = (Relax_option_format)dataSnapshot.child(single).child(index1).getValue(Relax_option_format.class);
                    suggestionsToDisplay.add(option);
                    option = (Relax_option_format)dataSnapshot.child(single).child(index2).getValue(Relax_option_format.class);
                    suggestionsToDisplay.add(option);
                }
                option = (Relax_option_format)dataSnapshot.child("comedy").child(index1).getValue(Relax_option_format.class);
                suggestionsToDisplay.add(option);
                option = (Relax_option_format)dataSnapshot.child("comedy").child(index2).getValue(Relax_option_format.class);
                suggestionsToDisplay.add(option);
                option = (Relax_option_format)dataSnapshot.child("yoga").child(index1).getValue(Relax_option_format.class);
                suggestionsToDisplay.add(option);
                option = (Relax_option_format)dataSnapshot.child("yoga").child(index2).getValue(Relax_option_format.class);
                suggestionsToDisplay.add(option);

                while(suggestionsToDisplay.size()<12)
                {
                    int i = rand.nextInt(6)+1;
                    //generate random number again to select from sub category, for now fixing it to 1
                    int j = rand.nextInt(8)+1;
                    option = (Relax_option_format)dataSnapshot.child(GenerateRandom.get(i)).child(j+"").getValue(Relax_option_format.class);
                    suggestionsToDisplay.add(option);
                }

                Collections.shuffle(suggestionsToDisplay);


                mRecyclerView.setAdapter(mAdapter);
                ProgressBar mRelaxProg = (ProgressBar) findViewById(R.id.relaxProgBar);
                mRelaxProg.setVisibility(View.INVISIBLE);
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
        getMenuInflater().inflate(R.menu.dashboard, menu);
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

            Intent intent = new Intent(this, RelaxOptsContent.class);
            intent.putExtra(activity,"yoga");
            startActivityForResult(intent,REQUEST_CODE);
        } else if (id == R.id.nav_Meditation) {

            Intent intent = new Intent(this, RelaxOptsContent.class);
            intent.putExtra(activity,"meditation");
            startActivityForResult(intent,REQUEST_CODE);

        } else if (id == R.id.nav_Dance) {

            Intent intent = new Intent(this, RelaxOptsContent.class);
            intent.putExtra(activity,"dance");
            startActivityForResult(intent,REQUEST_CODE);

        } else if (id == R.id.nav_Music) {

            Intent intent = new Intent(this, RelaxOptsContent.class);
            intent.putExtra(activity,"music");
            startActivityForResult(intent,REQUEST_CODE);

        } else if (id == R.id.nav_Comedy) {

            Intent intent = new Intent(this, RelaxOptsContent.class);
            intent.putExtra(activity,"comedy");
            startActivityForResult(intent,REQUEST_CODE);

        } else if (id == R.id.nav_TedTalks) {

            Intent intent = new Intent(this, RelaxOptsContent.class);
            intent.putExtra(activity,"tedtalk");
            startActivityForResult(intent,REQUEST_CODE);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1001){
            if (resultCode == RESULT_OK){
                this.finish();
            }
        }
    }

}
