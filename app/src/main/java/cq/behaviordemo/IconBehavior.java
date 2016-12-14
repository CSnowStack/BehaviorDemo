package cq.behaviordemo;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cqll on 2016/12/8.
 * 头像
 */

public class IconBehavior extends CoordinatorLayout.Behavior {

    //依赖的view的距离顶部的初始距离,被依赖的view会先绘制
    private float mIconStartLeft,
            mIconSizeStart, mIconSizeEnd, mIconDistanceX, mIconDistanceY, mHeightToolbar;
    private int mTabMaxTranslation;

    public IconBehavior() {

    }

    public IconBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHeightToolbar = context.getResources().getDimensionPixelOffset(R.dimen.toolbar_height);
        mIconSizeStart = context.getResources().getDimensionPixelOffset(R.dimen.img_icon_height_start);
        mIconSizeEnd = context.getResources().getDimensionPixelOffset(R.dimen.img_icon_height_end);

    }

    /**
     * 绘制完成后应该移动到toolbar下面,水平方向居中
     */
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);

        //tab最大的translation的距离
        mTabMaxTranslation = (int) (parent.findViewById(R.id.tab_layout).getTop() - mHeightToolbar);

        float mWidth = parent.findViewById(R.id.toolbar).getWidth();

        //头像开始时距离左边的距离
        mIconStartLeft = mWidth / 2 - mIconSizeStart / 2;

        //用头像的圆点计算
        mIconDistanceY = mHeightToolbar + mIconSizeStart / 2 - mHeightToolbar / 2;
        mIconDistanceX = mWidth / 2 - parent.findViewById(R.id.img_back).getRight() - mIconSizeEnd / 2;
        //移动到 toolbar下面
        child.offsetTopAndBottom((int) mHeightToolbar);
        child.offsetLeftAndRight((int) mIconStartLeft);
        return true;
    }

    //跟随 vp
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof TabLayout;
    }


    //tab向上向下移动是改变头像
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        //计算移动的比例 0~1
        float fraction = Math.abs(dependency.getTranslationY()) / mTabMaxTranslation;

        //负的向上
        child.setTranslationY(-mIconDistanceY * fraction);
        child.setTranslationX(-mIconDistanceX * fraction);

        fraction = 1 - (1 - mIconSizeEnd / mIconSizeStart) * fraction;
        //缩放
        child.setScaleX(fraction);
        child.setScaleY(fraction);

        return true;
    }
}
