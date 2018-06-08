package com.example.anudeepthi.feelgood;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
    public static String mUserID = "";
    public boolean flag = false;
    private ArrayList<String> firstName = new ArrayList<String>(Arrays.asList("Yung", "Dong", "Sang", "Choi", "lee", "Jung", "Huyn", "Jim", "Gun", "Wook"));
    private ArrayList<String> lastName = new ArrayList<String>(Arrays.asList("Noki", "Doshi", "Todota", "Ka Si", "Ki Na", "Modo Rika", "Ariku", "No Mo", "Hamadi", "Ting Wong"));
    private static String mUserTag = "";
    private static ArrayList<String> stressReliefOptions = new ArrayList<>();
    private static HashMap<String,String> reliefSuggestion = new HashMap<>();
    private HashMap<String,String> ageMap = new HashMap<>();
    private HashMap<String,String> genderMap = new HashMap<>();
    private HashMap<String,String> stressTag = new HashMap<>();
    private HashMap<String,String> toAddToList = new HashMap<>();
    static boolean formFlag = false;
    static RecyclerView facts_rv;
    static Button fillFormButton;
    static TextView feelingQuery;
    static LinearLayout images;
    static RelativeLayout buttonLayout;

    private void fillform() {
        buttonLayout = (RelativeLayout) findViewById(R.id.buttonLayout);
        buttonLayout.setVisibility(View.VISIBLE);
        fillFormButton = (Button) findViewById(R.id.fillForm);
        fillFormButton.setVisibility(View.VISIBLE);
        facts_rv = (RecyclerView) findViewById(R.id.rv_facts);
        facts_rv.setVisibility(View.GONE);
        feelingQuery = (TextView) findViewById(R.id.feeling_query);
        feelingQuery.setVisibility(View.GONE);
        images = (LinearLayout) findViewById(R.id.images);
        images.setVisibility(View.GONE);
        fillFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FillFormApp.class);
                startActivity(intent);
            }
        });
    }

    private void fillreliefoption()
    {
         mUserTag = "2_F_N_P_E_A_G";
        final String [] tags = mUserTag.split("_");
        String age = ageMap.get(tags[0]);
        String gender = genderMap.get(tags[1]);
        Log.e("age",age);
        Log.e("gender",gender);
        final int length = tags.length;
        final DatabaseReference toFetch = FirebaseDatabase.getInstance().getReference().child("relax_responses").child(age).child(gender);
        toFetch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int i=2;i<length;i++)
                {
                    final String current = stressTag.get(tags[i]);
                    if(current == "grief" || current == "anger")
                    {
                        if(!stressReliefOptions.contains("meditation"))
                        {
                            stressReliefOptions.add("meditation");
                        }

                    }
                    else
                    {
                        DatabaseReference tempref = toFetch.child(current);
                        Query query = tempref.orderByValue().limitToFirst(1);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String k = dataSnapshot.getValue().toString().split("=")[0].replace("{","");
                                if(k.length()>1 && stressReliefOptions.size()<4)
                                {
                                    if(!stressReliefOptions.contains("tedtalk"))
                                    {
                                        stressReliefOptions.add("tedtalk");
                                    }
                                    if(!stressReliefOptions.contains("music"))
                                    {
                                        stressReliefOptions.add("music");
                                    }
                                    if(!stressReliefOptions.contains("dance"))
                                    {
                                        stressReliefOptions.add("dance");
                                    }
                                    if(!stressReliefOptions.contains("meditation"))
                                    {
                                        stressReliefOptions.add("meditation");
                                    }
                                }
                                else if(k.length()==1)
                                {
                                    if(!stressReliefOptions.contains(toAddToList.get(k)))
                                    {
                                        stressReliefOptions.add(toAddToList.get(k));
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkUserTag(){
        DatabaseReference fortag = FirebaseDatabase.getInstance().getReference().child("users").child(mUserID);
        fortag.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String temp = dataSnapshot.child("userTag").getValue().toString();
                    if(temp.equals("Tag"))
                    {
                        mUserTag = "2_M";
                        fillreliefoption();
                        fillform();
                    }
                    else
                    {
                        mUserTag = temp;
                        fillreliefoption();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getusername() {
        DatabaseReference mUserdetailref = FirebaseDatabase.getInstance().getReference().child("users").child(mUserID).child("userName");
        ValueEventListener tocheckuser = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mUsername = dataSnapshot.getValue().toString();
                    TextView uname = (TextView) findViewById(R.id.textView);
                    uname.setText(mUsername);
                    checkUserTag();
//                    mUserTag = "1_F_C_E_M_N";


                } else {
                    Random rfunc = new Random();
                    final int firstNameId = rfunc.nextInt(10);
                    final int lastNameId = rfunc.nextInt(10);
                    final DatabaseReference togetusercount = FirebaseDatabase.getInstance().getReference().child("user_count");
                    final DatabaseReference fillFormTag = FirebaseDatabase.getInstance().getReference().child("users").child(mUserID).child("userTag");
                    togetusercount.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String number = dataSnapshot.getValue().toString();
                            System.out.println(number);
                            togetusercount.setValue(Integer.parseInt(number) + 1);
                            mUsername = firstName.get(firstNameId) + " " + lastName.get(lastNameId) + number;
                            TextView uname = (TextView) findViewById(R.id.textView);
                            uname.setText(mUsername);
                            User user = new User(mUsername, "Tag");
                            DatabaseReference mUserdetailref = FirebaseDatabase.getInstance().getReference().child("users");
                            mUserdetailref.child(mUserID).setValue(user);
                            checkUserTag();
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


    protected void fillDataSet(final boolean flag) {
        factsToDisplay.clear();
        this.flag = flag;
        mDatabaseReference = mFirebaseDatabase.getReference().child("facts").child(currentMood);
        final ProgressBar mProgBar = (ProgressBar) findViewById(R.id.progBar);
        final ArrayList<Integer> remduplicate = new ArrayList<>();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                while (factsToDisplay.size()<8)
                {
                    Random rand = new Random();
                    int i = rand.nextInt(20)+1;
                    if(!remduplicate.contains(i))
                    {
                        factsToDisplay.add(dataSnapshot.child(i+"").getValue().toString());
                        remduplicate.add(i);
                    }
                    if (flag == true) {
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mRecyclerView.setAdapter(mAdapter);
                        mProgBar.setVisibility(View.INVISIBLE);

                    }
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
        setContentView(R.layout.activity_main);
        mUsername = "DEFAULT";
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentMood = "dont_know";

//        --------------------------------------------------------
        reliefSuggestion.put("1","I don't feel stressed");
        reliefSuggestion.put("2","Talk to somebody");
        reliefSuggestion.put("3","Listen to music");
        reliefSuggestion.put("4","Get professional help");
        reliefSuggestion.put("5","Meditation");
        reliefSuggestion.put("6","Engage in a hobby");
        reliefSuggestion.put("7","Take Antidepressants");
//        --------------------------------------------------------
        ageMap.put("1","14_18");
        ageMap.put("2","18_24");
        ageMap.put("3","24_30");
        ageMap.put("4","30_40");
        ageMap.put("5","40");
//        --------------------------------------------------------
        genderMap.put("M","male");
        genderMap.put("F","female");
        genderMap.put("O","other");
//        --------------------------------------------------------
        stressTag.put("C","chemical");
        stressTag.put("P","physical");
        stressTag.put("E","emotional");
        stressTag.put("M","mental");
        stressTag.put("N","nutrition");
        stressTag.put("A","anger");
        stressTag.put("G","grief");
//        --------------------------------------------------------
        toAddToList.put("2","tedtalk");
        toAddToList.put("3","music");
        toAddToList.put("4","tedtalk");
        toAddToList.put("5","meditation");
        toAddToList.put("6","dance");
        toAddToList.put("7","tedtalk");




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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new FactsAdapter(this, factsToDisplay);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        fillDataSet(false);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mUserID = user.getUid();
                    onSignedInInitialize(user.getDisplayName());
                    getusername();

                } else {
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

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed In!", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
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
        if (id == R.id.action_signout) {
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

        if (id == R.id.nav_relax) {

            Intent intent = new Intent(this, RelaxOptions.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_blog) {
            Intent intent = new Intent(this, Blog.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_facts) {
            Intent intent = new Intent(this, MainFacts.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.bunny) {
            Intent intent = new Intent(this, Bunny.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_hotline) {

        }else if (id == R.id.nav_about) {

            Intent intent = new Intent(this, Communicate.class);
            startActivity(intent);
            return true;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
    }

    private void onSignedOutCleanUp() {
        mUsername = "DEFAULT";
    }


    public void excited(View view) {
        currentMood = "excited";
        Toast.makeText(this, "I'm excited", Toast.LENGTH_LONG).show();
        factsToDisplay.clear();
        fillDataSet(true);
    }

    public void happy(View view) {
        currentMood = "happy";
        Toast.makeText(this, "I'm happy", Toast.LENGTH_LONG).show();
        factsToDisplay.clear();
        fillDataSet(true);
    }

    public void dont_know(View view) {
        currentMood = "dont_know";
        Toast.makeText(this, "I'm not sure", Toast.LENGTH_LONG).show();
        factsToDisplay.clear();
        fillDataSet(true);
    }

    public void sad(View view) {
        currentMood = "sad";
        Toast.makeText(this, "I'm sad", Toast.LENGTH_LONG).show();
        factsToDisplay.clear();
        fillDataSet(true);
    }

    public void very_sad(View view) {
        currentMood = "very_sad";
        Toast.makeText(this, "I'm very sad", Toast.LENGTH_LONG).show();
        factsToDisplay.clear();
        fillDataSet(true);
    }

    public void very_angry(View view) {
        currentMood = "anger";
        Toast.makeText(this, "I'm very angry", Toast.LENGTH_LONG).show();
        factsToDisplay.clear();
        fillDataSet(true);
    }

    public static String getmUserID() {
        return mUserID;
    }

    public static String muserTag()
    {
        return mUserTag;
    }

    public static ArrayList<String> getStressReliefOptions()
    {
        return stressReliefOptions;
    }
}
