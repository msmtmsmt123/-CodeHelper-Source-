package com.zprogrammer.tool.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.zprogrammer.tool.MyApplication;
import com.zprogrammer.tool.R;
import com.zprogrammer.tool.bean.Data;

import cn.bmob.v3.listener.SaveListener;

public class NewActivity extends BaseActivity {
    //这个Activity是上传代码的界面
    private MyApplication me;
    private EditText editTitle, editMessage, editUser, editSummary;
    private String Title, Message, TitleMessage, User;
    private AlertDialog dialog;
//    private static final Pattern GREEN = Pattern.compile("//(.*)|/\\*(.|[\r\n])*?\\*/|=|==");
/*    private static final Pattern RED = Pattern.compile("true|false|^[1-9][0-9]*$|\".+?\"|\\d+(\\.\\d+)?");
    private static final Pattern BLUE = Pattern.compile("class|import|extends|package|implements|switch|while|break|case|private|public|protected|void|super|Bundle|this|static|final|if|else|return|new|catch|try");
    private static final Pattern OTHER = Pattern.compile(";|\\(|\\)|\\{|\\}|R\\..+?\\..+?|([\t|\n| ][A-Z].+? )|String |int |boolean |float |double |char |long |Override ");
	*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_write;
    }

    private void init() {
        me = (MyApplication) this.getApplication();
        editTitle = (EditText) findViewById(R.id.newlayoutEditText1);
        editMessage = (EditText) findViewById(R.id.textEditorView);
        editUser = (EditText) findViewById(R.id.newlayoutEditText3);
        editSummary = (EditText) findViewById(R.id.et_summary);
        editSummary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                int lines = editSummary.getLineCount();
                // 限制最大输入行数
                if (lines > 2) {
                    String str = s.toString();
                    int cursorStart = editSummary.getSelectionStart();
                    int cursorEnd = editSummary.getSelectionEnd();
                    if (cursorStart == cursorEnd && cursorStart < str.length() && cursorStart >= 1) {
                        str = str.substring(0, cursorStart - 1) + str.substring(cursorStart);
                    } else {
                        str = str.substring(0, s.length() - 1);
                    }
                    // setText会触发afterTextChanged的递归
                    editSummary.setText(str);
                    // setSelection用的索引不能使用str.length()否则会越界
                    editSummary.setSelection(editSummary.getText().length());
                }
            }
        });
        //hei();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_new:
                Title = editTitle.getText().toString();
                Message = editMessage.getText().toString();
                User = editUser.getText().toString();
                //拆分Message(内容)大于20。
                if (Message.length() > 20) {
                    TitleMessage = Message.substring(0, 20) + "…";
                } else {
                    TitleMessage = Message;
                }
                if (Title.equals("") || Message.equals("") || User.equals("") || Title == null || Message == null || User == null) {
                    me.showToast("其中一项为空，无法提交!");
                } else if (Title.length() < 5) {
                    me.showToast("标题小于5个字符,无法提交");
                } else if (Message.length() < 20) {
                    me.showToast("内容少于20个字符,无法提交");
                } else if (User.length() < 5) {
                    me.showToast("作者少于5个字符,无法提交");
                } else if (User.equals("安智zzyandzzy") && !me.getString("imei", "0").equalsIgnoreCase("864587026432063")) {
                    showDialog("提示:", "我靠，你小子行啊，敢问冒充我!", "我错了π_π", "嗯(´-ω-`)", -1);
                } else {
                    showDialog("提示:", "您真的要上传代码？\n有些重复或者内容不符的会被删除", "确定", "取消", 0);
                }
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog(String T, String M, String ok, String qx, final int i) {
        dialog = new AlertDialog.Builder(this).setTitle(T)
                .setMessage(M)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        switch (i) {
                            case 0:
                                me.editString("Title", Title);
                                saveData(Title, Message, TitleMessage, User, me.getString("imei", "0"));
                                finish();
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(qx, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    //上传数据Title为标题，Message为内容，TitleMessage为预览内容，User为用户名,imei为imei
    public void saveData(String Title, String Message, String TitleMessage, String User, String imei) {
        Data bmobdata = new Data();
        bmobdata.setTitle(Title);
        bmobdata.setMessage(Message);
        bmobdata.setTitleMessage(TitleMessage);
        bmobdata.setUser(User);
        bmobdata.setimei(imei);
        String summary = editSummary.getText().toString();
        if (summary.length() <= 0)
            summary = editMessage.getText().toString().substring(0, 30);
        bmobdata.setTitleMessage(summary);
        //默认false，需要在服务器上面改。
        bmobdata.setjing(false);
        bmobdata.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                me.showToast("上传代码成功!");
            }

            @Override
            public void onFailure(int code, String arg0) {
                me.showToast("上传代码失败:" + arg0);
            }
        });
    }

   /* public Editable highlight(final Editable e) {
        clearSpans(e);
        if (e.length() == 0) {
            return e;
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    for (Matcher m = BLUE.matcher(e); m.find(); ) {
                        e.setSpan(new ForegroundColorSpan(ZZ.COLOR_BLUE), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    for (Matcher m = OTHER.matcher(e); m.find(); ) {
                        e.setSpan(new ForegroundColorSpan(ZZ.COLOR_OTHER), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    for (Matcher m = RED.matcher(e); m.find(); ) {
                        e.setSpan(new ForegroundColorSpan(ZZ.COLOR_RED), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    for (Matcher m = GREEN.matcher(e); m.find(); ) {
                        e.setSpan(new ForegroundColorSpan(ZZ.COLOR_GREEN), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } catch (Exception ignored){
                }
            }
        }, 500);
        return e;
    }*/

 /*   // 清除颜色
    public void clearSpans(final Editable e) {
        {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    ForegroundColorSpan[] spans = e.getSpans(0, e.length(), ForegroundColorSpan.class);

                    for (int n = spans.length; n-- > 0; ) {
                        e.removeSpan(spans[n]);
                    }
                }

                {
                    BackgroundColorSpan[] spans = e.getSpans(0, e.length(), BackgroundColorSpan.class);

                    for (int n = spans.length; n-- > 0; ) {
                        e.removeSpan(spans[n]);
                    }
                }
            }, 0);
        }
    }

    public void hei() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                highlight(editMessage.getText());
                hei();
            }
        }, 500);
    }*/

/*	//正则表达式之替换字符
    public String zztable(String pa,String data,String th){
		Pattern pattern = Pattern.compile(pa);
		Matcher matcher = pattern.matcher(data);
		return matcher.replaceAll(th);
	}*/
}
