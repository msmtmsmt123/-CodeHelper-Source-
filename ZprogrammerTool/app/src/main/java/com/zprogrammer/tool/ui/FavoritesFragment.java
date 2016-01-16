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

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zprogrammer.tool.MyApplication;
import com.zprogrammer.tool.R;
import com.zprogrammer.tool.adapter.MainRecyclerAdapter;
import com.zprogrammer.tool.bean.Data;
import com.zprogrammer.tool.util.FavoritesUtil;

import java.io.File;
import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

public class FavoritesFragment extends Fragment implements MainRecyclerAdapter.OnLoadingMoreListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private MainRecyclerAdapter mainRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainRecyclerAdapter = new MainRecyclerAdapter(getActivity(), null, this);
        mainRecyclerAdapter.setHintNoMore(R.string.footer_msg_no_more_favorites);
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mainRecyclerAdapter);
        adapter = new SlideInBottomAnimationAdapter(alphaInAnimationAdapter);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onLoadingMore(MainRecyclerAdapter adapter) {
        adapter.setFooterState(MainRecyclerAdapter.FOOTER_STATE_NO_MORE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainRecyclerAdapter.setDataList(FavoritesUtil.readDataListFromFavorites());
        adapter.notifyDataSetChanged();
    }
}
