package com.zprogrammer.tool.view;

import android.app.*;
import android.support.v4.view.*;
import android.view.*;
import java.util.*;

public class MyPagerAdapter extends PagerAdapter
{
	private List<View> mListViews;
	Activity activity;
	
	public MyPagerAdapter(List<View> mListViews,Activity activity){
		this.mListViews=mListViews;
		this.activity=activity;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		container.removeView(mListViews.get(position));//删除页卡
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		container.addView(mListViews.get(position));//添加页卡
		return mListViews.get(position);
	}

	//页卡有多少
	@Override
	public int getCount()
	{
		return mListViews.size();
	}
	
	@Override
	public boolean isViewFromObject(View p1, Object p2)
	{
		return p1==p2;
	}

}
