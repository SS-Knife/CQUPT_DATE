package com.example.cqupt_date.app.Find;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.cqupt_date.R;
import com.example.cqupt_date.app.Register.Fragment_Login;
import com.example.cqupt_date.app.Register.Fragment_Register;

import java.util.List;

import static com.example.cqupt_date.app.MainActivity.User;

/**
 * Created by 郝书逸 on 2018/8/25.
 */

public class Fragment_Love extends Fragment{
    private List<AVUser> userlist;
    private int flag=0;
    public static final int[] major_array={R.array.txarry,R.array.jsjarry,R.array.zdharry,
            R.array.xjarry,R.array.gdarry,R.array.rjarry,R.array.swarry,R.array.larry,
            R.array.jgarry,R.array.cmarry,R.array.wgyarry,R.array.gjarry,R.array.afarry,
            R.array.tyarry};
    Spinner SearchType,SearchCollege,SearchMajor;
    SearchView searchView;
    RecyclerView recyclerView;
    Button search;
    public static Fragment_Partner newInstance(){
        Fragment_Partner fragment = new Fragment_Partner();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_fragment_partner, container, false);
        search=view.findViewById(R.id.search);
        SearchType=view.findViewById(R.id.search_type);
        SearchCollege=view.findViewById(R.id.search_college);
        SearchMajor=view.findViewById(R.id.search_major);
        searchView=view.findViewById(R.id.searchview);
        recyclerView=view.findViewById(R.id.recycler);
        SearchCollege.setVisibility(View.GONE);
        SearchMajor.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
        searchView.setVisibility(View.VISIBLE);
        initData();
        return view;
    }
    private void initData(){
        SearchCollege.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String[] major=getResources().getStringArray(major_array[arg2]);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item, major);
                SearchMajor.setAdapter(dataAdapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        SearchType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                    case 1:
                        SearchCollege.setVisibility(View.GONE);
                        SearchMajor.setVisibility(View.GONE);
                        search.setVisibility(View.GONE);
                        searchView.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        SearchCollege.setVisibility(View.VISIBLE);
                        SearchMajor.setVisibility(View.VISIBLE);
                        search.setVisibility(View.VISIBLE);
                        searchView.setVisibility(View.GONE);
                        break;
                }
                flag = i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(TextUtils.isEmpty(query))
                {
                    Toast.makeText(getContext(), "请输入查找内容！", Toast.LENGTH_SHORT).show();
                }else {
                    String Key;
                    if(flag==0){
                        Key="username";
                    }else {
                        Key="studentid";
                    }
                    AVQuery<AVUser> avquery=new AVQuery<>("_User");
                    avquery.whereNotEqualTo("sex",User.get("sex"));
                    avquery.whereEqualTo("single",0);
                    avquery.whereNotEqualTo("objectId",User.getObjectId());
                    avquery.whereEqualTo(Key,query);
                    avquery.findInBackground(new FindCallback<AVUser>() {
                        @Override
                        public void done(List<AVUser> list, AVException e) {
                            if (e == null) {
                                if(list.size()==0){
                                    Toast.makeText(getContext(), "这里没有这个人喵", Toast.LENGTH_SHORT).show();

                                }else{
                                    userlist=list;
                                    setRecycler();
                                }

                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AVQuery<AVUser> avquery=new AVQuery<>("_User");
                avquery.whereNotEqualTo("sex",User.get("sex"));
                avquery.whereNotEqualTo("objectId",User.getObjectId());
                avquery.whereEqualTo("single",0);
                avquery.whereEqualTo("college",SearchCollege.getSelectedItemPosition());
                avquery.whereEqualTo("major",SearchMajor.getSelectedItemPosition());
                avquery.findInBackground(new FindCallback<AVUser>() {
                    @Override
                    public void done(List<AVUser> list, AVException e) {
                        if (e == null) {
                            if(list.size()==0){
                                Toast.makeText(getContext(), "这里没有这个人喵", Toast.LENGTH_SHORT).show();

                            }else{
                                userlist=list;
                                setRecycler();
                            }

                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    private void setRecycler(){
        RecyclerAdapter adapter=new RecyclerAdapter(getContext(),userlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}
