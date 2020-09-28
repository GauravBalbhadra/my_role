package com.myrole.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.myrole.R;
import com.myrole.databinding.MixupListItemBinding;
import com.myrole.fragments.viewmodel.SharedViewModel;
import com.myrole.listeners.SongApplyClickCallback;
import com.myrole.listeners.SongClickCallback;
import com.myrole.model.Song;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Song} and makes a call to the
 * specified
 * TODO: Replace the implementation with code for your data type.
 */
public class MySoundTrendingAdapter extends RecyclerView.Adapter<MySoundTrendingAdapter.MixupViewHolder> {

    private final List<Song> mSongList;
    private SongClickCallback mSongClickCallback;
    private SongApplyClickCallback mSongApplyClickCallback;

    public MySoundTrendingAdapter(SongClickCallback songClickCallback, SongApplyClickCallback songApplyClickCallback, List<Song> songItemList, SharedViewModel model) {
        mSongList = songItemList;
        mSongClickCallback = songClickCallback;
        mSongApplyClickCallback = songApplyClickCallback;
    }

    @NotNull
    @Override
    public MixupViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        MixupListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.mixup_list_item, parent, false);

        binding.setApplyCallback(mSongApplyClickCallback);

        binding.setClickCallback(mSongClickCallback);

        return new MixupViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final MixupViewHolder holder, final int position) {
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
