package com.myrole.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.myrole.MyRoleDeatil;
import com.myrole.R;
import com.myrole.components.BottomBar;
import com.myrole.dashboard.MainDashboardActivity;
import com.myrole.data.FollowingDTO;
import com.myrole.fragments.FollowingActivityFragment;
import com.myrole.fragments.OtherProfileFragment;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.Utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class ActivityFollowingAdapter extends RecyclerView.Adapter<ActivityFollowingAdapter.viewHolder> {
    Context context;
    List<FollowingDTO> current_data;
    BottomBar bottom_bar;
    String myrole_user_id;
    Activity mactivity;
    private FollowingActivityFragment gffragment;

    public ActivityFollowingAdapter(Activity activity, Context context, List<FollowingDTO> current_data, BottomBar bottom_bar) {

        this.mactivity = activity;
        this.context = context;
        this.current_data = current_data;
        this.bottom_bar = bottom_bar;
        myrole_user_id = AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID);
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.following_view, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {

        final FollowingDTO current = current_data.get(position);
        try {
            if (current.getMsg() != null && !current.getMsg().equals("")) {
                String time = Utils.timeCalculation(current.getAgo());
                time = "<font COLOR='#D2D0D2'><b>" + time + "</b></font>";
                String txtAppend = current.getMsg();
                txtAppend = "<b>" + StringEscapeUtils.unescapeJava(txtAppend) + "</b>" + " " + time;
                Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                        Html.fromHtml(txtAppend));

                Spannable processedText = removeUnderlines(spannedText);

                if (holder.txt_message != null) {
                    holder.txt_message.setText(processedText);
                }
            }
        } catch (Exception e) {
        }

        holder.txt_message.setMovementMethod(LinkMovementMethod.getInstance());
        final String activity = current.getActivity();
        String Btn_value = current.getBtn_value();
        final int user_to_id = current.getUser_to();
        final int user_for_id = current.getUser_for();
        final String current_Username_to = current.getUsername_to();

        if (activity.equals("followers") || activity.equals("followInvite")) {


//            if (user_for_id != Integer.parseInt(myrole_user_id) && user_to_id != Integer.parseInt(myrole_user_id)) {
//
//            } else {
            holder.img.setVisibility(View.GONE);
            if (Btn_value != null && !Btn_value.isEmpty()) {
                if (Btn_value.equals("1")) {
                    holder.btn_follow.setVisibility(View.VISIBLE);
//                    holder.btn_follow.setVisibility(View.GONE);
                    holder.card_following.setVisibility(View.GONE);
                    holder.btn_request.setVisibility(View.GONE);
                } else if (Btn_value.equals("2")) {
                    holder.card_following.setVisibility(View.VISIBLE);
//                    holder.card_following.setVisibility(View.GONE);
                    holder.btn_follow.setVisibility(View.GONE);
                    holder.btn_request.setVisibility(View.GONE);

                } else if (Btn_value.equals("3")) {
                    holder.btn_request.setVisibility(View.VISIBLE);
//                    holder.btn_request.setVisibility(View.GONE);
                    holder.btn_follow.setVisibility(View.GONE);
                    holder.card_following.setVisibility(View.GONE);
                }
            }
//            }

        } else {
            holder.btn_follow.setVisibility(View.GONE);
            holder.img.setVisibility(View.VISIBLE);
        }

//        else if (activity.equals("followInvite"))  {
//
//            holder.img.setVisibility(View.GONE);
//            if (Btn_value!=null && !Btn_value.isEmpty()) {
//                if (Btn_value.equals("1")) {
//                    holder.btn_follow.setVisibility(View.VISIBLE);
//                    holder.card_following.setVisibility(View.GONE);
//                    holder.btn_request.setVisibility(View.GONE);
//                } else if (Btn_value.equals("2")) {
//                    holder.card_following.setVisibility(View.VISIBLE);
//                    holder.btn_follow.setVisibility(View.GONE);
//                    holder.btn_request.setVisibility(View.GONE);
//
//                }else if(Btn_value.equals("3")) {
//                    holder.btn_request.setVisibility(View.VISIBLE);
//                    holder.btn_follow.setVisibility(View.GONE);
//                    holder.card_following.setVisibility(View.GONE);
//                }
//            }
//        }


//        String time = Utils.timeCalculation(current.getAgo());
//        holder.textTime.setText(time);
//      holder.txt_message.setText(Html.fromHtml(current_data.get(position).getMsg()));

//      holder.textTime.setText(Utils.timeCalculation(current.getAgo()));

        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_for_id == Integer.parseInt(myrole_user_id)) {
                    new FollowFriendTask().execute(String.valueOf(user_to_id));
                } else {
                    new FollowFriendTask().execute(String.valueOf(user_for_id));
                }
                holder.card_following.setVisibility(View.VISIBLE);
                holder.btn_follow.setVisibility(View.GONE);
            }
        });

        holder.card_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog(holder, user_to_id, current_Username_to);
                // new UnfollowFriendTask().execute(String.valueOf(user_to_id));
