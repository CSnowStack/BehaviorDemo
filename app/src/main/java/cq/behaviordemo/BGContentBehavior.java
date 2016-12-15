package cq.behaviordemo;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by cqll on 2016/12/12.
 */

public class BGContentBehavior extends CoordinatorLayout.Behavior{
    private int mTabMaxTranslation,mHeightToolbar,mIconSizeStart,mBgWidthStart,mBgHeightStart,mWidth,mDistance;
    private float mFraction;
    private View mStatistics;
    public BGContentBehavior() {
    }

    public BGContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHeightToolbar = context.getResources().getDimensionPixelOffset(R.dimen.toolbar_height);
        mIconSizeStart = context.getResources().getDimensionPixelOffset(R.dimen.img_icon_height_start);

        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        mWidth=metrics.widthPixels;
        mBgWidthStart= (int) (mWidth*Constants.FRACTION_WIDTH_BGCONTENT);
        mBgHeightStart= (int) (mBgWidthStart*Constants.FRACTION_HEIGHT_BGCONTENT);

    }



    //测量宽高
    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        child.measure(View.MeasureSpec.makeMeasureSpec(mBgWidthStart, View.MeasureSpec.EXACTLY),View.MeasureSpec.makeMeasureSpec(mBgHeightStart, View.MeasureSpec.EXACTLY));
        return true;
    }

    //移动view到中间
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        parent.onLayoutChild(child,layoutDirection);
        int dependencyInitialTop = parent.findViewById(R.id.tab_layout).getTop();

        //当child上滑动到 toolbar 下面的时候,大小应该正好占满屏幕

        //计算此时 tab移动的比例
        float fraction = 1-(dependencyInitialTop-mIconSizeStart- mHeightToolbar) /1.0f/ (dependencyInitialTop - mHeightToolbar);

        //计算缩放要调整的比例
        mFraction=(mWidth/1.0f/mBgWidthStart-1)/fraction;


        mDistance=2*mHeightToolbar+mIconSizeStart/2;
        child.offsetTopAndBottom(mHeightToolbar+mIconSizeStart/2);
        child.offsetLeftAndRight(mWidth/2-mBgWidthStart/2);

        mStatistics=child.findViewById(R.id.lyt_statistics);

        //tab最大的translation的距离
        mTabMaxTranslation=parent.findViewById(R.id.tab_layout).getTop()-mHeightToolbar;
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof TabLayout;
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        //计算移动的比例 0~1
        float fraction = Math.abs(dependency.getTranslationY())/ mTabMaxTranslation;
        child.setScaleX(1 + (fraction*mFraction));

        //下面的文字不缩放..
        mStatistics.setScaleX(1/(1 + (fraction*mFraction)));

        child.setTranslationY(-fraction*mDistance);
        if(fraction<0.7f){
            mStatistics.setAlpha(1-fraction/0.7f);
        }else {
            mStatistics.setAlpha(0);
        }

        return true;
    }
}
