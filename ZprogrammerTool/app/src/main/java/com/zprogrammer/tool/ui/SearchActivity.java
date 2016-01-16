/*
 * Copyright 2015. Alex Zhang aka. ztc1997
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zprogrammer.tool.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.zprogrammer.tool.BuildConfig;
import com.zprogrammer.tool.MyApplication;
import com.zprogrammer.tool.R;
import com.zprogrammer.tool.adapter.MainRecyclerAdapter;
import com.zprogrammer.tool.bean.Data;
import com.zprogrammer.tool.util.QueryUtil;

import org.apache.commons.collections4.list.SetUniqueList;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

public class SearchActivity extends BaseActivity implements MainRecyclerAdapter.OnLoadingMoreListener {
    private RecyclerView recyclerView;
    private String queryText;
    private List<Data> datas = SetUniqueList.setUniqueList(new ArrayList<Data>());
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queryText = getIntent().getStringExtra("query");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(new MainRecyclerAdapter(this, datas, this));
        adapter = new SlideInBottomAnimationAdapter(alphaInAnimationAdapter);
        recyclerView.setAdapter(adapter);
        queryDatas(null);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_search;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        int id = searchView.getContext().getResources().getIdentifier("search_src_text", "id", BuildConfig.APPLICATION_ID);
        final EditText editText = (EditText) searchView.findViewById(id);
        editText.setTextColor(Color.WHITE);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                ActivityCompat.finishAfterTransition(SearchActivity.this);
                return true;
            }
        });
        searchItem.expandActionView();
        searchView.setQuery(queryText, false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryText = query;
                queryDatas(null);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public void onLoadingMore(MainRecyclerAdapter adapter) {
        queryDatas(adapter);
    }

    private void queryDatas(final MainRecyclerAdapter mainRecyclerAdapter) {
        setTitle(getString(R.string.title_activity_search_result, queryText));
        BmobQuery<Data> query = new BmobQuery<>();
        query.setLimit(MyApplication.QUERY_LIMIT);
        //bmob根据时间降序
        query.order("-createdAt");
        //query.order("objectid");
        query.addWhereContains("Title", queryText);
        if (mainRecyclerAdapter == null) {
            datas.clear();
        } else if (!datas.isEmpty()) {
            query.addWhereLessThan("createdAt", QueryUtil.parseTime(datas.get(datas.size() - 1).getCreatedAt(), 1000));
            query.setSkip(QueryUtil.countDatasByCreateAt(datas, datas.get(datas.size() - 1).getCreatedAt()));
        }
        query.findObjects(this, new FindListener<Data>() {
            @Override
            public void onSuccess(List<Data> losts) {
                //lostAdapter.clear();
                if (losts == null || losts.size() == 0) {
                    if (mainRecyclerAdapter != null)
                        mainRecyclerAdapter.setFooterState(MainRecyclerAdapter.FOOTER_STATE_NO_MORE);
                    return;
                }
                if (!datas.isEmpty() && !losts.get(losts.size() - 1).equals(datas.get(0)))
                    datas.clear();
                datas.addAll(0, losts);
                //把数据加进LostAdapter
                //lostAdapter.addAll(losts);
                adapter.notifyDataSetChanged();
            }

            //当获取数据失败时bmob自动返回
            @Override
            public void onError(int code, String arg0) {
                if (mainRecyclerAdapter != null)
                    mainRecyclerAdapter.setFooterState(MainRecyclerAdapter.FOOTER_STATE_FAILED);
                Toast.makeText(SearchActivity.this, arg0, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
