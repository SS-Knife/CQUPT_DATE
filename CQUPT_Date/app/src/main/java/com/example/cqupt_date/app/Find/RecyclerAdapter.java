package com.example.cqupt_date.app.Find;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cqupt_date.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import static com.example.cqupt_date.app.Find.Fragment_Partner.major_array;


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
        CardView usercard;
        TextView username,studentid,major;
        ImageView background;
        RoundedImageView head;
        ViewHolder(View view) {
            super(view);
            usercard=view.findViewById(R.id.usercard);
            username=view.findViewById(R.id.username);
            studentid=view.findViewById(R.id.studentid);
            major=view.findViewById(R.id.major);
            background=view.findViewById(R.id.background);
            head=view.findViewById(R.id.head);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_fragment_user,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final RecyclerAdapter.ViewHolder holder, final int position) {
        final AVUser User=list.get(position);
        holder.username.setText(User.getUsername());
        if((int)list.get(position).get("sex")==1){
            holder.background.setImageResource(R.drawable.shape_female);
            holder.head.setBorderColor(context.getResources().getColor(R.color.app_pink));
        }
        if(list.get(position).get("head_url")!=null) {
            Glide.with(context)
                    .load(User.get("head_url").toString())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.head);
        }
        String majorStr="";
        int i=0;
        if(User.get("college")!=null){
            majorStr=majorStr+context.getResources().getStringArray(R.array.collegearry)[User.getInt("college")]+" ";
            i=1;
        }
        if(User.get("major")!=null){
            majorStr=majorStr+context.getResources().getStringArray(major_array[User.getInt("college")])[User.getInt("major")]+" ";
            i=1;
        }
        if(i==1){
            holder.major.setText(majorStr);
        }
        if(User.get("studentid")!=null){
            holder.studentid.setText(User.get("studentid").toString());
        }
        holder.usercard.setOnClickListener(new mOnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,UserMenu_Activity.class);
                intent.putExtra("userid",list.get(position).getObjectId());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        //获取item的数量（可以从url数量获得）
        return list.size();
    }
}
