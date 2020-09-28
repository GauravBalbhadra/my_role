package com.myrole.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myrole.R;
import com.myrole.fragments.SoundTrendingFragment;
import com.myrole.fragments.dummy.DummyContent.DummyItem;
import com.myrole.listeners.SongClickCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link SoundTrendingFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySoundListAdapter extends RecyclerView.Adapter<MySoundListAdapter.ViewHolder> {

    private final List<String> mValues;

    public MySoundListAdapter(List<DummyItem> items, SongClickCallback songClickCallback) {
        mValues = new ArrayList<String>();
        mValues.add("xyz");
        mValues.add("Happy Birthday");
        mValues.add("MyRole Hot");
        mValues.add("Love");
        mValues.add("Comedy");
        mValues.add("Funny");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //holder.mItem = mValues.get(position);
        //  holder.mIdView.setText(mValues.get(position).id);
        // holder.mContentView.setText(mValues.get(position).content);
        holder.tvPlayListName.setText(mValues.get(position));

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView tvPlayListName;
        //public final TextView mIdView;
        //public final TextView mContentView;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvPlayListName = (TextView) view.findViewById(R.id.tv_playlist_name);
            //mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + " clicked '";
        }
    }
}
