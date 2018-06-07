package com.example.anudeepthi.feelgood;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RelaxOptsContent extends AppCompatActivity
{
    private ArrayList<String> imageUrl;
    private ArrayList<String> activityUrl;
    private ViewPager viewPager;
    private FragmentStatePagerAdapter adapter;
    private LinearLayout thumbnailsContainer;
    private static ArrayList<String> resourceIDUrls = new ArrayList<>();
    private static ArrayList<String> resourceLinks = new ArrayList<>();
    static String activity = null;
    static String VMFlag = null;
    static String[] click;
    static String[] actId;
    static Integer id;
    final static String Url = "Url";
    static String suggClick;
    static boolean Flag = false;


    private void fillData (String category)
    {
        resourceIDUrls = new ArrayList<>();
        DatabaseReference toAdd = FirebaseDatabase.getInstance().getReference().child("relax_options").child(category);
        toAdd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot each : dataSnapshot.getChildren())
                {
                    resourceIDUrls.add(each.child("image").getValue().toString());
                    resourceLinks.add(each.child("url").getValue().toString());
                }

                setStage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relax_content);

        Intent intent = getIntent();
        activity = intent.getStringExtra(RelaxOptions.activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent2 = getIntent();
        suggClick = intent2.getStringExtra(RelaxationAdapter.suggClick);

        if(suggClick != null){
            click = suggClick.split(";");
            suggClick = null;
            if(click.length > 1){
                Flag = true;
                actId = click[0].split("_");
                activity = actId[0];
                id = Integer.parseInt(actId[1]);
            }else {
                activity = click[0];
            }

        }


        if(activity.equals("yoga")){
            VMFlag = "image";
            fillData("yoga");
        }else if(activity.equals("meditation")){
            VMFlag = "image";
            fillData("meditation");
        }else if(activity.equals("dance")){
            VMFlag = "image";
            fillData("dance");
        }else if(activity.equals("music")){
            VMFlag = "image";
            fillData("music");
        }else if(activity.equals("comedy")){
            VMFlag = "image";
            fillData("comedy");
        }else if(activity.equals("tedtalk")){
            VMFlag = "image";
            fillData("tedtalk");
        }


    }

    private void setStage()
    {
        imageUrl = new ArrayList<>();
        activityUrl = new ArrayList<>();

        //find view by id
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        thumbnailsContainer = (LinearLayout) findViewById(R.id.container);

        setData();

        if(VMFlag.equals("image")){
            adapter = new ViewPagerAdapter(getSupportFragmentManager(), imageUrl);
        }

        viewPager.setAdapter(adapter);
        if(Flag){
            viewPager.setCurrentItem(id-1);
        }

        try {
            inflateThumbnails();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }



    private void setData() {

        if(VMFlag.equals("image")){
            for(int i=0; i<resourceIDUrls.size(); i++){
                imageUrl.add(resourceIDUrls.get(i)+";"+resourceLinks.get(i));
            }
        }



    }





    private void inflateThumbnails() throws Throwable {
        Context context = getApplicationContext();
        if(VMFlag.equals("image")){
            for(int i=0; i<imageUrl.size(); i++) {
                String mainImage = imageUrl.get(i).split(";")[0];
                View thumbLayout = getLayoutInflater().inflate(R.layout.relax_content_image, null);
                ImageView imageView = (ImageView) thumbLayout.findViewById(R.id.img_thumb);
                imageView.setOnClickListener(onChagePageClickListener(i));
                Glide.with(context).load(mainImage).thumbnail(0.5f).into(imageView);
                thumbnailsContainer.addView(thumbLayout);
            }
        }

    }



    private View.OnClickListener onChagePageClickListener(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(i);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.relax_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_relax) {
            finish();
        }else if(id == R.id.action_dashboard){
            setResult(RESULT_OK, null);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }



    public static class PageFragment extends Fragment {

        private String dataUrl;

        public static PageFragment newInstance(String resourceID) {
            PageFragment f = new PageFragment();
            Bundle args = new Bundle();
            args.putString("data_url",resourceID);
            f.setArguments(args);
            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            dataUrl = getArguments().getString("data_url");

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_relax_content, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            final ImageView imageView = (ImageView) view.findViewById(R.id.mainImage);
            String mainImage;
            final String url;
            mainImage = dataUrl.split(";")[0];
            Log.e("url",dataUrl);
            url = dataUrl.split(";")[1];
            Glide.with(getContext()).load(mainImage).thumbnail(0.5f).into(imageView);


            if(!url.equals("null")){
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), WebApp.class);
                        intent.putExtra(Url,url);
                        startActivity(intent);
                    }
                });
            }


        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    }



    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private List<String> dataUrl = null;
        public ViewPagerAdapter(FragmentManager fm, List<String> dataUrls) {
            super(fm);
            this.dataUrl = dataUrls;
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(dataUrl.get(position));
        }


        @Override
        public int getCount() {
            return dataUrl.size();
        }

    }
}