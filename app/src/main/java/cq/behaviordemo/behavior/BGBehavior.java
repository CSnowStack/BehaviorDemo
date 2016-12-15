package cq.behaviordemo.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import cq.behaviordemo.Constants;
import cq.behaviordemo.R;

/**
 * Created by cqll on 2016/12/12.
 */

public class BGBehavior extends CoordinatorLayout.Behavior{

    private int mHeightToolbar,mIconSizeStart,mWidth,mStartTop;
    public BGBehavior() {
    }

    public BGBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHeightToolbar = context.getResources().getDimensionPixelOffset(R.dimen.toolbar_height);
        mIconSizeStart = context.getResources().getDimensionPixelOffset(R.dimen.img_icon_height_start);
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        mWidth=metrics.widthPixels;

        mStartTop=(int) (mHeightToolbar+mIconSizeStart/2+mWidth* Constants.FRACTION_WIDTH_BGCONTENT*Constants.FRACTION_HEIGHT_BGCONTENT/2f);
    }


    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        child.measure(parentWidthMeasureSpec, View.MeasureSpec.makeMeasureSpec(parent.findViewById(R.id.tab_layout).getTop()-mStartTop, View.MeasureSpec.EXACTLY));
        return true;
    }

    //向下滑到指定的地点
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        parent.onLayoutChild(child,layoutDirection);
        child.offsetTopAndBottom(mStartTop);
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof TabLayout;
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        child.setTranslationY((int) (dependency.getTranslationY()));
        return true;
    }
}
