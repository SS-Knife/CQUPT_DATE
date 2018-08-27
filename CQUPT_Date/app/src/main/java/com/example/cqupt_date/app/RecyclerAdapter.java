package com.example.cqupt_date.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cqupt_date.R;
import com.example.cqupt_date.app.Find.UserMenu_Activity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import static com.example.cqupt_date.app.Find.Fragment_Partner.major_array;
import static com.example.cqupt_date.app.MainActivity.Friend;
import static com.example.cqupt_date.app.MainActivity.User;


/**
 * Created by 郝书逸 on 2018/8/13.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    //添加数据，如每个item中显示的图片文字或其url
    interface mOnClickListener extends View.OnClickListener{};
    private List<AVUser> list;
    private Context context;

    RecyclerAdapter(Context context, List<AVUser> list){
        this.list=list;
        this.context=context;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView username,studentid;
        RoundedImageView head;
        Button accept,refuse;
        ViewHolder(View view) {
            super(view);
            accept=view.findViewById(R.id.accept);
            refuse=view.findViewById(R.id.refuse);
            username=view.findViewById(R.id.username);
            studentid=view.findViewById(R.id.studentid);
            head=view.findViewById(R.id.head);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_fragment_application,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final RecyclerAdapter.ViewHolder holder, final int position) {
        final AVUser user=list.get(position);
        holder.username.setText(user.getUsername());
        if(list.get(position).get("head_url")!=null) {
            Glide.with(context)
                    .load(user.get("head_url").toString())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.head);
        }
        if(user.get("studentid")!=null){
            holder.studentid.setText(user.get("studentid").toString());
        }
        holder.head.setOnClickListener(new mOnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,UserMenu_Activity.class);
                intent.putExtra("userid",list.get(position).getObjectId());
                context.startActivity(intent);
            }
        });
        holder.accept.setOnClickListener(new mOnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<Object> application=(ArrayList<Object>)Friend.get("friendapplication");
                AVQuery<AVObject> avQuery = new AVQuery<>("friend");
                avQuery.getInBackground(user.get("friendclassid").toString(), new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        ArrayList<Object> array=(ArrayList<Object>)avObject.get("friendarray");
                        array.add(application.get(position));
                        avObject.put("friendarray",array);
                        avObject.saveInBackground();
                        array=(ArrayList<Object>)Friend.get("friendarray");
                        array.add(application.get(position));
                        application.remove(position);
                        Friend.put("friendapplication",application);
                        Friend.put("friendarray",array);
                        Friend.saveInBackground();
                        removeData(position);
                    }
                });

            }
        });
        holder.refuse.setOnClickListener(new mOnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Object> application=(ArrayList<Object>)Friend.get("friendapplication");
                application.remove(position);
                Friend.put("friendapplication",application);
                Friend.saveInBackground();
                removeData(position);
            }
        });
    }

    public void removeData(int position) {
        list.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        //获取item的数量（可以从url数量获得）
        return list.size();
    }
}
