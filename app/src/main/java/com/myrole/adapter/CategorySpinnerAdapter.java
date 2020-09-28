package com.myrole.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.myrole.R;
import com.myrole.vo.Category;

import java.util.ArrayList;

/**
 * Created by vikesh.kumar on 5/27/2016.
 */
public class CategorySpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context activity;
    private ArrayList<Category> list;

    public CategorySpinnerAdapter(Context context, ArrayList<Category> list) {
        this.list = list;
        activity = context;
    }


    public int getCount() {
        return list.size();
    }

    public Object getItem(int i) {
        return list.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(activity);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(14);
        txt.setText(list.get(position).name);
        txt.setTextColor(Color.parseColor("#434242"));
        Typeface face = Typeface.createFromAsset(activity.getAssets(), "fonts/Nexa Light.otf");
        txt.setTypeface(face);
        return txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(activity);
        txt.setTextSize(14);
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
        txt.setText(list.get(i).name);
        txt.setTextColor(Color.parseColor("#434242"));
        Typeface face = Typeface.createFromAsset(activity.getAssets(), "fonts/Nexa Light.otf");
        txt.setTypeface(face);
        return txt;
    }

}