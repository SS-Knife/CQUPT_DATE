package com.example.cqupt_date.app.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.example.cqupt_date.app.Chat.CustomUserProvider;
import com.example.cqupt_date.app.MainActivity;
import com.example.cqupt_date.R;


/**
 * Created by 郝书逸 on 2018/8/9.
 */

public class Fragment_Login extends Fragment{
    EditText username;
    EditText password;
    Button login;
    public static Fragment_Login newInstance(){
        Fragment_Login fragment = new Fragment_Login();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment_login , container, false);
        username=view.findViewById(R.id.username);
        password=view.findViewById(R.id.password);
        login=view.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogIn();
            }
        });
        return view;
    }
    private void LogIn(){
        AVUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    CustomUserProvider.getnewInstance();
                    Intent intent=new Intent(getContext(), MainActivity.class);
                    intent.putExtra("CurrentUserObjectID",avUser.getObjectId());
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
