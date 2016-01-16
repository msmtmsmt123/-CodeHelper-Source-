package com.zprogrammer.tool.bean;

import cn.bmob.v3.*;

//反馈
public class Feedback extends BmobObject
{
	private String feedback;
	
	public Feedback(){
		this.setTableName("feedback");
	}
	public void setfeedback(String feedback){
		this.feedback=feedback;
	}

	public String getfeedback() {
        return feedback;
    }
}
