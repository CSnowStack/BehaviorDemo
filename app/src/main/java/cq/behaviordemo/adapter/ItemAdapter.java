package cq.behaviordemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cq.behaviordemo.ItemFragment;
import cq.behaviordemo.listener.IsChildRequestScrollListener;
import cq.behaviordemo.listener.NeedExpandListener;
import cq.behaviordemo.listener.SupportNeedExpendListener;

/**
 * Created by cqll on 2016/12/15.
 */

public  class ItemAdapter extends FragmentStatePagerAdapter implements IsChildRequestScrollListener,SupportNeedExpendListener{
    private String [] titles={"MEDIA","ABOUT","REVIEWS"};
    private WeakReference<ViewPager> mViewPager;//也许有点用
    private List<ItemFragment> mFragments;
    private NeedExpandListener mNeedExpandListener;
    public ItemAdapter(FragmentManager fm) {
        this(fm,null);
    }

    public ItemAdapter(FragmentManager fm, ViewPager viewPager) {
        super(fm);
        mViewPager = new WeakReference<ViewPager>(viewPager);

        mFragments=new ArrayList<ItemFragment>();
        mFragments.add(ItemFragment.newInstance());
        mFragments.add(ItemFragment.newInstance());
        mFragments.add(ItemFragment.newInstance());
        fillListener();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }


    @Override
    public boolean requestScroll() {
        //有子项目,有设置 vp ,没被清掉
        if(mViewPager!=null&&mViewPager.get()!=null ){
            int currentItem=mViewPager.get().getCurrentItem();
            //实现了接口
            if(getItem(currentItem) instanceof IsChildRequestScrollListener)
                return ((IsChildRequestScrollListener)getItem(currentItem)).requestScroll();
        }

        return false;
    }

    @Override
    public void setNeedExpendListener(NeedExpandListener listener) {
        mNeedExpandListener=listener;
        fillListener();
    }

    @Override
    public NeedExpandListener getNeedExpendListener() {
        return mNeedExpandListener;
    }

    private void fillListener(){
        if(mFragments.size()>0){
            for(Fragment fragment:mFragments){
                if(fragment instanceof SupportNeedExpendListener &&((SupportNeedExpendListener)fragment).getNeedExpendListener()==null){
                    ((SupportNeedExpendListener)fragment).setNeedExpendListener(mNeedExpandListener);
                }
            }
        }
    }
}
