package com.zprogrammer.tool.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.zprogrammer.tool.MyApplication;
import com.zprogrammer.tool.R;
import com.zprogrammer.tool.bean.Data;
import com.zprogrammer.tool.util.FavoritesUtil;

public class FindActivity extends BaseActivity {
    public static final boolean ADMIN = false;

    private MyApplication me;
    //内容
    private TextView ed1;
    private AlertDialog dialog;
    private Data data;
    //代码高亮
    //private SpannableString spannableString;
    private String str = "";
    private static final String START = "start";
    private static final String END = "end";
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_find;
    }

    private void init() {
        me = (MyApplication) this.getApplication();
        //获取Intent
        data = (Data) getIntent().getSerializableExtra("data");
        setTitle(data.getTitle());
        ed1 = (TextView) findViewById(R.id.textEditorView);
        ed1.setText(data.getMessage());
        //((ViewGroup)ed1.getParent()).measure(0, 0);
        //if (me.getBoolean("dq_b", true)) {
        ed1.post(new Runnable() {
            @Override
            public void run() {
                if (ed1.getVisibility() == View.GONE) {
                    ed1.setVisibility(View.VISIBLE);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        android.animation.Animator animator = ViewAnimationUtils.createCircularReveal(ed1,
                                getWindow().getDecorView().getWidth() / 2,
                                getWindow().getDecorView().getHeight() / 2 - toolbar.getHeight(),
                                0,
                                Math.min(getWindow().getDecorView().getWidth(), getWindow().getDecorView().getHeight()));
                        animator.setInterpolator(new LinearInterpolator());
                        animator.setDuration(500);
                        animator.start();
                    } else {
                        AnimationSet set = new AnimationSet(true);
                        set.setInterpolator(new DecelerateInterpolator());
                        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 200, 0);
                        set.addAnimation(alphaAnimation);
                        set.addAnimation(translateAnimation);
                        set.setDuration(300);
                        ed1.startAnimation(set);
                    }
                }
            }
        });
        //代码高亮
/*		spannableString = new SpannableString(str);
        heightLight(ZZ.BLUE, Color.argb(200, 44, 130, 200));
		heightLight(ZZ.OTHER, Color.argb(200, 0, 150, 255));
		heightLight(ZZ.RED, Color.argb(200, 255, 0, 0));
		heightLight(ZZ.GREEN, Color.argb(200, 0, 124, 31));*/
        //r1.setVisibility(View.GONE);
       /* } else {
            query(false, me.getint("dq_i", 0), "", ed1);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_find, menu);
        isFavorite = FavoritesUtil.readDataFromFavorites(data.getObjectId()) != null;
        MenuItem item = menu.findItem(R.id.action_favorite);
        item.setIcon(isFavorite ?
                R.mipmap.ic_star_white_36dp : R.mipmap.ic_star_outline_white_36dp);
        if (!ADMIN) menu.removeItem(R.id.action_edit);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_favorite:
                if (isFavorite) {
                    if (FavoritesUtil.deleteDataFromFavorites(data.getObjectId())) {
                        isFavorite = false;
                        item.setIcon(R.mipmap.ic_star_outline_white_36dp);
                    }
                } else {
                    if (FavoritesUtil.writeDataToFavorites(data)) {
                        isFavorite = true;
                        item.setIcon(R.mipmap.ic_star_white_36dp);
                    }
                }
                return true;
/*            case android.R.id.home:
                finish();
                break;*/
            case R.id.action_edit:
                if (!ADMIN)
                    throw new RuntimeException("操作被禁止");
                //删除数据，内部人员专用
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //bmob通过id获取内容
  /*  public void query(boolean b, int index, String id, final TextView tv) {
        if (b) {
            BmobQuery<Data> query = new BmobQuery<Data>();
            query.getObject(this, id, new GetListener<Data>() {
                @Override
                public void onSuccess(Data data) {
                    setTitle(data.getTitle());
                    str = data.getMessage();
                    //代码高亮
                    spannableString = new SpannableString(str);
                    heightLight(ZZ.BLUE, Color.argb(200, 44, 130, 200));
                    heightLight(ZZ.OTHER, Color.argb(200, 0, 150, 255));
                    heightLight(ZZ.RED, Color.argb(200, 255, 0, 0));
                    heightLight(ZZ.GREEN, Color.argb(200, 0, 124, 31));
                    tv.setText(spannableString);
                    r1.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int code, String arg0) {
                    me.showToast("获取数据失败：\n" + arg0);
                }

            });
        } else {
            //这个是获取精品区的内容，这里没用。
            BmobQuery<good> query = new BmobQuery<good>();
            query.addWhereEqualTo("index", index);
            query.findObjects(this, new FindListener<good>() {
                @Override
                public void onSuccess(List<good> object) {
                    for (good data : object) {
                        setTitle(data.getTitle());
                        tv.setText(data.getMessage() + 555);
                        r1.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(int code, String msg) {
                    me.showToast("获取数据失败：\n" + msg);
                }
            });
        }
    }*/

    private void deletedata(String id) {
        showDialog("删除数据", "您确定要删除 \"" + data.getTitle() + "\" 这条数据？", "删除", "取消", 1, id);
    }

    private void showDialog(String T, String M, String ok, String qx, final int i, final String id) {
        dialog = new AlertDialog.Builder(this).setTitle(T)
                .setMessage(M)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        switch (i) {
                            case 0:
                                finish();
                                break;
                            case 1:
                                me.delete(id);
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

    /*    //代码高亮
        private void heightLight(String pattern, int color) {
            ArrayList<Map<String, String>> lists = getStartAndEnd(Pattern.compile(pattern));
            for (Map<String, String> str : lists) {
                ForegroundColorSpan span = new ForegroundColorSpan(color);
                spannableString.setSpan(span, Integer.parseInt(str.get(START)), Integer.parseInt(str.get(END)), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        }

        private ArrayList<Map<String, String>> getStartAndEnd(Pattern pattern) {
            ArrayList<Map<String, String>> lists = new ArrayList<Map<String, String>>(0);
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                Map<String, String> map = new HashMap<>(0);
                map.put(START, matcher.start() + "");
                map.put(END, matcher.end() + "");
                lists.add(map);
            }
            return lists;
        }
    */
    @Override
    public void onAnimationStart(Animator animation) {
        super.onAnimationStart(animation);
        if (ed1 != null)
            ed1.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        ed1.requestLayout();
    }
}
