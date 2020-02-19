package com.commonsware.cwac.camera.demo.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonsware.cwac.camera.demo.other.Helper;
import com.example.claimmate.R;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;

import java.util.ArrayList;

public class OneTimeActivity extends AppCompatActivity {

    ViewPager viewPager;
    DotIndicator dotIndicator;
    TextView txtNext;
    ArrayList<Integer> arrayListImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time);

        init();
    }

    private void init() {
        viewPager = findViewById(R.id.viewPager);
        dotIndicator = findViewById(R.id.dotIndicator);
        txtNext = findViewById(R.id.txtNext);

        arrayListImage = new ArrayList<>();
        arrayListImage.add(R.drawable.test);
        arrayListImage.add(R.drawable.test);
        arrayListImage.add(R.drawable.test);

        viewPager.setAdapter(new SlidingImageAdapter());

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                dotIndicator.setSelectedItem(position, true);
                if (position == (arrayListImage.size()-1)) {
                    txtNext.setText("Start");
                } else {
                    txtNext.setText("Next");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void onClick(View view) {
        if (view.getId() == R.id.txtSkip  || txtNext.getText().toString().equals("Start")) {
            Helper.setFirstTime();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if (view.getId() == R.id.txtNext) {
            viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
        }
    }

    class SlidingImageAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        public SlidingImageAdapter() {
            inflater = LayoutInflater.from(OneTimeActivity.this);
        }

        @Override
        public int getCount() {
            return arrayListImage.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.item_onetime_page, view, false);

            assert imageLayout != null;
            final ImageView imageView = imageLayout.findViewById(R.id.image);

            imageView.setImageResource(arrayListImage.get(position));

            view.addView(imageLayout, 0);

            return imageLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }
}
