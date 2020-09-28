package com.myrole.adapter;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.myrole.vo.Liker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LikerAdapter extends RecyclerView.Adapter<LikerAdapter.LikerHolder>  implements Filterable {
    List<Liker> liker_list;
    List<Liker> likerListFiltered;
    Context context;
    Activity activity;
    String userId;
    private ProgressBarHandler progressBarHandler;

    public LikerAdapter(List<Liker> liker_list, Context context, Activity activity) {
        this.liker_list = liker_list;
        this.likerListFiltered = liker_list;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public LikerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_likers_item, parent, false);
//        progressBarHandler = new ProgressBarHandler(context);
        userId = AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID);
        return new LikerHolder(view);
    }

    @Override
    public void onBindViewHolder(LikerHolder holder, final int position) {
        progressBarHandler = holder.progressBarHandler;
        final Liker liker = liker_list.get(position);
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
//      holder.btnInvite.setVisibility(View.GONE);


        if (liker.follows.equals("0")) {
            if (liker.user_id.equals(userId)) {
                holder.btnFollow.setVisibility(View.GONE);
            } else {
                holder.btnFollow.setVisibility(View.VISIBLE);
            }

        } else {
            if (liker.user_id.equals(userId)) {
                holder.btnFollowing.setVisibility(View.GONE);
            } else {
                holder.btnFollowing.setVisibility(View.VISIBLE);
            }
        }
        holder.name.setText(liker.name);

        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FollowFriendTask().execute(position + "");
            }
        });

        holder.btnFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UnfollowFriendTask().execute(position + "");
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

        Glide.with(context).asBitmap().load(liker.image)
                .placeholder(R.drawable.user_mobile).centerCrop().into(holder.icon);

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainDashboardActivity) context).fragmentManager != null) {
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(liker.user_id));
                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
                            .replace(R.id.container,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT")
                            .addToBackStack("OTHERPROFILEFRAGMENT")
                            .commit();
                }
            }
        });
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainDashboardActivity) context).fragmentManager != null) {
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(liker.user_id));
                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
                            .replace(R.id.container,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT")
                            .addToBackStack("OTHERPROFILEFRAGMENT")
                            .commit();
                }
            }
        });


        if (position == 0)
            holder.line.setVisibility(View.INVISIBLE);
        else
            holder.line.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return liker_list.size();
    }

    class LikerHolder extends RecyclerView.ViewHolder {
        TextView name, number, btnFollow, btnInvite;
        ImageView icon;
        CardView btnFollowing;
        View line;
        ProgressBarHandler progressBarHandler;



        public LikerHolder(View itemView) {
            super(itemView);
            progressBarHandler = new ProgressBarHandler(activity);
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
            postDataParams.put("user_id", liker_list.get(pos).user_id);
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
                    liker_list.get(pos).follows = "1";
                    notifyDataSetChanged();
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
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
            pos = Integer.parseInt(params[0]);
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", liker_list.get(pos).user_id);
            postDataParams.put("follower_id", AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID));
            return HTTPUrlConnection.getInstance().load(context, Config.UNFOLLOW_USER, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
           progressBarHandler.hide();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    liker_list.get(pos).follows = "0";
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
                    liker_list = likerListFiltered;
                } else {
                    List<Liker> filteredList = new ArrayList<>();
                    for (Liker row : likerListFiltered) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        // if (row.username.toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
                        if (row.name.toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
                        }
                    }

                    liker_list = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = liker_list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                liker_list = (ArrayList<Liker>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}