package com.produce.leosa.timecaps;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.produce.leosa.customcomp.LeoCalendar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leosa on 2018/2/4.
 */

public class MainActivity extends BaseActivity {
    private TextView titleText;
    //汉仪清庭简体字体
    private Typeface hyqtTypeface;
    private Button todayTab,futureTab,calendarTab;
    private View todayView,futureView,calendarView;
    private List<View> viewList=new ArrayList<>();
    private ViewPager mainPager;
    private LeoCalendar leoCalendar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComp();
        initFunc();
    }

    private void initComp() {
        LayoutInflater inflater=getLayoutInflater();
        todayView=inflater.inflate(R.layout.activity_main_today,null);
        futureView=inflater.inflate(R.layout.activity_main_future,null);
        calendarView=inflater.inflate(R.layout.activity_main_calendar,null);
        titleText=(TextView)findViewById(R.id.titleText);
        hyqtTypeface = Typeface.createFromAsset(getAssets(), "fonts/hyqt.ttf");
        todayTab=(Button)findViewById(R.id.todayTab);
        futureTab=(Button)findViewById(R.id.futureTab);
        calendarTab=(Button)findViewById(R.id.calendarTab);
        mainPager=(ViewPager)findViewById(R.id.mainPager);
        todayTab.setTypeface(hyqtTypeface);
        futureTab.setTypeface(hyqtTypeface);
        calendarTab.setTypeface(hyqtTypeface);
        leoCalendar=(LeoCalendar)calendarView.findViewById(R.id.MyCalendar);
    }

    private void initFunc() {
        initViewPager();
        todayTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTabFunc();
                mainPager.setCurrentItem(0);
                todayTab.setBackgroundResource(R.drawable.tab_bg_checked);
                todayTab.setPadding(0,0,0,4);
            }
        });
        futureTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTabFunc();
                mainPager.setCurrentItem(1);
                futureTab.setBackgroundResource(R.drawable.tab_bg_checked);
                futureTab.setPadding(0,0,0,4);
            }
        });
        calendarTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTabFunc();
                mainPager.setCurrentItem(2);
                calendarTab.setBackgroundResource(R.drawable.tab_bg_checked);
                calendarTab.setPadding(0,0,0,4);
            }
        });
        leoCalendar.setOnDateClickListener(new LeoCalendar.OnDateClickListener() {
            @Override
            public void OnDateClick(int day) {
                Toast.makeText(MainActivity.this,leoCalendar.getCurrentYear()+":"+day+"",Toast.LENGTH_SHORT).show();
            }
        });

        titleText.setTypeface(hyqtTypeface);
    }

    private void initViewPager() {
        viewList.add(todayView);
        viewList.add(futureView);
        viewList.add(calendarView);
        PagerAdapter pagerAdapter=new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }

        };
        mainPager.setAdapter(pagerAdapter);
        mainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                initTabFunc();
                switch (position){
                    case 0:
                        todayTab.setBackgroundResource(R.drawable.tab_bg_checked);
                        todayTab.setPadding(0,0,0,4);
                        break;
                    case 1:
                        futureTab.setBackgroundResource(R.drawable.tab_bg_checked);
                        futureTab.setPadding(0,0,0,4);
                        break;
                    case 2:
                        calendarTab.setBackgroundResource(R.drawable.tab_bg_checked);
                        calendarTab.setPadding(0,0,0,4);
                        break;
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTabFunc() {
        todayTab.setBackgroundResource(R.drawable.tab_bg_normal);
        futureTab.setBackgroundResource(R.drawable.tab_bg_normal);
        calendarTab.setBackgroundResource(R.drawable.tab_bg_normal);
        todayTab.setPadding(0,0,0,1);
        futureTab.setPadding(0,0,0,1);
        calendarTab.setPadding(0,0,0,1);
    }

    @Override
    int getViewId() {
        return R.layout.activity_main;
    }
}
