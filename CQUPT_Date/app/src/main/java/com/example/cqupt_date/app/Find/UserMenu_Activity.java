package com.example.cqupt_date.app.Find;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cqupt_date.R;
import com.example.cqupt_date.app.MainActivity;
import com.example.cqupt_date.app.PersonalMenu.PersonalEdit_Activity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKitUser;

import static com.example.cqupt_date.app.Find.Fragment_Partner.major_array;
import static com.example.cqupt_date.app.MainActivity.User;

/**
 * Created by 郝书逸 on 2018/8/21.
 */

public class UserMenu_Activity extends AppCompatActivity{
    Boolean friendflag=true;
    Boolean loveflag=true;
    Button back,add,love;
    ImageView background,sex;
    String userid;
    AVObject currentFriend;
    AVObject userFriend;
    AVUser user,current;
    RoundedImageView head;
    TextView username,major,person,single,phone,qq,sthtosay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //安卓版本大于4.4设置状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_usermenu);
        Intent intent=getIntent();
        userid = intent.getStringExtra("userid");

        current=AVUser.getCurrentUser();
        String friendclassid = AVUser.getCurrentUser().get("friendclassid").toString();
        AVQuery<AVObject> avQuery = new AVQuery<>("friend");
        avQuery.getInBackground(friendclassid, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                currentFriend=avObject;
            }
        });

        AVQuery<AVUser> query=new AVQuery<>("_User");
        query.whereEqualTo("objectId",userid);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null) {
                    user = list.get(0);
                    String friendclassid=user.get("friendclassid").toString();
                    AVQuery<AVObject> avQuery = new AVQuery<>("friend");
                    avQuery.getInBackground(friendclassid, new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            userFriend=avObject;
                            initView();
                            initData();
                        }
                    });

                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView(){
        love=findViewById(R.id.love);
        add=findViewById(R.id.add);
        back=findViewById(R.id.back);
        background=findViewById(R.id.background);
        sex=findViewById(R.id.sex);
        head=findViewById(R.id.head);
        username=findViewById(R.id.username);
        sthtosay=findViewById(R.id.sthtosay);
        major=findViewById(R.id.major);
        person=findViewById(R.id.person);
        single=findViewById(R.id.single);
        phone=findViewById(R.id.phone);
        qq=findViewById(R.id.qq);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }
    private void initData(){
        friend();
        date();

        if(user.get("head_url")!=null) {
            Glide.with(getBaseContext())
                    .load(user.get("head_url").toString())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(head);
        }
        username.setText(user.getUsername());
        String sexStr;
        if((int)user.get("sex")==0){
            sexStr="他";
            sex.setImageResource(R.drawable.male);
        }else{
            sexStr="她";
            sex.setImageResource(R.drawable.female);
        }
        if(user.get("head_url")!=null){
            Glide.with(this)
                    .load(user.get("head_url").toString())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(head);
        }
        if(user.get("sthtosay")!=null){
            sthtosay.setText(user.get("sthtosay").toString());
        }
        String majorStr="";
        int i=0;
        if(user.get("grade")!=null){
            majorStr=majorStr+user.get("grade").toString()+" ";
            i=1;
        }
        if(user.get("college")!=null){
            majorStr=majorStr+getResources().getStringArray(R.array.collegearry)[user.getInt("college")]+" ";
            i=1;
        }
        if(user.get("major")!=null){
            majorStr=majorStr+getResources().getStringArray(major_array[user.getInt("college")])[user.getInt("major")]+" ";
            i=1;
        }
        if(i==1){
            major.setText(majorStr);
        }
        i=0;
        String personStr="";
        if(user.get("home")!=null){
            personStr=personStr+user.get("home").toString()+" ";
            i=1;
        }
        if(user.get("age")!=null){
            personStr=personStr+user.get("age").toString()+" ";
            i=1;
        }
        if(user.get("studentid")!=null){
            personStr=personStr+user.get("studentid").toString();
            i=1;
        }
        if(i==1){
            person.setText(personStr);
        }

        if(user.get("phone")!=null){
            phone.setText(user.get("phone").toString());
        }
        if(user.get("qq")!=null){
            qq.setText(user.get("qq").toString());
        }
        if(user.get("single")!=null){
            if((int)user.get("single")==0) {
                single.setText(sexStr+"还是单身");
            }else{
                single.setText(sexStr+"已经脱单啦");
            }
        }else{
            single.setText(sexStr+"还是单身");
        }
    }



    private void friend(){
        if(userFriend.get("friendarray")!=null){
            ArrayList<Object> idarray=(ArrayList<Object>) userFriend.get("friendarray");
            final ArrayList<Object> applyarray=(ArrayList<Object>) userFriend.get("friendapplication");
            int flag=0;
            for(Object friendid: idarray){
                if(current.getObjectId().equals(friendid)){
                    flag=1;
                    break;
                }
            }
            for(Object applyid: applyarray){
                if(current.getObjectId().equals(applyid)){
                    flag=2;
                    break;
                }
            }
            if(flag==1){
                add.setBackgroundColor(getResources().getColor(R.color.app_white));
                add.setText("已是好友");
            }else if(flag==2) {
                add.setBackgroundColor(getResources().getColor(R.color.app_white));
                add.setText("申请过了");
            }else{
                if(user.getObjectId().equals(current.getObjectId())){
                    add.setBackgroundColor(getResources().getColor(R.color.app_white));
                    add.setText("这是你啊");
                }else {

                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(friendflag) {
                                applyarray.add(current.getObjectId());
                                userFriend.put("friendapplication", applyarray);
                                userFriend.saveInBackground();
                                Toast.makeText(getBaseContext(), "向" + user.getUsername() + "提交好友申请", Toast.LENGTH_SHORT).show();
                                add.setBackgroundColor(getResources().getColor(R.color.app_white));
                                add.setText("申请过了");
                                friendflag=false;
                            }
                        }
                    });
                }
            }
        }else{
            if(user.getObjectId().equals(current.getObjectId())){
                add.setBackgroundColor(getResources().getColor(R.color.app_white));
                add.setText("这是你啊");
            }else {
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<Object> applyarray=new ArrayList<>();
                        applyarray.add(current.getObjectId());

                        AVObject object = new AVObject("DataTypes");
                        ArrayList<Object> arrayList = new ArrayList<>();
                        int number = 2015;
                        String string = number + " 年度音乐排行";
                        arrayList.add(number);
                        arrayList.add(string);
                        object.put("testArrayList", arrayList);
                        object.saveInBackground();

                        userFriend.put("friendapplication",applyarray);
                        userFriend.saveInBackground();
                        Toast.makeText(getBaseContext(),"向"+user.getUsername()+"提交好友申请",Toast.LENGTH_SHORT).show();
                        add.setBackgroundColor(getResources().getColor(R.color.app_white));
                        add.setText("申请过了");
                    }
                });
            }
        }
    }




    private void date(){
        if((int)user.get("single")==1||user.get("sex").equals(current.get("sex"))){
            love.setAlpha(0f);
        }else{
            int x=0;
            if( (int)current.get("single")==1){
                x=2;
            }
            ArrayList<String> datearray=new ArrayList<String>();
            if(userFriend.get("dateapplication")!=null){
                datearray=(ArrayList<String>) userFriend.get("dateapplication");
                for(String dateid:datearray){
                    if(current.getObjectId().equals(dateid)){
                        x=1;
                    }
                }
            }
            switch (x){
                case 0:
                    final ArrayList<String> finalDatearray = datearray;
                    love.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(loveflag) {
                                finalDatearray.add(current.getObjectId());
                                userFriend.put("dateapplication", finalDatearray);
                                userFriend.saveInBackground();

                                for (String id : (ArrayList<String>) currentFriend.get("dateapplication")) {
                                    if (id .equals( user.getObjectId())) {
                                        Toast.makeText(getBaseContext(), "牵手成功，快去找ta吧", Toast.LENGTH_LONG);
                                    }
                                }
                                love.setText("\uD83D\uDC97");
                                loveflag=false;
                            }
                        }
                    });
                    break;
                case 1:
                    love.setText("\uD83D\uDC97");
                    break;
                case 2:
                    love.setText("不可以花心哦");
                    break;
            }
        }
    }
}
