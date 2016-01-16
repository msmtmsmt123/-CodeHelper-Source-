package com.zprogrammer.tool.db;

import android.content.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBHelper extends SQLiteOpenHelper {  

//这个为早期版本的数据库
    public static final int VERSION = 1;  
	
    //必须要有构造函数  
    public DBHelper(Context context, String name, CursorFactory factory,int version) {  
        super(context, name, factory, version);
	}  

    // 当第一次创建数据库的时候，调用该方法   
    public void onCreate(SQLiteDatabase db) {  
        String sql = "create table zzy_table(id int,Title varchar(30),Message varchar(2000),Time varchar(40))";
	    db.execSQL(sql);  
    }  

    //当更新数据库的时候执行该方法  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
    }  
}  
