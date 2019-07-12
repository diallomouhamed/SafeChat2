package com.diallo.safechat;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class FragmentActivity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        ViewPager viewPager = findViewById(R.id.viewPager);

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setCurrentItem(1);

    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return ChatFragment.newInstance();
                case 1:
                    return CameraFragment.newInstance();
                case 2:
                    return LocationFragment.newInstance();

            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
