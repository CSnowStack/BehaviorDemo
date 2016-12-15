package cq.behaviordemo.listener;

/**
 * Created by cqll on 2016/12/15.
 * 是否支持 展开
 */

public interface SupportNeedExpendListener {
    void setNeedExpendListener(NeedExpandListener listener);
    NeedExpandListener getNeedExpendListener();
}
