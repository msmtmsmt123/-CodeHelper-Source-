package com.zprogrammer.tool.bean;

import cn.bmob.v3.BmobObject;

public class Data extends BmobObject {
    //bmob数据表
    //不用管
    private String Title;
    private String Message;
    private String User;
    private String TitleMessage;
    private String imei;
    private Boolean jing;

    public Data() {
        this.setTableName("Data");
    }

    public String getTitle() {
        return Title;
    }

    public String getMessage() {
        return Message;
    }

    public String getTitleMessage() {
        return TitleMessage;
    }

    public String getUser() {
        return User;
    }

    public String getimei() {
        return imei;
    }

    public Boolean getjing() {
        return jing;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setTitleMessage(String TitleMessage) {
        this.TitleMessage = TitleMessage;
    }

    public void setUser(String user) {
        this.User = user;
    }

    public void setimei(String imei) {
        this.imei = imei;
    }

    public void setjing(Boolean jing) {
        this.jing = jing;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Data)) return false;
        Data data = (Data) o;
        return !(data.getObjectId() == null || getObjectId() == null) && getObjectId().equals(data.getObjectId());
    }

    @Override
    public int hashCode() {
        return getObjectId().hashCode();
    }
}
