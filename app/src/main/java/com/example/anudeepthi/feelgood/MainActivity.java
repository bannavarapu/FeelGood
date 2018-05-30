package com.example.anudeepthi.feelgood;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
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
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final int RC_SIGN_IN = 1;
    private String mUsername;
    private FactsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private String currentMood;
    private DatabaseReference mDatabaseReference;
    private ArrayList<String> factsToDisplay;
    private FirebaseDatabase mFirebaseDatabase;
    public static String mUserID =  "";
    public boolean flag = false;
    private ArrayList<String> firstName = new ArrayList<String>(Arrays.asList("Yung", "Dong", "Sang", "Choi", "lee", "Jung", "Huyn", "Jim", "Gun", "Wook"));
    private ArrayList<String> lastName = new ArrayList<String>(Arrays.asList("Noki", "Doshi", "Todota", "Ka Si", "Ki Na", "Modo Rika", "Ariku", "No Mo", "Hamadi", "Ting Wong"));
    private String mUserTag = "";

    private String fillform()
    {
        Toast.makeText(this, "Bro you need to fill up a form!", Toast.LENGTH_LONG).show();
        return "Tag";
    }

    private void getusername ()
    {
        DatabaseReference  mUserdetailref =  FirebaseDatabase.getInstance().getReference().child("users").child(mUserID).child("userName");
        ValueEventListener tocheckuser = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    mUsername = dataSnapshot.getValue().toString();
                    TextView uname = (TextView) findViewById(R.id.textView);
                    uname.setText(mUsername);
                }
                else
                {
                    mUserTag = fillform();
                    Random rfunc = new Random();
                    final int firstNameId = rfunc.nextInt(10);
                    final int lastNameId = rfunc.nextInt(10);
                    final DatabaseReference togetusercount = FirebaseDatabase.getInstance().getReference().child("user_count");
                    togetusercount.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String number = dataSnapshot.getValue().toString();
                            togetusercount.setValue(Integer.parseInt(number)+1);
                            mUsername = firstName.get(firstNameId)+" "+lastName.get(lastNameId)+number;
                            TextView uname = (TextView) findViewById(R.id.textView);
                            uname.setText(mUsername);
                            User user = new User (mUsername, mUserTag);
                            DatabaseReference  mUserdetailref =  FirebaseDatabase.getInstance().getReference().child("users");
                            mUserdetailref.child(mUserID).setValue(user);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mUserdetailref.addValueEventListener(tocheckuser);
    }


    protected void fillDataSet(final boolean flag)
    {
        this.flag = flag;
        String link="facts/"+currentMood+"/";
        mDatabaseReference = mFirebaseDatabase.getReference().child(link);
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String data = dataSnapshot.getValue(String.class);
                factsToDisplay.add(data);
                if(flag==true) {mAdapter.notifyDataSetChanged();}
                else
                {
                    mRecyclerView.setAdapter(mAdapter);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUsername = "DEFAULT";
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentMood="dont_know";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        factsToDisplay = new ArrayList<String>();
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_facts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new FactsAdapter(this,factsToDisplay);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        fillDataSet(false);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                mUserID = user.getUid();
                onSignedInInitialize(user.getDisplayName());
                getusername();
                }
                else{
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Signed In!", Toast.LENGTH_LONG).show();
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Signed In Cancelled!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_signout){
            AuthUI.getInstance().signOut(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_prog) {
            // Handle the camera action
        } else if (id == R.id.nav_relax) {

            Intent intent = new Intent(this, RelaxOptions.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_blog) {
            Intent intent = new Intent(this, Blog.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_hotline) {
            Intent intent = new Intent(this, Bunny.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onSignedInInitialize(String username){
        mUsername = username;
    }

    private void onSignedOutCleanUp(){
        mUsername = "DEFAULT";
    }


    public void excited(View view){
        currentMood="excited";
        Toast.makeText(this,"I'm excited",Toast.LENGTH_LONG).show();
        factsToDisplay.clear();
        fillDataSet(true);
    }

    public void happy(View view)
    {
        currentMood="happy";
        Toast.makeText(this,"I'm happy",Toast.LENGTH_LONG).show();
        factsToDisplay.clear();
        fillDataSet(true);
    }

    public void dont_know(View view){
        currentMood="dont_know";
        Toast.makeText(this,"I'm not sure",Toast.LENGTH_LONG).show();
        factsToDisplay.clear();
        fillDataSet(true);
    }

    public void sad(View view){
        currentMood="sad";
        Toast.makeText(this,"I'm sad",Toast.LENGTH_LONG).show();
        factsToDisplay.clear();
        fillDataSet(true);
    }

    public void very_sad(View view){
        currentMood="very_sad";
        Toast.makeText(this,"I'm very sad",Toast.LENGTH_LONG).show();
        factsToDisplay.clear();
        fillDataSet(true);
    }

    public void very_angry(View view){
        currentMood="anger";
        Toast.makeText(this,"I'm very angry",Toast.LENGTH_LONG).show();
//        System.out.println(Arrays.toString(factsToDisplay.toArray())+"full array");
        factsToDisplay.clear();
//        System.out.println(Arrays.toString(factsToDisplay.toArray())+"empty array");
        fillDataSet(true);
    }

    public static String getmUserID()
    {
        return mUserID;
    }
}
