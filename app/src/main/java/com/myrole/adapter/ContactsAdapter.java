package com.myrole.adapter;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myrole.R;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.ProgressBarHandler;
import com.myrole.utils.Utils;
import com.myrole.vo.Contact;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactHolder> {
    List<Contact> contacts;
    Context context;
    Activity activity;
    private static final int PERMISSION_REQUEST = 1;
    private ProgressBarHandler progressBarHandler;

    public ContactsAdapter(List<Contact> contacts, Context context, Activity activity) {
        this.contacts = contacts;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_contacts_item, parent, false);
        progressBarHandler = new ProgressBarHandler(activity);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactHolder holder, final int position) {
        final Contact contact = contacts.get(position);
        if (contact.type != null) {
            if (contact.type.equalsIgnoreCase("FACEBOOK")) {
                holder.number.setVisibility(View.GONE);
            } else {
                holder.number.setVisibility(View.VISIBLE);
                holder.number.setText(contact.phone);
            }
        }


        // holder.btnFollow.setText("Follow Request Sent");

        holder.btnFollowing.setVisibility(View.GONE);
        holder.btnFollow.setVisibility(View.GONE);
        holder.btnInvite.setVisibility(View.GONE);
        holder.btn_private_follow.setVisibility(View.GONE);
        holder.btn_private_following.setVisibility(View.GONE);


        //  Toast.makeText(context,contact.status, Toast.LENGTH_LONG).show();
        if (contact.private_user.equals("0")) {

            if (contact.status.equalsIgnoreCase("FOLLOWING")) {
//            if( contact.private_user != null){
//                if( contact.private_user .equals("1")){
//                    holder.btnFollow.setText("Follow Request");
//                }else{
//                    holder.btnFollow.setText("Follow ");
//                }
//            }
                holder.btnFollowing.setVisibility(View.VISIBLE);
            } else if (contact.status.equalsIgnoreCase("FOLLOW")) {
//            if( contact.private_user != null){
//                if( contact.private_user .equals("1")){
//                    holder.btnFollow.setText("Follow Request");
//                }else{
//                    holder.btnFollow.setText("Follow ");
//                }
//            }
                holder.btnFollow.setVisibility(View.VISIBLE);
            } else {
                 if(contact.follow!=null){
                if (contact.follow.equals("0")) {
                    holder.btnFollowing.setVisibility(View.GONE);
                    holder.btnFollow.setVisibility(View.VISIBLE);
                    holder.btnInvite.setVisibility(View.GONE);
                } else if ((Integer.parseInt(contact.follow)) > 0) {
                    holder.btnFollowing.setVisibility(View.VISIBLE);
                    holder.btnFollow.setVisibility(View.GONE);
                    holder.btnInvite.setVisibility(View.GONE);
                }
//                if (contact.follow == null) {
//                    holder.btnFollowing.setVisibility(View.GONE);
//                    holder.btnFollow.setVisibility(View.GONE);
//                    holder.btnInvite.setVisibility(View.VISIBLE);
//                }
                }else {
                     holder.btnFollowing.setVisibility(View.GONE);
                     holder.btnFollow.setVisibility(View.GONE);
                     holder.btnInvite.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (contact.status.equalsIgnoreCase("REQUEST")) {
                holder.btn_private_follow.setVisibility(View.VISIBLE);
            } else if (contact.status.equalsIgnoreCase("SEND_REQUEST")) {
                holder.btn_private_following.setVisibility(View.VISIBLE);
            } else {
                 if(contact.follow!=null){
                if (contact.follow.equals("0")) {
                    holder.btnFollowing.setVisibility(View.GONE);
                    holder.btnFollow.setVisibility(View.VISIBLE);
                    holder.btnInvite.setVisibility(View.GONE);
                } else if ((Integer.parseInt(contact.follow)) > 0) {
                    holder.btnFollowing.setVisibility(View.VISIBLE);
                    holder.btnFollow.setVisibility(View.GONE);
                    holder.btnInvite.setVisibility(View.GONE);
                }
//                if (contact.follow == null) {
//                    holder.btnFollowing.setVisibility(View.GONE);
//                    holder.btnFollow.setVisibility(View.GONE);
//                    holder.btnInvite.setVisibility(View.VISIBLE);
//                }
                }else {
                     holder.btnFollowing.setVisibility(View.GONE);
                     holder.btnFollow.setVisibility(View.GONE);
                    holder.btnInvite.setVisibility(View.VISIBLE);
                }
            }

        }
        holder.name.setText(contact.name);

        holder.btn_private_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SendFollowRequestTask().execute(position + "");
            }
        });


        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if( contact.private_user != null){
//                    if( contact.private_user .equals("1")){
//                        holder.btnFollow.setText("Follow Request Sent");
//                        new SendFollowRequestTask().execute(position + "");
//                    }else{
//                       // holder.btnFollow.setText("Follow ");
                new FollowFriendTask().execute(position + "");
//                    }
//                }


            }
        });

        holder.btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//                sendIntent.setData(Uri.parse("sms:"+contacts.get(position).phone));
