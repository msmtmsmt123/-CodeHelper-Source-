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

package com.zprogrammer.tool.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.ThumbnailListener;
import com.dd.processbutton.iml.ActionProcessButton;
import com.squareup.picasso.Picasso;
import com.zprogrammer.tool.R;
import com.zprogrammer.tool.bean.MyUser;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity extends AppCompatActivity {

    private ImageView userPortraitPic;
    private EditText userName, userPassword;
    private ActionProcessButton btnLogin;
    private CharSequence currFindingUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userPortraitPic = (ImageView) findViewById(R.id.user_portrait_pic);
        userName = (EditText) findViewById(R.id.user_name);
        userPassword = (EditText) findViewById(R.id.user_password);
        btnLogin = (ActionProcessButton) findViewById(R.id.btn_login);

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currFindingUserName = s;
                BmobQuery<MyUser> query = new BmobQuery<>();
                query.addWhereEqualTo("username", s);
                query.setLimit(1);
                query.findObjects(LoginActivity.this, new FindListener<MyUser>() {
                    @Override
                    public void onSuccess(List<MyUser> list) {
                        final MyUser user = list.get(0);
                        if (user.getUsername().equals(currFindingUserName)) {
                            BmobProFile portraitPic = BmobProFile.getInstance(LoginActivity.this);
                            portraitPic.submitThumnailTask(user.getPortraitPic(), 1, new ThumbnailListener() {
                                @Override
                                public void onSuccess(String thumbnailName, String thumbnailUrl) {
                                    if (user.getUsername().equals(currFindingUserName))
                                        Picasso.with(LoginActivity.this).load(thumbnailUrl).into(userPortraitPic);
                                }

                                @Override
                                public void onError(int statuscode, String errormsg) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnLogin.setMode(ActionProcessButton.Mode.ENDLESS);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setProgress(50);
                BmobUser.loginByAccount(LoginActivity.this, userName.getText().toString(), userPassword.getText().toString(), new LogInListener<MyUser>() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        if (e != null || myUser == null) {
                            btnLogin.setProgress(-1);
                            return;
                        }
                        btnLogin.setProgress(100);
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
