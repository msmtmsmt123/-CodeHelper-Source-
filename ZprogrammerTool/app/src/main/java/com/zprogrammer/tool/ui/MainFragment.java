package com.zprogrammer.tool.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.telephony.TelephonyManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melnykov.fab.FloatingActionButton;
import com.zprogrammer.tool.BuildConfig;
import com.zprogrammer.tool.MyApplication;
import com.zprogrammer.tool.R;
import com.zprogrammer.tool.adapter.MainRecyclerAdapter;
import com.zprogrammer.tool.bean.Data;
import com.zprogrammer.tool.util.FileUtil;
import com.zprogrammer.tool.util.QueryUtil;
import com.zprogrammer.tool.util.UiUtil;

import org.apache.commons.collections4.list.SetUniqueList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, MainRecyclerAdapter.OnLoadingMoreListener {
    private static final String LOCAL_LIST_FILE_NAME = MyApplication.CACHE_PATH + File.separator + MainFragment.class.getSimpleName() + "_LOCAL_LIST.json";

    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerView;
    //method为公共的方法
    private MyApplication me;
    //点击ListView的item获取RelativeLayout
    private RelativeLayout r;
    //获取到TextView的Text值，用来标识id，可以检测用户点了那条数据。
    private TelephonyManager tm;

    private List<Data> datas;
    private Gson gson = new Gson();

    {
        //从缓存读取列表
        try {
            datas = SetUniqueList.setUniqueList((List<Data>) gson.fromJson(FileUtil.readString(LOCAL_LIST_FILE_NAME), new TypeToken<List<Data>>() {
            }.getType()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果读取列表失败则新建一个
        if (datas == null)
            datas = SetUniqueList.setUniqueList(new ArrayList<Data>());
    }

    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //初始化布局
        initView(rootView);
        //初始化数据
        initData();
        return rootView;
    }

    private void initView(View view) {
/*        listview = (ListView) view.findViewById(R.id.ListView);
        listview.setOnItemClickListener(this);*/
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(new MainRecyclerAdapter(getActivity(), datas, this));
        adapter = new SlideInBottomAnimationAdapter(alphaInAnimationAdapter);
        recyclerView.setAdapter(adapter);
        //初始化浮动按钮
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToRecyclerView(recyclerView);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(), NewActivity.class));
                //getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
/*                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), fab, "write_bg");
                Intent intent = new Intent(getActivity(), NewActivity.class);
                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());*/
                UiUtil.startActivity(getActivity(), new Intent(getActivity(), NewActivity.class), Pair.create(fab, "write_bg"));

            }
        });
        swipeLayout.setOnRefreshListener(this);
        //设定下拉刷新控件的颜色
        swipeLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_blue_bright);
    }

    public void initData() {
        //初始化数据
        me = (MyApplication) getActivity().getApplication();
        //这个方法是刷新数据的
        queryLosts(false);
        //获取imei并储存在本地
        me.editString("imei", getimei());
        //更新初始化，method的getBoolean方法返回一个boolean值用来确定用户是否开启了自动更新
        bmob(me.getBoolean("update", true));
    }

    private void bmob(boolean b) {
        //bmob更新项目
        if (b) {
            BmobUpdateAgent.setUpdateOnlyWifi(false);
            BmobUpdateAgent.update(getActivity());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //保存列表至缓存
        FileUtil.writeString(LOCAL_LIST_FILE_NAME, gson.toJson(new ArrayList<>(datas)));
    }

    @Override
    public void onRefresh() {
        //下拉刷新
        queryLosts(true);
    }

/*	private void han(long duration,final int i,final int p3){
        new Handler().postDelayed(new Runnable(){
				@Override
				public void run()
				{
					switch(i){
						case 1:
							me.editBoolean("dq_b",false);
							me.editint("dq_i",p3);
							intent(MainActivity.this,FindActivity.class);
							break;
					}
				}
			},duration);
	}
	
	public void intent(Context context,Class<?> context2){
		Intent i=new Intent(context,context2);
		startActivity(i);
	}
	*/

    //刷新数据
    private void queryLosts(final boolean cleanOld) {
        BmobQuery<Data> query = new BmobQuery<>();
        query.setLimit(MyApplication.QUERY_LIMIT);
        //bmob根据时间降序
        query.order("-createdAt");
        //query.order("objectid");
        if (!cleanOld && !datas.isEmpty())
            query.addWhereGreaterThan("createdAt", QueryUtil.parseTime(datas.get(0).getCreatedAt(), -1000));
        query.findObjects(getActivity(), new FindListener<Data>() {
            @Override
            public void onSuccess(List<Data> losts) {
                //lostAdapter.clear();
                if (losts == null || losts.size() == 0) {
                    return;
                }
                //把数据的长度记录下来
                me.editint("index", losts.size());
                if (cleanOld || !datas.isEmpty() && !losts.get(losts.size() - 1).equals(datas.get(0)))
                    datas.clear();
                datas.addAll(0, losts);
                //把数据加进LostAdapter
                //lostAdapter.addAll(losts);
                adapter.notifyDataSetChanged();

                //把下拉刷新设置为不可见
                swipeLayout.setRefreshing(false);
            }

            //当获取数据失败时bmob自动返回
            @Override
            public void onError(int code, String arg0) {
                me.showToast("获取数据失败：\n" + arg0);
                swipeLayout.setRefreshing(false);
            }
        });
    }

	/*private void queryGood(final TextView v,final TextView time,final RelativeLayout r,int index){
        BmobQuery<good> query = new BmobQuery<good>();
		query.addWhereEqualTo("index", index);
		query.findObjects(this, new FindListener<good>() {
				@Override
				public void onSuccess(List<good> object) {
					for (good data : object) {
						v.setText(data.getTitle());
						time.setText(data.getUpdatedAt());
						r.setVisibility(View.GONE);
					}
				}
				@Override
				public void onError(int code, String msg) {
				}
			});
	}*/

    //获取imei
    private String getimei() {
        tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }
/*
    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {

    }*/

    @Override
    public void onLoadingMore(final MainRecyclerAdapter mainRecyclerAdapter) {
        BmobQuery<Data> query = new BmobQuery<>();
        query.setLimit(MyApplication.QUERY_LIMIT);
        //bmob根据时间降序
        query.order("-createdAt");
        if (datas.isEmpty()) {
            return;
        } else {
            int skip = QueryUtil.countDatasByCreateAt(datas, datas.get(datas.size() - 1).getCreatedAt());
            query.setSkip(skip);
            BmobDate date = QueryUtil.parseTime(datas.get(datas.size() - 1).getCreatedAt(), 1000);
            query.addWhereLessThan("createdAt", date);
        }
        query.findObjects(getActivity(), new FindListener<Data>() {
            @Override
            public void onSuccess(List<Data> losts) {
                //lostAdapter.clear();
                if (losts == null || losts.size() == 0) {
                    mainRecyclerAdapter.setFooterState(MainRecyclerAdapter.FOOTER_STATE_NO_MORE);
                    return;
                }
                //把数据的长度记录下来
                me.editint("index", losts.size());
                //把数据加进dataList
                datas.addAll(losts);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String arg0) {
                me.showToast("获取数据失败：\n" + arg0);
                mainRecyclerAdapter.setFooterState(MainRecyclerAdapter.FOOTER_STATE_FAILED);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        int id = searchView.getContext().getResources().getIdentifier("search_src_text", "id", BuildConfig.APPLICATION_ID);
        EditText editText = (EditText) searchView.findViewById(id);
        editText.setTextColor(Color.WHITE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("query", query);
                UiUtil.startActivity(getActivity(), intent);
                searchItem.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