//                holder.btn_follow.setVisibility(View.VISIBLE);
//                holder.card_following.setVisibility(View.GONE);
            }
        });
        holder.btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendFollowRequestTask().execute(String.valueOf(user_to_id));
            }
        });
        if (current.getImage_to() != null) {
            Glide.with(context).asBitmap().load(current.getImage_to()).centerCrop().into(new BitmapImageViewTarget(holder.postImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.postImage.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        if (current.getImages() != null && current.getImages().size() > 0) {
            if (current.getImages().size() == 1) {
                holder.img.setVisibility(View.VISIBLE);
                Glide.with(context).asBitmap().load(current.getImages().get(0).getImage_url()).centerCrop().into(new BitmapImageViewTarget(holder.img));
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        current_data.get(position).getImages().get(0).getImage_url();
                        current_data.get(position).getImage_to();
                        current_data.get(position).getImages().get(0).getId();
//                        if ( (current_data.get(position).getImages().get(0).getImage_url())!=null){
//                            gffragment.PostDetails2(String.valueOf( current_data.get(position).getImages().get(0).getId()), current_data.get(position).getImage_to(),current_data.get(position).getImages().get(0).getImage_url());
//                        }
//                        String url = current_data.get(position).get();
////                       Log.e("Sub>>", url.substring(url.length() - 4, url.length()));
//                        if (url.substring(url.length() - 4, url.length()).equalsIgnoreCase(".mp4")) {
//                            gffragment.PostDetails3(String.valueOf(post_list.get(position).getPost_id()), post_list.get(position).getOwner_image(),post_list.get(position).getThumbnail(),post_list.get(position).getUrl());
//                        } else {
//                            gffragment.PostDetails2(String.valueOf(post_list.get(position).getPost_id()), post_list.get(position).getOwner_image(),post_list.get(position).getUrl());
//                        }
//                        gffragment.PostDetails2(String.valueOf(current_data.get(position).getPost_id()),current_data.get(position).getOwner_image(),current_data.get(position).getUrl());
                       /* if (((HomeActivity) context).fragmentManager != null) {
                            if ((current_data.get(position).getImages().get(0).getImage_url()) != null) {
//                                Toast.makeText(context,  current_data.get(position).getImages().get(0).getType(), Toast.LENGTH_LONG).show();
//                                PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(0).getId()), current_data.get(position).getImage_to(), current_data.get(position).getImages().get(0).getImage_url());
                                PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(0).getId()), null, current_data.get(position).getImages().get(0).getImage_url(),null);
                                ((HomeActivity) context).fragmentManager.beginTransaction()
                                        .add(R.id.main_frame,
                                                fragment, "POSTDETAILFRAGMENT")
                                        .addToBackStack(null).commit();
                            }
                        }*/
                    }
                });
            } else {
                holder.img_layout.setVisibility(View.VISIBLE);
                if (current.getImages() != null && current.getImages().size() > 4) {
                    holder.img_layout_2.setVisibility(View.VISIBLE);
                } else if (current.getImages() != null && current.getImages().size() > 8) {
                    holder.img_layout_3.setVisibility(View.VISIBLE);
                } else if (current.getImages() != null && current.getImages().size() > 12) {
                    holder.img_layout_4.setVisibility(View.VISIBLE);
                }

                for (int i = 0; i < current.getImages().size(); i++) {
                    if (i == 0) {
                        Glide.with(context).asBitmap().load(current.getImages().get(i).getImage_url()).placeholder(R.drawable.default_avatar).centerCrop().into(new BitmapImageViewTarget(holder.img1));

                        holder.img1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                /*if (((HomeActivity) context).fragmentManager != null) {
                                    if ((current_data.get(position).getImages().get(0).getImage_url()) != null) {
//                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(0).getId()), null, current_data.get(position).getImages().get(0).getImage_url());
                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(0).getId()), null, null,null);
                                        ((HomeActivity) context).fragmentManager.beginTransaction()
                                                .add(R.id.main_frame,
                                                        fragment,
                                                        "POSTDETAILFRAGMENT")
                                                .addToBackStack(null)
                                                .commit();
                                    }*/
                                //}
                            }
                        });
                    }
                    if (i == 1) {
                        Glide.with(context).asBitmap().load(current.getImages().get(i).getImage_url()).centerCrop().into(new BitmapImageViewTarget(holder.img2));

                        holder.img2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                              /*  if (((HomeActivity) context).fragmentManager != null) {
                                    if ((current_data.get(position).getImages().get(1).getImage_url()) != null) {
//                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(0).getId()), current_data.get(position).getImage_to(), current_data.get(position).getImages().get(1).getImage_url());
                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(1).getId()), null, null,null);
                                        ((HomeActivity) context).fragmentManager.beginTransaction()
                                                .add(R.id.main_frame,
                                                        fragment,
                                                        "POSTDETAILFRAGMENT")
                                                .addToBackStack(null)
                                                .commit();
                                    }
                                }
                            }*/
                            }
                        });
                    }
                    if (i == 2) {
                        Glide.with(context).asBitmap().load(current.getImages().get(i).getImage_url()).centerCrop().into(new BitmapImageViewTarget(holder.img3));
                        holder.img3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (((MainDashboardActivity) context).fragmentManager != null) {
                                    if ((current_data.get(position).getImages().get(2).getImage_url()) != null) {
                                       // PostDetailFragment fragment;
                                        //String url = current_data.get(position).getImages().get(2).getImage_url();
                                        // Log.e("Sub>>", url.substring(url.length() - 4, url.length()));
//                                        if (url.substring(url.length() - 4, url.length()).equalsIgnoreCase(".mp4")) {
//                                            fragment = PostDetailFragment.newInstance3(String.valueOf(current_data.get(position).getImages().get(0).getId()), current_data.get(position).getImage_to(), current_data.get(position).getImages().get(2).getImage_url());
//                                            ((HomeActivity) context).fragmentManager.beginTransaction()
//                                                    .add(R.id.main_frame,
//                                                            fragment,
//                                                            "POSTDETAILFRAGMENT")
//                                                    .addToBackStack(null)
//                                                    .commit();
//                                        }else {
//                                        fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(2).getId()), null, current_data.get(position).getImages().get(2).getImage_url());
                                        /*fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(2).getId()), null, null,null);
                                        ((HomeActivity) context).fragmentManager.beginTransaction()
                                                .add(R.id.main_frame,
                                                        fragment,
                                                        "POSTDETAILFRAGMENT")
                                                .addToBackStack(null)
                                                .commit();*/
//                                        }
                                    }
                                }
                            }
                        });
                    }
                    if (i == 3) {
                        Glide.with(context).asBitmap().load(current.getImages().get(i).getImage_url()).centerCrop().into(new BitmapImageViewTarget(holder.img4));
                        holder.img4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                /*if (((HomeActivity) context).fragmentManager != null) {
                                    if ((current_data.get(position).getImages().get(3).getImage_url()) != null) {
//                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(3).getId()), null, current_data.get(position).getImages().get(3).getImage_url());
                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(3).getId()), null, null,null);
                                        ((HomeActivity) context).fragmentManager.beginTransaction()
                                                .add(R.id.main_frame, fragment, "POSTDETAILFRAGMENT")
                                                .addToBackStack(null).commit();
                                    }
                                }*/
                            }
                        });
                    }
                    if (i == 4) {
                        Glide.with(context).asBitmap().load(current.getImages().get(i).getImage_url()).centerCrop().into(new BitmapImageViewTarget(holder.img5));
                        holder.img5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               /* if (((HomeActivity) context).fragmentManager != null) {
                                    if ((current_data.get(position).getImages().get(4).getImage_url()) != null) {
//                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(4).getId()), null, current_data.get(position).getImages().get(4).getImage_url());
                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(4).getId()), null, null,null);
                                        ((HomeActivity) context).fragmentManager.beginTransaction()
                                                .add(R.id.main_frame, fragment, "POSTDETAILFRAGMENT")
                                                .addToBackStack(null).commit();
                                    }
                                }*/
                            }
                        });
                    }
                    if (i == 5) {
                        Glide.with(context).asBitmap().load(current.getImages().get(i).getImage_url()).centerCrop().into(new BitmapImageViewTarget(holder.img6));
                        holder.img6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               /* if (((HomeActivity) context).fragmentManager != null) {
                                    if ((current_data.get(position).getImages().get(5).getImage_url()) != null) {
//                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(5).getId()), null, current_data.get(position).getImages().get(5).getImage_url());
                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(5).getId()), null, null,null);
                                        ((HomeActivity) context).fragmentManager.beginTransaction()
                                                .add(R.id.main_frame, fragment, "POSTDETAILFRAGMENT")
                                                .addToBackStack(null).commit();
                                    }
                                }*/
                            }
                        });
                    }
                    if (i == 6) {
                        Glide.with(context).asBitmap().load(current.getImages().get(i).getImage_url()).centerCrop().into(new BitmapImageViewTarget(holder.img7));
                        holder.img7.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                if (((HomeActivity) context).fragmentManager != null) {
//                                    if ((current_data.get(position).getImages().get(6).getImage_url()) != null) {
////                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(6).getId()), null, current_data.get(position).getImages().get(6).getImage_url());
//                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(6).getId()), null, null,null);
//                                        ((HomeActivity) context).fragmentManager.beginTransaction()
//                                                .add(R.id.main_frame, fragment, "POSTDETAILFRAGMENT")
//                                                .addToBackStack(null).commit();
//                                    }
//                                }
                            }
                        });
                    }
                    if (i == 7) {
                        Glide.with(context).asBitmap().load(current.getImages().get(i).getImage_url()).centerCrop().into(new BitmapImageViewTarget(holder.img8));
                        /*holder.img8.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (((HomeActivity) context).fragmentManager != null) {
                                    if ((current_data.get(position).getImages().get(7).getImage_url()) != null) {
//                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(7).getId()), null, current_data.get(position).getImages().get(7).getImage_url());
                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(7).getId()), null,null,null);
                                        ((HomeActivity) context).fragmentManager.beginTransaction()
                                                .add(R.id.main_frame, fragment, "POSTDETAILFRAGMENT")
                                                .addToBackStack(null).commit();
                                    }
                                }
                            }
                        });*/
                    }
                    if (i == 8) {
                        Glide.with(context).asBitmap().load(current.getImages().get(i).getImage_url()).centerCrop().into(new BitmapImageViewTarget(holder.img9));
                        /*holder.img9.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (((HomeActivity) context).fragmentManager != null) {
                                    if ((current_data.get(position).getImages().get(8).getImage_url()) != null) {
//                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(8).getId()), null, current_data.get(position).getImages().get(8).getImage_url());
                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(8).getId()), null, null,null);
                                        ((HomeActivity) context).fragmentManager.beginTransaction()
                                                .add(R.id.main_frame, fragment, "POSTDETAILFRAGMENT")
                                                .addToBackStack(null).commit();
                                    }
                                }
                            }
                        });*/
                    }
                    if (i == 9) {
                        Glide.with(context).asBitmap().load(current.getImages().get(i).getImage_url()).centerCrop().into(new BitmapImageViewTarget(holder.img10));
                       /* holder.img10.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (((HomeActivity) context).fragmentManager != null) {
                                    if ((current_data.get(position).getImages().get(9).getImage_url()) != null) {
//                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(9).getId()), null, current_data.get(position).getImages().get(9).getImage_url());
                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(9).getId()), null, null,null);
                                        ((HomeActivity) context).fragmentManager.beginTransaction()
                                                .add(R.id.main_frame, fragment, "POSTDETAILFRAGMENT")
                                                .addToBackStack(null).commit();
                                    }
                                }
                            }
                        });*/
                    }
                    if (i == 10) {
                        Glide.with(context).asBitmap().load(current.getImages().get(i).getImage_url()).centerCrop().into(new BitmapImageViewTarget(holder.img11));
                        /*holder.img11.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (((HomeActivity) context).fragmentManager != null) {
                                    if ((current_data.get(position).getImages().get(10).getImage_url()) != null) {
//                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(10).getId()), null, current_data.get(position).getImages().get(10).getImage_url());
                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(10).getId()), null, null,null);
                                        ((HomeActivity) context).fragmentManager.beginTransaction()
                                                .add(R.id.main_frame, fragment, "POSTDETAILFRAGMENT")
                                                .addToBackStack(null).commit();
                                    }
                                }
                            }
                        });*/
                    }
                    if (i == 11) {
                        Glide.with(context).asBitmap().load(current.getImages().get(i).getImage_url()).centerCrop().into(new BitmapImageViewTarget(holder.img12));
                        /*holder.img12.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (((HomeActivity) context).fragmentManager != null) {
                                    if ((current_data.get(position).getImages().get(11).getImage_url()) != null) {
//                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(11).getId()), null, current_data.get(position).getImages().get(11).getImage_url());
                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(11).getId()), null, null,null);
                                        ((HomeActivity) context).fragmentManager.beginTransaction()
                                                .add(R.id.main_frame, fragment, "POSTDETAILFRAGMENT")
                                                .addToBackStack(null).commit();
                                    }
                                }
                            }
                        });*/
                    }
                    if (i == 12) {
                        Glide.with(context).asBitmap().load(current.getImages().get(i).getImage_url()).centerCrop().into(new BitmapImageViewTarget(holder.img13));
                        /*holder.img13.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (((HomeActivity) context).fragmentManager != null) {
                                    if ((current_data.get(position).getImages().get(12).getImage_url()) != null) {
//                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(12).getId()), null, current_data.get(position).getImages().get(12).getImage_url());
                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(12).getId()), null, null,null);
                                        ((HomeActivity) context).fragmentManager.beginTransaction()
                                                .add(R.id.main_frame, fragment, "POSTDETAILFRAGMENT")
                                                .addToBackStack(null).commit();
                                    }
                                }
                            }
                        });*/
                    }
                    if (i == 13) {
                        Glide.with(context).asBitmap().load(current.getImages().get(i).getImage_url()).centerCrop().into(new BitmapImageViewTarget(holder.img14));
                        /*holder.img14.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (((HomeActivity) context).fragmentManager != null) {
                                    if ((current_data.get(position).getImages().get(13).getImage_url()) != null) {
//                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(13).getId()), null, current_data.get(position).getImages().get(13).getImage_url());
                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(13).getId()), null, null,null);
                                        ((HomeActivity) context).fragmentManager.beginTransaction()
                                                .add(R.id.main_frame, fragment, "POSTDETAILFRAGMENT")
                                                .addToBackStack(null).commit();
                                    }
                                }
                            }
                        });*/
                    }
                    if (i == 14) {
                        Glide.with(context).asBitmap().load(current.getImages().get(i).getImage_url()).centerCrop().into(new BitmapImageViewTarget(holder.img15));
//                        holder.img15.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (((HomeActivity) context).fragmentManager != null) {
//                                    if ((current_data.get(position).getImages().get(14).getImage_url()) != null) {
////                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(14).getId()), null, current_data.get(position).getImages().get(14).getImage_url());
//                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(14).getId()), null, null,null);
//                                        ((HomeActivity) context).fragmentManager.beginTransaction()
//                                                .add(R.id.main_frame, fragment, "POSTDETAILFRAGMENT")
//                                                .addToBackStack(null).commit();
//                                    }
//                                }
//                            }
//                        });
                    }
                    if (i == 15) {
                        Glide.with(context).asBitmap().load(current.getImages().get(i).getImage_url()).centerCrop().into(new BitmapImageViewTarget(holder.img16));
                        /*holder.img16.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (((HomeActivity) context).fragmentManager != null) {
                                    if ((current_data.get(position).getImages().get(15).getImage_url()) != null) {
//                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(15).getId()), null, current_data.get(position).getImages().get(15).getImage_url());
                                        PostDetailFragment fragment = PostDetailFragment.newInstance2(String.valueOf(current_data.get(position).getImages().get(15).getId()), null, null,null);
                                        ((HomeActivity) context).fragmentManager.beginTransaction()
                                                .add(R.id.main_frame, fragment, "POSTDETAILFRAGMENT")
                                                .addToBackStack(null).commit();
                                    }
                                }
                            }
                        });*/
                    }
                }
            }
        } else {
            if (activity.equals("category")) {
                //  Glide.with(context).load(R.drawable.ic_right_arrow).into(holder.img);
                holder.img.setImageResource(R.drawable.ic_right_arrow);
                holder.img.setPadding(30, 30, 20, 30);
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        send_borat_cast();
                    }
                });
            }
        }
