package com.example.billingbilling;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by chanjun2016 on 16/11/28.
 */

public class LineChartView extends View {

    private boolean noTitle;

    private float marginLeft;// 左边距
    private float marginRight;// 右边距
    private float marginTop;// 上边距
    private float marginBottom;//下边距
    private float widthInterval;// 单位宽
    private float heightInterval;// 单位高
    private float viewHeight = 0;// 控件高度
    private float viewWidth = 0;// 控件宽度

    private List<Double> values;// 折线值
    private List<String> dates;// 日期
    private List<String> stringValues = new ArrayList<String>();//折线值String
    private double maxV;// 最大值
    private double minV;// 最小值

    private float valueInterval;// 每格像素的value值
    private List<String> numbers;// 纵坐标

    private int pointNum = 0;// 选中的是哪一个点
    private float textSize = 0;// 字体大小
    private float titleSize;
    private float xSize;
    private float ySize;
    private int fillCircleRadio = 14;// 实心圆的半径
    private int strokeCircleRadio = 16;//空心圆半径
    private int lineSize = 10;
    private int fillPointSize = 5;//实心圆
    private int strokePointSize = 10;//空心圆粗细

    private TextPaint FontPaint;
    private Context mContext;


    // 构造方法
    public LineChartView(Context context) {
        this(context, null, 0);
        this.mContext = context;

    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
    }

    public LineChartView(Context context, AttributeSet attrs,
                             int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    /**
     * 设置折线的值
     *
     * @param values 折线值
     * @param dates  日期（七天）
     */
    public void setData(List<Double> values, List<String> dates, boolean noTitle) {

        if (values == null || values.size() == 0 || dates == null || dates.size() == 0) {
            return;
        }

        // 保存传入的参数
        this.values = values;
        this.dates = dates;
        this.noTitle = noTitle;

        reInitData();

        // 重绘
        this.invalidate();

    }

    // 设置字体大小
    public void setTextSize(int textSize, int titleSize, int xSize, int ySize) {
        this.textSize = textSize;
        this.titleSize = titleSize;
        this.xSize = xSize;
        this.ySize = ySize;
    }


    @Override
    protected void onDraw(Canvas canvas) {


        clearCanvas(canvas);// 清空画布
        if (values == null || values.size() == 0 || dates == null || dates.size() == 0) {
            return;
        }
        initData();
        if (!noTitle) {
            drawTitle(canvas);// 画"近七天支出趋势"
        } else {
            marginTop = marginBottom;
        }

        drewTable(canvas);// 画表格虚线

        drawX(canvas);// 画日期
        drawY(canvas);// 画纵坐标数字

        drawDetail(canvas);// 画详情信息(折线,圆点,圆角矩形)


        setClickable(true);// 设置控件为可点击
    }

    private void reInitData() {
        maxV = Collections.max(values);
        minV = Collections.min(values);
        maxV = (((int) (maxV - minV - 0.001d)) / (dates.size() - 1) + 1) * (dates.size() - 1) + minV;

        //得到最长数,和最短数
        for (double d : values) {
            String str = String.valueOf(d);
            stringValues.add(str);
        }

        pointNum = (dates.size() - 1);

        // 计算出纵坐标显示的值
        numbers = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            if (maxV == minV) {
                numbers.add(String.format("%.0f", 2 * maxV - (i) * maxV / (dates.size() - 1)));
            } else {
                numbers.add(String.valueOf((int) ((maxV - minV) * (values.size() - 1 - i) / (values.size() - 1) + minV)));
            }
        }

        lineSize = DisplayUtil.dip2px(mContext, 2f);//折线粗细
        fillCircleRadio = DisplayUtil.dip2px(mContext, 3.5f);//实心圆半径
        strokeCircleRadio = DisplayUtil.dip2px(mContext, 4.25f);//空心圆半径
        fillPointSize = DisplayUtil.dip2px(mContext, 1.5f);//实心圆粗细
        strokePointSize = DisplayUtil.dip2px(mContext, 2f);//空心圆粗细
    }

    private void initData() {

        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        // 计算出字体大小
        if (textSize == 0 || titleSize == 0 || xSize == 0 || ySize == 0) {
            textSize = viewWidth / 36;// 8.88
            titleSize = textSize;
            xSize = textSize * 2 / 3;
            ySize = textSize * 2 / 3;
        }

        if (null == numbers || numbers.size() == 0) {
            return;
        }

        // 计算出左边距
        marginLeft = viewWidth / 30 + getTextWidth(ySize, numbers.get(0)) + strokeCircleRadio + strokePointSize;
        // 计算出右边距
        marginRight = viewWidth / 12;
        // 计算出上边距
        marginTop = getTextHeight(titleSize, "近七天支出趋势") * 4;
        //计算出下边距
        marginBottom = viewHeight / 10;

        // 计算出单位宽
        widthInterval = (viewWidth - marginLeft - marginRight) / (dates.size() - 1);
        // 计算出单位高
        heightInterval = (viewHeight - marginTop - marginBottom) / (values.size() - 1);

        // 计算出每格像素的value值
        valueInterval = ((float) (maxV - minV)) / ((values.size() - 1) * heightInterval);
    }

