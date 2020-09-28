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

import java.util.ArrayList;

/**
 * Created by vikesh.kumar on 5/27/2016.
 */
public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context activity;
    private ArrayList<String> asr;

    public CustomSpinnerAdapter(Context context, ArrayList<String> asr) {
        this.asr=asr;
        activity = context;
    }



    public int getCount()
    {
        return asr.size();
    }

    public Object getItem(int i)
    {
        return asr.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(activity);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(14);
        txt.setText(asr.get(position));
        txt.setTextColor(Color.parseColor("#434242"));
        Typeface face = Typeface.createFromAsset(activity.getAssets(), "fonts/Nexa Light.otf");
        txt.setTypeface(face);
        return  txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(activity);
        txt.setTextSize(14);
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
        txt.setText(asr.get(i));
        txt.setTextColor(Color.parseColor("#434242"));
        Typeface face = Typeface.createFromAsset(activity.getAssets(), "fonts/Nexa Light.otf");
        txt.setTypeface(face);
        return  txt;
    }

}