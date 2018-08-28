package com.example.cqupt_date.app.Chat;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfileProvider;
import cn.leancloud.chatkit.LCChatProfilesCallBack;

import static com.example.cqupt_date.app.MainActivity.User;


public class CustomUserProvider implements LCChatProfileProvider {
    private static List<LCChatKitUser> allUser = new ArrayList<LCChatKitUser>();
    ;
    private static CustomUserProvider customUserProvider;

    public synchronized static CustomUserProvider getInstance() {
        if (null == customUserProvider) {
            customUserProvider = new CustomUserProvider();
        }
        return customUserProvider;
    }

    public synchronized static CustomUserProvider getnewInstance() {
        customUserProvider = new CustomUserProvider(1);
        return customUserProvider;
    }
    private CustomUserProvider() {

    }

    private CustomUserProvider(int i){
        allUser = new ArrayList<LCChatKitUser>();
        if(AVUser.getCurrentUser()!=null) {
            String classid = AVUser.getCurrentUser().get("friendclassid").toString();
            AVQuery<AVObject> avQuery = new AVQuery<>("friend");
            avQuery.getInBackground(classid, new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (avObject.get("friendarray") != null) {

                        ArrayList<String> friendsid_list = (ArrayList<String>) avObject.get("friendarray");
                        for (String userId : friendsid_list) {
                            AVQuery<AVUser> userQuery = new AVQuery<>("_User");
                            userQuery.whereEqualTo("objectId", userId);
                            userQuery.findInBackground(new FindCallback<AVUser>() {
                                @Override
                                public void done(List<AVUser> list, AVException e) {
                                    if (e == null) {
                                        AVUser avUser = list.get(0);
                                        LCChatKitUser chatKitUser = new LCChatKitUser(avUser.getObjectId(), avUser.get("username").toString(), avUser.get("head_url").toString());
                                        allUser.add(chatKitUser);
                                        Log.d("TAG", "done: 1111111111111111111111111111111 " + allUser.get(0).getUserId());
                                    } else {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                }
            });
            LCChatKitUser chatKitUser = new LCChatKitUser(AVUser.getCurrentUser().getObjectId(), AVUser.getCurrentUser().get("username").toString(), AVUser.getCurrentUser().get("head_url").toString());
            allUser.add(chatKitUser);
        }
    }
    static {
        if(AVUser.getCurrentUser()!=null) {
            String classid = AVUser.getCurrentUser().get("friendclassid").toString();
            AVQuery<AVObject> avQuery = new AVQuery<>("friend");
            avQuery.getInBackground(classid, new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (avObject.get("friendarray") != null) {

                        ArrayList<String> friendsid_list = (ArrayList<String>) avObject.get("friendarray");
                        for (String userId : friendsid_list) {
                            AVQuery<AVUser> userQuery = new AVQuery<>("_User");
                            userQuery.whereEqualTo("objectId", userId);
                            userQuery.findInBackground(new FindCallback<AVUser>() {
                                @Override
                                public void done(List<AVUser> list, AVException e) {
                                    if (e == null) {
                                        AVUser avUser = list.get(0);
                                        LCChatKitUser chatKitUser = new LCChatKitUser(avUser.getObjectId(), avUser.get("username").toString(), avUser.get("head_url").toString());
                                        allUser.add(chatKitUser);
                                        Log.d("TAG", "done: 1111111111111111111111111111111 " + allUser.get(0).getUserId());
                                    } else {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                }
            });
            LCChatKitUser chatKitUser = new LCChatKitUser(AVUser.getCurrentUser().getObjectId(), AVUser.getCurrentUser().get("username").toString(), AVUser.getCurrentUser().get("head_url").toString());
            allUser.add(chatKitUser);
        }
    }

    public CustomUserProvider UpdateCustomUserProvider() {
        customUserProvider = new CustomUserProvider();
        return customUserProvider;
    }

    @Override
    public void fetchProfiles(List<String> list, LCChatProfilesCallBack callBack) {
        List<LCChatKitUser> userList = new ArrayList<LCChatKitUser>();
        if (allUser != null) {
            for (String userId : list) {
                for (LCChatKitUser user : allUser) {
                    if (user.getUserId().equals(userId)) {
                        userList.add(user);
                        break;
                    }
                }
            }
        }
        callBack.done(userList, null);
    }

    public List<LCChatKitUser> getAllUsers() {

        return allUser;

    }
}
