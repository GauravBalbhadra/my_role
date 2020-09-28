package com.myrole.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestHolder> {
    List<Contact> contacts;
    Context context;
    Activity activity;
    String userId;
    String status;
    int po;
    private ProgressBarHandler progressBarHandler;

    public RequestAdapter(List<Contact> contacts, Context context, Activity activity) {
        this.contacts = contacts;
        this.context = context;
        this.activity = activity;
    }

    public void delete(int position) {
        contacts.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAt(int position) {
        contacts.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, contacts.size());
    }


    @Override
    public RequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_request_item, parent, false);
        progressBarHandler = new ProgressBarHandler(activity);
        return new RequestHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestHolder holder, final int position) {
        final Contact contact = contacts.get(position);
        userId = AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID);



        holder.name.setText(contact.name);

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "1";
                new FollowRequestActionTask().execute(position + "");
               // removeAt(position);
                po=position;
            }
        });

        holder.btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "0";
                new FollowRequestActionTask().execute(position + "");
//                removeAt(position);
                po=position;
            }
        });

        Glide.with(context).asBitmap().load(contact.image)
                .placeholder(R.drawable.user_mobile).centerCrop().into(holder.icon);
        if (position == 0)
            holder.line.setVisibility(View.INVISIBLE);
        else
            holder.line.setVisibility(View.VISIBLE);


        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainDashboardActivity) context).fragmentManager != null) {
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(contacts.get(position).id));
                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
                            .replace(R.id.container,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT")
                            .addToBackStack("m")
                            .commit();
                }
            }
        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainDashboardActivity) context).fragmentManager != null) {
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(contacts.get(position).id));
                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
                            .replace(R.id.container,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT")
                            .addToBackStack("m")
                            .commit();
                }
            }
        });


    }



    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class RequestHolder extends RecyclerView.ViewHolder {
        TextView name, number, btnAccept, btnDecline;
        ImageView icon;
        View line;


        public RequestHolder(View itemView) {
            super(itemView);
                Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf");

               // ((TextView) view.findViewById(R.id.txt_update_status)).setTypeface(face);
                name = (TextView) itemView.findViewById(R.id.txt_name);
                number = (TextView) itemView.findViewById(R.id.txt_number);
                btnAccept = (TextView) itemView.findViewById(R.id.btn_accept);
                btnDecline = (TextView) itemView.findViewById(R.id.btn_decline);
                icon = (ImageView) itemView.findViewById(R.id.user_icon);
                line = (View) itemView.findViewById(R.id.top_line);
                btnAccept.setTypeface(face);
                btnDecline.setTypeface(face);
                Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_name), Config.NEXA, Config.BOLD);
//            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_number), Config.NEXA, Config.REGULAR);
          //  Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.btn_accept), Config.NEXA, Config.REGULAR);
         //   Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.btn_decline), Config.NEXA, Config.REGULAR);
        }
    }

    class FollowRequestActionTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;
        int pos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
//            pos = Integer.parseInt(params[0]);
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id",  AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID));
            postDataParams.put("follower_id",contacts.get(pos).id);
            postDataParams.put("status",status);
//            final  int h1=params[0];
            return HTTPUrlConnection.getInstance().load(context, Config.FOLLOW_REQUEST_ACTION, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBarHandler.hide();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                   // delete(pos);
                    removeAt(po);
//                    notifyDataSetChanged();
//                    if (contacts.size() == 0 || contacts == null){
//
//                    }
                } else {
                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}