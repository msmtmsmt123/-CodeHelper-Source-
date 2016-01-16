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

package com.editor.text;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import com.zprogrammer.tool.R;

public class HorScrollViewText extends HorizontalScrollView {


    private CodeEditor codeEditor;

    public HorScrollViewText(Context context) {
        this(context, null);

    }


    public HorScrollViewText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO: Implement this method
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    public void computeScroll() {
        // TODO: Implement this method
        super.computeScroll();

        codeEditor.scrollX(getScrollX());
        codeEditor.getViewWidth(getWidth());
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO: Implement this method

        return super.onTouchEvent(ev);
    }

    @Override
    public boolean executeKeyEvent(KeyEvent event) {
        // TODO: Implement this method
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            scrollTo(0, getScrollY());
        }


        return super.executeKeyEvent(event);
    }


    public void setContainerView(CodeEditor codeEditor) {
        this.codeEditor = codeEditor;
    }

    public CodeEditor getContainerView() {
        return codeEditor;
    }


    public int getRowHeight() {
        if (codeEditor == null) {
            codeEditor = (CodeEditor) findViewById(R.id.codeEditor);
        }
        return codeEditor.getRowHeight();
    }

    public int getTotalRows() {
        return codeEditor.getTotalRows();
    }

    public int getCurrRow() {
        return codeEditor.getCurrRow();
    }

}
