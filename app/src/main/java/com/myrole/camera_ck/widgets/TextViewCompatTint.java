package com.myrole.camera_ck.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.drawable.DrawableCompat;

import com.myrole.R;

/**
 * Created by Divine on 09-08-2017.
 */

public class TextViewCompatTint extends AppCompatTextView {
    public TextViewCompatTint(Context context) {
        this(context, null);
    }

    public TextViewCompatTint(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public TextViewCompatTint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewCompatTint, defStyleAttr, 0);

        if (typedArray.hasValue(R.styleable.TextViewCompatTint_drawableTint)) {
            int color = typedArray.getColor(R.styleable.TextViewCompatTint_drawableTint, 0);

            Drawable[] drawables = getCompoundDrawables();

            for (Drawable drawable : drawables) {
                if (drawable == null) continue;
                DrawableCompat.setTint(DrawableCompat.wrap(drawable).mutate(), color);
            }
        }

        typedArray.recycle();
    }
}
