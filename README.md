## Behavior 实现的漂亮的效果

### 简介

#### 建议
请先阅读[这篇文章](http://www.jianshu.com/p/f7989a2a3ec2)
> 好多东西抄自这里

#### 效果来源
[这里是地址](https://material.uplabs.com/posts/profile-4f03fc6b-1a82-42ab-8a3e-f50dcbc10253)

![原图](https://assets.materialup.com/uploads/210a9886-c3fd-4dd9-9077-628779eda61a/preview.gif)

![实现的效果](https://github.com/CSnowStack/BehaviorDemo/blob/master/img/c.gif)

[项目地址](https://github.com/CSnowStack/BehaviorDemo)

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

//ListBehavior
@Override
int getScrollRange(View v) {
    if (isDependOn(v)) {
        return -mHeightToolbar;
    } else {
        return super.getScrollRange(v);
    }
}
//   HeaderScrollingViewBehavior$onMeasure  所以要返回 - mHeightToolbar
final int height = availableHeight - header.getMeasuredHeight()
        + getScrollRange(header);

```

>代码是蛮简单的 ,直接看项目即可,就那几行代码
