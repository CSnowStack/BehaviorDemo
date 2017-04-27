package cq.airbnb;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import cq.behaviordemo.Constants;
import cq.behaviordemo.R;
import cq.behaviordemo.listener.IsChildRequestScrollListener;
import cq.behaviordemo.listener.NeedExpandListener;
import cq.behaviordemo.listener.SupportNeedExpendListener;

/**
 * 控制tab
 */

public class TabBehavior extends CoordinatorLayout.Behavior implements NeedExpandListener {
    /**
     * 向下滑动的最大距离
     */
    private int mTranslationMax;

    /**
     * 最小的距离
     */
    private int mTranslationMin;

    private int mHeightChild;
    /**
     * 向上滑动,还是向下滑动
     */
    private boolean mUp;
    /**
     * 手指控制的滑动
     */
    private boolean mControlChange;

    /**
     * mValueAnimatorStyle 改变样式
     */
    private ValueAnimator mValueAnimator;
    private ViewPager mViewPager;
    private TabLayout mTab;

    /**
     * 标记是否是需要改变样式
     * 1 头部只显示tab的时候 切换一次
     * 2 头部的显示超出 tab和 all的时候切换一次
     */
    private boolean mWhiteStyle = false;
    private TextView mTxtAll;
    private View mLytContent, mLytAll;
    private Context mContext;
    private int mWhite = 0xFFFFFFFF;
    private int mGreen = 0xFF128488;

