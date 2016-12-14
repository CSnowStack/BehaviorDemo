package cq.behaviordemo;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cqll on 2016/12/13.
 */

public class NameBehavior extends CoordinatorLayout.Behavior{
    private float mPaddingIcon, mPaddingScore,mIconSizeStart,mIconSizeEnd,mHeightToolbar,mChildWidth,mChildHeight,mWidth,mIconDistanceY,mIconDistanceX;
    private float mFractionScale=0.1f;//缩小的比例
    private int mTabMaxTranslation,mEditorPadding;
    public NameBehavior() {
    }

    public NameBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHeightToolbar = context.getResources().getDimensionPixelOffset(R.dimen.toolbar_height);
        mIconSizeStart = context.getResources().getDimensionPixelOffset(R.dimen.img_icon_height_start);
        mIconSizeEnd = context.getResources().getDimensionPixelOffset(R.dimen.img_icon_height_end);
        mPaddingIcon = context.getResources().getDimensionPixelOffset(R.dimen.icon_padding);
        mPaddingScore = context.getResources().getDimensionPixelOffset(R.dimen.score_padding);
        mWidth=context.getResources().getDisplayMetrics().widthPixels;
        mEditorPadding =context.getResources().getDimensionPixelOffset(R.dimen.editor_padding);
    }


    //移动到指定位置
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        parent.onLayoutChild(child,layoutDirection);
        mChildWidth=child.getWidth();
        mChildHeight=child.getHeight();

        int scoreHeight=parent.findViewById(R.id.lyt_score).getHeight();
        mIconDistanceY =mChildHeight/2+ scoreHeight/2+ mPaddingScore +mIconSizeStart+mHeightToolbar-mHeightToolbar/4;
        mIconDistanceX = mWidth / 2 - parent.findViewById(R.id.img_back).getRight() - mIconSizeEnd - mPaddingIcon -mChildWidth/2;

        //tab最大的translation的距离
        mTabMaxTranslation= (int) (parent.findViewById(R.id.tab_layout).getTop()-mHeightToolbar-mEditorPadding);

        child.offsetTopAndBottom((int) (mHeightToolbar+mIconSizeStart+scoreHeight/2+ mPaddingScore ));
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

        //缩小
        child.setPivotX(0);
        child.setPivotY(child.getHeight()/2);
        child.setScaleX(1-fraction*mFractionScale);
        child.setScaleY(1-fraction*mFractionScale);

        child.setTranslationY(-mIconDistanceY * fraction);
        child.setTranslationX(-mIconDistanceX * fraction);
        return true;
    }
}
