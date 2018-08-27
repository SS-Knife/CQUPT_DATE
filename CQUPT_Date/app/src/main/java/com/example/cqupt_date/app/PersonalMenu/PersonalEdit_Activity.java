package com.example.cqupt_date.app.PersonalMenu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cqupt_date.app.MainActivity;
import com.example.cqupt_date.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static com.example.cqupt_date.app.Find.Fragment_Partner.major_array;


/**
 * Created by 郝书逸 on 2018/8/22.
 */

public class PersonalEdit_Activity extends AppCompatActivity implements View.OnClickListener{
    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。（生成bitmap貌似有时要报错？可试下把大小弄小点）
    private static int output_X = 300;
    private static int output_Y = 300;
    private Bitmap photo;

    private PopupWindow popupWindow;
    private TextView photograph, albums, cancel;

    Toolbar toolbar;
    AVUser User;
    Button register;
    EditText grade,studentid,home,age,phone,qq;
    Spinner college,major;
    RoundedImageView head;
    RadioGroup single;
    RadioButton yes,no;
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
        setContentView(R.layout.activity_personaledit);
        User = MainActivity.User;
        initView();
        initData();
    }
    private void initView(){
        register=findViewById(R.id.register);
        grade=findViewById(R.id.grade);
        college=findViewById(R.id.college);
        major=findViewById(R.id.major);
        studentid=findViewById(R.id.studentid);
        home=findViewById(R.id.home);
        age=findViewById(R.id.age);
        phone=findViewById(R.id.telephone);
        qq=findViewById(R.id.qq);
        single=findViewById(R.id.single);
        yes=findViewById(R.id.yes);
        no=findViewById(R.id.no);
        head=findViewById(R.id.head);
    }
    private void initData(){
        if(User.get("head_url")!=null){
            Glide.with(this)
                    .load(User.get("head_url").toString())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(head);
        }
        if(User.get("grade")!=null){
            grade.setText(User.get("grade").toString());
        }
        if(User.get("college")!=null){
            college.setSelection(User.getInt("college"));
        }
        if(User.get("major")!=null){
            major.setSelection(User.getInt("major"));
        }
        if(User.get("studentid")!=null){
            studentid.setText(User.get("studentid").toString());
        }
        if(User.get("home")!=null){
            home.setText(User.get("home").toString());
        }
        if(User.get("age")!=null){
            age.setText(User.get("age").toString());
        }
        if(User.get("phone")!=null){
            phone.setText(User.get("phone").toString());
        }
        if(User.get("qq")!=null){
            qq.setText(User.get("qq").toString());
        }
        if(User.get("single")!=null){
            if((int)User.get("single")==0) {
                yes.setChecked(true);
            }else{
                no.setChecked(true);
            }
        }
        college.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String[] majorstr=getResources().getStringArray(major_array[arg2]);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_dropdown_item, majorstr);
                major.setAdapter(dataAdapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupWindow();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.put("phone",phone.getText().toString());
                User.put("grade",grade.getText().toString());
                User.put("college",college.getSelectedItemPosition());
                User.put("major",major.getSelectedItemPosition());
                User.put("studentid",studentid.getText().toString());
                User.put("home",home.getText().toString());
                User.put("age",age.getText().toString());
                User.put("qq",qq.getText().toString());
                int single;
                if(yes.isChecked()){
                    single=0;
                }else{
                    single=1;
                }
                User.put("single",single);
                User.saveInBackground();
                Intent intent=new Intent(getBaseContext(),PersonalMenu_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    private void showPopupWindow() {

        if (popupWindow == null) {

            View view = View.inflate(this, R.layout.personaledit_popup, null);

            popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,

                    LayoutParams.MATCH_PARENT, true);

            initPop(view);

        }

        popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);

        popupWindow.setFocusable(true);

        popupWindow.setOutsideTouchable(true);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow

                .setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popupWindow.showAtLocation(head, Gravity.CENTER, 0, 0);



    }



    public void initPop(View view) {

        photograph = (TextView) view.findViewById(R.id.photograph);// 拍照

        albums = (TextView) view.findViewById(R.id.albums);// 相册

        cancel = (TextView) view.findViewById(R.id.cancel);// 取消

        photograph.setOnClickListener(this);

        albums.setOnClickListener(this);

        cancel.setOnClickListener(this);

    }





    @Override

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.photograph:

                choseHeadImageFromCameraCapture();

                break;

            case R.id.albums:

                choseHeadImageFromGallery();

                break;

            case R.id.cancel:

                popupWindow.dismiss();

                break;

        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this,"取消",Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;

            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(this,"没有SDCard!",Toast.LENGTH_LONG).show();
                }

                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            head.setImageBitmap(photo);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] datas = baos.toByteArray();
            final AVFile file=new AVFile("head.bitmap",datas);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    User.put("head_url",file.getUrl());
                    User.saveInBackground();
                }
            });

        }
    }

    // 将图片转换成base64编码
    public String getBase64(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //压缩的质量为60%
        bitmap.compress(Bitmap.CompressFormat.PNG, 60, out);
        //生成base64字符
        String base = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
        return base;
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent("android.media.action.IMAGE_CAPTURE");

        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }

        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }


    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent(Intent.ACTION_PICK);
        // 设置文件类型
        intentFromGallery.setType("image/*");
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }
    public boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

}
