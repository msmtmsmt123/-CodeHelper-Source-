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

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.nineoldandroids.animation.Animator;
import com.zprogrammer.tool.R;

public abstract class BaseActivity extends AppCompatActivity implements Animator.AnimatorListener {
    protected Toolbar toolbar;
    private MaterialMenuDrawable menuDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        menuDrawable = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.REGULAR);
        toolbar.setNavigationIcon(menuDrawable);
        menuDrawable.setAnimationListener(this);
        menuDrawable.setIconState(MaterialMenuDrawable.IconState.BURGER);
        menuDrawable.animateIconState(MaterialMenuDrawable.IconState.ARROW);
    }

    protected abstract int getLayoutRes();

    @Override
    public void onBackPressed() {
        menuDrawable.setIconState(MaterialMenuDrawable.IconState.ARROW);
        menuDrawable.animateIconState(MaterialMenuDrawable.IconState.BURGER);
        ActivityCompat.finishAfterTransition(this);
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
