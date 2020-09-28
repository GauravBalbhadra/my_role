package com.myrole.adapter;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myrole.R;
import com.myrole.dashboard.MainDashboardActivity;
import com.myrole.fragments.OtherProfileFragment;
import com.myrole.utils.Config;
import com.myrole.utils.Utils;
import com.myrole.vo.Contact;

import java.util.List;

//import com.myrole.fragments.OtherProfileFragment2;

public class SearchContactsAdapter extends RecyclerView.Adapter<SearchContactsAdapter.ContactHolder> {
    List<Contact> contacts;
    Context context;

    public SearchContactsAdapter(Context context, List<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_contacts_item, parent, false);

        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, final int position) {
        final Contact contact = contacts.get(position);
        holder.name.setText(contact.name);
//        if (contact.phone.isEmpty()) {
//            holder.number.setVisibility(View.GONE);
//        } else {
//            holder.number.setVisibility(View.VISIBLE);
//            holder.number.setText(contact.phone);
//        }

        holder.btnFollowing.setVisibility(View.GONE);
        holder.btnFollow.setVisibility(View.GONE);
        holder.btnInvite.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeybord(context,v);
                //Utils.hideKeybord2(activity,v);

                //when i find other solution i can replace with this solution
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {


                if (((MainDashboardActivity) context).fragmentManager != null) {
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(contacts
                            .get(position).id);
                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
                            .add(R.id.container,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT")
                            .addToBackStack("g")
                            .commit();
                }
                    }
                }, 500);//end
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
        TextView name, number, btnFollow, btnInvite;
        ImageView icon;
        CardView btnFollowing;
        View line;


        public ContactHolder(View itemView) {
            super(itemView);
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

}