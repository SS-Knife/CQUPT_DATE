package com.example.cqupt_date.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.example.cqupt_date.R;
import com.example.cqupt_date.app.Chat.Chat_Activity;
import com.example.cqupt_date.app.Find.*;
import com.example.cqupt_date.app.PersonalMenu.PersonalMenu_Activity;
import com.example.cqupt_date.app.Register.Register_Activity;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;

import static java.security.AccessController.getContext;

public class MainActivity extends Activity implements View.OnClickListener{
    TextView news;
    RecyclerView recyclerView;

    public static String CurrentUserObjectID;
    public static AVUser User;
    public static AVObject Friend;
    TextView username;
    ResideMenu resideMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=getIntent();
        if(intent!=null){
            CurrentUserObjectID=intent.getStringExtra("CurrentUserObjectID");
        }
        User= AVUser.getCurrentUser();
        AVQuery<AVObject> avQuery = new AVQuery<>("friend");
        avQuery.getInBackground(User.get("friendclassid").toString(), new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                Friend = avObject;
                initView();
                getnews();
                setresideMenu();
                setRecyclerView();
            }
        });
    }
    private void initView(){
        news=findViewById(R.id.news);
        recyclerView=findViewById(R.id.recycler);
        username=findViewById(R.id.username);
        username.setText(User.getUsername());
    }

    private void getnews(){
        AVQuery<AVObject> avQuery = new AVQuery<>("news");
        avQuery.getInBackground("5b837162808ca4003d788287", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if(avObject.get("new")!=null){
                    news.setText(avObject.get("new").toString());
                }else{
                    news.setVisibility(View.GONE);
                }
            }
        });

    }

    private void setRecyclerView(){
        final ArrayList<String> application=(ArrayList<String>) Friend.get("friendapplication");
        final List<AVUser> userlist=new ArrayList<>();

        for(String id:application) {
            AVQuery<AVUser> avQuery = new AVQuery<>("_User");
            avQuery.getInBackground(id, new GetCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    userlist.add(avUser);
                    if(userlist.size()==application.size()){
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        RecyclerAdapter adapter=new RecyclerAdapter(getBaseContext(),userlist);
                        recyclerView.setAdapter(adapter);
                    }
                }
            });
        }
    }

    private void setresideMenu(){
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.top_gradient);
        resideMenu.attachToActivity(this);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        // create menu items;
        String titles[] = { "个人主页", "好友约", "寻找ta", "设置","切换账户" };
        int icon[] = { R.drawable.personal, R.drawable.chat, R.drawable.love, R.drawable.setting,R.drawable.switchuser };

        for (int i = 0; i < titles.length; i++){
            ResideMenuItem item = new ResideMenuItem(this,icon[i], titles[i]);
            switchOnclickListener(item,i);
            resideMenu.addMenuItem(item,  ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
        }
    }
    private void switchOnclickListener(ResideMenuItem item,int i){
        switch (i){
            case 0:
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getBaseContext(), PersonalMenu_Activity.class);
                        startActivity(intent);
                    }
                });
                break;
            case 1:
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LCChatKit.getInstance().open(CurrentUserObjectID, new AVIMClientCallback() {
                            @Override
                            public void done(AVIMClient avimClient, AVIMException e) {
                                if (null == e) {
                                    Intent intent=new Intent(getBaseContext(), Chat_Activity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
                break;
            case 2:
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getBaseContext(), Find_Activity.class);
                        startActivity(intent);
                    }
                });
                break;
            case 4:
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AVUser.logOut();// 清除缓存用户对象
                        AVUser currentUser = AVUser.getCurrentUser();
                        Intent intent=new Intent(getBaseContext(),Register_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
    }
}
