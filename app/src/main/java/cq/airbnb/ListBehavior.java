package cq.airbnb;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import cq.behaviordemo.R;
import cq.behaviordemo.behavior.HeaderScrollingViewBehavior;

/**
 * 控制列表的移动
 */

public class ListBehavior extends HeaderScrollingViewBehavior {
    private int mHeightToolbar;
    public ListBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHeightToolbar=context.getResources().getDimensionPixelOffset(R.dimen.toolbar_height);
    }


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return isDependOn(dependency);
    }


    @Override public View findFirstDependency(List<View> views) {
        for (View view : views) {
            if (isDependOn(view))
                return view;
        }
        return null;
    }

    @Override protected int getScrollRange(View v) {
        if (isDependOn(v)) {
            return 0;
        } else {
            return super.getScrollRange(v);
        }
    }

    private boolean isDependOn(View dependency) {
        return dependency instanceof TabLayout;
    }

    //跟随tab移动
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        child.setTranslationY(dependency.getTranslationY());
        return true;
    }
}
