package com.zprogrammer.tool.db;

import android.content.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory; 

public class GoodHelper extends SQLiteOpenHelper
 {  

    public static final int VERSION = 1;  

    //必须要有构造函数  
    public GoodHelper(Context context, String name, CursorFactory factory,int version) {  
        super(context, name, factory, version);
	}  

    // 当第一次创建数据库的时候，调用该方法   
    public void onCreate(SQLiteDatabase db) {  
        String sql = "create table good_table(id int,Title varchar(40),Message varchar(2000))";
		db.execSQL(sql);  
    }  

    //当更新数据库的时候执行该方法  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
    }  
}
