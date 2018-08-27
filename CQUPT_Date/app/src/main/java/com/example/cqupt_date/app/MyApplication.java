package com.example.cqupt_date.app;

import android.app.Application;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.example.cqupt_date.app.Chat.CustomUserProvider;

import cn.leancloud.chatkit.LCChatKit;

/**
 * Created by 郝书逸 on 2018/8/21.
 */

public class MyApplication extends Application{
    private final String APP_ID = "dwswDxcfcJ7iVpz1xwe3vgzU-gzGzoHsz";
    private final String APP_KEY = "ihiClAoa00vOCimdvJIwSYn8";

    @Override
    public void onCreate(){
        super.onCreate();
        AVOSCloud.initialize(this,APP_ID, APP_KEY);
        AVOSCloud.setDebugLogEnabled(true);
//    AVOSCloud.useAVCloudUS();
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        LCChatKit.getInstance().init(getApplicationContext(), APP_ID, APP_KEY);
        AVIMClient.setAutoOpen(true);
        PushService.setDefaultPushCallback(this, MainActivity.class);
        PushService.setAutoWakeUp(true);
        PushService.setDefaultChannelId(this, "default");

        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    // 保存成功
                    String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                    System.out.println("---  " + installationId);
                } else {
                    // 保存失败，输出错误信息
                    System.out.println("failed to save installation.");
                }
            }
        });


    }
}
