package com.myrole.adapter;//package com.myrole.adapter;
//
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Looper;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.PopupMenu;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.androidnetworking.AndroidNetworking;
//import com.androidnetworking.common.Priority;
//import com.androidnetworking.error.ANError;
//import com.androidnetworking.interfaces.AnalyticsListener;
//import com.androidnetworking.interfaces.StringRequestListener;
//import com.bumptech.glide.Glide;
//import com.myrole.AddComments;
//import com.myrole.HomeActivity;
//import com.myrole.R;
//import com.myrole.fragments.CommentsFragment;
//import com.myrole.fragments.LikerFragment;
//import com.myrole.fragments.NewUserGeneralPostFragment;
//import com.myrole.fragments.OtherProfileFragment;
//import com.myrole.fragments.PostDetailFragment;
//import com.myrole.utils.AppPreferences;
//import com.myrole.utils.Config;
//import com.myrole.utils.HTTPUrlConnection;
//import com.myrole.utils.Utils;
//import com.myrole.vo.Post;
//import com.squareup.picasso.Picasso;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.List;
//
//public class UserPostAdapter2 extends RecyclerView.Adapter<UserPostAdapter2.viewHolder> implements View.OnClickListener {
//    List<Post> postList;
//    Context context;
//    NewUserGeneralPostFragment fm;
//    AppPreferences preferences;
//    public UserPostAdapter2(List<Post> postList, Context context,NewUserGeneralPostFragment fm) {
//        this.postList = postList;
//        this.context = context;
//        this.fm=fm;
//        preferences = AppPreferences.getAppPreferences(context);
//    }
//
//    @Override
//    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.vh_fb_feed_post_photo1, parent, false);
//        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        view.setLayoutParams(layoutParams);
//        return new viewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final viewHolder holder, final int position) {
//
//        holder.postImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (((HomeActivity) context).fragmentManager != null) {
//                    PostDetailFragment fragment = PostDetailFragment.newInstance(postList
//                            .get(position).id);
//                    ((HomeActivity) context).fragmentManager.beginTransaction()
//                            .add(R.id.main_frame,
//                                    fragment,
//                                    "POSTDETAILFRAGMENT")
//                            .addToBackStack(null)
//                            .commit();
//                }
//            }
//        });
//        String url = postList.get(position).postImage;
//        Log.e("Sub>>", url.substring(url.length() - 4, url.length()));
//        if (url.substring(url.length() - 4, url.length()).equalsIgnoreCase(".mp4")) {
//            String[] url1=postList.get(position).postImage.split(".mp4");
//            String new_thumb =url1[0]+".jpg";
//            Glide.with(context).load(new_thumb).into(holder.postImage);
//        }
//        else
//            Glide.with(context).load(postList.get(position).postImage).into(holder.postImage);
//
//        //setValue
//        holder.txt_user_name.setText(postList.get(position).username);
//        if(postList.get(position).postTime.length()>0){
//            holder.txt_time.setText(Utils.timeCalculation(postList.get(position).postTime));
//        }
//
//        if(postList!=null && !postList.get(position).owner_image.equals("")){
//            Picasso.with(context).load(postList.get(position).owner_image).placeholder(R.drawable.default_avatar).into( holder.usericon);
//        }
////        else {
////            Picasso.with(context).load(R.drawable.default_avatar).into( holder.usericon);
////        }
//
//        holder.txt_description.setText(postList.get(position).description);
//        holder.txt_num_loves.setText(String.valueOf(postList.get(position).numLoves));
//        holder.lay_num_loves.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                Intent in_likes = new Intent(context, LikersActivity.class);
////                in_likes.putExtra("post_id",String.valueOf(postList.get(position).id));
////                context.startActivity(in_likes);
//
//                if (((HomeActivity) context).fragmentManager != null) {
//                    ((HomeActivity) context).fragmentManager.beginTransaction()
//                            .add(R.id.main_frame,
//                                    LikerFragment.newInstance(String.valueOf(postList.get(position).id)),
//                                    "LIKERFRAGMENT")
//                            .addToBackStack("LIKERFRAGMENT")
//                            .commit();
//                }
//
//            }
//        });
//
//        if(postList.get(position).islike.equals("1")){
//            holder.img_love.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_liked));
//        }
//        else {
//            holder.img_love.setImageDrawable(context.getResources().getDrawable(R.drawable.heart));
//        }
//        holder.txt_num_comment.setText(String.valueOf(postList.get(position).comment_count));
//        holder. img_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (((HomeActivity) context).fragmentManager != null) {
//                    ((HomeActivity) context).fragmentManager.beginTransaction()
//                            .add(R.id.main_frame,
//                                    CommentsFragment.newInstance(String.valueOf(postList.get(position).id), String.valueOf(position)),
//                                    "COMMENTSFRAGMENT")
//                            .addToBackStack("COMMENTSFRAGMENT_123")
//                            .commit();
//                }
//
////                Intent in_comment = new Intent(context, AddComments.class);
////                in_comment.putExtra("post_id",String.valueOf(postList.get(position).id));
////                context.startActivity(in_comment);
//            }
//        });
//         //txt_num_comment.setText(mItem.getC);
//        holder.txt_user_name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (((HomeActivity) context).fragmentManager != null) {
//                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(postList.get(position).ownerId));
//                    ((HomeActivity) context).fragmentManager.beginTransaction()
//                            .add(R.id.main_frame,
//                                    fragment,
//                                    "OTHERPROFILEFRAGMENT")
//                            .addToBackStack(null)
//                            .commit();
//                }
//            }
//        });
//        holder.usericon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (((HomeActivity) context).fragmentManager != null) {
//                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(postList.get(position).ownerId));
//                    ((HomeActivity) context).fragmentManager.beginTransaction()
//                            .add(R.id.main_frame,
//                                    fragment,
//                                    "OTHERPROFILEFRAGMENT")
//                            .addToBackStack(null)
//                            .commit();
//                }
//            }
//        });
//       holder. img_love.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String userId = AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID);
//                if (postList.get(position).islike.equals("0")) {
//                    postList.get(position).numLoves=String.valueOf(Integer.parseInt(postList.get(position).numLoves)+1);
//                    postList.get(position).islike="1";
//                } else {
//                    postList.get(position).numLoves=String.valueOf(Integer.parseInt(postList.get(position).numLoves)-1);
//                    postList.get(position).islike="0";
//                }
//                notifyDataSetChanged();
//                holder. img_love.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_liked));
//
//                new LikeTask().execute(userId, String.valueOf(postList.get(position).id),
//                        String.valueOf(postList.get(position).ownerId), "1",position+"");
//
//
//            }
//        });
//        holder.tv_more.setTag(position);
//        holder. tv_more.setOnClickListener(this);
//        Utils.setTypeface(context,   holder.txt_user_name, Config.QUICKSAND, Config.BOLD);
//        //Utils.setTypeface(mContext,  holder1.categoty_name, Config.QUICKSAND, Config.REGULAR);
//        Utils.setTypeface(context,   holder.txt_description, Config.QUICKSAND, Config.BOLD);
//        Utils.setTypeface(context,   holder.txt_comment, Config.QUICKSAND, Config.REGULAR);
//        Utils.setTypeface(context,   holder.txt_num_comment, Config.QUICKSAND, Config.REGULAR);
//        Utils.setTypeface(context,   holder.txt_loves, Config.QUICKSAND, Config.REGULAR);
//        Utils.setTypeface(context,   holder.txt_num_loves, Config.QUICKSAND, Config.REGULAR);
//        Utils.setTypeface(context,   holder.txt_time, Config.QUICKSAND, Config.REGULAR);
//
//    }
//    class LikeTask extends AsyncTask<String, Void, String> {
//        HashMap<String, String> postDataParams;
//        int index;
//        ProgressDialog progressDialog;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
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
//            return HTTPUrlConnection.getInstance().load(context,Config.ADD_LIKE, postDataParams);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            // progressDialog.dismiss();
//            try {
//                JSONObject object = new JSONObject(result);
//                if (object.getBoolean("status")) {
//
//                    Log.v("MYROLE", result);
//                } else {
//                    // Toast.makeText(mContext, object.getString("message"), Toast.LENGTH_LONG).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//    }
//    class LikeTask1 extends AsyncTask<String, Void, String> {
//        HashMap<String, String> postDataParams;
//        int index;
//        ProgressDialog progressDialog;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
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
//            return HTTPUrlConnection.getInstance().load(context,Config.ADD_LIKE, postDataParams);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            // progressDialog.dismiss();
//            try {
//                JSONObject object = new JSONObject(result);
//                if (object.getBoolean("status")) {
//
//                    Log.v("MYROLE", result);
//                } else {
//                    // Toast.makeText(mContext, object.getString("message"), Toast.LENGTH_LONG).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return postList.size();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tv_more:
//                onPopupButtonClick(v,(int)v.getTag());
//                break;
//        }
//    }
//    public void onPopupButtonClick(View button, final int position) {
//        PopupMenu popup = new PopupMenu(context, button);
//        final Post postListDTO =postList.get(position);
//        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
//        if(postListDTO.ownerId.equalsIgnoreCase(preferences.getStringValue(AppPreferences.USER_ID))){
//            MenuItem item = popup.getMenu().findItem(R.id.Report);
//            item.setVisible(false);
//            MenuItem item1 = popup.getMenu().findItem(R.id.delete);
//            item1.setVisible(true);
//        }
//        else {
//            MenuItem item = popup.getMenu().findItem(R.id.Report);
//            item.setVisible(true);
//            MenuItem item1 = popup.getMenu().findItem(R.id.delete);
//            item1.setVisible(false);
//        }
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.Share:
//
//                        Intent share = new Intent(android.content.Intent.ACTION_SEND);
//                        share.setType("text/plain");
//                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//
//                        // Add data to the intent, the receiving app will decide
//                        // what to do with it.
//                        share.putExtra(Intent.EXTRA_SUBJECT, "MY Role");
//                        share.putExtra(Intent.EXTRA_TEXT,  postListDTO.url);
//
//                        context.startActivity(Intent.createChooser(share, "Share link!"));
//
//                        break;
//                    case R.id.Report:
//                        optionDialog(position);
//                        break;
//                    case R.id.delete:
//                        callServiceForFeeds(Integer.parseInt(postList.get(position).id));
//                        AlertDialog(position);
//
//                        break;
//                }
//                return true;
//            }
//        });
//
//        popup.show();
//    }
//    public void AlertDialog(final int pos){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//
//        // Setting Dialog Title
//        alertDialog.setTitle("MY Role");
//
//        // Setting Dialog Message
//        alertDialog.setMessage("Are you sure you want delete this?");
//
//        // Setting Positive "Yes" Button
//        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog,int which) {
//                deleteRecord(pos);
//            }
//        });
//
//        // Setting Negative "NO" Button
//        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                // Write your code here to invoke NO event
//                dialog.cancel();
//            }
//        });
//
//        // Showing Alert Message
//        alertDialog.show();
//    }
//    private void callServiceForFeeds(int post_id) {
//
//        AppPreferences preferences = AppPreferences.getAppPreferences(context);
//        AndroidNetworking.post(Config.DELETE_USER_POST)
//                .setTag(this)
//                .addBodyParameter("user_id",preferences.getStringValue(AppPreferences.USER_ID))
//                .addBodyParameter("post_id",String.valueOf(post_id))
//
//                .setPriority(Priority.IMMEDIATE)
//                .build()
//                .setAnalyticsListener(new AnalyticsListener() {
//                    @Override
//                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
//                        Log.d("TAG", " timeTakenInMillis : " + timeTakenInMillis);
//                        Log.d("TAG", " bytesSent : " + bytesSent);
//                        Log.d("TAG", " bytesReceived : " + bytesReceived);
//                        Log.d("TAG", " isFromCache : " + isFromCache);
//                    }
//                })
//                .getAsString(new StringRequestListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        //dismissProgressDialog();
//
//                        Log.d("TAG", "delete_onResponse object : " + response.toString());
//                        Log.d("TAG", "onResponse isMainThread : " + String.valueOf(Looper.myLooper() == Looper.getMainLooper()));
//
//
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//
//                        //dismissProgressDialog();
//                        if (error.getErrorCode() != 0) {
//                            // received ANError from server
//                            // error.getErrorCode() - the ANError code from server
//                            // error.getErrorBody() - the ANError body from server
//                            // error.getErrorDetail() - just a ANError detail
//                            Log.d("TAG", "onError errorCode : " + error.getErrorCode());
//                            Log.d("TAG", "onError errorBody : " + error.getErrorBody());
//                            Log.d("TAG", "onError errorDetail : " + error.getErrorDetail());
//                        } else {
//                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
//                            Log.d("TAG", "onError errorDetail : " + error.getErrorDetail());
//                        }
//                    }
//                });
//
//    }
//    public void optionDialog(final int pos){
//
//        CharSequence options[] = new CharSequence[] {"It’s Spam", "It’s Inappropriate"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setCancelable(true);
//        builder.setTitle("Select your option:");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // the user clicked on options[which]
//                switch (which) {
//                    case 0:
//                        callServiceForReport("It’s Spam");
//                        deleteRecord(pos);
//                        break;
//                    case 1:
//                        callServiceForReport("It’s Inappropriate");
//                        deleteRecord(pos);
//                        break;
//
//                }
//            }
//        });
//        builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //the user clicked on Cancel
//            }
//        });
//        builder.show();
//    }
//
//    private void callServiceForReport(String s) {
//
//        AppPreferences preferences = AppPreferences.getAppPreferences(context);
//        AndroidNetworking.post(Config.REPORT_USER_POST)
//                .setTag(this)
//                .addBodyParameter("user_id",preferences.getStringValue(AppPreferences.USER_ID))
//                .addBodyParameter(" comment",s)
//
//                .setPriority(Priority.IMMEDIATE)
//                .build()
//                .setAnalyticsListener(new AnalyticsListener() {
//                    @Override
//                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
//                        Log.d("TAG", " timeTakenInMillis : " + timeTakenInMillis);
//                        Log.d("TAG", " bytesSent : " + bytesSent);
//                        Log.d("TAG", " bytesReceived : " + bytesReceived);
//                        Log.d("TAG", " isFromCache : " + isFromCache);
//                    }
//                })
//                .getAsString(new StringRequestListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        //dismissProgressDialog();
//
//                        Log.d("TAG", "delete_onResponse object : " + response.toString());
//                        Log.d("TAG", "onResponse isMainThread : " + String.valueOf(Looper.myLooper() == Looper.getMainLooper()));
//
//
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//
//                        //dismissProgressDialog();
//                        if (error.getErrorCode() != 0) {
//                            // received ANError from server
//                            // error.getErrorCode() - the ANError code from server
//                            // error.getErrorBody() - the ANError body from server
//                            // error.getErrorDetail() - just a ANError detail
//                            Log.d("TAG", "onError errorCode : " + error.getErrorCode());
//                            Log.d("TAG", "onError errorBody : " + error.getErrorBody());
//                            Log.d("TAG", "onError errorDetail : " + error.getErrorDetail());
//                        } else {
//                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
//                            Log.d("TAG", "onError errorDetail : " + error.getErrorDetail());
//                        }
//                    }
//                });
//
//    }
//
//    public void deleteRecord(int Pos){
//        postList.remove(Pos);
//        fm.recyclerView.setAdapter(new UserPostAdapter2(postList, context,fm));
//        notifyDataSetChanged();
//
//    }
//
//    class viewHolder extends RecyclerView.ViewHolder {
//        ImageView postImage;
//        public ImageView usericon,img_love,img_comment;
//        public TextView mInfo,txt_user_name,txt_time,txt_num_loves,txt_num_comment,txt_description,txt_loves,txt_comment,tv_more;
//        LinearLayout lay_num_loves;
//
//        public viewHolder(View itemView) {
//            super(itemView);
//            lay_num_loves = (LinearLayout)itemView.findViewById(R.id.lay_num_loves);
//            postImage = (ImageView) itemView.findViewById(R.id.img_post);
//            img_love = (ImageView) itemView.findViewById(R.id.img_love);
//            img_comment = (ImageView) itemView.findViewById(R.id.img_comment);
//            usericon = (ImageView) itemView.findViewById(R.id.usericon);
//            mInfo = (TextView) itemView.findViewById(R.id.info);
//            tv_more = (TextView) itemView.findViewById(R.id.tv_more);
//            txt_comment = (TextView) itemView.findViewById(R.id.txt_comment);
//            txt_loves = (TextView) itemView.findViewById(R.id.txt_loves);
//            txt_num_loves = (TextView) itemView.findViewById(R.id.txt_num_loves);
//            txt_num_comment = (TextView) itemView.findViewById(R.id.txt_num_comment);
//            txt_description = (TextView) itemView.findViewById(R.id.txt_description);
//            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
//            txt_user_name = (TextView) itemView.findViewById(R.id.txt_user_name);
//        }
//    }
//
//
//}