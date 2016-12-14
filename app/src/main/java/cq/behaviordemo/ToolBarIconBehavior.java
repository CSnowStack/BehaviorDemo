package cq.behaviordemo;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by cqll on 2016/12/13.
 */

public class ToolBarIconBehavior extends CoordinatorLayout.Behavior {
    private float mHeightToolbar, mDependencyMaxTranslation, mIconSizeStart;
    private ImageView mImgBack, mImgShare;

    public ToolBarIconBehavior() {
    }

    public ToolBarIconBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHeightToolbar = context.getResources().getDimensionPixelOffset(R.dimen.toolbar_height);
        mIconSizeStart = context.getResources().getDimensionPixelOffset(R.dimen.img_icon_height_start);

    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        mImgBack = (ImageView) parent.findViewById(R.id.img_back);
        mImgShare = (ImageView) parent.findViewById(R.id.img_share);

        mDependencyMaxTranslation = 2*mHeightToolbar+mIconSizeStart/2;
        return true;
    }


    //依赖白色的内容背景
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency.getId() == R.id.fyt_content;
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float fraction =  Math.abs(dependency.getTranslationY()) / mDependencyMaxTranslation;

        if (fraction < 0.2) {//用于白色变化
            fraction = 1 - fraction / 0.2f;// 从1到0
            setWhiteIcon();

        } else {//用于黑色变化
            fraction = (fraction - 0.2f) / 0.8f;// 从0到1
            setBlackIcon();
        }
        mImgBack.setAlpha(fraction);
        mImgShare.setAlpha(fraction);

        return true;

    }

    private void setWhiteIcon() {
        mImgBack.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        mImgShare.setImageResource(R.drawable.ic_share_white_24dp);
    }

    private void setBlackIcon() {
        mImgBack.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        mImgShare.setImageResource(R.drawable.ic_share_black_24dp);
    }



}
