package cq.airbnb;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;

import cq.behaviordemo.R;

/**
 * 搜索所有的behavior
 */

public class SearchAllBehavior extends CoordinatorLayout.Behavior {

    /**
     * 改变行为的高度
     * translationY  由 mTranslationMin mTranslationMin+mChangeHeight
     */
    private int mChangeHeight, mTranslationMin, mTranslationMax;
    private int mMarginTop;

    /**
     * 需要gone的时候移动到屏幕外
     */
    private int mTranslationGone;

    private int mHeightUp;

    public SearchAllBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMarginTop = context.getResources().getDimensionPixelOffset(R.dimen.airbnb_padding_content);
        mHeightUp = context.getResources().getDimensionPixelOffset(R.dimen.airbnb_up_height);
        mTranslationMin = context.getResources().getDimensionPixelOffset(R.dimen.airbnb_translation_min);
        mTranslationMax = context.getResources().getDimensionPixelOffset(R.dimen.airbnb_translation_max);
        mChangeHeight = mHeightUp - mMarginTop;

    }

    @Override public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        if (mTranslationGone == 0)
            mTranslationGone = -child.getRight();
        return true;
    }

    /**
     * 依赖tabLayout
     */
    @Override public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof TabLayout;
    }

    @Override public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float translationY = dependency.getTranslationY();
        if (translationY < mTranslationMin) {//向上移动的时候跟随tab
            child.setTranslationX(0);
            child.setTranslationY(translationY - mTranslationMin);
        } else {//展开的时候,根据比例设置
            //tab 位移的比例
            float fraction = (translationY - mTranslationMin) / (mTranslationMax - mTranslationMin);
            //根据比例设置位移的距离
            if (fraction <= 1 / 3f) {
                child.setTranslationX(0);
                child.setTranslationY(fraction * mChangeHeight);
                child.setAlpha(1 - fraction * 3);

            } else {
                if (child.getTranslationX() != mTranslationGone) {
                    child.setTranslationX(mTranslationGone);
                    child.setAlpha(0);
                }

            }
        }
        return true;
    }
}

