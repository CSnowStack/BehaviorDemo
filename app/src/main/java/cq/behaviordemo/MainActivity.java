package cq.behaviordemo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import cq.behaviordemo.adapter.ItemAdapter;

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

        mViewPager.setAdapter(new ItemAdapter(getSupportFragmentManager(),mViewPager));
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





}
