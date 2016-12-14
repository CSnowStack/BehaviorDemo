package cq.behaviordemo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initView();
        initEvent();
    }

    private void findView() {
        mTabLayout=(TabLayout) findViewById(R.id.tab_layout);
        mViewPager=(ViewPager) findViewById(R.id.viewpager);
    }


    private void initView() {

        mViewPager.setAdapter(new ItemAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initEvent() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



    static class ItemAdapter extends FragmentStatePagerAdapter{
        private String [] titles={"MEDIA","ABOUT","REVIEWS"};

        public ItemAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ItemFragment.newInstance();
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }


}
