package com.myrole.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myrole.R;

import java.util.List;

public class AdapterSahreApp extends RecyclerView.Adapter<AdapterSahreApp.ViewHolder> {
    private PackageManager pm;
    private Context context;
    private List<ResolveInfo> mList;
    private OnItemClickListener mListener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
        mListener = listener;
    }

    public AdapterSahreApp(Context context,PackageManager pm, List<ResolveInfo> mList) {
        this.context = context;
        this.mList = mList;
        this.pm = pm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_share_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ResolveInfo model = mList.get(position);
        viewHolder.textViewTitle.setText(model.loadLabel(pm));
        viewHolder.image.setImageDrawable(model.loadIcon(pm));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView textViewTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }
    }

}
