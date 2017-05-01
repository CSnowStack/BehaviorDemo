package cq.airbnb;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;

import cq.behaviordemo.R;

/**
 * 向上的图片的 behavior
 */

public class UpIconBehavior extends CoordinatorLayout.Behavior {
    private int mChangeHeight, mTranslationMin, mTranslationMax;


    /**
     * 需要gone的时候移动到屏幕外
     */
    private int mTranslationGone;

    public UpIconBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTranslationMin = context.getResources().getDimensionPixelOffset(R.dimen.airbnb_translation_min);

        mTranslationMax = context.getResources().getDimensionPixelOffset(R.dimen.airbnb_translation_max);
        mChangeHeight = context.getResources().getDimensionPixelOffset(R.dimen.airbnb_up_height) / 4;
    }

    @Override public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        if (mTranslationGone == 0)
            mTranslationGone = -child.getRight();
        return true;
    }

    @Override public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof TabLayout;
    }

    @Override public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float translationY = dependency.getTranslationY();
        float fraction = (translationY - mTranslationMin) / (mTranslationMax - mTranslationMin);
        //根据比例设置位移的距离
        if (fraction >= 1 / 3f) {
            child.setTranslationX(0);

            float fractionItem = (fraction * 3 - 1) / 2;
            child.setTranslationY(-(1 - fractionItem) * mChangeHeight);
            child.setAlpha(fractionItem);
        } else if (child.getTranslationX() != mTranslationGone) {
            child.setTranslationX(mTranslationGone);
            child.setAlpha(0);

        }

        return true;
    }
}
