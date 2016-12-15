package cq.behaviordemo.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;

import cq.behaviordemo.R;

/**
 * Created by cqll on 2016/12/13.
 */

public class ScoreBehavior extends CoordinatorLayout.Behavior{
    private float mIconPadding,mIconSizeStart,mIconSizeEnd,mHeightToolbar,mChildWidth,mChildHeight,mWidth,mIconDistanceY,mIconDistanceX;
    private int mTabMaxTranslation;
    public ScoreBehavior() {
    }

    public ScoreBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHeightToolbar = context.getResources().getDimensionPixelOffset(R.dimen.toolbar_height);
        mIconSizeStart = context.getResources().getDimensionPixelOffset(R.dimen.img_icon_height_start);
        mIconSizeEnd = context.getResources().getDimensionPixelOffset(R.dimen.img_icon_height_end);
        mIconPadding = context.getResources().getDimensionPixelOffset(R.dimen.icon_padding);
        mWidth=context.getResources().getDisplayMetrics().widthPixels;

    }


    //移动到指定位置
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        parent.onLayoutChild(child,layoutDirection);
        mChildWidth=child.getWidth();
        mChildHeight=child.getHeight();
        //tab最大的translation的距离
        mTabMaxTranslation= (int) (parent.findViewById(R.id.tab_layout).getTop()-mHeightToolbar);

        mIconDistanceY = mIconSizeStart+mHeightToolbar-mHeightToolbar*3/4;
        mIconDistanceX = mWidth / 2 - parent.findViewById(R.id.img_back).getRight() - mIconSizeEnd - mIconPadding-mChildWidth/2;


        child.offsetTopAndBottom((int) (mHeightToolbar+mIconSizeStart-mChildHeight/2));
        child.offsetLeftAndRight((int) (mWidth/2-mChildWidth/2));
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

        child.setTranslationY(-mIconDistanceY * fraction);
        child.setTranslationX(-mIconDistanceX * fraction);
        return true;
    }
}