//                sendIntent.putExtra("sms_body", "Welcome to My Role");
//                context.startActivity(sendIntent);

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    //SEE NEXT STEP
//
//                    if (ContextCompat.checkSelfPermission(activity,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//                        //SEE NEXT STEP
//
//                        if (ActivityCompat.shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
//                            //show some description about this permission to the user as a dialog, toast or in Snackbar
//                            Snackbar.make(findViewById(R.id.rl), "You need to grant SEND SMS permission to send sms",
//                                    Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    //request the permission again
//                                    ActivityCompat.requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST);
//                                }
//                            }).show();
//                        } else {
//
//                            //request for the permission
//
//                        }
//
//                    } else {
//
//                        sendSMS("Welcome to My Role",contacts.get(position).phone);
//
//                    }
//
//                }else{
//
//                    sendSMS("Welcome to My Role",contacts.get(position).phone);
//
//                }

//                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//                    // request permission (see result in onRequestPermissionsResult() method)
//                    ActivityCompat.requestPermissions(activity,
//                            new String[]{Manifest.permission.SEND_SMS},
//                            1);
//                }else {
//                    sendSMS("Welcome to My Role",contacts.get(position).phone);
//                }

                String short_url_of_myrole_playstore="https://goo.gl/HDW5Yb";
                sendSMS("Boost your hidden creativity and talent with MY ROLE "+short_url_of_myrole_playstore, contacts.get(position).phone);
//                shareMyRole(contacts.get(position).phone);

            }
        });

        holder.btnFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if( contact.private_user != null){
//                    if( contact.private_user .equals("1")){
//                        holder.btnFollow.setText("Follow Request Sent");
//                    }else{
//                        holder.btnFollow.setText("Follow ");
//                    }
//                }
                new UnfollowFriendTask().execute(position + "");
            }
        });

        Glide.with(context).asBitmap().load(contact.image)
                .placeholder(R.drawable.user_mobile).centerCrop().into(holder.icon);
        if (position == 0)
            holder.line.setVisibility(View.INVISIBLE);
        else
            holder.line.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactHolder extends RecyclerView.ViewHolder {
        TextView name, number, btnFollow, btnInvite, btn_private_follow, btn_private_following;
        ImageView icon;
        CardView btnFollowing;
        View line;


        public ContactHolder(View itemView) {
            super(itemView);
            btn_private_follow = (TextView) itemView.findViewById(R.id.btn_private_follow);
            btn_private_following = (TextView) itemView.findViewById(R.id.btn_private_following);
            name = (TextView) itemView.findViewById(R.id.txt_name);
            number = (TextView) itemView.findViewById(R.id.txt_number);
            btnFollow = (TextView) itemView.findViewById(R.id.btn_follow);
            btnInvite = (TextView) itemView.findViewById(R.id.btn_invite);
            icon = (ImageView) itemView.findViewById(R.id.user_icon);
            btnFollowing = (CardView) itemView.findViewById(R.id.card_following);
            line = (View) itemView.findViewById(R.id.top_line);

            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_name), Config.NEXA, Config.BOLD);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_number), Config.NEXA, Config.REGULAR);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.btn_follow), Config.NEXA, Config.REGULAR);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.btn_invite), Config.NEXA, Config.REGULAR);
        }
    }

    class FollowFriendTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;
        int pos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            ((BaseActivity) context).showProgessDialog();
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
            pos = Integer.parseInt(params[0]);
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", contacts.get(pos).id);
            postDataParams.put("follower_id", AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID));

            return HTTPUrlConnection.getInstance().load(context, Config.FOLLOW_USER, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            ((BaseActivity) context).dismissProgressDialog();
//            Toast.makeText(context, "user -id :" + contacts.get(pos).id, Toast.LENGTH_LONG).show();
//            Toast.makeText(context, "follower_id :" + AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID), Toast.LENGTH_LONG).show();
            progressBarHandler.hide();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    contacts.get(pos).follow = contacts.get(pos).id;
                    contacts.get(pos).status = "FOLLOWING";
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class SendFollowRequestTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;
        int pos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//           showProgessDialog();
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
//          pos = Integer.parseInt(params[0]);
            pos = Integer.parseInt(params[0]);
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", contacts.get(pos).id);
            postDataParams.put("follower_id", AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID));

