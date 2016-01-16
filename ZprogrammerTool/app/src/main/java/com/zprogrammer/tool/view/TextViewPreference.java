package com.zprogrammer.tool.view;

import android.content.*;
import android.preference.DialogPreference;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.zprogrammer.tool.*;

public class TextViewPreference extends DialogPreference
{
	private TextView TextView1;
	private Context c;
	
	public TextViewPreference(Context context, AttributeSet attrs){
		super(context,attrs);
		c=context;
	}
	
	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		TextView1 = (TextView)view.findViewById(R.id.textviewTextView1);
		TextView1.setText(R.string.about);
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			}else{
		}
	}
	
}
