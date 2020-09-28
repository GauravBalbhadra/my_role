package com.myrole.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myrole.R;
import com.myrole.dashboard.MainDashboardActivity;
import com.myrole.fragments.OtherProfileFragment;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.ProgressBarHandler;
import com.myrole.utils.Utils;
import com.myrole.vo.Contact;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ContactHolder> implements Filterable {
    List<Contact> contactsList;
    List<Contact> contactListFiltered;
    Context context;
    Activity activity;
    String userId,getuser_id;
    private ProgressBarHandler progressBarHandler;

    public FollowingAdapter(List<Contact> contacts, Context context, Activity activity, String user_id, String getuser_id) {
        this.contactsList = contacts;
        this.contactListFiltered = contacts;
        this.context = context;
        this.activity = activity;
        this.getuser_id = getuser_id;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_likers_item, parent, false);
        progressBarHandler = new ProgressBarHandler(activity);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactHolder holder, final int position) {
        final Contact contact = contactsList.get(position);
        userId = AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID);
//        if(contact.type != null) {
//            if (contact.type.equalsIgnoreCase("FACEBOOK")) {
//                holder.number.setVisibility(View.GONE);
//            } else {
//                holder.number.setVisibility(View.VISIBLE);
//                holder.number.setText(contact.phone);
//            }
//        }
        holder.btnFollowing.setVisibility(View.GONE);
        holder.btnFollow.setVisibility(View.GONE);
//        holder.btnInvite.setVisibility(View.GONE);

//        if (contact.follow.equals(contact.id)) {
//            holder.btnFollowing.setVisibility(View.VISIBLE);
//        } else if (contact.follow.equals("0")) {
//            holder.btnFollow.setVisibility(View.VISIBLE);
//        } else {
//            holder.btnInvite.setVisibility(View.VISIBLE);
//        }


        if (contact.id.equals(userId)){
            holder.btnFollowing.setVisibility(View.GONE);
            holder.btnFollow.setVisibility(View.GONE);
        }else  if (userId.equals(getuser_id)){
            holder.btnFollow.setVisibility(View.GONE);
            holder.btnFollowing.setVisibility(View.VISIBLE);
        }else {
            if (contact.follow.equals("0")){
                holder.btnFollow.setVisibility(View.VISIBLE);
                holder.btnFollowing.setVisibility(View.GONE);
            }else {
                holder.btnFollow.setVisibility(View.GONE);
                holder.btnFollowing.setVisibility(View.VISIBLE);
            }
        }
//        if (contact.id.equals(userId)){
//            holder.btnFollowing.setVisibility(View.GONE);
//        }else {
//            holder.btnFollowing.setVisibility(View.VISIBLE);
//        }

        holder.name.setText(contact.name);
        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FollowFriendTask().execute(position + "");
            }
        });

        holder.btnFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog(holder,position,contact.name);

               // new UnfollowFriendTask().execute(position + "");
            }
        });

//        holder.btnInvite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//                sendIntent.setData(Uri.parse("sms:"));
//                sendIntent.putExtra("sms_body", "Welcome to My Role");
//                context.startActivity(sendIntent);
//            }
//        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // context.startActivity(new Intent(context, OtherProfileActivity.class).putExtra("ownerId", String.valueOf(contact.id)));
                if (((MainDashboardActivity) context).fragmentManager != null) {
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(contact.id));
                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
                            .replace(R.id.container,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT2")
                            .addToBackStack( "OTHERPROFILEFRAGMENT2")
                            .commit();
                }
//                if (((MainDashboardActivity) context).fragmentManager != null) {
//
//                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(contact.id));
//                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
//                            .add(R.id.main_frame,
//                                    fragment,
//                                    "OTHERPROFILEFRAGMENT")
//                            .addToBackStack( "OTHERPROFILEFRAGMENT")
////                            .addToBackStack( null)
//                            .commit();
//                }
            }
        });
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // context.startActivity(new Intent(context, OtherProfileActivity.class).putExtra("ownerId", String.valueOf(contact.id)));
                if (((MainDashboardActivity) context).fragmentManager != null) {
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(contact.id));
                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
                            .replace(R.id.container,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT2")
                            .addToBackStack( "OTHERPROFILEFRAGMENT2")
//                            .addToBackStack( null)
                            .commit();
                }
            }
        });


        Glide.with(context).asBitmap().load(contact.image)
                .placeholder(R.drawable.default_avatar).centerCrop().into(holder.icon);
        if (position == 0)
            holder.line.setVisibility(View.INVISIBLE);
        else
            holder.line.setVisibility(View.VISIBLE);

    }
    public void AlertDialog(final ContactHolder holder, final int pos, String name) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("MY Role");

        // Setting Dialog Message
        alertDialog.setMessage("Unfollow " + name + "?");

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new UnfollowFriendTask().execute(String.valueOf(pos));
                //holder.btn_follow.setVisibility(View.VISIBLE);
               // holder.card_following.setVisibility(View.GONE);

            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    class ContactHolder extends RecyclerView.ViewHolder {
        TextView name, number, btnFollow, btnInvite;
        ImageView icon;
        CardView btnFollowing;
        View line;


        public ContactHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txt_name);
//            number = (TextView) itemView.findViewById(R.id.txt_number);
            btnFollow = (TextView) itemView.findViewById(R.id.btn_follow);
//            btnInvite = (TextView) itemView.findViewById(R.id.btn_invite);
            icon = (ImageView) itemView.findViewById(R.id.liker_icon);
            btnFollowing = (CardView) itemView.findViewById(R.id.card_following);
            line = (View) itemView.findViewById(R.id.top_line);

            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_name), Config.NEXA, Config.BOLD);
//            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_number), Config.NEXA, Config.REGULAR);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.btn_follow), Config.NEXA, Config.REGULAR);
//            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.btn_invite), Config.NEXA, Config.REGULAR);
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
            postDataParams.put("user_id",  contactsList.get(pos).id);
            postDataParams.put("follower_id", AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID));
            return HTTPUrlConnection.getInstance().load(context, Config.FOLLOW_USER, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            ((BaseActivity) context).dismissProgressDialog();
            progressBarHandler.hide();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    contactsList.get(pos).follow = userId;
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent("REFRESH_PROFILE");
                context.sendBroadcast(intent);
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
            postDataParams.put("user_id",contactsList.get(pos).id);
            postDataParams.put("follower_id", AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID));
            return HTTPUrlConnection.getInstance().load(context, Config.UNFOLLOW_USER, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            ((BaseActivity) context).dismissProgressDialog();
            progressBarHandler.hide();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    contactsList.get(pos).follow = "0";
                    contactsList.remove(pos);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactsList = contactListFiltered;
                } else {
                    List<Contact> filteredList = new ArrayList<>();
                    for (Contact row : contactListFiltered) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        // if (row.username.toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
                        if (row.name.toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
                        }
                    }

                    contactsList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactsList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactsList = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}