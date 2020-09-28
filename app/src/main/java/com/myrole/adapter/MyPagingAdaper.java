package com.myrole.adapter;
/*

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.myrole.holders.PostListDTO;
import com.paging.gridview.PagingBaseAdapter;

import java.util.ArrayList;


public class MyPagingAdaper extends PagingBaseAdapter<String> {

	private Context mContext;
	ArrayList<PostListDTO> post_list;
	public MyPagingAdaper(Context mContext, ArrayList<PostListDTO> post_list) {
		this.mContext = mContext;
		this.post_list = post_list;
	}
	@Override
	public int getCount() {
		return post_list.size();
	}

	@Override
	public String getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		MyViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext())
					.inflate(com.myrole.R.layout.explore_card, parent, false);
			viewHolder = new MyViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (MyViewHolder) convertView.getTag();
		}
		String url = post_list.get(position).getUrl();
		Log.e("Sub>>", url.substring(url.length() - 4));
		if (url.substring(url.length() - 4).equalsIgnoreCase(".mp4")) {
			String[] url1=post_list.get(position).getUrl().split(".mp4");
			String new_thumb =url1[0]+".jpg";
			Glide.with(mContext).load(new_thumb).into(viewHolder.thumbnail);
		}
		else
			Glide.with(mContext).load(post_list.get(position).getUrl()).into(viewHolder.thumbnail);


		viewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				*/
/*if (((HomeActivity) mContext).fragmentManager != null) {
					PostDetailFragment fragment = PostDetailFragment.newInstance(String.valueOf(post_list
							.get(position).getPost_id()));
					((HomeActivity) mContext).fragmentManager.beginTransaction()
							.add(com.myrole.R.id.main_frame,
									fragment,
									"POSTDETAILFRAGMENT")
							.addToBackStack("POSTDETAILFRAGMENT")
							.commit();
				}*//*

			}
		});

		return convertView;
	}

	public class MyViewHolder  {
		public ImageView thumbnail;

		public MyViewHolder(View view) {
			thumbnail = (ImageView) view.findViewById(com.myrole.R.id.thumbnail);

		}
	}
}
*/
