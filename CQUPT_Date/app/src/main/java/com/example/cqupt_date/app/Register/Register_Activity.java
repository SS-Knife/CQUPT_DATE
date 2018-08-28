package com.example.cqupt_date.app.Register;

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
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.example.cqupt_date.app.Chat.CustomUserProvider;
import com.example.cqupt_date.app.MainActivity;
import com.example.cqupt_date.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郝书逸 on 2018/8/21.
 */

public class Register_Activity extends AppCompatActivity{

    Toolbar toolbar;
    TextView title;
    TabLayout tabLayout;
    ViewPager viewPager;

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
        setContentView(R.layout.activity_register);
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            // 跳转到首页
            Intent intent=new Intent(this, MainActivity.class);
            intent.putExtra("CurrentUserObjectID",currentUser.getObjectId());
            startActivity(intent);
        }
        initView();


    }
    private void initView(){
        toolbar=findViewById(R.id.toolbar);
        title=findViewById(R.id.title);
        tabLayout=findViewById(R.id.tablayout);
        viewPager=findViewById(R.id.viewpager);
        List<Fragment>fragments = new ArrayList<>();
        Fragment_Login fragment_login=Fragment_Login.newInstance();
        Fragment_Register fragment_register=Fragment_Register.newInstance();
        fragments.add(fragment_login);
        fragments.add(fragment_register);
        pagerAdapter adapter = new pagerAdapter(getSupportFragmentManager(),fragments);
        String[]titles={"登陆","注册"};
        adapter.setTitles(titles);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
