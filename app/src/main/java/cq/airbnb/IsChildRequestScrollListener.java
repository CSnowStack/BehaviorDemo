package cq.airbnb;

/**
 * 判断子view是否需要滑动
 */

public interface IsChildRequestScrollListener {
    boolean requestScroll(boolean up,boolean shouldNotRefresh);
}
