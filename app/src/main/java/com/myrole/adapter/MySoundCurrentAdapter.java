package com.myrole.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.myrole.R;
import com.myrole.databinding.MixupListItemBinding;
import com.myrole.fragments.viewmodel.SharedViewModel;
import com.myrole.listeners.SongClickCallback;
import com.myrole.model.Song;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MySoundCurrentAdapter extends RecyclerView.Adapter<MySoundCurrentAdapter.MixupViewHolder>{

    private final List<Song> mSongList;
    private SongClickCallback mSongClickCallback;

    public MySoundCurrentAdapter(SongClickCallback songClickCallback, List<Song> songItemList, SharedViewModel model) {
        mSongList = songItemList;
        mSongClickCallback = songClickCallback;
    }

    @NotNull
    @Override
    public MySoundCurrentAdapter.MixupViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        MixupListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.mixup_list_item, parent, false);
        binding.setClickCallback(mSongClickCallback);
        return new MySoundCurrentAdapter.MixupViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final MySoundCurrentAdapter.MixupViewHolder holder, final int position) {
        final Song mSongItem = mSongList.get(position);

        holder.binding.setSong(mSongItem);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    static class MixupViewHolder extends RecyclerView.ViewHolder {

        final MixupListItemBinding binding;

        public MixupViewHolder(MixupListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
