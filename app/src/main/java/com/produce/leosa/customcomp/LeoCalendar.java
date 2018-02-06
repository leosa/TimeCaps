package com.produce.leosa.customcomp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.produce.leosa.timecaps.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Leosa on 2018/2/5.
 */

public class LeoCalendar extends View {

    //region 变量的定义
    private Paint paint=new Paint();
    private int titleColor;
    private int titleHeight;
    private int titleTextColor;
    private int titleTextSize;
    private int leftArrow;
    private int rightArrow;
    private int yearLeftArrow;
    private int yearRightArrow;
    private int currentYear=2018;
    private int currentMonth=2;
    private List<Point> pointList=new ArrayList<>();
    private Point currentPoint;
    private Rect monthLeftRect=new Rect();
    private Rect monthRightRect=new Rect();
    private Rect yearLeftRect=new Rect();
    private Rect yearRightRect=new Rect();
    private OnDateClickListener dateClickListener;
    private OnPageChangeListener pageChangeListener;
    //endregion
    public LeoCalendar(Context context) {
        super(context);
    }


    public LeoCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //region 接收组件属性信息
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LeoCalendar);
        titleColor = typedArray.getColor(R.styleable.LeoCalendar_title_color, Color.parseColor("#ff8de8"));
        titleHeight=typedArray.getDimensionPixelSize(R.styleable.LeoCalendar_title_height,80);
        titleTextColor=typedArray.getColor(R.styleable.LeoCalendar_title_text_color,Color.WHITE);
        titleTextSize=typedArray.getDimensionPixelSize(R.styleable.LeoCalendar_title_text_size,20);
        leftArrow=typedArray.getResourceId(R.styleable.LeoCalendar_month_leftArrow,R.drawable.custom_calendar_row_left);
        rightArrow=typedArray.getResourceId(R.styleable.LeoCalendar_month_rightArrow,R.drawable.custom_calendar_row_right);
        yearLeftArrow=typedArray.getResourceId(R.styleable.LeoCalendar_year_leftArrow,R.drawable.year_left);
        yearRightArrow=typedArray.getResourceId(R.styleable.LeoCalendar_year_rightArrow,R.drawable.year_right);
        //endregion
        typedArray.recycle();   //回收
    }

    public LeoCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LeoCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    //region 自定义的事件监听器相应接口以及实现
    public interface OnDateClickListener{
        public void OnDateClick(int day);
    }
    public void setOnDateClickListener(OnDateClickListener listener){
        dateClickListener=listener;
    }
    public interface OnPageChangeListener{
        public void OnPageChanged();
    }
    public void setOnPageChangeListener(OnPageChangeListener listener){
        pageChangeListener=listener;
    }
    //endregion

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(currentPoint!=null){
            paint.setColor(Color.parseColor("#FF7F00"));
            canvas.drawCircle(currentPoint.x,currentPoint.y,30,paint);
        }
        initTitleBar(canvas);
        initWeek(canvas);
        initDay(canvas);


    }

    private void initDay(Canvas canvas) {
        int basePos=0;
        int flag=0;
        int lineY=0;
        List<String> dayList=new ArrayList<>();
        paint.setColor(Color.parseColor("#000000"));
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);  //文字抗锯齿
        paint.setTypeface(null);
        int partWidth=getWidth()/7;
        paint.setTextSize(30);
        paint.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/hyqt.ttf"));
        Calendar c = Calendar.getInstance();
        c.set(currentYear, currentMonth, 0); //输入类型为int类型
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c.setTime(format.parse(currentYear+"-"+currentMonth+"-01"));
            flag=c.get(Calendar.DAY_OF_WEEK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(int i=0;i<dayOfMonth;i++){
            dayList.add((i+1)+"");
        }
        for(int i=0;i<dayList.size();i++){
            float textWidth = paint.measureText(dayList.get(i));//计算长度，进行居中
            float x = partWidth / 2 - textWidth / 2 + (flag-1)*partWidth;
            Paint.FontMetrics metrics = paint.getFontMetrics();
            float dy = -(metrics.descent + metrics.ascent) / 2;
            float y = titleHeight*2  + dy+(int)(80*lineY);
            Point point=new Point();
            point.set((int)(x+ textWidth / 2),(int)(y-dy));
            pointList.add(point);
            flag++;
            if(flag==8) {
                flag = 1;
                lineY += 1;
            }
            if(currentPoint!=null&&currentPoint.equals(point))
                paint.setColor(Color.WHITE);
            canvas.drawText(dayList.get(i),x,y,paint);
            paint.setColor(Color.BLACK);
        }
    }

    private void initWeek(Canvas canvas) {
        paint.setColor(Color.parseColor("#555555"));
        String[] strings={"Sun","Mon","Tues","Wed","Thur","Fri","Sat"};
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);  //文字抗锯齿
        paint.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/hyqt.ttf"));
        int partWidth=getWidth()/7;
        paint.setTextSize(24);
        for(int i=0;i<7;i++){
            float textWidth = paint.measureText(strings[i]);//计算长度，进行居中
            float x = partWidth / 2 - textWidth / 2 + partWidth * i;
            Paint.FontMetrics metrics = paint.getFontMetrics();
            float dy = -(metrics.descent + metrics.ascent) / 2;
            float y = titleHeight / 4 + dy+titleHeight;
            canvas.drawText(strings[i],x,y,paint);
        }
    }

    private void initTitleBar(Canvas canvas) {
        //绘制标题背景
        paint.setColor(titleColor);
        canvas.drawRect(getLeft(),getTop(),getRight(),getTop()+titleHeight,paint);
        //绘制标题文字
        paint.setColor(titleTextColor);
        paint.setTextSize(titleTextSize);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);  //文字抗锯齿
        paint.setTypeface(null);
        float textWidth = paint.measureText(currentYear+"年"+currentMonth+"月");//计算长度，进行居中
        float x = getWidth() / 2 - textWidth / 2;
        Paint.FontMetrics metrics = paint.getFontMetrics();
        float dy = -(metrics.descent + metrics.ascent) / 2;
        float y = titleHeight / 2 + dy;
        canvas.drawText(currentYear+"年"+currentMonth+"月",x,y,paint);

        //region 绘制日历切换箭头相应代码
        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), yearLeftArrow);
        canvas.drawBitmap(bitmap3,getLeft()+40,getTop()+(titleHeight-bitmap3.getHeight())/2,paint);
        yearLeftRect.set(getLeft(),getTop(),getLeft()+80+bitmap3.getWidth()/2,getTop()+titleHeight);    //计算点击相应范围，供点击方法使用
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), leftArrow);
        canvas.drawBitmap(bitmap,getLeft()+120,getTop()+(titleHeight-bitmap.getHeight())/2,paint);
        monthLeftRect.set(getLeft()+80+bitmap3.getWidth()/2,getTop(),getLeft()+120+bitmap.getWidth()+40,getTop()+titleHeight);    //计算点击相应范围，供点击方法使用
        Bitmap bitmap4 = BitmapFactory.decodeResource(getResources(), yearRightArrow);
        canvas.drawBitmap(bitmap4,getRight()-bitmap4.getWidth()-40,getTop()+(titleHeight-bitmap4.getHeight())/2,paint);
        yearRightRect.set(getRight()-80-bitmap4.getWidth()/2,getTop(),getRight(),getTop()+titleHeight);    //计算点击相应范围，供点击方法使用
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), rightArrow);
        canvas.drawBitmap(bitmap2,getRight()-bitmap2.getWidth()-120,getTop()+(titleHeight-bitmap2.getHeight())/2,paint);
        monthRightRect.set(getRight()-120-bitmap4.getWidth()-40,getTop(),getRight()-80-bitmap4.getWidth()/2,getTop()+titleHeight);    //计算点击相应范围，供点击方法使用
        //endregion
    }

    /**
     * 重写点击事件，通过x与y获取相应点击的控件并实现功能
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                //获取屏幕上点击的坐标
                float x=event.getX();
                float y = event.getY();
                //region 日期点击相应代码
                //循环判断现有日期坐标点，若点击在日期上，则重新绘制试图
                for(int i=0;i<pointList.size();i++){
                    if(Math.sqrt(Math.abs(x-pointList.get(i).x)*Math.abs(x-pointList.get(i).x)+Math.abs(y-pointList.get(i).y)*Math.abs(y-pointList.get(i).y))<30){
                        currentPoint=pointList.get(i);
                        if(dateClickListener!=null)     //实现自定义的日期点击事件
                            dateClickListener.OnDateClick(i+1);
                        invalidate();//更新视图
                        return true;
                    }
                }
                //endregion
                //region 切换日历箭头点击相应代码
                if(monthLeftRect.contains((int)x,(int)y)){
                    currentMonth-=1;
                    if(currentMonth==0) {
                        currentMonth = 12;
                        currentYear-=1;
                    }
                    currentPoint=null;
                    if(pageChangeListener!=null)        //实现自定义的日历切换监听器
                        pageChangeListener.OnPageChanged();
                    pointList.clear();
                    invalidate();
                }else if(monthRightRect.contains((int)x,(int)y)){
                    currentMonth+=1;
                    if(currentMonth==13){
                        currentMonth=1;
                        currentYear+=1;
                    }
                    currentPoint=null;
                    if(pageChangeListener!=null)
                        pageChangeListener.OnPageChanged();
                    pointList.clear();
                    invalidate();
                }else if(yearLeftRect.contains((int)x,(int)y)){
                    currentYear-=1;
                    currentPoint=null;
                    if(pageChangeListener!=null)
                        pageChangeListener.OnPageChanged();
                    pointList.clear();
                    invalidate();
                }else if(yearRightRect.contains((int)x,(int)y)){
                    currentYear+=1;
                    currentPoint=null;
                    if(pageChangeListener!=null)
                        pageChangeListener.OnPageChanged();
                    pointList.clear();
                    invalidate();
                }
                //endregion
                break;
            case MotionEvent.ACTION_UP:
                return true;
        }
        return super.onTouchEvent(event);
    }

    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED: {//如果没有指定大小，就设置为默认大小
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.AT_MOST: {//如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                mySize = size;
                break;
            }
            case MeasureSpec.EXACTLY: {//如果是固定的大小，那就不要去改变它
                mySize = size;
                break;
            }
        }
        return mySize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMySize(100, widthMeasureSpec);
        int height = getMySize(100, heightMeasureSpec);

        if (width < height) {
            height = width;
        } else {
            width = height;
        }

        setMeasuredDimension(width, height);
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(int currentMonth) {
        this.currentMonth = currentMonth;
    }
}
