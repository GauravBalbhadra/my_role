package com.myrole.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.myrole.R;
import com.myrole.dashboard.MainDashboardActivity;
import com.myrole.data.CommentsDTO;
import com.myrole.fragments.OtherProfileFragment;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.Utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Rakesh on 8/2/2016.
 */

public class CommentsAdapter extends BaseAdapter {
    final static String TAG = "MYROLE";
    private Activity activity;
    Context context;
    private LayoutInflater inflater;
    private List<CommentsDTO> comm_list;
    private CommentsDTO current;
    private String userId;

    public CommentsAdapter(Context context,Activity activity, List<CommentsDTO> movieItems) {

        this.activity = activity;
        this.context = context;
        this.comm_list = movieItems;

    }

    @Override
    public int getCount() {

        return comm_list.size();

    }

    @Override
    public Object getItem(int location) {
        return comm_list.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        current = comm_list.get(position);

        userId = AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.USER_ID);

        Utils.setTypeface(activity, (TextView) convertView.findViewById(R.id.tv_user_name), Config.QUICKSAND, Config.BOLD);

        Utils.setTypeface(activity, (TextView) convertView.findViewById(R.id.tv_msg), Config.QUICKSAND, Config.REGULAR);

        Utils.setTypeface(activity, (TextView) convertView.findViewById(R.id.tv_time), Config.QUICKSAND, Config.REGULAR);

        final ImageView iv_profile = (ImageView) convertView.findViewById(R.id.iv_profile);
        String image=comm_list.get(position).getImage();
        try{
            Glide.with(context).load(image).placeholder(R.drawable.default_avatar)
                    .centerCrop().into(iv_profile);
        }catch (Exception e){}


        if (userId.equals(comm_list.get(position).getUser_id()) || userId.equals(comm_list.get(position).getPost_user_id())) {

            convertView.findViewById(R.id.txt_delete).setVisibility(View.VISIBLE);


        } else {

            convertView.findViewById(R.id.txt_delete).setVisibility(View.GONE);

        }


//        if (comm_list.get(position).getImage() != null) {
//            Glide.with(activity).load(comm_list.get(position).getImage()).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_profile) {
//                @Override
//                protected void setResource(Bitmap resource) {
//                    RoundedBitmapDrawable circularBitmapDrawable =
//                            RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
//                    circularBitmapDrawable.setCircular(true);
//                    iv_profile.setImageDrawable(circularBitmapDrawable);
//                }
//            });
//        }

        final View finalConvertView = convertView;
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                ((ImageView) finalConvertView.findViewById(R.id.txt_delete)).setImageDrawable(activity.getResources().getDrawable(R.drawable.delete_black));
                finalConvertView.findViewById(R.id.txt_delete).setEnabled(true);
                return false;
            }
        });






        // getting movie data for the row
        CommentsDTO m = comm_list.get(position);

        ((TextView) convertView.findViewById(R.id.tv_user_name)).setText(m.getName());
        ((TextView) convertView.findViewById(R.id.tv_msg)).setText(StringEscapeUtils.unescapeJava(m.getComments()));
        if (!m.getCreated_at().equalsIgnoreCase("Just Now")) {
            ((TextView) convertView.findViewById(R.id.tv_time)).setText(Utils.timeCalculation(m.getCreated_at()));
        } else {
            ((TextView) convertView.findViewById(R.id.tv_time)).setText(m.getCreated_at());
        }

    //    convertView.findViewById(R.id.txt_delete).setEnabled(false);


        convertView.findViewById(R.id.txt_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              int index = comm_list.indexOf(current);
//                if (comm_list.get(position).isDelete()){
//                    current = comm_list.get(position);
//                    comm_list.remove(current);
//                    callServiceForDeleteComments(current.getComment_id());
//                    notifyDataSetChanged();
//                }
               // Toast.makeText(activity, "clcik", Toast.LENGTH_LONG).show();
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
                        removeItem(current);

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

        convertView.findViewById(R.id.tv_user_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainDashboardActivity) activity).fragmentManager != null) {
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(current.getUser_id()));
                    ((MainDashboardActivity) activity).fragmentManager.beginTransaction()
                            .replace(R.id.container,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT")
                            .addToBackStack( "OTHERPROFILEFRAGMENT")
                            .commit();

                }

//                Intent intent = new Intent(activity,MainDashboardActivity.class);
//                intent.putExtra("OTHERPROFILEFRAGMENT",true);
//                intent.putExtra("OTHERPROFILEFRAGMENT_id",String.valueOf(current.getUser_id()));
//                activity.startActivity(intent);
//                activity.finish();
            }
        });

       convertView.findViewById(R.id.iv_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((MainDashboardActivity) context).fragmentManager != null) {
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(current.getUser_id()));
                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
                            .replace(R.id.container,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT")
                            .addToBackStack( "OTHERPROFILEFRAGMENT")
                            .commit();
                }

//
//                if (((MainDashboardActivity) activity).fragmentManager != null) {
//                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(current.getUser_id()));
//                    ((MainDashboardActivity) activity).fragmentManager.beginTransaction()
//                            .replace(R.id.main_frame,
//                                    fragment,
//                                    "OTHERPROFILEFRAGMENT")
//                            .addToBackStack(null)
//                            .commit();
//
//                }
            }
        });
//
        return convertView;
    }

    public void removeItem(CommentsDTO item) {
        comm_list.remove(item);
        notifyDataSetChanged();
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

//                              current = new Gson().fromJson(response, CommentsDTO.class);

                                ((ImageView) convertView.findViewById(R.id.txt_delete)).setImageDrawable(activity.getResources().getDrawable(R.drawable.delete_blur));


//                                int num_comm = ((AddComments) activity).getNum_comm();

                              //  ((AddComments) activity).setNum_comm(num_comm - 1);

                              //  removeItem(current);

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
                            // received ANError from server
                            // error.getErrorCode() - the ANError code from server
                            // error.getErrorBody() - the ANError body from server
                            // error.getErrorDetail() - just a ANError detail
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

//    public void onLongClickEvent(View convertView,int position){
//
////        comm_list.get(position).setDelete(true);
//        ((ImageView)convertView.findViewById(R.id.txt_delete)).setImageDrawable(activity.getResources().getDrawable(R.drawable.delete_black));
//        convertView.findViewById(R.id.txt_delete).setEnabled(true);
//
//
//    }

}