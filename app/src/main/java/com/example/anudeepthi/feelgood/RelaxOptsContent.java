package com.example.anudeepthi.feelgood;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RelaxOptsContent extends AppCompatActivity
{


    private ArrayList<String> imageUrl;

    private ViewPager viewPager;
    private FragmentStatePagerAdapter adapter;
    private LinearLayout thumbnailsContainer;

    private static String[] resourceIDUrls = new String[]{};
    String activity = null;
    static String VMFlag = null;
    static String[] click;
    static String suggClick;
    static boolean Flag = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relax_content);

        Intent intent = getIntent();
        activity = intent.getStringExtra(RelaxOptions.activity);
        System.out.println(activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent2 = getIntent();
        System.out.println(suggClick);
        suggClick = intent2.getStringExtra(RelaxationAdapter.suggClick);
        System.out.println(suggClick);
        if(suggClick != null){
            click = suggClick.split(";");
            suggClick = null;
            if(click.length > 1){
                Flag = true;
            }
            System.out.println(Arrays.toString(click));
            activity = click[0];
        }


        if(activity.equals("yoga")){
            VMFlag = "image";

        }else if(activity.equals("meditation")){
            VMFlag = "image";

        }else if(activity.equals("dance")){
            VMFlag = "image";

        }else if(activity.equals("music")){
            VMFlag = "music";
            resourceIDUrls = new String[]{"https://firebasestorage.googleapis.com/v0/b/feelgood-58fe5.appspot.com/o/music1.jpg?alt=media&token=6007e86c-9105-446b-b7ce-af7baf459680", "https://firebasestorage.googleapis.com/v0/b/feelgood-58fe5.appspot.com/o/music4.jpg?alt=media&token=1405bfa8-bbe9-48d2-844b-83ecc0ec0724",
                                                  "https://firebasestorage.googleapis.com/v0/b/feelgood-58fe5.appspot.com/o/music5.jpg?alt=media&token=eaaa8780-61a1-4fb0-958c-0e3fb3e286aa"};

        }else if(activity.equals("comedy")){
            VMFlag = "image";

            resourceIDUrls = new String[]{"https://lh3.googleusercontent.com/TD4GYLXpB_OYLKHOd7CRg96bWYKDgYKVLKw39RpvJUxwEnAfws8yFaHhSe1gaO4PMtculxxB9A=w300","https://lh3.googleusercontent.com/PD5rCr4nzJ0CFcDnEf2pUst_1W_PL96I28SeNE_RMWAXnaRMO6QQtdovE5HPzIaa9M69e7ufTpQ=w300",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ4cqfi3GrbCAzcGs646e-7OfL5T6RSKmKWJAekmXQpbkLqvcpD", "http://data.freehdw.com/cityscapes-skylines-chicago-hdr-photography-high-resolution-images.jpg",
                    "https://lh3.googleusercontent.com/TD4GYLXpB_OYLKHOd7CRg96bWYKDgYKVLKw39RpvJUxwEnAfws8yFaHhSe1gaO4PMtculxxB9A=w300", "https://lh3.googleusercontent.com/PD5rCr4nzJ0CFcDnEf2pUst_1W_PL96I28SeNE_RMWAXnaRMO6QQtdovE5HPzIaa9M69e7ufTpQ=w300",
                    "http://idolza.com/a/f/h/hour-sleeping-music-calming-for-stress-relief-relaxation-sleep_calm-colors_what-color-furniture-goes-with-gray-walls-mood-meanings-teenage-room-colors-retro-house-decor-big-ki.jpg",
                    "https://lh3.googleusercontent.com/QplltmIni6pRg87UqpM3-o9CFi-kGTejeZSzUdcr4WoaZ2s27CxoYbaeGY2nNtCihTJqR5Ee=w300" };

        }else if(activity.equals("tedtalk")){
            VMFlag = "image";
            resourceIDUrls = new String[]{};
        }


        imageUrl = new ArrayList<>();

        //find view by id
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        thumbnailsContainer = (LinearLayout) findViewById(R.id.container);

        setData();

        if(VMFlag.equals("image")){
            adapter = new ViewPagerAdapter(getSupportFragmentManager(), imageUrl);
        }

        viewPager.setAdapter(adapter);

        try {
            inflateThumbnails();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }



    private void setData() {

        if(VMFlag.equals("image")){
            for(int i=0; i<resourceIDUrls.length; i++){
                imageUrl.add(resourceIDUrls[i]);
            }
        }



    }





    private void inflateThumbnails() throws Throwable {


        Context context = getApplicationContext();
        if(VMFlag.equals("image")){
            for(int i=0; i<imageUrl.size(); i++) {
                View thumbLayout = getLayoutInflater().inflate(R.layout.relax_content_image, null);
                ImageView imageView = (ImageView) thumbLayout.findViewById(R.id.img_thumb);
                imageView.setOnClickListener(onChagePageClickListener(i));
                Glide.with(context).load(imageUrl.get(i)).thumbnail(0.5f).into(imageView);
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
            if(VMFlag.equals("image")){
                ImageView imageView = (ImageView) view.findViewById(R.id.mainImage);
                if(Flag){
                    Glide.with(getContext()).load(click[1]).thumbnail(0.5f).into(imageView);
                    Flag = false;

                }else{
                    Glide.with(getContext()).load(dataUrl).thumbnail(0.5f).into(imageView);
                }

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