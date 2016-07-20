package com.example.view.myview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 第一个自定义view简单点吧
 * <p/>
 * 居中显示一个文本框
 * 添加点击监听
 * Created by WZG on 2016/7/19.
 */
public class MyView1 extends View {
    //    距离边缘的位置
    private int pading = 60;
    //    边框距离x坐标
    private int startX = 100;
    //    边框距离y坐标
    private int startY = 100;
    //    文字的区域
    private RectF rectFTxt;
    //    边框区域
    private RectF rectF;


    public MyView1(Context context) {
        super(context);
    }

    public MyView1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paintCenterText("我就是一个文本显示的控件呀", canvas);
    }

    /**
     * 绘制一个居中显示的textview
     */
    private void paintCenterText(String name, Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        /**
         * 设置绘制文字时起始点X坐标的位置
         * CENTER:以文字的宽度的中心点为起始点向两边绘制
         * LEFT:以文字左边为起始点向右边开始绘制
         * RIGHT:以文字宽度的右边为起始点向左边绘制
         */
        textPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float textHeight = (int) Math.ceil(fm.bottom - fm.top);
        float textWith = textPaint.measureText(name);


        rectF = new RectF(startX, startY, startX + textWith + pading * 2, pading * 2 + startY + textHeight);
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);

        rectFTxt = new RectF(startX + pading, startY + pading, rectF.right - pading, rectF.bottom - pading);
        canvas.drawRect(rectF, paint);

        int baseline = (int) ((rectF.bottom + rectF.top - fm.bottom - fm.top) / 2);
        canvas.drawText(name, rectF.centerX(), baseline, textPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = 0;
        float y = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
            case MotionEvent.ACTION_UP:
                if (x > rectFTxt.left && x < rectFTxt.right && y > rectFTxt.top && y < rectFTxt.bottom) {
                    Log.i("tag", "文字被点击了！");
                }
                if (x > rectF.left && x < rectFTxt.left) {
                    Log.i("tag", "空白左的地方被点击了！");
                }
                if (x > rectFTxt.right && x < rectF.right) {
                    Log.i("tag", "空白右的地方被点击了！");
                }
                if (y > rectF.top && y < rectFTxt.top) {
                    Log.i("tag", "空白上的地方被点击了！");
                }
                if (y > rectFTxt.bottom && y < rectF.bottom) {
                    Log.i("tag", "空白下的地方被点击了！");
                }
                break;
        }
        return true;
    }


    public int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top) + 2;
    }
}
