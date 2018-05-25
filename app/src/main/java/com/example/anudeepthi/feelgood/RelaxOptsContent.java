package com.example.anudeepthi.feelgood;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class RelaxOptsContent extends AppCompatActivity {

    private ArrayList<Integer> images;
    private BitmapFactory.Options options;
    private ViewPager viewPager;
    private FragmentStatePagerAdapter adapter;
    private LinearLayout thumbnailsContainer;
    private static int[] resourceIDs = new int[]{R.drawable.happy};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relax_content);

        Intent intent = getIntent();
        String activity = intent.getStringExtra(RelaxOptions.activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(activity.equals("yoga")){
            resourceIDs = new int[]{R.drawable.yoga1, R.drawable.yoga2,R.drawable.yoga3, R.drawable.yoga4, R.drawable.yoga5, R.drawable.yoga6, R.drawable.yoga7};
        }else if(activity.equals("meditation")){
            resourceIDs = new int[]{R.drawable.meditation1, R.drawable.meditation2, R.drawable.meditation3, R.drawable.meditation4, R.drawable.meditation5, R.drawable.meditation6, R.drawable.shiva};
        }else if(activity.equals("dance")){
            resourceIDs = new int[]{R.drawable.dance1,R.drawable.dance2, R.drawable.dance3, R.drawable.dance4, R.drawable.dance5, R.drawable.dance6, R.drawable.dance7};
        }else if(activity.equals("music")){
            resourceIDs = new int[]{R.drawable.music1, R.drawable.music2, R.drawable.music3, R.drawable.music4, R.drawable.music5, R.drawable.music6, R.drawable.music7};
        }else if(activity.equals("comedy")){
            resourceIDs = new int[]{R.drawable.comedy,R.drawable.comedy1, R.drawable.comedy2, R.drawable.comedy3, R.drawable.comedy4, R.drawable.comedy6, R.drawable.comedy7};
        }else if(activity.equals("tedtalk")){
            resourceIDs = new int[]{R.drawable.tt1,R.drawable.tt2, R.drawable.tt3, R.drawable.tt4, R.drawable.tt5, R.drawable.tt6, R.drawable.tt7};
        }

        images = new ArrayList<>();

        //find view by id
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        thumbnailsContainer = (LinearLayout) findViewById(R.id.container);



        setImagesData();

        // init viewpager adapter and attach
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), images);
        viewPager.setAdapter(adapter);

        inflateThumbnails();
    }



    private void setImagesData() {
        for (int i = 0; i < resourceIDs.length; i++) {
            images.add(resourceIDs[i]);
        }
    }

    private void inflateThumbnails() {
        for (int i = 0; i < images.size(); i++) {
            View imageLayout = getLayoutInflater().inflate(R.layout.yoga_image, null);
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.img_thumb);
            imageView.setOnClickListener(onChagePageClickListener(i));
            options = new BitmapFactory.Options();
            options.inSampleSize = 3;
            options.inDither = false;
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), images.get(i), options );
            imageView.setImageBitmap(bitmap);
            //set to image view
            imageView.setImageBitmap(bitmap);
            //add imageview
            thumbnailsContainer.addView(imageLayout);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.relax_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_relax) {
            finish();
        }else if(id == R.id.action_dashboard){
            setResult(RESULT_OK, null);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    public static class PageFragment extends Fragment {

        private int imageResource;
        private Bitmap bitmap;

        public static PageFragment getInstance(int resourceID) {
            PageFragment f = new PageFragment();
            Bundle args = new Bundle();
            args.putInt("image_source", resourceID);
            f.setArguments(args);
            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            imageResource = getArguments().getInt("image_source");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_relax_content, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            ImageView imageView = (ImageView) view.findViewById(R.id.mainImage);

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inSampleSize = 4;
            o.inDither = false;
            bitmap = BitmapFactory.decodeResource(getResources(), imageResource, o);
            imageView.setImageBitmap(bitmap);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            bitmap.recycle();
            bitmap = null;
        }
    }


    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private List<Integer> images;

        public ViewPagerAdapter(FragmentManager fm, List<Integer> imagesList) {
            super(fm);
            this.images = imagesList;
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.getInstance(images.get(position));
        }

        @Override
        public int getCount() {
            return images.size();
        }
    }
}