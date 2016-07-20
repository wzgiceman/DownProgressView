# DownProgressView
Android自定义view之仿ios-appstore下载进度按钮可设置大小，高度，颜色，各状态图片类型！
#仿IOS -appstore之下载进度按钮

###先上效果
程序员不懂设计，大家可以自己配色换图（凑合看看）

![效果](https://github.com/wzgiceman/DownProgressView/blob/master/gif/ios_progress.gif)

###使用
```java
setListner(StateProgressListner listner)设置回调监听
setmProgress(int mProgress)设置下载进度
setmFirstColor(int mFirstColor)设置进度圆环的颜色
setmSecondColor(int mSecondColor)设置第一个圆环的颜色
setmCircleWidth(int mCircleWidth)设置加载环的宽度
void setSrcStorp(int srcStorp)设置停止状态图片
setSrcStart(int srcStart)设置开始状态图片资源
void onDestory() 销毁处理
```

##思路
* 画出一个空的大圆环
* 然后画出进度的弧度
* 监听`touch`事件控制画出暂停和开始的`bitmap`
* 自定义监听返回按钮的状态

###初始化工作
```java
   //      第一圈的颜色
    private int mFirstColor;
    //     第二圈的颜色
    private int mSecondColor;
    //      圈的宽度
    private int mCircleWidth;
    //      画笔
    private Paint mPaint;
    //     当前进度
    private int mProgress;
    //    圆环中心点
    private int centre;
    //    状态图片
    private Bitmap bitmap;
    //    开始状态图片
    private int srcStart;
    //    暂停状态图片
    private int srcStorp;
    //    0开始状态；1暂停；2完成
    private int state;
    //    回调接口
    private StateProgressListner listner;
```


###画圆
```java
 centre = getWidth() / 2; // 获取圆心的x坐标
        int radius = centre - mCircleWidth;// 半径
        mPaint.setStrokeWidth(mCircleWidth); // 设置圆环的宽度
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心
        // 用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);
        mPaint.setColor(mFirstColor); // 设置圆环的颜色
        canvas.drawCircle(centre, centre, radius, mPaint); // 画出圆环
```

###画出经度的弧度

```java
mPaint.setColor(mSecondColor); // 设置圆环的颜色
mPaint.setStrokeCap(Paint.Cap.ROUND);//两边带圆弧
canvas.drawArc(oval, -90, mProgress, false, mPaint); // 根据进度画圆弧
```


###监听`touch`事件

**1**.监听MotionEvent事件
```java
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
//            判断当前状态
            if (state == 0) {
                stop();
            } else if (state == 1) {
                start();
            }
        }
        return true;
    }
```
**2**.绘制开始和暂停状态图片
```java
    /**
     * 画出当前的状态图标
     *
     * @param canvas
     */
    private void drawBitmap(Canvas canvas) {
        if (bitmap != null) bitmap.recycle();
        bitmap = BitmapFactory.decodeResource(getResources(), state == 0 ? srcStart : srcStorp);
        if (bitmap != null) {
            int with = bitmap.getWidth();
            int height = bitmap.getHeight();
            canvas.drawBitmap(bitmap, centre - with / 2, centre - height / 2, mPaint);
        }
    }
```

##自定义回调接口
**1**创建回调类
```java
    /**
     * 自定义监听器
     */
    public interface StateProgressListner {
        /**
         * 开始
         */
        void onstart();

        /**
         * 暂停
         */
        void onstop();

        /**
         * 结束
         */
        void onfinish();
    }
```
**2**触发回调事件

```java
 /**
     * 停止
     */
    private void stop() {
        state = 1;
        if (listner != null) {
            listner.onstart();
        }
        invalidate();
    }

    /**
     * 开始
     */
    private void start() {
        state = 0;
        if (listner != null) {
            listner.onstop();
        }
        invalidate();
    }

    public int getmProgress() {
        return mProgress;
    }

    /**
     * 更新进度条
     *
     * @param mProgress
     */
    public void setmProgress(int mProgress) {
        this.mProgress = mProgress;
        if (mProgress == 360 && listner != null) {
            listner.onfinish();
        }
        invalidate();
    }
```

##主线程中控制状态
这里我并没有真正的下载，用hander模拟下载进度变化
```java
public class MyView1Activity extends AppCompatActivity implements DownProgressView.StateProgressListner {
    @BindView(R.id.view1)
    DownProgressView view1;
    int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_view1);
        ButterKnife.bind(this);
        view1.setListner(this);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            view1.setmProgress(++progress);
            handler.sendEmptyMessageDelayed(1, 20);
        }

    };

    @Override
    public void onstart() {
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onstop() {
        handler.removeMessages(1);
    }

    @Override
    public void onfinish() {
        progress = 0;
        Toast.makeText(this, "下载完成了!继续开始", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
        view1.onDestory();
    }
}
```


