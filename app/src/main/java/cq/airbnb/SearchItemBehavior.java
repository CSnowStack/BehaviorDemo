package cq.airbnb;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;

import cq.behaviordemo.R;

/**
 * 搜索的分类的view的behavior
 */

public class SearchItemBehavior extends CoordinatorLayout.Behavior {

    /**
     * 最大位移距离 mChangeHeight
     * translationY  由 mTranslationMin 移动到mTranslationMax
     */
    private int mChangeHeight, mTranslationMin, mTranslationMax;
    private int mMarginTop;

    /**
     * 下面的三个item
     */
    private View mItemWhere, mItemWhen, mItemWho;

    /**
     * 需要gone的时候移动到屏幕外
     */
    private int mTranslationGone;
    private int mHeightUp;

    public SearchItemBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMarginTop = context.getResources().getDimensionPixelOffset(R.dimen.airbnb_padding_content);
        mHeightUp = context.getResources().getDimensionPixelOffset(R.dimen.airbnb_up_height);

        mTranslationMin =context.getResources().getDimensionPixelOffset(R.dimen.airbnb_translation_min);
        mTranslationMax =context.getResources().getDimensionPixelOffset(R.dimen.airbnb_translation_max);
    }

    @Override public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        mChangeHeight = mHeightUp - mMarginTop;

        mItemWhere = child.findViewById(R.id.lyt_where);
        mItemWhen = child.findViewById(R.id.lyt_when);
        mItemWho = child.findViewById(R.id.lyt_who);

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
        if (translationY >= mTranslationMin) {
            if (child.getTranslationX() != 0) {
                child.setTranslationX(0);
                child.setAlpha(1);
            }

            //tab 位移的比例
            float fraction = (translationY - mTranslationMin) / (mTranslationMax - mTranslationMin);
            //根据比例设置位移的距离
            child.setTranslationY(fraction * mChangeHeight);

            //根据比例设置透明度
            if (fraction < 1 / 3f) {
                if (mItemWhere.getTranslationX() != 0) {
                    mItemWhere.setTranslationX(0);
                }
                if (mItemWhen.getTranslationX() != mTranslationGone) {
                    mItemWhen.setTranslationX(mTranslationGone);

                }
                if (mItemWho.getTranslationX() != mTranslationGone) {
                    mItemWho.setTranslationX(mTranslationGone);
                }


                mItemWhere.setAlpha(fraction * 3);
                mItemWhen.setAlpha(0);
                mItemWho.setAlpha(0);
            } else if (fraction < 2 / 3f) {
                if (mItemWhere.getTranslationX() != 0) {
                    mItemWhere.setTranslationX(0);
                }
                if (mItemWhen.getTranslationX() != 0) {
                    mItemWhen.setTranslationX(0);

                }
                if (mItemWho.getTranslationX() != mTranslationGone) {
                    mItemWho.setTranslationX(mTranslationGone);
                }

                mItemWhere.setAlpha(1);
                mItemWhen.setAlpha(fraction * 3 - 1);
                mItemWho.setAlpha(0);
            } else {
                if (mItemWhere.getTranslationX() != 0) {
                    mItemWhere.setTranslationX(0);
                }
                if (mItemWhen.getTranslationX() != 0) {
                    mItemWhen.setTranslationX(0);

                }
                if (mItemWho.getTranslationX() != 0) {
                    mItemWho.setTranslationX(0);
                }

                mItemWhere.setAlpha(1);
                mItemWhen.setAlpha(1);
                mItemWho.setAlpha(fraction * 3 - 2);
            }
        } else {
            if (child.getTranslationX() != mTranslationGone) {
                child.setTranslationX(mTranslationGone);
                child.setAlpha(0);
            }

        }
        return true;

    }
}

