package com.myrole.adapter;//package com.myrole.adapter;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.myrole.AddComments;
//import com.myrole.HomeActivity;
//
//import com.myrole.R;
//import com.myrole.fragments.LikerFragment;
//import com.myrole.fragments.OtherProfileFragment;
//import com.myrole.holders.VideoViewHolder;
//import com.myrole.utils.AppPreferences;
//import com.myrole.utils.Config;
//import com.myrole.utils.HTTPUrlConnection;
//import com.myrole.utils.Utils;
//
//import com.myrole.vo.Post;
//import com.squareup.picasso.Picasso;
//
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.List;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
///**
// * Created by Rakesh on 8/2/2016.
// */
//
//public class GeneralCategoryPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    List<Post> postList;
//    //private  List<BaseVideoItem> mList;
//    private  Context mContext;
//    LayoutInflater inflater;
//    private final int VIEW_IMAGE=1;
//    private final int VIEW_VIDEO=2;
//    VideoViewHolder holder1;
//    /*public GeneralCategoryPostAdapter(List<Post> postList, Context context, List<BaseVideoItem> list){
//        this.postList = postList;
//        mContext = context;
//        mList = list;
//        inflater = LayoutInflater.from(this.mContext);
//    }*/
//
//    @Override
//    public int getItemViewType(int position) {
//        String url = postList.get(position).postImage;
//            Log.e("Sub>>", url.substring(url.length() - 4, url.length()));
//            if (url.substring(url.length() - 4, url.length()).equalsIgnoreCase(".mp4")) {
//                return VIEW_VIDEO;
//            }
//            else
//                return VIEW_IMAGE;
//
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if(viewType==VIEW_VIDEO){
//           /* BaseVideoItem videoItem = mList.get(viewType);
//            View resultView = videoItem.createView(mContext,parent, mContext.getResources().getDisplayMetrics().widthPixels);
//            return new VideoViewHolder(resultView);*/
//            View view = inflater.inflate(R.layout.layout_item_general1, parent, false);
//            PostViewHolder holder = new PostViewHolder(view) ;
//            view.setTag(holder);
//            return holder;
//        }
//        else {
//            View view = inflater.inflate(R.layout.layout_item_general1, parent, false);
//            PostViewHolder holder = new PostViewHolder(view) ;
//            view.setTag(holder);
//            return holder;
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder vholder, final int position) {
//        switch (getItemViewType(position)) {
//            case VIEW_VIDEO:
//                try{
//                    holder1 = (VideoViewHolder) vholder;
//                    //BaseVideoItem videoItem = mList.get(position);
//                    //videoItem.update(position, holder1, mVideoPlayerManager);
//                    holder1.media_controlar_layout.setVisibility(View.INVISIBLE);
//                    if(postList!=null&&postList.get(position).owner_image!=null&&postList.get(position).owner_image.length()>0){
//                        Picasso.with(mContext).load(postList.get(position).owner_image).into( holder1.userIcon);
//                    }
//                    else {
//                        Picasso.with(mContext).load(R.drawable.default_avatar).into( holder1.userIcon);
//                    }
//                    if(postList.get(position).islike.equals("1")){
//                        holder1.loveIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_liked));
//                    }
//                    else {
//                        holder1.loveIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
//                    }
//
//                    holder1.name.setText(postList.get(position).username);
//                    holder1.txt_time.setText(Utils.timeCalculation(postList.get(position).postTime));
//                    holder1.description.setText(postList.get(position).description);
//                    holder1.num_loves.setText(postList.get(position).numLoves);
//                    holder1.num_comment.setText(postList.get(position).numComment);
//                    holder1.name.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                           // mContext.startActivity(new Intent(mContext, OtherProfileActivity.class).putExtra("ownerId", String.valueOf(postList.get(position).ownerId)));
//                            if (((HomeActivity) mContext).fragmentManager != null) {
//                                OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(postList.get(position).ownerId));
//                                ((HomeActivity) mContext).fragmentManager.beginTransaction()
//                                        .add(R.id.main_frame,
//                                                fragment,
//                                                "OTHERPROFILEFRAGMENT")
//                                        .addToBackStack(null)
//                                        .commit();
//                            }
//                        }
//                    });
//                    holder1.userIcon.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (((HomeActivity) mContext).fragmentManager != null) {
//                                OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(postList.get(position).ownerId));
//                                ((HomeActivity) mContext).fragmentManager.beginTransaction()
//                                        .add(R.id.main_frame,
//                                                fragment,
//                                                "OTHERPROFILEFRAGMENT")
//                                        .addToBackStack(null)
//                                        .commit();
//                            }
//                        }
//                    });
//                    holder1.loveIcon.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            String userId = AppPreferences.getAppPreferences(mContext).getStringValue(AppPreferences.USER_ID);
//                            if (postList.get(position).islike.equals("0")) {
//                                postList.get(position).numLoves=String.valueOf(Integer.parseInt(postList.get(position).numLoves)+1);
//                                postList.get(position).islike="1";
//                            } else {
//                                postList.get(position).numLoves=String.valueOf(Integer.parseInt(postList.get(position).numLoves)-1);
//                                postList.get(position).islike="0";
//                            }
//                            notifyDataSetChanged();
//                            holder1.loveIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_liked));
//                            new LikeTask().execute(userId,
//                                    postList.get(position).id,
//                                    postList.get(position).ownerId, "1",position+"");
//                        }
//                    });
//                    holder1.lay_num_loves.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
////                            Intent in_likes = new Intent(mContext, LikersActivity.class);
////                            in_likes.putExtra("post_id", String.valueOf(postList.get(position).id));
////                            mContext.startActivity(in_likes);
//
//                            if (((HomeActivity) mContext).fragmentManager != null) {
//                                ((HomeActivity) mContext).fragmentManager.beginTransaction()
//                                        .add(R.id.main_frame,
//                                                LikerFragment.newInstance(String.valueOf(postList.get(position).id)),
//                                                "LIKERFRAGMENT")
//                                        .addToBackStack("LIKERFRAGMENT")
//                                        .commit();
//                            }
//
//
//                        }
//                    });
//
//                    holder1.commentIcon.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            Intent in_comment = new Intent(mContext, AddComments.class);
//                            in_comment.putExtra("post_id", String.valueOf(postList.get(position).id));
//                            mContext.startActivity(in_comment);
//
//                        }
//                    });
//                    Utils.setTypeface(mContext,  holder1.name, Config.QUICKSAND, Config.BOLD);
//                    Utils.setTypeface(mContext,  holder1.categoty_name, Config.QUICKSAND, Config.REGULAR);
//                    Utils.setTypeface(mContext,  holder1.description, Config.QUICKSAND, Config.BOLD);
//                    Utils.setTypeface(mContext,  holder1.comment, Config.QUICKSAND, Config.REGULAR);
//                    Utils.setTypeface(mContext,  holder1.num_comment, Config.QUICKSAND, Config.REGULAR);
//                    Utils.setTypeface(mContext,  holder1.love, Config.QUICKSAND, Config.REGULAR);
//                    Utils.setTypeface(mContext,  holder1.num_loves, Config.QUICKSAND, Config.REGULAR);
//                    Utils.setTypeface(mContext,  holder1.txt_time, Config.QUICKSAND, Config.REGULAR);
//                }catch (Exception e){}
//
//
//                break;
//            case VIEW_IMAGE:
//
//                try{
//                    final PostViewHolder holder = (PostViewHolder) vholder;
//                    holder.name.setText(postList.get(position).username);
//                    Glide.with(mContext).load(postList.get(position).postImage).asBitmap()
//                           .centerCrop().into(holder.postImage);
//                    if (!postList.get(position).categoryName.isEmpty()) {
//                        holder.categoty_name.setVisibility(View.VISIBLE);
//                        holder.categoty_name.setText(postList.get(position).categoryName);
//                    } else {
//                        holder.categoty_name.setVisibility(View.INVISIBLE);
//                    }
//                    if(postList!=null&&postList.get(position).owner_image!=null&&postList.get(position).owner_image.length()>0){
//                        Picasso.with(mContext).load(postList.get(position).owner_image).centerCrop().into( holder.userIcon);
//                    }else {
//                        Picasso.with(mContext).load(R.drawable.default_avatar).centerCrop().into( holder.userIcon);
//                    }
//                    if(postList.get(position).islike.equals("1")){
//                        holder.loveIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_liked));
//                    }
//                    else {
//                        holder.loveIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
//                    }
//
//                    holder.txt_time.setText(Utils.timeCalculation(postList.get(position).postTime));
//                    holder.description.setText(postList.get(position).description);
//                    holder.num_loves.setText(postList.get(position).numLoves);
//                    holder.num_comment.setText(postList.get(position).numComment);
//                    holder.name.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (((HomeActivity) mContext).fragmentManager != null) {
//                                OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(postList.get(position).ownerId));
//                                ((HomeActivity) mContext).fragmentManager.beginTransaction().add(R.id.main_frame, fragment,
//                                                "OTHERPROFILEFRAGMENT").addToBackStack(null).commit();
//                            }
//                        }
//                    });
//                    holder.userIcon.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (((HomeActivity) mContext).fragmentManager != null) {
//                                OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(postList.get(position).ownerId));
//                                ((HomeActivity) mContext).fragmentManager.beginTransaction()
//                                        .add(R.id.main_frame,
//                                                fragment,
//                                                "OTHERPROFILEFRAGMENT")
//                                        .addToBackStack(null)
//                                        .commit();
//                            }
//                        }
//                    });
//                    holder.loveIcon.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            String userId = AppPreferences.getAppPreferences(mContext).getStringValue(AppPreferences.USER_ID);
//                            if (postList.get(position).islike.equals("0")) {
//                                postList.get(position).numLoves=String.valueOf(Integer.parseInt(postList.get(position).numLoves)+1);
//                                postList.get(position).islike="1";
//                            } else {
//                                postList.get(position).numLoves=String.valueOf(Integer.parseInt(postList.get(position).numLoves)-1);
//                                postList.get(position).islike="0";
//                            }
//                            notifyDataSetChanged();
//                            holder.loveIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_liked));
//                            new LikeTask().execute(userId,
//                                    postList.get(position).id,
//                                    postList.get(position).ownerId, "1",position+"");
//                        }
//                    });
//                    holder.commentIcon.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            Intent in_comment = new Intent(mContext, AddComments.class);
//                            in_comment.putExtra("post_id",String.valueOf(postList.get(position).id));
//                            mContext.startActivity(in_comment);
//
//                        }
//                    });
//                    holder.lay_num_loves.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
////                            Intent in_likes = new Intent(mContext, LikersActivity.class);
////                            in_likes.putExtra("post_id",String.valueOf(postList.get(position).id));
////                            mContext.startActivity(in_likes);
//
//                            if (((HomeActivity) mContext).fragmentManager != null) {
//                                ((HomeActivity) mContext).fragmentManager.beginTransaction()
//                                        .add(R.id.main_frame,
//                                                LikerFragment.newInstance(String.valueOf(postList.get(position).id)),
//                                                "LIKERFRAGMENT")
//                                        .addToBackStack("LIKERFRAGMENT")
//                                        .commit();
//                            }
//
//
//                        }
//                    });
//                }catch (Exception e){}
//
//                break;
//
//        }
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        return postList.size();
//    }
//
//    public class PostViewHolder extends RecyclerView.ViewHolder {
//        public TextView name, categoty_name, num_loves, num_comment, description,txt_time;
//        ImageView postImage, userIcon, loveIcon, commentIcon;
//        public RelativeLayout postVideo;
//        public LinearLayout lay_num_loves;
//
//        public PostViewHolder(View itemView) {
//            super(itemView);
//            lay_num_loves = (LinearLayout)itemView.findViewById(R.id.lay_num_loves);
//            name = (TextView) itemView.findViewById(R.id.txt_user_name);
//            categoty_name = (TextView) itemView.findViewById(R.id.txt_category_name);
//            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
//            num_loves = (TextView) itemView.findViewById(R.id.txt_num_loves);
//            num_comment = (TextView) itemView.findViewById(R.id.txt_num_comment);
//            description = (TextView) itemView.findViewById(R.id.txt_description);
//            postImage = (ImageView) itemView.findViewById(R.id.img_post);
//            loveIcon = (ImageView) itemView.findViewById(R.id.img_love);
//            commentIcon = (ImageView) itemView.findViewById(R.id.img_comment);
//            userIcon = (ImageView) itemView.findViewById(R.id.usericon);
//            postVideo = (RelativeLayout) itemView.findViewById(R.id.video_post);
//            Utils.setTypeface(mContext, (TextView) itemView.findViewById(R.id.txt_user_name), Config.QUICKSAND, Config.BOLD);
//            Utils.setTypeface(mContext, (TextView) itemView.findViewById(R.id.txt_category_name), Config.QUICKSAND, Config.REGULAR);
//            Utils.setTypeface(mContext, (TextView) itemView.findViewById(R.id.txt_description), Config.QUICKSAND, Config.BOLD);
//            Utils.setTypeface(mContext, (TextView) itemView.findViewById(R.id.txt_comment), Config.QUICKSAND, Config.REGULAR);
//            Utils.setTypeface(mContext, (TextView) itemView.findViewById(R.id.txt_num_comment), Config.QUICKSAND, Config.REGULAR);
//            Utils.setTypeface(mContext, (TextView) itemView.findViewById(R.id.txt_loves), Config.QUICKSAND, Config.REGULAR);
//            Utils.setTypeface(mContext, (TextView) itemView.findViewById(R.id.txt_num_loves), Config.QUICKSAND, Config.REGULAR);
//            Utils.setTypeface(mContext, (TextView) itemView.findViewById(R.id.txt_time), Config.QUICKSAND, Config.REGULAR);
//        }
//    }
//
//
//
//    class LikeTask extends AsyncTask<String, Void, String> {
//        HashMap<String, String> postDataParams;
//        int index;
//        ProgressDialog progressDialog;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            /*progressDialog = new ProgressDialog(mContext);
//            progressDialog.setCancelable(false);
//            progressDialog.setTitle("Please wait...");
//            progressDialog.show();*/
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            index = Integer.parseInt(params[4]);
//            postDataParams = new HashMap<String, String>();
//            postDataParams.put("user_id", params[0]);
//            postDataParams.put("post_id", params[1]);
//            postDataParams.put("owner_id", params[2]);
//            postDataParams.put("type", params[3]);
//
//            return HTTPUrlConnection.getInstance().load(mContext,Config.ADD_LIKE, postDataParams);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            //progressDialog.dismiss();
//            try {
//                JSONObject object = new JSONObject(result);
//                if (object.getBoolean("status")) {
//                    postList.get(index).numLoves = ""+(Integer.parseInt(postList.get(index).numLoves)+1);
//                    notifyDataSetChanged();
//                    Log.v("MYROLE", result);
//                } else {
//                    Toast.makeText(mContext, object.getString("message"), Toast.LENGTH_LONG).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//    }
//
//}
