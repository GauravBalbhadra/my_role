package com.myrole.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.myrole.R;
import com.myrole.databinding.MixupListItemBinding;
import com.myrole.fragments.dummy.DummyContent.DummyItem;
import com.myrole.listeners.SongClickCallback;
import com.myrole.model.Song;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMixUpListAdapter extends RecyclerView.Adapter<MyMixUpListAdapter.MixupViewHolder> {

    private List<Song> mSongList;
    private final SongClickCallback mSongClickCallback;

    public MyMixUpListAdapter(SongClickCallback songClickCallback, List<Song> songItemList) {
        mSongClickCallback = songClickCallback;
        mSongList = songItemList;
    }

    @Override
    public MixupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MixupListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.mixup_list_item, parent, false);
        binding.setClickCallback(mSongClickCallback);
        return new MixupViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final MixupViewHolder holder, int position) {
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