    public TabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mContext = context;
        mTranslationMax = context.getResources().getDimensionPixelOffset(R.dimen.airbnb_translation_max);
        mTranslationMin = context.getResources().getDimensionPixelOffset(R.dimen.airbnb_translation_min);
        mHeightChild=context.getResources().getDimensionPixelOffset(R.dimen.tab_height);

    }

    /**
     * 移动初始位置到 all 的下面
     */
    @Override public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        mTab = (TabLayout) child;
        mViewPager = (ViewPager) parent.findViewById(R.id.viewpager);
        mLytAll = parent.findViewById(R.id.lyt_all);
        mTxtAll = (TextView) mLytAll.findViewById(R.id.txt_all);
        mLytContent = parent.findViewById(R.id.content);

        return true;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        mControlChange = true;

        //传个接口,需要展开的时候回调
        PagerAdapter adapter = mViewPager.getAdapter();
        if (adapter != null && //有适配器
                adapter.getCount() > 0 &&//有item
                adapter instanceof SupportNeedExpendListener &&
                ((SupportNeedExpendListener) adapter).getNeedExpendListener() == null) {
            ((SupportNeedExpendListener) adapter).setNeedExpendListener(this);
        }
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }


    /**
     * @param dy 向上滑大于0
     */
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        mUp = dy > 0;
        if (mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }

        //如果list需要滑动 且 不是(向上滑&&tab不在顶部)
        if (isChildRequestScroll(child.getTranslationY())) {
            consumed[1] = 0;
            return;
        }

        consumed[1] = dy;//全部消耗
        int distance = -dy / 2;//降低移动的速度


        if (child.getTranslationY() + distance > mTranslationMax) {//大于最大距离
            distance = mTranslationMax;
        } else if (child.getTranslationY() + distance < 0) {//到顶部
            distance = 0;
        } else {//正常
            distance = (int) (child.getTranslationY() + distance);
        }

        //判断应该显示的样式
        if (mUp && distance < (mTranslationMin - mHeightChild / 2) && !mWhiteStyle) {
            setWhiteStyle();
        } else if (!mUp && distance > (mTranslationMin + mHeightChild / 2) && mWhiteStyle) {
            setGreenStyle();
        }
        child.setTranslationY(distance);
    }

    private void setGreenStyle() {
        mWhiteStyle=false;
        mTab.setTabTextColors(ContextCompat.getColor(mContext, android.R.color.darker_gray), mWhite);
        mLytAll.setBackgroundResource(R.drawable.bg_airbnb_condition);
        mTxtAll.setTextColor(mWhite);
        mTab.setSelectedTabIndicatorColor(mWhite);

        mLytContent.setBackgroundResource(R.color.airbnb_bg);

    }

    private void setWhiteStyle() {
        mWhiteStyle=true;
        mTab.setTabTextColors(ContextCompat.getColor(mContext, android.R.color.darker_gray), mGreen);
        mLytAll.setBackgroundResource(R.drawable.bg_airbnb_condition_gray);
        mTxtAll.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
        mTab.setSelectedTabIndicatorColor(mGreen);

        mLytContent.setBackgroundResource(android.R.color.white);
    }


    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        mControlChange = false;
        float translationY = child.getTranslationY();
        if (Math.abs(translationY) == mTranslationMax || translationY == mTranslationMin || translationY == 0) {
            return;
        }

        scroll(child, translationY);

    }


    /**
     * list 不需要滑动就拦截.需要就不拦截
     */

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        if (velocityY < -1000 && child.getTranslationY() == 0) {//向下滑的速度小于负1000
            mControlChange = false;
            showSearchAll();
        }

        return !isChildRequestScroll(child.getTranslationY());
    }

    /**
     * 三种情况
     * 1 在顶部
     * 2 在all的下面
     * 3 在condition的下面
     */
    private void scroll(final View child, final float translationY) {
        final float shouldMoveDistance;
        if (translationY < mHeightChild / 2) {//这段去最上面
            shouldMoveDistance = -translationY;
        } else if ((translationY > mHeightChild / 2 && translationY < (mTranslationMin + mHeightChild / 2)) ||
                (mUp && translationY < (mTranslationMax - mHeightChild))) {//回到中间的点
            shouldMoveDistance = mTranslationMin - translationY;
        } else {//去最下面
            shouldMoveDistance = mTranslationMax - translationY;
        }


        mValueAnimator.setDuration((long) (Math.abs(shouldMoveDistance) / mTranslationMax * Constants.DURATION_SCROLL));
        mValueAnimator.removeAllUpdateListeners();
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                child.setTranslationY(translationY + animation.getAnimatedFraction() * shouldMoveDistance);
            }
        });
        mValueAnimator.start();
    }


    /**
     * Child是否需要滑动
     */
    private boolean isChildRequestScroll(float translationY) {
        PagerAdapter adapter = mViewPager.getAdapter();
        return ((translationY == 0 || (!mUp && translationY == mTranslationMin))/*在顶部,或者向下滑且在初始位置*/ &&
                adapter != null && //有适配器
                adapter.getCount() > 0 &&//有item
                adapter instanceof IsChildRequestScrollListener && //实现了
                ((IsChildRequestScrollListener) adapter).requestScroll(mUp)//需要滑动
        );
    }


    /**
     * 显示搜索全部
     */
    public void showSearchAll() {
        if (!mControlChange && mTab.getTranslationY() == -0 && !mValueAnimator.isRunning()) {
            mValueAnimator.setDuration(500);
            final float startTranslation = mTab.getTranslationY();
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mTab.setTranslationY(startTranslation + animation.getAnimatedFraction() * (mTranslationMin - startTranslation));
                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mTab.setTranslationY(mTranslationMin);
                    mValueAnimator.removeAllListeners();
                    mValueAnimator.removeAllUpdateListeners();
                }
            });
            mValueAnimator.start();
        }
    }

    /**
     * list fling到头的时候 展开
     */
    @Override
    public void needExpand() {
        if (!mControlChange && mTab.getTranslationY() != mTranslationMax && !mValueAnimator.isRunning()) {
            mValueAnimator.setDuration(500);
            final float startTranslation = mTab.getTranslationY();

            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int translation = (int) (startTranslation + animation.getAnimatedFraction() * (mTranslationMax - startTranslation));
                    mTab.setTranslationY(translation);
                    if (mWhiteStyle && translation > (mTranslationMin + mHeightChild / 2)) {
                        mWhiteStyle = false;
                        setGreenStyle();
                    }

                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mTab.setTranslationY(mTranslationMax);
                    mValueAnimator.removeAllListeners();
                    mValueAnimator.removeAllUpdateListeners();
                }
            });
            mValueAnimator.start();
        }
    }


    /**
     * 隐藏搜索的详细条件
     */
    public void hideItem() {
        if (!mValueAnimator.isRunning()) {

            mValueAnimator.setDuration(500);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float translation = mTranslationMin + (1 - animation.getAnimatedFraction()) * (mTranslationMax - mTranslationMin);
                    mTab.setTranslationY(translation);
                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mTab.setTranslationY(mTranslationMin);
                    mValueAnimator.removeAllListeners();
                    mValueAnimator.removeAllUpdateListeners();
                }
            });
            mValueAnimator.start();
        }
    }

    @Override public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        mValueAnimator.cancel();
    }
}
