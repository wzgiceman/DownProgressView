package com.example.view.myview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.view.myview.R;

/**
 * 模仿苹果下载暂停控件
 * Created by WZG on 2016/7/20.
 */
public class DownProgressView extends View {
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

    public DownProgressView(Context context) {
        super(context);
    }

    public DownProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFirstColor = getResources().getColor(R.color.white);
        mSecondColor = getResources().getColor(R.color.progress);
        mCircleWidth = 5;
        mPaint = new Paint();
        mProgress = 0;
        srcStart = R.drawable.btn_vedio_restart;
        srcStorp = R.drawable.btn_down_pause;
        state = 0;
    }

    public DownProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCirle(canvas);
        drawBitmap(canvas);
    }


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

    /**
     * 圆弧进度
     *
     * @param canvas
     */
    private void drawCirle(Canvas canvas) {
        centre = getWidth() / 2; // 获取圆心的x坐标
        int radius = centre - mCircleWidth;// 半径
        mPaint.setStrokeWidth(mCircleWidth); // 设置圆环的宽度
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心
        // 用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);
        mPaint.setColor(mFirstColor); // 设置圆环的颜色
        canvas.drawCircle(centre, centre, radius, mPaint); // 画出圆环
        mPaint.setColor(mSecondColor); // 设置圆环的颜色
        mPaint.setStrokeCap(Paint.Cap.ROUND);//两边带圆弧
        canvas.drawArc(oval, -90, mProgress, false, mPaint); // 根据进度画圆弧
    }


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

    public void setListner(StateProgressListner listner) {
        this.listner = listner;
    }

    /**
     * 销毁处理
     */
    public void onDestory() {
        if (bitmap != null) bitmap.recycle();
    }

    /**
     * 设置开始状态图片资源
     *
     * @param srcStart
     */
    public void setSrcStart(int srcStart) {
        this.srcStart = srcStart;
    }


    /**
     * 设置停止状态图片
     *
     * @param srcStorp
     */
    public void setSrcStorp(int srcStorp) {
        this.srcStorp = srcStorp;
    }

    /**
     * 设置加载环的宽度
     *
     * @param mCircleWidth
     */
    public void setmCircleWidth(int mCircleWidth) {
        this.mCircleWidth = mCircleWidth;
    }


    /**
     * 设置第一个圆环的颜色
     *
     * @param mSecondColor
     */
    public void setmSecondColor(int mSecondColor) {
        this.mSecondColor = mSecondColor;
    }

    /**
     * 设置进度圆环的颜色
     *
     * @param mFirstColor
     */
    public void setmFirstColor(int mFirstColor) {
        this.mFirstColor = mFirstColor;
    }

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
}
