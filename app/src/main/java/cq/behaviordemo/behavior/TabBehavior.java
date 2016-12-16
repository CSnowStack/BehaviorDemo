package cq.behaviordemo.behavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cq.behaviordemo.Constants;
import cq.behaviordemo.R;
import cq.behaviordemo.listener.IsChildRequestScrollListener;
import cq.behaviordemo.listener.NeedExpandListener;
import cq.behaviordemo.listener.SupportNeedExpendListener;

/**
 * Created by cqll on 2016/12/9.
 */

public class TabBehavior extends CoordinatorLayout.Behavior implements NeedExpandListener {
    private int mHeightToolbar, mMaxDistance, mEditorPadding;
    private Context mContext;
    private boolean mUp/*向上滑动,还是向下滑动*/,mControlChange/*手指控制的滑动*/;
    private ValueAnimator mValueAnimator;
    private ViewPager mViewPager;
    private View mTab;
    private List<View> mHardwareViews;
    public TabBehavior() {
    }

    public TabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mHeightToolbar = context.getResources().getDimensionPixelOffset(R.dimen.toolbar_height);
        mEditorPadding = context.getResources().getDimensionPixelOffset(R.dimen.editor_padding);

        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mHardwareViews=new ArrayList<>();
    }


    /**
     * 计算tab应该出现的位置
     * toolbar 高度
     * icon 高度 /2
     * child.getWidth()*0.9f*0.64f 白色填充的当前高度
     * child.getWidth()*0.9f*0.64/16 间隙的高度
     */
    @Override
    public boolean onLayoutChild(final CoordinatorLayout parent, final View child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        mTab=child;
        mViewPager = (ViewPager) parent.findViewById(R.id.viewpager);
        mMaxDistance = (int) (child.getWidth() * Constants.FRACTION_WIDTH_BGCONTENT * 0.64f + mContext.getResources().getDimensionPixelOffset(R.dimen.img_icon_height_start) / 2 + child.getWidth() * Constants.FRACTION_PADDING + mEditorPadding);
        child.offsetTopAndBottom(mMaxDistance + mHeightToolbar);
        if(mHardwareViews.size()==0){
            mHardwareViews.add(parent.findViewById(R.id.txt_name));
            mHardwareViews.add(parent.findViewById(R.id.img_icon));
            mHardwareViews.add(parent.findViewById(R.id.lyt_score));
            mHardwareViews.add(parent.findViewById(R.id.tab_layout));
            mHardwareViews.add(parent.findViewById(R.id.bg));
            mHardwareViews.add(parent.findViewById(R.id.lyt_editor));
            mHardwareViews.add(parent.findViewById(R.id.lyt_statistics));

            //开启硬件离屏缓存
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    for(View v:mHardwareViews){
                        v.setLayerType(View.LAYER_TYPE_NONE,null);
                    }
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    for(View v:mHardwareViews){
                        v.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                    }
                }
            });
        }



        return true;
    }


    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        mControlChange=true;

        if(mViewPager.getAdapter() != null && //有适配器
                mViewPager.getAdapter().getCount() > 0 &&//有item
                mViewPager.getAdapter() instanceof SupportNeedExpendListener&&
               ((SupportNeedExpendListener) mViewPager.getAdapter()).getNeedExpendListener()==null){
           ((SupportNeedExpendListener) mViewPager.getAdapter()).setNeedExpendListener(this);
       }
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }


    /**
     * @param dy 向上滑大于0
     */
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        mUp = dy > 0;
        if(isChildRequestScroll(child.getTranslationY())){//如果list需要滑动这边就不动
            consumed[1]=0;
            return;
        }
        consumed[1]=dy;//全部消耗
        int distance = -dy / 2;//降低移动的速度


        if (child.getTranslationY() + distance < -mMaxDistance) {
            distance = -mMaxDistance;
        } else if (child.getTranslationY() + distance > 0) {
            distance = 0;
        } else {
            distance = (int) (child.getTranslationY() + distance);
        }
        child.setTranslationY(distance);
    }


    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        mControlChange=false;
        float translationY = child.getTranslationY();
        if (Math.abs(translationY) == mMaxDistance || translationY == 0) {
            return;
        }

        scroll(child, translationY);

    }


    /**
     *  list 不需要滑动就拦截.需要就不拦截
     */

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        return !isChildRequestScroll(child.getTranslationY());
    }

    private void scroll(final View child, final float translationY) {
        final float shouldMoveDistance;
        if (mUp) {
            //没超过 1/8
            if (Math.abs(translationY) < mMaxDistance / 8f) {
                shouldMoveDistance = Math.abs(translationY);
            } else {
                shouldMoveDistance = Math.abs(translationY) - mMaxDistance;
            }
        } else {//向下滑动
//            没超过 1/8
            if (Math.abs(translationY) > mMaxDistance * 7f / 8f) {
                shouldMoveDistance = -(mMaxDistance + translationY);
            } else {
                shouldMoveDistance = Math.abs(translationY);
            }
        }

        mValueAnimator.setDuration((long) (Math.abs(shouldMoveDistance) / mMaxDistance * Constants.DURATION_SCROLL));
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
    private boolean isChildRequestScroll(float  translationY) {
        return (translationY == -mMaxDistance &&//在顶部
                        mViewPager.getAdapter() != null && //有适配器
                        mViewPager.getAdapter().getCount() > 0 &&//有item
                        mViewPager.getAdapter() instanceof IsChildRequestScrollListener && //实现了
                        ((IsChildRequestScrollListener) mViewPager.getAdapter()).requestScroll(mUp)//需要滑动
                );
    }


    /**
     * list fling到头的时候 展开
     */
    @Override
    public void needExpand() {
        if(!mControlChange){
            mValueAnimator.setDuration(500);
            mValueAnimator.removeAllUpdateListeners();
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mTab.setTranslationY((animation.getAnimatedFraction()-1)*mMaxDistance);
                }
            });
            mValueAnimator.start();
        }
    }
}
