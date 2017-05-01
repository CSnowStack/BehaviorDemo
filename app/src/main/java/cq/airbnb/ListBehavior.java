package cq.airbnb;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 控制列表的移动
 */

public class ListBehavior extends CoordinatorLayout.Behavior {

    final Rect mTempRect1 = new Rect();
    final Rect mTempRect2 = new Rect();

    public ListBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 计算宽高
     */
    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, View child,
                                  int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec,
                                  int heightUsed) {
        final int childLpHeight = child.getLayoutParams().height;
        if (childLpHeight == ViewGroup.LayoutParams.MATCH_PARENT
                || childLpHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            // If the menu's height is set to match_parent/wrap_content then measure it
            // with the maximum visible height

            final List<View> dependencies = parent.getDependencies(child);
            final View header = findFirstDependency(dependencies);
            if (header != null) {
                if (ViewCompat.getFitsSystemWindows(header)
                        && !ViewCompat.getFitsSystemWindows(child)) {
                    // If the header is fitting system windows then we need to also,
                    // otherwise we'll get CoL's compatible measuring
                    ViewCompat.setFitsSystemWindows(child, true);

                    if (ViewCompat.getFitsSystemWindows(child)) {
                        // If the set succeeded, trigger a new layout and return true
                        child.requestLayout();
                        return true;
                    }
                }

                //可用的高度
                int availableHeight = View.MeasureSpec.getSize(parentHeightMeasureSpec);
                if (availableHeight == 0) {
                    // If the measure spec doesn't specify a size, use the current height
                    availableHeight = parent.getHeight();
                }

                //列表高度为=可用高度-依赖的view的高度
                final int height = availableHeight - header.getMeasuredHeight();
                final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height,
                        childLpHeight == ViewGroup.LayoutParams.MATCH_PARENT
                                ? View.MeasureSpec.EXACTLY
                                : View.MeasureSpec.AT_MOST);

                // Now measure the scrolling view with the correct height
                parent.onMeasureChild(child, parentWidthMeasureSpec,
                        widthUsed, heightMeasureSpec, heightUsed);

                return true;
            }
        }
        return false;
    }

    @Override public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        final List<View> dependencies = parent.getDependencies(child);
        final View header = findFirstDependency(dependencies);

        if (header != null) {
            final CoordinatorLayout.LayoutParams lp =
                    (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            final Rect available = mTempRect1;
            //列表可用的 layoutParams . bottom 是为了 列表的高度为parent的可用高度
            available.set(parent.getPaddingLeft() + lp.leftMargin,
                    header.getBottom() + lp.topMargin,
                    parent.getWidth() - parent.getPaddingRight() - lp.rightMargin,
                    parent.getHeight() + header.getBottom()
                            - parent.getPaddingBottom() - lp.bottomMargin);

            final Rect out = mTempRect2;
            //设置gravity
            GravityCompat.apply(resolveGravity(lp.gravity), child.getMeasuredWidth(),
                    child.getMeasuredHeight(), available, out, layoutDirection);

            child.layout(out.left, out.top, out.right, out.bottom);
        } else {
            // If we don't have a dependency, let super handle it
            super.onLayoutChild(parent, child, layoutDirection);
        }
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return isDependOn(dependency);
    }

    private View findFirstDependency(List<View> views) {
        for (View v : views) {
            if (isDependOn(v)) {
                return v;
            }
        }
        return null;
    }

    private boolean isDependOn(View dependency) {
        return dependency instanceof TabLayout;
    }

    private static int resolveGravity(int gravity) {
        return gravity == Gravity.NO_GRAVITY ? GravityCompat.START | Gravity.TOP : gravity;
    }


    //跟随tab移动
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        child.setTranslationY(dependency.getTranslationY());
        return true;
    }
}
