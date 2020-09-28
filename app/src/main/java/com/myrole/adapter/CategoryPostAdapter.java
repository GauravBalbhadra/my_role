package com.myrole.adapter;


import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.myrole.vo.Post;

import java.util.List;

/**
 * Created by Rakesh on 8/2/2016.
 */

public class CategoryPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Post> postList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
   /*
    private  List<BaseVideoItem> mList;
    private  Context mContext;
    LayoutInflater inflater;
    private final int VIEW_IMAGE=1;
    private final int VIEW_VIDEO=2;
    VideoViewHolder holder1;
    public CategoryPostAdapter(List<Post> postList, Context context, List<BaseVideoItem> list){
        this.postList = postList;
        mVideoPlayerManager = videoPlayerManager;
        mContext = context;
        mList = list;
        inflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public int getItemViewType(int position) {
        String url = postList.get(position).postImage;

        if(url.length()>0){
            Log.e("Sub>>", url.substring(url.length() - 4, url.length()));
            if (url.substring(url.length() - 4, url.length()).equalsIgnoreCase(".mp4")) {
                return VIEW_VIDEO;
            }
            else
                return VIEW_IMAGE;
        }

            return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==VIEW_VIDEO){
            BaseVideoItem videoItem = mList.get(viewType);
            View resultView = videoItem.createView(mContext,parent, mContext.getResources().getDisplayMetrics().widthPixels);
            return new VideoViewHolder(resultView);
        }
        else {
            View view = inflater.inflate(R.layout.layout_item_general1, parent, false);
            PostViewHolder holder = new PostViewHolder(view) ;
            view.setTag(holder);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vholder, final int position) {
        switch (getItemViewType(position)) {
            case VIEW_VIDEO:
                try{
                    holder1 = (VideoViewHolder) vholder;
                    BaseVideoItem videoItem = mList.get(position);
                    videoItem.update(position, holder1, mVideoPlayerManager);
                    if(postList!=null&&postList.get(position).owner_image!=null&&postList.get(position).owner_image.length()>0){
                        Picasso.with(mContext).load(postList.get(position).owner_image).into( holder1.userIcon);
                    }
                    else {
                        Picasso.with(mContext).load(R.drawable.icon).into( holder1.userIcon);
                    }

                    holder1.name.setText(postList.get(position).username);
                   // holder1.txt_time.setText(Utils.timeCalculation(postList.get(position).postTime));
                    holder1.description.setText(postList.get(position).description);
                    holder1.num_loves.setText(postList.get(position).numLoves);
                    holder1.num_comment.setText(postList.get(position).numComment);
                    holder1.name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((HomeActivity) mContext).fragmentManager != null) {
                                OtherProfileFragment fragment = OtherProfileFragment.newInstance(postList
                                        .get(position).ownerId);
                                ((HomeActivity) mContext).fragmentManager.beginTransaction()
                                        .add(R.id.main_frame,
                                                fragment,
                                                "OTHERPROFILEFRAGMENT")
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                    });
                    holder1.userIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((HomeActivity) mContext).fragmentManager != null) {
                                OtherProfileFragment fragment = OtherProfileFragment.newInstance(postList
                                        .get(position).ownerId);
                                ((HomeActivity) mContext).fragmentManager.beginTransaction()
                                        .add(R.id.main_frame,
                                                fragment,
                                                "OTHERPROFILEFRAGMENT")
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                    });
                    holder1.loveIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String userId = AppPreferences.getAppPreferences(mContext).getStringValue(AppPreferences.USER_ID);
                            new LikeTask().execute(userId,
                                    postList.get(position).id,
                                    postList.get(position).ownerId, "1",position+"");
                        }
                    });
                    holder1.commentIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    Utils.setTypeface(mContext,  holder1.name, Config.QUICKSAND, Config.BOLD);
                    Utils.setTypeface(mContext,  holder1.categoty_name, Config.QUICKSAND, Config.REGULAR);
                    Utils.setTypeface(mContext,  holder1.description, Config.QUICKSAND, Config.BOLD);
                    Utils.setTypeface(mContext,  holder1.comment, Config.QUICKSAND, Config.REGULAR);
                    Utils.setTypeface(mContext,  holder1.num_comment, Config.QUICKSAND, Config.REGULAR);
                    Utils.setTypeface(mContext,  holder1.love, Config.QUICKSAND, Config.REGULAR);
                    Utils.setTypeface(mContext,  holder1.num_loves, Config.QUICKSAND, Config.REGULAR);
                    Utils.setTypeface(mContext,  holder1.txt_time, Config.QUICKSAND, Config.REGULAR);
                }catch (Exception e){}


                break;
            case VIEW_IMAGE:

                try{
                    final PostViewHolder holder = (PostViewHolder) vholder;
                    holder.name.setText(postList.get(position).username);
                    Glide.with(mContext).load(postList.get(position).postImage).asBitmap()
                            .placeholder(R.drawable.singer_bg).centerCrop().into(holder.postImage);
                    if (!postList.get(position).categoryName.isEmpty()) {
                        holder.categoty_name.setVisibility(View.VISIBLE);
                        holder.categoty_name.setText(postList.get(position).categoryName);
                    } else {
                        holder.categoty_name.setVisibility(View.INVISIBLE);
                    }
                    if(postList!=null&&postList.get(position).owner_image!=null&&postList.get(position).owner_image.length()>0){
                        Picasso.with(mContext).load(postList.get(position).owner_image).into( holder.userIcon);
                    }else {
                        Picasso.with(mContext).load(R.drawable.icon).into( holder.userIcon);
                    }

                    //holder.txt_time.setText(Utils.timeCalculation(postList.get(position).postTime));
                    holder.description.setText(postList.get(position).description);
                    holder.num_loves.setText(postList.get(position).numLoves);
                    holder.num_comment.setText(postList.get(position).numComment);
                    holder.name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((HomeActivity) mContext).fragmentManager != null) {
                                OtherProfileFragment fragment = OtherProfileFragment.newInstance(postList
                                        .get(position).ownerId);
                                ((HomeActivity) mContext).fragmentManager.beginTransaction()
                                        .add(R.id.main_frame,
                                                fragment,
                                                "OTHERPROFILEFRAGMENT")
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                    });
                    holder.userIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((HomeActivity) mContext).fragmentManager != null) {
                                OtherProfileFragment fragment = OtherProfileFragment.newInstance(postList
                                        .get(position).ownerId);
                                ((HomeActivity) mContext).fragmentManager.beginTransaction()
                                        .add(R.id.main_frame,
                                                fragment,
                                                "OTHERPROFILEFRAGMENT")
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                    });
                    holder.loveIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String userId = AppPreferences.getAppPreferences(mContext).getStringValue(AppPreferences.USER_ID);
                            new LikeTask().execute(userId,
                                    postList.get(position).id,
                                    postList.get(position).ownerId, "1",position+"");
                        }
                    });
                    holder.commentIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }catch (Exception e){}

                break;

        }
    }



    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView name, categoty_name, num_loves, num_comment, description,txt_time;
        ImageView postImage, userIcon, loveIcon, commentIcon;
        public RelativeLayout postVideo;

        public PostViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txt_user_name);
            categoty_name = (TextView) itemView.findViewById(R.id.txt_category_name);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            num_loves = (TextView) itemView.findViewById(R.id.txt_num_loves);
            num_comment = (TextView) itemView.findViewById(R.id.txt_num_comment);
            description = (TextView) itemView.findViewById(R.id.txt_description);
            postImage = (ImageView) itemView.findViewById(R.id.img_post);
            loveIcon = (ImageView) itemView.findViewById(R.id.img_love);
            commentIcon = (ImageView) itemView.findViewById(R.id.img_comment);
            userIcon = (ImageView) itemView.findViewById(R.id.usericon);
            postVideo = (RelativeLayout) itemView.findViewById(R.id.video_post);
            Utils.setTypeface(mContext, (TextView) itemView.findViewById(R.id.txt_user_name), Config.QUICKSAND, Config.BOLD);
            Utils.setTypeface(mContext, (TextView) itemView.findViewById(R.id.txt_category_name), Config.QUICKSAND, Config.REGULAR);
            Utils.setTypeface(mContext, (TextView) itemView.findViewById(R.id.txt_description), Config.QUICKSAND, Config.BOLD);
            Utils.setTypeface(mContext, (TextView) itemView.findViewById(R.id.txt_comment), Config.QUICKSAND, Config.REGULAR);
            Utils.setTypeface(mContext, (TextView) itemView.findViewById(R.id.txt_num_comment), Config.QUICKSAND, Config.REGULAR);
            Utils.setTypeface(mContext, (TextView) itemView.findViewById(R.id.txt_loves), Config.QUICKSAND, Config.REGULAR);
            Utils.setTypeface(mContext, (TextView) itemView.findViewById(R.id.txt_num_loves), Config.QUICKSAND, Config.REGULAR);
            Utils.setTypeface(mContext, (TextView) itemView.findViewById(R.id.txt_time), Config.QUICKSAND, Config.REGULAR);
        }
    }



    class LikeTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;
        int index;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            *//*progressDialog = new ProgressDialog(mContext);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Please wait...");
            progressDialog.show();*//*
        }

        @Override
        protected String doInBackground(String... params) {
            index = Integer.parseInt(params[4]);
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", params[0]);
            postDataParams.put("post_id", params[1]);
            postDataParams.put("owner_id", params[2]);
            postDataParams.put("type", params[3]);

            return HTTPUrlConnection.getInstance().load(Config.ADD_LIKE, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //progressDialog.dismiss();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    postList.get(index).numLoves = ""+(Integer.parseInt(postList.get(index).numLoves)+1);
                    notifyDataSetChanged();
                    Log.v("MYROLE", result);
                } else {
                    Toast.makeText(mContext, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
*/
}
