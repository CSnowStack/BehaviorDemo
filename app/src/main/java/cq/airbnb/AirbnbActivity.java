package cq.airbnb;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import cq.behaviordemo.R;
import cq.behaviordemo.adapter.ItemAdapter;

/**
 * 仿爱彼迎主页
 */

public class AirbnbActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LinearLayout mLytUP;
    private LinearLayout mLytAll;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airbnb);
        findView();
        initView();
        initEvent();
        mTabLayout.setTranslationY(getResources().getDimensionPixelOffset(R.dimen.airbnb_translation_min));
    }

    private void findView() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mLytUP = (LinearLayout) findViewById(R.id.lyt_up);
        mLytAll = (LinearLayout) findViewById(R.id.lyt_all);

        mLytUP.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ((TabBehavior) ((CoordinatorLayout.LayoutParams) mTabLayout.getLayoutParams()).getBehavior()).hideItem();

            }
        });
        mLytAll.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ((TabBehavior) ((CoordinatorLayout.LayoutParams) mTabLayout.getLayoutParams()).getBehavior()).needExpand();

            }
        });
    }

    private void initView() {
        mViewPager.setAdapter(new ItemAdapter(getSupportFragmentManager(), mViewPager, 2));
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
