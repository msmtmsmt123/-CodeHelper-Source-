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

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.zprogrammer.tool.R;
import com.zprogrammer.tool.bean.Data;

import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.UpdateListener;

public class EditActivity extends BaseActivity {
    private EditText editTitle, editMessage, editUser, editSummary;
    private Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!FindActivity.ADMIN)
            throw new RuntimeException("操作被禁止");
        super.onCreate(savedInstanceState);
        editTitle = (EditText) findViewById(R.id.newlayoutEditText1);
        editMessage = (EditText) findViewById(R.id.textEditorView);
        editUser = (EditText) findViewById(R.id.newlayoutEditText3);
        editSummary = (EditText) findViewById(R.id.et_summary);

        data = (Data) getIntent().getSerializableExtra("data");

        editTitle.setText(data.getTitle());
        editSummary.setText(data.getTitleMessage());
        editUser.setText(data.getUser());
        editMessage.setText(data.getMessage());
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_write;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_highlight);
        item.setChecked(data.getjing());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteData();
                return true;

            case R.id.action_save:
                saveData();
                return true;

            case R.id.action_highlight:
                data.setjing(!data.getjing());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveData() {
        new AlertDialog.Builder(this)
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage("保存?")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.setMessage(editMessage.getText().toString());
                        data.setTitle(editTitle.getText().toString());
                        data.setUser(editUser.getText().toString());
                        String summary = editSummary.getText().toString();
                        if (summary.length() <= 0)
                            summary = editMessage.getText().toString().substring(0, 30);
                        data.setTitleMessage(summary);
                        data.update(EditActivity.this, data.getObjectId(), new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(EditActivity.this, "保存成功：" + data.getTitle(), Toast.LENGTH_SHORT).show();
                                ActivityCompat.finishAfterTransition(EditActivity.this);
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(EditActivity.this, "保存失败：" + s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).show();
    }

    private void deleteData() {
        new AlertDialog.Builder(this)
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage("删除?")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.delete(EditActivity.this, new DeleteListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(EditActivity.this, "删除成功：" + data.getTitle(), Toast.LENGTH_SHORT).show();
                                ActivityCompat.finishAfterTransition(EditActivity.this);
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(EditActivity.this, "删除失败：" + s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).show();
    }

}
