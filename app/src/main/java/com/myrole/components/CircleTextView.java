package com.myrole.components;

import android.content.Context;

import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class CircleTextView extends AppCompatTextView {

    public CircleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthMeasureSpec > heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } else {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        }

    }
}