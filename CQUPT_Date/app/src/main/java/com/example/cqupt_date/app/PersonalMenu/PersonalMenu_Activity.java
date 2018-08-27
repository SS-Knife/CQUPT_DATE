package com.example.cqupt_date.app.PersonalMenu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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


import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cqupt_date.app.MainActivity;
import com.example.cqupt_date.R;
import com.makeramen.roundedimageview.RoundedImageView;

import static com.example.cqupt_date.app.Find.Fragment_Partner.major_array;

/**
 * Created by 郝书逸 on 2018/8/21.
 */

public class PersonalMenu_Activity extends AppCompatActivity{
    Button back;
    ScrollView scrollView;
    ImageView background,sex;
    AVUser User;
    RoundedImageView head;
    TextView username,major,person,single,phone,qq,edit;
    EditText sthtosay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //安卓版本大于4.4设置状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_personalmenu);
        User = MainActivity.User;
        initView();
        initData();


    }
    private void initView(){
        back=findViewById(R.id.back);
        scrollView=findViewById(R.id.content);
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
        edit=findViewById(R.id.edit);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(),PersonalEdit_Activity.class);
                startActivity(intent);
                finish();
            }
        });
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.setFocusable(true);
                scrollView.setFocusableInTouchMode(true);
                scrollView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(sthtosay.getWindowToken(), 0);
                User.put("sthtosay",sthtosay.getText());
                User.saveInBackground();
                return false;
            }
        });
    }
    private void initData(){
        if(User.get("head_url")!=null) {
            Glide.with(getBaseContext())
                    .load(User.get("head_url").toString())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(head);
        }
        username.setText(User.getUsername());
        String sexStr;
        if((int)User.get("sex")==0){
            sexStr="他";
            sex.setImageResource(R.drawable.male);
        }else{
            sexStr="她";
            sex.setImageResource(R.drawable.female);
        }
        if(User.get("head_url")!=null){
            Glide.with(this)
                    .load(User.get("head_url").toString())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(head);
        }
        if(User.get("sthtosay")!=null){
            sthtosay.setText(User.get("sthtosay").toString());
        }
        String majorStr="";
        int i=0;
        if(User.get("grade")!=null){
            majorStr=majorStr+User.get("grade").toString()+" ";
            i=1;
        }
        if(User.get("college")!=null){
            majorStr=majorStr+getResources().getStringArray(R.array.collegearry)[User.getInt("college")]+" ";
            i=1;
        }
        if(User.get("major")!=null){
            majorStr=majorStr+getResources().getStringArray(major_array[User.getInt("college")])[User.getInt("major")]+" ";
            i=1;
        }
        if(i==1){
            major.setText(majorStr);
        }
        i=0;
        String personStr="";
        if(User.get("home")!=null){
            personStr=personStr+User.get("home").toString()+" ";
            i=1;
        }
        if(User.get("age")!=null){
            personStr=personStr+User.get("age").toString()+" ";
            i=1;
        }
        if(User.get("studentid")!=null){
            personStr=personStr+User.get("studentid").toString();
            i=1;
        }
        if(i==1){
            person.setText(personStr);
        }

        if(User.get("phone")!=null){
            phone.setText(User.get("phone").toString());
        }
        if(User.get("qq")!=null){
            qq.setText(User.get("qq").toString());
        }
        if(User.get("single")!=null){
            if((int)User.get("single")==0) {
                single.setText(sexStr+"还是单身");
            }else{
                single.setText(sexStr+"已经脱单啦");
            }
        }else{
            single.setText(sexStr+"还是单身");
        }
    }
}