    // 清空画布
    private void clearCanvas(Canvas canvas) {
        canvas.drawARGB(0, 0, 0, 0);
    }

    // 画"近七天收益趋势"
    private void drawTitle(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.color_666666));
        paint.setTextSize(titleSize);
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        float textHeight = getTextHeight(titleSize, "近七天支出趋势");
        textHeight = marginTop - textHeight * 5 / 2;
        canvas.drawText("近七天支出趋势", getTextWidth(titleSize, "------"), textHeight,
                paint);
    }

    // 画表格虚线
    private void drewTable(Canvas canvas) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.color_dddddd));
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.FILL.FILL_AND_STROKE);
        //画虚线的
        PathEffect effects = new DashPathEffect(new float[]{2, 6}, 1);
        paint.setPathEffect(effects);

        // 横线
        for (int i = 0; i < values.size(); i++) {
            Path path = new Path();
            path.moveTo(marginLeft, marginTop + i * heightInterval);
            path.lineTo(marginLeft + (dates.size() - 1) * widthInterval, marginTop + i
                    * heightInterval);
            canvas.drawPath(path, paint);
        }

        // 竖线
        for (int i = 0; i < dates.size(); i++) {
            Path path = new Path();
            path.moveTo(marginLeft + i * widthInterval, marginTop);
            path.lineTo(marginLeft + i * widthInterval, marginTop + (values.size() - 1)
                    * heightInterval);
            canvas.drawPath(path, paint);
        }
    }

    private void drawDetail(Canvas canvas) {
        if (values == null || values.size() == 0 || textSize == 0) {
            return;
        }

        drawFoldLine(canvas);// 画折线
        drawCircle(canvas);// 画圆圈
        drawRoundRect(canvas);// 画圆角矩形
    }

    // 画折线
    private void drawFoldLine(Canvas canvas) {

        Paint paintFoldLine = new Paint();
        paintFoldLine.setColor(getResources().getColor(R.color.text_ff6900));
        paintFoldLine.setStrokeWidth(lineSize);// 数据线宽度（粗细）
        paintFoldLine.setAntiAlias(true);
        paintFoldLine.setStyle(Paint.Style.FILL.STROKE);

        Path pathFoldLine = new Path();
        pathFoldLine.moveTo(marginLeft,
                marginTop + Float.parseFloat((maxV - values.get(0)) + "")
                        / valueInterval);
        for (int i = 1; i < dates.size(); i++) {
            pathFoldLine.lineTo(marginLeft + i * widthInterval, marginTop
                    + Float.parseFloat((maxV - values.get(i)) + "")
                    / valueInterval);
        }

        canvas.drawPath(pathFoldLine, paintFoldLine);
    }

    // 画圆圈
    private void drawCircle(Canvas canvas) {

        Paint fillPaint = new Paint();
        fillPaint.setColor(getResources().getColor(R.color.text_ff6900));
        fillPaint.setStrokeWidth(fillPointSize);
        fillPaint.setAntiAlias(true);
        fillPaint.setStyle(Paint.Style.FILL.FILL);

        Paint fillPaintWhite = new Paint();
        fillPaintWhite.setColor(Color.WHITE);
        fillPaintWhite.setStrokeWidth(strokePointSize);
        fillPaintWhite.setAntiAlias(true);
        fillPaintWhite.setStyle(Paint.Style.FILL);

        Paint paintStroke = new Paint();
        paintStroke.setColor(getResources().getColor(R.color.text_ff6900));
        paintStroke.setStrokeWidth(strokePointSize);
        paintStroke.setAntiAlias(true);
        paintStroke.setStyle(Paint.Style.STROKE);

        for (int i = 0; i < dates.size(); i++) {
            float sPointWidth = marginLeft + i * widthInterval;
            float sPointHeight = marginTop
                    + Float.parseFloat((maxV - values.get(i)) + "")
                    / valueInterval;


            if (i != pointNum) {
                canvas.drawCircle(sPointWidth, sPointHeight, fillCircleRadio,
                        fillPaint);
            } else {
                canvas.drawCircle(sPointWidth, sPointHeight, strokeCircleRadio,
                        fillPaintWhite);
                canvas.drawCircle(sPointWidth, sPointHeight, strokeCircleRadio,
                        paintStroke);
            }


        }

    }

    // 画圆角矩形
    private void drawRoundRect(Canvas canvas) {
        // 画圆角矩形
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);// 充满
        paint.setColor(getResources().getColor(R.color.text_ff6900));
        paint.setAntiAlias(true);// 设置画笔的锯齿效果

        // 画文字
        Paint paintText = new Paint();
        paintText.setStyle(Paint.Style.FILL);// 充满
        paintText.setColor(Color.WHITE);
        paintText.setTextSize(textSize);

        float textWidth = getTextWidth(textSize, stringValues.get(pointNum));
        float textHeight = getTextHeight(textSize, stringValues.get(pointNum));


        float left;
        float leftText;
        float right;

        float top;
        float bottom;

        float skewing = strokeCircleRadio + strokePointSize / 2;
        if (mContext == null) {
            return;
        }
        float skewingWidth = DisplayUtil.dip2px(mContext, 5f);

        if (pointNum == 0) {
            left = marginLeft - skewing;
            right = marginLeft + textWidth + 2 * skewingWidth - skewing;

        } else if (pointNum == (dates.size() - 1)) {

            left = marginLeft + pointNum * widthInterval - textWidth - 2 * skewingWidth + skewing;
            right = marginLeft + pointNum * widthInterval + skewing;
        } else {

            left = marginLeft + pointNum * widthInterval - textWidth * 1 / 2 - skewingWidth;
            right = marginLeft + pointNum * widthInterval + textWidth * 1 / 2 + skewingWidth;
        }
        leftText = left
                + skewingWidth;

        top = marginTop
                + Float.parseFloat(maxV - values.get(pointNum) + "")
                / valueInterval - textHeight * 7 / 2;
        bottom = marginTop
                + Float.parseFloat(maxV - values.get(pointNum) + "")
                / valueInterval - textHeight * 3 / 2;

        RectF oval3 = new RectF(left, top, right, bottom);// 设置个新的长方形
        canvas.drawRoundRect(oval3, DisplayUtil.dip2px(mContext, 7.7f), DisplayUtil.dip2px(mContext, 5.5f), paint);// 第二个参数是x半径，第三个参数是y半径

        canvas.drawText(stringValues.get(pointNum), leftText, top + textHeight * 3 / 2, paintText);
    }

    // 画日期(横坐标)
    private void drawX(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.color_666666));
        paint.setTextSize(xSize);
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);// 数字字体的样式 （粗细/实体或空心什么的...）

        float textHeight = getTextHeight(xSize, dates.get(0));

        float height = marginTop + (dates.size() - 1) * heightInterval + textHeight;

        for (int i = 0; i < dates.size(); i++) {
            Path path = new Path();
            path.moveTo(marginLeft + i * widthInterval - getTextWidth(xSize, dates.get(i)) / 2, height + textHeight * 7/ 2+35);// 只用于移动移动画笔。
            path.lineTo(marginLeft + i * widthInterval + getTextWidth(xSize, dates.get(i)) / 2, height + textHeight * 7 / 2);// 用于进行直线绘制。
            canvas.drawTextOnPath(dates.get(i), path, 0, 0, paint);
        }
    }

    // 画纵坐标数字
    private void drawY(Canvas canvas) {
        if (numbers == null || numbers.size() == 0 || ySize == 0) {
            return;
        }

        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.color_666666));
        paint.setTextSize(ySize);
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);// 数字字体的样式 （粗细/实体或空心什么的...）

        float width = marginLeft - getTextWidth(ySize, numbers.get(0)) - strokeCircleRadio - strokePointSize;
        float height = marginTop + getTextHeight(ySize, numbers.get(0)) / 2;
        for (int i = 0; i < numbers.size(); i++) {
            canvas.drawText(numbers.get(i), width, i * heightInterval
                   + height, paint);
        }
    }

    // 点击事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != values && values.size() != 0) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // 得到坐标
                float fx = event.getX();
                float fy = event.getY();

                for (int i = 0; i < values.size(); i++) {

                    float width = marginLeft + i * widthInterval;
                    float height = marginTop
                            + Float.parseFloat((maxV - values.get(i)) + "")
                            / valueInterval;

                    if (fx > (width - widthInterval / 2) && fx < (width + widthInterval / 2)) {
                        if (fy > (height - heightInterval) && fy < (height + heightInterval)) {
                            pointNum = i;
                            this.invalidate();
                        }
                    }
                }

            }
        }
        return super.onTouchEvent(event);
    }


    private float getTextWidth(float Size, String text) {
        if (FontPaint == null) {
            FontPaint = new TextPaint();
        }
        FontPaint.setTextSize(Size);
        return FontPaint.measureText(text);
    }

    private int getTextHeight(float Size, String text) {
        if (FontPaint == null) {
            FontPaint = new TextPaint();
        }
        FontPaint.setTextSize(Size);
        Rect bounds = new Rect();
        FontPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.bottom + bounds.height();
    }

}

