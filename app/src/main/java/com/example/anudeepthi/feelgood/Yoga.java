package com.example.anudeepthi.feelgood;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Yoga extends AppCompatActivity {

    private ArrayList<Integer> images;
    private BitmapFactory.Options options;
    private ViewPager viewPager;
    private FragmentStatePagerAdapter adapter;
    private LinearLayout thumbnailsContainer;
    private final static int[] resourceIDs = new int[]{R.drawable.happy, R.drawable.excited, R.drawable.dont_know,
                                                    R.drawable.pink_heart, R.drawable.very_angry,
            R.drawable.happy, R.drawable.excited, R.drawable.dont_know,
            R.drawable.pink_heart, R.drawable.very_angry};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga);
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
            return inflater.inflate(R.layout.fragment_yoga, container, false);
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