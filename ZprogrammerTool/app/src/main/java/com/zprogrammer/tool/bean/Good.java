package com.zprogrammer.tool.bean;

import cn.bmob.v3.*;

public class Good extends BmobObject
{
	//这个为精品区在这个版本没用到。
    private String Title;
	private String Message;
    private Number index;

    public Good() {
        this.setTableName("good");
    }

    public String getTitle() {
        return Title;
    }

	public String getMessage(){
		return Message;
	}

	public Number getindex(){
		return index;
	}

	public void setMessage(String Message){
		this.Message=Message;
	}

	public void setTitle(String Title){
		this.Title=Title;
	}

	public void setindex(Number index){
		this.index=index;
	}
}
