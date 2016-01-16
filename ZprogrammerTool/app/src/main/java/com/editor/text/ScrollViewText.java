package com.editor.text;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ScrollView;

import com.zprogrammer.tool.R;

public class ScrollViewText extends ScrollView {

    private int rowHeight, currRow;
    private int totalRows;
    private HorScrollViewText horScrollText;

    public ScrollViewText(Context context) {
        this(context, null);
    }


    public ScrollViewText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (horScrollText == null) {
            horScrollText = (HorScrollViewText) findViewById(R.id.horScrollViewText);
        }
        rowHeight = horScrollText.getRowHeight();
        currRow = horScrollText.getCurrRow();
        totalRows = horScrollText.getCurrRow();
    }


    @Override
    public boolean executeKeyEvent(KeyEvent event) {
        // TODO: Implement this method
        if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
            if (totalRows * rowHeight <= getHeight() / 2) {
                scrollTo(getScrollX(), 0);
            }
        }
        return super.executeKeyEvent(event);
    }


    @Override
    public void computeScroll() {
        // TODO: Implement this method
        super.computeScroll();
        horScrollText.getContainerView().scrollY(getScrollY());

        horScrollText.getContainerView().getViewHeight(getHeight());
    }

}
