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
 * Created by cqll on 2016/12/13.
 */

public class EditorBehavior extends CoordinatorLayout.Behavior{
    private float mIconSizeStart,mDistance,mWidth;
    private int  mHeightToolbar,mTabMaxTranslation,mChildWidth,mChildHeight,mBgContentHeight,mEditorPadding;
    public EditorBehavior() {
    }

    public EditorBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHeightToolbar = context.getResources().getDimensionPixelOffset(R.dimen.toolbar_height);
        mIconSizeStart = context.getResources().getDimensionPixelOffset(R.dimen.img_icon_height_start);
        mEditorPadding = context.getResources().getDimensionPixelOffset(R.dimen.editor_padding);
        mDistance=2*mHeightToolbar+mIconSizeStart/2;

        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        mWidth=metrics.widthPixels;
        mChildWidth= (int) (mWidth* Constants.FRACTION_WIDTH_BGCONTENT);
        mChildHeight= (int) (mChildWidth*Constants.FRACTION_HEIGHT_EDITOR);
        mBgContentHeight= (int) (mChildWidth*Constants.FRACTION_HEIGHT_BGCONTENT);
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        child.measure(View.MeasureSpec.makeMeasureSpec(mChildWidth, View.MeasureSpec.EXACTLY),View.MeasureSpec.makeMeasureSpec(mChildHeight, View.MeasureSpec.EXACTLY));
        return true;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        parent.onLayoutChild(child,layoutDirection);

        //tab最大的translation的距离
        mTabMaxTranslation=parent.findViewById(R.id.tab_layout).getTop()-mHeightToolbar;
        //留下预先设置好的padding
        child.offsetTopAndBottom((int) (mHeightToolbar+mIconSizeStart/2+mBgContentHeight+mEditorPadding));
        child.offsetLeftAndRight((int) (mWidth/2-mChildWidth/2));
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof TabLayout;
    }


    //跟 bgContent的移动一样,只不过是自身 alpha的变化
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        //计算移动的比例 0~1
        float fraction = Math.abs(dependency.getTranslationY())/ mTabMaxTranslation;;
        child.setTranslationY(-fraction*mDistance);

        if(fraction<0.7f){
            child.setAlpha(1-fraction/0.7f);
        }else {
            child.setAlpha(0);
        }

        return true;

    }
}
