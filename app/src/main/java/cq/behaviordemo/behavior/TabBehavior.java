package cq.behaviordemo.behavior;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import cq.behaviordemo.Constants;
import cq.behaviordemo.IsChildRequestScrollListener;
import cq.behaviordemo.R;

/**
 * Created by cqll on 2016/12/9.
 *
 */

public class TabBehavior extends CoordinatorLayout.Behavior {
    private int mHeightToolbar, mMaxDistance,mEditorPadding;
    private Context mContext;
    private boolean mUp/*向上滑动,还是向下滑动*/;
    private ValueAnimator mValueAnimator;
    private ViewPager mViewPager;
    public TabBehavior() {
    }

    public TabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        mHeightToolbar =context.getResources().getDimensionPixelOffset(R.dimen.toolbar_height);
        mEditorPadding =context.getResources().getDimensionPixelOffset(R.dimen.editor_padding);

        mValueAnimator=ValueAnimator.ofFloat(0,1);
    }


    /**
     * 计算tab应该出现的位置
     * toolbar 高度
     * icon 高度 /2
     * child.getWidth()*0.9f*0.64f 白色填充的当前高度
     * child.getWidth()*0.9f*0.64/16 间隙的高度
     */
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        mViewPager= (ViewPager) parent.findViewById(R.id.viewpager);
        mMaxDistance = (int) (child.getWidth() * Constants.FRACTION_WIDTH_BGCONTENT * 0.64f  + mContext.getResources().getDimensionPixelOffset(R.dimen.img_icon_height_start) / 2 + child.getWidth() * Constants.FRACTION_PADDING+mEditorPadding);
        child.offsetTopAndBottom(mMaxDistance+mHeightToolbar);
        return true;
    }


    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0&&//垂直
                //item不需要滑动
                !(child.getTranslationY()==-mMaxDistance&&//在顶部
                        mViewPager.getAdapter()!=null&& //有适配器
                        mViewPager.getAdapter().getCount()>0&&//有item
                        mViewPager.getAdapter() instanceof IsChildRequestScrollListener && //实现了
                        ((IsChildRequestScrollListener)mViewPager.getAdapter()).requestScroll()//需要滑动
                );
    }


    /**
     *
     * @param dy 向上滑大于0
     */
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        //只要开始拦截，就需要把所有Scroll事件消费掉
        consumed[1]=dy;
        int distance=-dy/2;//降低移动的速度
        mUp=dy>0;

        if(child.getTranslationY()+distance<-mMaxDistance){
            distance=-mMaxDistance;
        }else if(child.getTranslationY()+distance>0){
            distance=0;
        }else {
          distance= (int) (child.getTranslationY()+distance);
        }
        child.setTranslationY(distance);
    }


    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        float translationY=child.getTranslationY();

        if(Math.abs(translationY)==mMaxDistance||translationY==0){
            return;
        }

        scroll(child,translationY);

    }

    private void scroll(final View child, final float translationY) {
        final float shouldMoveDistance;
        if(mUp){
            //没超过 1/8
            if(Math.abs(translationY)<mMaxDistance/8f){
                shouldMoveDistance=Math.abs(translationY);
            }else {
                shouldMoveDistance=Math.abs(translationY)-mMaxDistance;
            }
        }else {//向下滑动
            //没超过 1/8
            if(Math.abs(translationY)>mMaxDistance*7f/8f){
                shouldMoveDistance=-(mMaxDistance+translationY);
            }else {
                shouldMoveDistance=Math.abs(translationY);
            }
        }

        mValueAnimator.setDuration((long) (Math.abs(shouldMoveDistance)/mMaxDistance*Constants.DURATION_SCROLL));
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                child.setTranslationY(translationY+animation.getAnimatedFraction()*shouldMoveDistance);
            }
        });
        mValueAnimator.start();
    }
}
