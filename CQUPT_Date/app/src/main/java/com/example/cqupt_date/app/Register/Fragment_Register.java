package com.example.cqupt_date.app.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.example.cqupt_date.app.MainActivity;
import com.example.cqupt_date.R;

import java.util.ArrayList;


/**
 * Created by 郝书逸 on 2018/8/9.
 */

public class Fragment_Register extends Fragment{

    int temp;
    RadioGroup sex;
    RadioButton female;
    RadioButton male;
    EditText username;
    EditText password;
    Button register;
    public static Fragment_Register newInstance(){
        Fragment_Register fragment = new Fragment_Register();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment_register, container, false);
        sex = view.findViewById(R.id.sex);
        female = view.findViewById(R.id.female);
        male = view.findViewById(R.id.male);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);

        register = view.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
        sex.setOnCheckedChangeListener(new OnCheckedChangeListenerImp());
        return view;
    }
    private class OnCheckedChangeListenerImp implements RadioGroup.OnCheckedChangeListener {

        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(male.getId()==checkedId){
                temp=0;
            }
            else if(female.getId()==checkedId){
                temp=1;
            }
        }

    }
    private void Register(){
        final AVUser user = new AVUser();// 新建 AVUser 对象实例
        user.setUsername(username.getText().toString());// 设置用户名
        user.setPassword(password.getText().toString());// 设置密码
        user.put("sex",temp);
        final AVObject object=new AVObject("friend");
        ArrayList<Object> array=new ArrayList<>();
        object.put("dateapplication",array);
        object.put("friendapplication",array);
        object.put("friendarray",array);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 存储成功
                    user.put("friendclassid",object.getObjectId());
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                // 注册成功，把用户对象赋值给当前用户 AVUser.getCurrentUser()
                                Intent intent=new Intent(getContext(), MainActivity.class);
                                intent.putExtra("CurrentUserObjectID",user.getObjectId());
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                // 失败的原因可能有多种，常见的是用户名已经存在。
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // 失败的话，请检查网络环境以及 SDK 配置是否正确
                }
            }
        });

    }
}
