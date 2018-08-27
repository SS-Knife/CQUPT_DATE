package com.example.cqupt_date.app.Find;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.avos.avoscloud.AVUser;
import com.example.cqupt_date.R;

import java.util.ArrayList;

import static com.example.cqupt_date.app.MainActivity.User;

/**
 * Created by 郝书逸 on 2018/8/25.
 */

public class Find_Activity extends AppCompatActivity{
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    AVUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //安卓版本大于4.4设置状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //绑定toolbara
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_find);
        user=User;
        initView();
        initData();
    }
    private void initView(){
        tabLayout=findViewById(R.id.tablayout);
        viewPager=findViewById(R.id.viewpager);
        toolbar=findViewById(R.id.toolbar);
    }
    private void initData(){
        String ta;
        if((int)user.get("sex")==0){
            ta="找她";
        }else{
            ta="找他";
        }
        String[] title={"找伙伴",ta};
        ArrayList<Fragment>fragments=new ArrayList<>();
        fragments.add(Fragment_Partner.newInstance());
        fragments.add(Fragment_Love.newInstance());
        pagerAdapter adapter=new pagerAdapter(getSupportFragmentManager(),fragments);
        adapter.setTitles(title);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
