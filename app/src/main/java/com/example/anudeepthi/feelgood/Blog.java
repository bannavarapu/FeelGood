package com.example.anudeepthi.feelgood;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Blog extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static RecyclerView mRecyclerView;
    private static BlogAdapter mAdapter;
    static DatabaseReference blogRef = FirebaseDatabase.getInstance().getReference().child("blog_posts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);





        final Context context = getApplicationContext();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostBlog.class);
                startActivity(intent);

            }
        });

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public void fillmyblogadapter(final Context context, ArrayList<Blog_format> blog_posts,boolean flag)
        {
            mAdapter = new BlogAdapter(context, blog_posts,flag);
            mRecyclerView.setAdapter(mAdapter);
        }

        public final void fillPosts(final Context context, final ArrayList<String> user_posts, final boolean flag)
        {
            final ArrayList<Blog_format> myPosts = new ArrayList<>();
            for(String s : user_posts)
            {
                DatabaseReference newRef = blogRef.child(s);
                newRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String > forOne = new ArrayList<>();
                        for(DataSnapshot post: dataSnapshot.getChildren())
                        {
                            forOne.add(post.getValue().toString());
                        }

                        myPosts.add(new Blog_format(forOne.get(3),forOne.get(2),forOne.get(1),forOne.get(0),forOne.get(4)));
                        if(myPosts.size()==user_posts.size())
                        {
                            fillmyblogadapter(context, myPosts, flag);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            DatabaseReference publicBlogRef = FirebaseDatabase.getInstance().getReference().child("blog_posts");
            final ArrayList<Blog_format> allposts = new ArrayList<>();
            View rootView = inflater.inflate(R.layout.fragment_blog, container, false);
            final Context context = getContext();
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.blogView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            final boolean[] flag = {false};


            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1){
                DatabaseReference userPostRef = FirebaseDatabase.getInstance().getReference().child("users").child(MainActivity.getmUserID()).child("fav_blog");
                userPostRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> user_posts = new ArrayList<>();
                        for(DataSnapshot snap: dataSnapshot.getChildren())
                        {
                            if(!snap.getValue().toString().equals("dummy"))
                                user_posts.add(snap.getValue().toString().split("_")[1]);
                        }
                        fillPosts(context, user_posts, true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER) == 2){

                  DatabaseReference userPostRef = FirebaseDatabase.getInstance().getReference().child("users").child(MainActivity.getmUserID()).child("my_blog");
                  userPostRef.addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(DataSnapshot dataSnapshot) {
                          ArrayList<String> user_posts = new ArrayList<>();
                          for(DataSnapshot snap: dataSnapshot.getChildren())
                          {
                              if(!snap.getValue().toString().equals("dummy"))
                              user_posts.add(snap.getValue().toString().split("_")[1]);
                          }
                          fillPosts(context, user_posts, false);
                      }

                      @Override
                      public void onCancelled(DatabaseError databaseError) {
                      }
                  });
;

            }else if(getArguments().getInt(ARG_SECTION_NUMBER) == 3){

                ValueEventListener tofillposts = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        allposts.clear();
                       for(DataSnapshot post : dataSnapshot.getChildren())
                       {
                           final HashMap <String, String> singlepost = (HashMap<String, String>)post.getValue();
                           if(singlepost.get("visibility").equals("public"))
                           {
                               allposts.add(new Blog_format(singlepost.get("userId"),singlepost.get("title"),singlepost.get("postID"),singlepost.get("description"),singlepost.get("visibility")));
                           }
                       }
                        mAdapter = new BlogAdapter(context, allposts,false);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                blogRef.addValueEventListener(tofillposts);

            }

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private String[] titles = new String[]{"My favorites", "My Blog", "Public Blog"};

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
              return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
