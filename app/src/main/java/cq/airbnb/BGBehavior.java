package cq.airbnb;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;

import cq.behaviordemo.R;

/**
 * 背景的behavior
 */

public class BGBehavior extends CoordinatorLayout.Behavior {

    private  int mTranslationMax;
    public BGBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTranslationMax=context.getResources().getDimensionPixelOffset(R.dimen.airbnb_translation_max);
    }


    @Override public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof TabLayout;
    }

    @Override public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        child.setTranslationY(dependency.getTranslationY()-mTranslationMax);

        return true;
    }
}