//            postDataParams.put("user_id", userId);
//            postDataParams.put("follower_id", AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.USER_ID));
            return HTTPUrlConnection.getInstance().load(context, Config.SEND_FOLLOW_REQUEST, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.hide();

            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    contacts.get(pos).follow = contacts.get(pos).id;
                    contacts.get(pos).status = "SEND_REQUEST";
                    notifyDataSetChanged();

//                  liker_list.get(pos).follows = "1";

//                    isSendRequest = true;
//                    view.findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
//                    view.findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
//                    ((TextView) view.findViewById(R.id.btn_follow)).setText("Follow Request Sent");
//                    adapter.notifyDataSetChanged();

//                    if (private_user.equals("1")){
//                        if (isFollow){
//
//                            pager.setVisibility(View.VISIBLE);
//
//                            getView().findViewById(R.id.txt_private_user).setVisibility(View.GONE);
//
//                            tabLayout.setVisibility(View.VISIBLE);
//                        }else {
//                            pager.setVisibility(View.GONE);
//
//                            getView().findViewById(R.id.txt_private_user).setVisibility(View.VISIBLE);
//
//                            tabLayout.setVisibility(View.GONE);
//                        }
//
//                    }else {
//                        Toast.makeText(getContext(), "Public user", Toast.LENGTH_SHORT).show();
//                    }

                } else {
                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class UnfollowFriendTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;
        int pos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            ((BaseActivity) context).showProgessDialog();
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
            pos = Integer.parseInt(params[0]);
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", contacts.get(pos).id);
            postDataParams.put("follower_id", AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID));
            return HTTPUrlConnection.getInstance().load(context, Config.UNFOLLOW_USER, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            ((BaseActivity) context).dismissProgressDialog();
            progressBarHandler.hide();


//            try {
//                JSONObject object = new JSONObject(result);
//                if (object.getBoolean("status")) {
//                    adapter.notifyDataSetChanged();
//                    isSendRequest = false;
//                    isFollow = false;
//                    view.findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
//
//                    if (private_user.equals("1")) {
//                        holder.btnFollow.setVisibility(View.VISIBLE);
//                        ((TextView) view.findViewById(R.id.btn_follow)).setText("Follow Request");
//                        if (isSendRequest) {
//
//                            view.findViewById(R.id.txt_about).setVisibility(View.VISIBLE);
//
//                            view.findViewById(R.id.followers).setClickable(true);
//
//                            view.findViewById(R.id.following).setClickable(true);
//
//                            pager.setVisibility(View.VISIBLE);
//
//                            view.findViewById(R.id.txt_private_user).setVisibility(View.GONE);
//
//                            tabLayout.setVisibility(View.VISIBLE);
//
//                        } else {
//
//                            pager.setVisibility(View.GONE);
//
//                            view.findViewById(R.id.txt_private_user).setVisibility(View.VISIBLE);
//
//                            tabLayout.setVisibility(View.GONE);
//
//                            view.findViewById(R.id.txt_about).setVisibility(View.GONE);
//
//                            view.findViewById(R.id.followers).setClickable(false);
//
//                            view.findViewById(R.id.following).setClickable(false);
//
//                        }
//
//                    } else {
//                        view.findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
//                        Toast.makeText(context, "Public user", Toast.LENGTH_SHORT).show();
//
//                    }
//                } else {
//
//                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
//                }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    contacts.get(pos).follow = "0";
                    contacts.get(pos).status = "FOLLOW";
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void sendSMS(String message, String phoneNumber) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                // request permission (see result in onRequestPermissionsResult() method)
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.SEND_SMS},
                        6969);
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context); // Need to change the build to API 19

                    Uri mUri = Uri.parse("smsto:" + phoneNumber);
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO, mUri);
                    smsIntent.putExtra("sms_body", message);

                    if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
                    // any app that support this intent.
                    {
                        smsIntent.setPackage(defaultSmsPackageName);
                    }
                    context.startActivity(smsIntent);
                }

            } else {

                String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context); // Need to change the build to API 19

                Uri mUri = Uri.parse("smsto:" + phoneNumber);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, mUri);
                sendIntent.putExtra("sms_body", message);


//                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
////                    smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
////                    smsIntent.setType("vnd.android-dir/mms-sms");
//                smsIntent.setType("text/plain");
////                    smsIntent.setData(Uri.parse("sms:" + phoneNumber));
////                    smsIntent.putExtra("sms_body", message);
//                smsIntent.putExtra(Intent.EXTRA_SUBJECT, "APP NAME (Open it in Google Play Store to Download the Application)");
//                smsIntent.putExtra(Intent.EXTRA_TEXT, message);
//                smsIntent.setData(Uri.parse("sms:" + phoneNumber));

                if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
                // any app that support this intent.
                {
                    sendIntent.setPackage(defaultSmsPackageName);
                }

                context.startActivity(sendIntent);
            }
        } else // For early versions, do what worked for you before.
        {
            Uri mUri = Uri.parse("smsto:" + phoneNumber);
            Intent sentIntent = new Intent(Intent.ACTION_SENDTO, mUri);
            sentIntent.putExtra("sms_body", message);
            context.startActivity(sentIntent);
        }
    }

    private void shareMyRole(String phoneNumber) {

        String shareBody = "https://play.google.com/store/apps/details?id=************************";

//        Uri smsUri = Uri.parse("smsto:"+phoneNumber);
//        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//        intent.putExtra("address", phoneNumber);
//        intent.putExtra("sms_body", "Welcome to My Role");
//        intent.setType("vnd.android-dir/mms-sms");
//        context.startActivity(intent);
//
        Uri mUri = Uri.parse("smsto:" + phoneNumber);
        Intent sharingIntent = new Intent(Intent.ACTION_SENDTO, mUri);
        sharingIntent.putExtra("sms_body", "Welcome to MY ROLE");
        context.startActivity(sharingIntent);

    }

}