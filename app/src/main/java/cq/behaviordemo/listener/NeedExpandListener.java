package cq.behaviordemo.listener;

/**
 * Created by cqll on 2016/12/15.
 * list可以滑动的时候,向下滑,然后松开手指,list滑到顶的时候触发
 * 通知 tab 展开头部
 */

public interface NeedExpandListener {
    void needExpand();
}
