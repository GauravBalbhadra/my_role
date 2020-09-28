package com.myrole.adapter;

/**
 * Created by welcome on 03-01-2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.myrole.R;
import com.myrole.dashboard.MainDashboardActivity;
import com.myrole.dashboard.interfaces.InvokedFeedsFunctions;
import com.myrole.data.CommentsDTO;
import com.myrole.fragments.OtherProfileFragment;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.Utils;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//;
//

public class CommentsAdapter2 extends RecyclerView.Adapter<CommentsAdapter2.MyViewHolder> {

    View itemView;

    final static String TAG = "MYROLE";
    private Activity activity;
    Context context;
    private LayoutInflater inflater;
    private List<CommentsDTO> comm_list;
    private CommentsDTO current;
    private String userId;
    InvokedFeedsFunctions mFunctionsFeeds ;

    public CommentsAdapter2(Context context, Activity activity, List<CommentsDTO> movieItems, InvokedFeedsFunctions mFunctions) {
        this.activity = activity;
        this.context = context;
        this.comm_list = movieItems;
        mFunctionsFeeds = mFunctions ;

    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        userId = AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.USER_ID);
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CommentsDTO item = comm_list.get(position);

        //  DashboradDTO dashboradDTO = dashboradDTOArrayList.get(position);
        String image_url = item.getImage();


        if (userId.equals(comm_list.get(position).getUser_id()) || userId.equals(comm_list.get(position).getPost_user_id())) {

            holder.txt_delete.setVisibility(View.VISIBLE);


        } else {

            holder.txt_delete.setVisibility(View.GONE);

        }

        Picasso.get().load(image_url).placeholder(R.drawable.default_avatar).into(holder.iv_profile);

        final View finalConvertView = itemView;
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                holder.txt_delete.setImageDrawable(activity.getResources().getDrawable(R.drawable.delete_black));
                holder.txt_delete.setEnabled(true);
                return false;
            }
        });

        CommentsDTO m = comm_list.get(position);
        holder.tv_user_name.setText(m.getName());
        try {
            holder.tv_msg.setText(StringEscapeUtils.unescapeJava(m.getComments()));
        } catch (Exception e) {
        }

        if (!m.getCreated_at().equalsIgnoreCase("Just Now")) {
            holder.tv_time.setText(Utils.timeCalculation(m.getCreated_at()));
        } else {
            holder.tv_time.setText(m.getCreated_at());
        }

        holder.txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current = comm_list.get(position);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setCancelable(false);
                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete");
                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete this comment?");
                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.delete_black);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke YES event
//                        comm_list.remove(position);
//                        adapter.notifyDataSetChanged()
//                            notifyItemRemoved(position);
//                            notifyItemRangeChanged(position, comm_list.size());
                        // removeItem(current);
                        removeAt(position);
                        callServiceForDeleteComments(finalConvertView, current.getComment_id());
                    }
                });
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });
                // Showing Alert Message
                alertDialog.show();
            }
        });

        holder.tv_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                current = comm_list.get(position);
                if (((MainDashboardActivity) activity).fragmentManager != null) {
                    if(mFunctionsFeeds != null) {
                        mFunctionsFeeds.dismissCommentSheet();
                    }
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(comm_list.get(position).getUser_id()));
                    ((MainDashboardActivity) activity).fragmentManager.beginTransaction()
                            .replace(R.id.container,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT")
                            .addToBackStack("OTHERPROFILEFRAGMENT")
                            .commit();
                }
            }
        });

        holder.iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((MainDashboardActivity) context).fragmentManager != null) {
                    if(mFunctionsFeeds != null) {
                        mFunctionsFeeds.dismissCommentSheet();
                    }
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(comm_list.get(position).getUser_id()));
                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
                            .replace(R.id.container,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT")
                            .addToBackStack("OTHERPROFILEFRAGMENT")
                            .commit();
                }
            }
        });
    }


    public void removeAt(int position) {
        comm_list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, comm_list.size());
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_user_name, tv_msg, tv_time;
        public ImageView txt_delete;

        ImageView img_trip_type;
        CircleImageView iv_profile;

        public MyViewHolder(View view) {
            super(view);
            iv_profile = (CircleImageView) view.findViewById(R.id.iv_profile);
            tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
            tv_msg = (TextView) view.findViewById(R.id.tv_msg);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            txt_delete = (ImageView) view.findViewById(R.id.txt_delete);

            Utils.setTypeface(activity, (TextView) view.findViewById(R.id.tv_user_name), Config.QUICKSAND, Config.BOLD);
            Utils.setTypeface(activity, (TextView) view.findViewById(R.id.tv_msg), Config.QUICKSAND, Config.REGULAR);
            Utils.setTypeface(activity, (TextView) view.findViewById(R.id.tv_time), Config.QUICKSAND, Config.REGULAR);
        }
    }

    private void callServiceForDeleteComments(final View convertView, String s) {
        AndroidNetworking.post(Config.DELETE_COMMENT)
                .setTag(this)
                .addBodyParameter("user_id", userId)
                .addBodyParameter("comment_id", s)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("DELETE_COMMENTS:-" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("status")) {
                                Toast.makeText(activity, "Comment deleted.", Toast.LENGTH_SHORT).show();
                                ((ImageView) convertView.findViewById(R.id.txt_delete)).setImageDrawable(activity.getResources().getDrawable(R.drawable.delete_blur));
                            } else {
                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        //dismissProgressDialog();
                        if (error.getErrorCode() != 0) {
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return comm_list.size();
    }
}