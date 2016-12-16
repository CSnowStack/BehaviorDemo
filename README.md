## Behavior 实现的漂亮的效果

### 简介

[项目地址](https://github.com/CSnowStack/BehaviorDemo)

[下载体验](https://github.com/CSnowStack/BehaviorDemo/blob/master/img/Behavior.apk)

#### 建议
请先阅读[这篇文章](http://www.jianshu.com/p/f7989a2a3ec2)
> 好多东西抄自这里

#### 效果来源

[这里是地址](https://material.uplabs.com/posts/profile-4f03fc6b-1a82-42ab-8a3e-f50dcbc10253)


![原图](https://github.com/CSnowStack/BehaviorDemo/blob/master/img/preview.gif)

#### 我的实现

![实现的效果](https://github.com/CSnowStack/BehaviorDemo/blob/master/img/c.gif)




> 一些细节没有实现 ,见谅,录制的gif效果也不太好 :-(


### 实现

#### 依赖关系
 `Tab` 监听 `onNestedPreScroll`来进行滑动,`Toolbar` 依赖 `FytContent`,其余的依赖 `Tab`

#### 变化
- `Tab` 的移动是手指滑动距离的 1/2 ,会根据停下来的位置判断是应该回到原位置还是下一个状态并进行移动

- `VP` 跟随 `Tab`, `HeaderScrollingViewBehavior`什么的请看建议,移动则没什么难度

- `BGContent` 跟随 `Tab`,根据`Tab`运动的比例,缩放,移动,修改 里面`View`的`Alpha`

- `BG` 跟随 `Tab`,首先向下移动到 `BGContent` 的高度的 1/2的地方

- `Editor` 跟随 `Tab`,首先移动到 `BGContent`下面加上预留的`Padding`,随着比例移动并设置alpha

- `Icon` 跟随 `Tab`,根据`Tab`运动的比例进行移动和调整大小

- `Name` 同上

- `Socre` 同上,没有缩放


- `ToolBarIcon` 跟随`BGContent` ,根据`BGContent`移动的比例修改图标的`Alpha`

#### 部分代码

```java
//TabBehavior
public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
    super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);

    if(isChildRequestScroll(child.getTranslationY())){//如果list需要滑动这边就不动
        consumed[1]=0;
        return;
    }

    consumed[1]=dy;//全部消耗
    int distance = -dy / 2;//降低移动的速度
    mUp = dy > 0;

    if (child.getTranslationY() + distance < -mMaxDistance) {
        distance = -mMaxDistance;
    } else if (child.getTranslationY() + distance > 0) {
        distance = 0;
    } else {
        distance = (int) (child.getTranslationY() + distance);
    }
    child.setTranslationY(distance);
}

/**
 * Child是否需要滑动
 */
private boolean isChildRequestScroll(float  translationY) {
    return (translationY == -mMaxDistance &&//在顶部
                    mViewPager.getAdapter() != null && //有适配器
                    mViewPager.getAdapter().getCount() > 0 &&//有item
                    mViewPager.getAdapter() instanceof IsChildRequestScrollListener && //实现了
                    ((IsChildRequestScrollListener) mViewPager.getAdapter()).requestScroll()//需要滑动
            );
}


//设置 listener 检测是否需要展开
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
  * list fling到头的时候 展开
  */
 @Override
 public void needExpand() {
     if(!mControlChange){//如果是手指在控制就不管
         mValueAnimator.setDuration(500);
         mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
             @Override
             public void onAnimationUpdate(ValueAnimator animation) {
                 mTab.setTranslationY((animation.getAnimatedFraction()-1)*mMaxDistance);
             }
         });
         mValueAnimator.start();
     }
 }

 /**
   * 开启硬件离屏缓存,放入不服导致缓存失效的 view
   * 效果都还好
   */
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


```

>代码是蛮简单的 ,直接看项目即可,就那几行代码, 又加了点功能 耦合度变高了的感觉欢迎 Star,提 issue 还有PR


##### 开启硬件离屏缓存
![开启硬件离屏缓存](https://github.com/CSnowStack/BehaviorDemo/blob/master/img/open.gif)

##### 关闭硬件离屏缓存
![关闭硬件离屏缓存](https://github.com/CSnowStack/BehaviorDemo/blob/master/img/close.gif)


### TODO
- 在向上fling的过程中,向下滑,会出现错乱的情况 :-(