//        holder.txt_message.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (((HomeActivity) context).fragmentManager != null) {
//                    String user_id_2 = String.valueOf(current.getUser_to());
//
//
//                    //that is use to change the coler off the bootam bar in myrole
//                    bottom_bar.selectTab(Config.PROFILE);
//                    //end
//
//
//                    //Toast.makeText(context, user_id_2, Toast.LENGTH_LONG).show();
//                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(current.getUser_to()));
//                    ((HomeActivity) context).fragmentManager.beginTransaction()
//                            .replace(R.id.main_frame,
//                                    fragment,
//                                    "OTHERPROFILEFRAGMENT")
//                            .addToBackStack("m")
//                            .commit();
//                }
//            }
//        });

        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((MainDashboardActivity) context).fragmentManager != null) {
                   // String user_id_2 = String.valueOf(current.getUser_to());

                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(current.getUser_to()));
                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment, "OTHERPROFILEFRAGMENT")
                            .addToBackStack("m")
                            .commit();
                }
            }
        });
    }

    public void myrole_intent() {
        Intent intent = new Intent(mactivity, MyRoleDeatil.class);
        intent.setAction("com.myrole.updateNewEntry");
        intent.putExtra("fromAddComment", "");
        mactivity.startActivity(intent);
//        intent.putExtra("position", mactivity.getStringExtra("fromAddComment").getStringExtra("position"));
//        intent.putExtra("num_comm", getIntent().getStringExtra("num_comm"));
    }


    public void send_borat_cast() {
//        if (((HomeActivity) context).fragmentManager != null) {
//            ((HomeActivity) context).fragmentManager.beginTransaction()
//                    .replace(R.id.main_frame, HomeFragment.newInstance(), "HOMEFRAGMENT")
//                    .addToBackStack("m")
//                    .commit();
//        }
//
//        AppPreferences.getAppPreferences(context).putStringValue(AppPreferences.NOTIFICATION_CATEGORY, "1");
        Intent intent = new Intent(mactivity, MainDashboardActivity.class);
        //intent.setAction("com.myrole.gotophootshoot");
        intent.putExtra("com.myrole.gotophootshoot", "yo");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("position", activity.getIntent().getStringExtra("position"));
//        intent.putExtra("num_comm", activity.getIntent().getStringExtra("num_comm"));
        mactivity.startActivity(intent);
        // activity.sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {

        return current_data.size();

    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class viewHolder extends RecyclerView.ViewHolder {
        ImageView postImage, img, img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, img12, img13, img14, img15, img16;
        TextView txt_message, textTime;
        LinearLayout img_layout, img_layout_2, img_layout_3, img_layout_4;
        TextView btn_follow, btn_request;
        CardView card_following;

        public viewHolder(View itemView) {
            super(itemView);

            postImage = (ImageView) itemView.findViewById(R.id.img_user_icon);
            img = (ImageView) itemView.findViewById(R.id.img);
            img1 = (ImageView) itemView.findViewById(R.id.img1);
            img2 = (ImageView) itemView.findViewById(R.id.img2);
            img3 = (ImageView) itemView.findViewById(R.id.img3);
            img4 = (ImageView) itemView.findViewById(R.id.img4);
            img5 = (ImageView) itemView.findViewById(R.id.img5);
            img6 = (ImageView) itemView.findViewById(R.id.img6);
            img7 = (ImageView) itemView.findViewById(R.id.img7);
            img8 = (ImageView) itemView.findViewById(R.id.img8);
            img9 = (ImageView) itemView.findViewById(R.id.img9);
            img10 = (ImageView) itemView.findViewById(R.id.img10);
            img11 = (ImageView) itemView.findViewById(R.id.img11);
            img12 = (ImageView) itemView.findViewById(R.id.img12);
            img13 = (ImageView) itemView.findViewById(R.id.img13);
            img14 = (ImageView) itemView.findViewById(R.id.img14);
            img15 = (ImageView) itemView.findViewById(R.id.img15);
            img16 = (ImageView) itemView.findViewById(R.id.img16);

            txt_message = (TextView) itemView.findViewById(R.id.txt_message);
            btn_follow = (TextView) itemView.findViewById(R.id.btn_follow);
            btn_request = (TextView) itemView.findViewById(R.id.btn_request);
            card_following = (CardView) itemView.findViewById(R.id.card_following);
//          textTime = (TextView) itemView.findViewById(R.id.textTime);
            img_layout = (LinearLayout) itemView.findViewById(R.id.img_layout_1);
            img_layout_2 = (LinearLayout) itemView.findViewById(R.id.img_layout_2);
            img_layout_3 = (LinearLayout) itemView.findViewById(R.id.img_layout_3);
            img_layout_4 = (LinearLayout) itemView.findViewById(R.id.img_layout_4);
            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_message), Config.QUICKSAND, Config.REGULAR);
//          Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.textTime), Config.QUICKSAND, Config.REGULAR);

        }
    }

    public static Spannable removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new Utils.URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);
        }
        return p_Text;
    }

    public void AlertDialog(final viewHolder holder, final int pos, String name) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // Setting Dialog Title
        alertDialog.setTitle("MY Role");
        // Setting Dialog Message
        alertDialog.setMessage("Unfollow " + name + "?");
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new UnfollowFriendTask().execute(String.valueOf(pos));
                holder.btn_follow.setVisibility(View.VISIBLE);
                holder.card_following.setVisibility(View.GONE);

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

    class UnfollowFriendTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;
//        int pos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
//            pos = Integer.parseInt(params[0]);
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", params[0]);
            postDataParams.put("follower_id", AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID));
            return HTTPUrlConnection.getInstance().load(context, Config.UNFOLLOW_USER, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                } else {
                    //  Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class FollowFriendTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;
//        int pos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
//            pos = Integer.parseInt(params[0]);
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", params[0]);
            postDataParams.put("follower_id", AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID));
            return HTTPUrlConnection.getInstance().load(context, Config.FOLLOW_USER, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                } else {
                    //    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class SendFollowRequestTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
//          pos = Integer.parseInt(params[0]);
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", params[0]);
            postDataParams.put("follower_id", AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID));
            return HTTPUrlConnection.getInstance().load(context, Config.SEND_FOLLOW_REQUEST, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                } else {
                    //  Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}