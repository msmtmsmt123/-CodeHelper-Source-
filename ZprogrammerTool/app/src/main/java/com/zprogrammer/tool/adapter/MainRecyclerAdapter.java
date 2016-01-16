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

package com.zprogrammer.tool.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zprogrammer.tool.R;
import com.zprogrammer.tool.bean.Data;
import com.zprogrammer.tool.ui.FindActivity;
import com.zprogrammer.tool.util.UiUtil;

import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter {
    public static final int FOOTER_STATE_LOADING = 0;
    public static final int FOOTER_STATE_NO_MORE = 1;
    public static final int FOOTER_STATE_FAILED = 2;
    public static final int FOOTER_STATE_HIDDEN = 3;
    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_FOOTER = 1;

    private Activity activity;

    private List<Data> dataList;
    private FooterViewHolder footerViewHolder;
    private int footerState;
    private OnLoadingMoreListener listener;
    private int hintNoMore = R.string.footer_msg_no_more;

    public MainRecyclerAdapter(Activity activity, List<Data> dataList, OnLoadingMoreListener listener) {
        this.activity = activity;
        this.dataList = dataList;
        this.listener = listener;
        @SuppressLint("InflateParams")
        View footerView = LayoutInflater.from(activity).inflate(R.layout.footer_recycler_view, null, false);
        footerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        footerViewHolder = new FooterViewHolder(footerView);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (footerState == FOOTER_STATE_LOADING) return;
                setFooterState(FOOTER_STATE_LOADING);
                if (MainRecyclerAdapter.this.listener != null) {
                    MainRecyclerAdapter.this.listener.onLoadingMore(MainRecyclerAdapter.this);
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        switch (i) {
            case VIEW_TYPE_NORMAL:
                View normalView = LayoutInflater.from(activity).inflate(R.layout.itew_recycler_main, viewGroup, false);
                return new NormalViewHolder(normalView);

            case VIEW_TYPE_FOOTER:
                return footerViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof NormalViewHolder) {
            final NormalViewHolder holder = (NormalViewHolder) viewHolder;
            final Data data = dataList.get(i);

            holder.title.setText(data.getTitle());
            if (data.getTitleMessage() != null)
                holder.message.setText(data.getTitleMessage());
            else
                holder.message.setText(data.getMessage().substring(0, 40));
            holder.time.setText(data.getCreatedAt());
            // holder.id.setText(data.getObjectId());
            holder.user.setText(data.getUser() != null ? data.getUser() : data.getObjectId());
            if (data.getjing() != null && data.getjing()) {
                holder.title.setTextColor(Color.RED);
                holder.user.setTextColor(Color.RED);
                holder.message.setTextColor(Color.RED);
                holder.user.append("被管理员临时高亮!");
            } else {
                holder.title.setTextColor(Color.rgb(0, 136, 255));
                holder.user.setTextColor(Color.rgb(140, 140, 140));
                holder.message.setTextColor(Color.rgb(140, 140, 140));
            }
            holder.bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击item的事件
                    //me.editBoolean("dq_b", true);
                    //跳转到FindActivity查看详细的代码
                    Intent id = new Intent(activity, FindActivity.class);
                    id.putExtra("data", data);
                    UiUtil.startActivity(activity, id, Pair.create(holder.bg, "element_bg"));
                }
            });
        } else if (viewHolder instanceof FooterViewHolder) {
            setFooterState(FOOTER_STATE_LOADING);
            if (listener != null)
                listener.onLoadingMore(this);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? VIEW_TYPE_FOOTER : VIEW_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if (dataList == null) return 0;
        return dataList.size() + 1;
    }

    public void setFooterState(int footerState) {
        this.footerState = footerState;
        switch (footerState) {
            case FOOTER_STATE_HIDDEN:
                footerViewHolder.root.setVisibility(View.GONE);
                break;

            case FOOTER_STATE_LOADING:
                footerViewHolder.root.setVisibility(View.VISIBLE);
                footerViewHolder.progress.setVisibility(View.VISIBLE);
                footerViewHolder.msg.setText(R.string.footer_msg_loading);
                break;

            case FOOTER_STATE_NO_MORE:
                footerViewHolder.root.setVisibility(View.VISIBLE);
                footerViewHolder.progress.setVisibility(View.GONE);
                footerViewHolder.msg.setText(hintNoMore);
                break;

            case FOOTER_STATE_FAILED:
                footerViewHolder.root.setVisibility(View.VISIBLE);
                footerViewHolder.progress.setVisibility(View.GONE);
                footerViewHolder.msg.setText(R.string.footer_msg_failed);
                break;
        }
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }

    public void setHintNoMore(int hintNoMore) {
        this.hintNoMore = hintNoMore;
    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        public TextView title, message, time, user;
        public CardView bg;

        public NormalViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.list_title);
            message = (TextView) itemView.findViewById(R.id.list_message);
            time = (TextView) itemView.findViewById(R.id.list_time);
            user = (TextView) itemView.findViewById(R.id.list_user);
            bg = (CardView) itemView.findViewById(R.id.element_bg);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public ViewGroup root;
        public TextView msg;
        public ProgressBar progress;

        public FooterViewHolder(View itemView) {
            super(itemView);
            root = (ViewGroup) itemView.findViewById(R.id.footer_root);
            msg = (TextView) itemView.findViewById(R.id.footer_msg);
            progress = (ProgressBar) itemView.findViewById(R.id.footer_progress);
        }
    }

    public interface OnLoadingMoreListener {
        void onLoadingMore(MainRecyclerAdapter adapter);
    }
}
