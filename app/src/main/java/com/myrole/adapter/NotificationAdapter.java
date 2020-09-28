package com.myrole.adapter;//package com.myrole.adapter;
//
///**
// * Created by welcome on 03-01-2017.
// */
//
//
//import android.content.Context;
//
//import android.graphics.Bitmap;
//import android.graphics.Typeface;
//import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
//import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
//import android.support.v7.widget.RecyclerView;
//import android.text.Html;
//import android.text.Spannable;
//import android.text.method.LinkMovementMethod;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.BitmapImageViewTarget;
//import com.myrole.HomeActivity;
//import com.myrole.R;
//import com.myrole.data.CommentsDTO;
//import com.myrole.data.ContactDemoDTO;
//import com.myrole.data.FollowingDTO;
//import com.myrole.data.User;
//import com.myrole.fragments.OtherProfileFragment;
//import com.myrole.fragments.PostDetailFragment;
//import com.myrole.utils.Config;
//import com.myrole.utils.Utils;
//import com.myrole.vo.Contact;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import static com.myrole.adapter.ActivityFollowingAdapter.removeUnderlines;
//
////
////
//
//public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    private Context context;
//    private List<CommentsDTO> articlesDTOList;
////    public NotificationAdapter(List<CommentsDTO> articlesDTOList, Context context) {
////        this.articlesDTOList = articlesDTOList;
////        this.context = context;
////
////    }
//
//    // The items to display in your RecyclerView
//    private List<Object> items;
//
//    private final int CONTACT = 0, FOLLWEING = 1;
//
//    // Provide a suitable constructor (depends on the kind of dataset)
//    public NotificationAdapter(List<Object> items,Context context) {
//        this.items = items;
//        this.context = context;
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (items.get(position) instanceof ContactDemoDTO) {
//            return CONTACT;
//        } else if (items.get(position) instanceof FollowingDTO) {
//            return FOLLWEING;
//        }
//        return -1;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        RecyclerView.ViewHolder viewHolder;
//        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
//
//        switch (viewType) {
//            case CONTACT:
//                View v1 = inflater.inflate(R.layout.layout_request_item, viewGroup, false);
//                viewHolder = new ViewHolder1(v1);
//                break;
//            case FOLLWEING:
//                View v2 = inflater.inflate(R.layout.following_view, viewGroup, false);
//                viewHolder = new ViewHolder2(v2);
//                break;
//            default:
//                View v = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
//                viewHolder = new RecyclerViewSimpleTextViewHolder(v);
//                break;
//        }
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
//        switch (viewHolder.getItemViewType()) {
//            case CONTACT:
//                ViewHolder1 vh1 = (ViewHolder1) viewHolder;
//                configureViewHolder1(vh1, position);
//                break;
//            case FOLLWEING:
//                ViewHolder2 vh2 = (ViewHolder2) viewHolder;
//                configureViewHolder2(vh2, position);
//                break;
//            default:
//                RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) viewHolder;
//                configureDefaultViewHolder(vh, position);
//                break;
//        }
//    }
//    private void configureDefaultViewHolder(RecyclerViewSimpleTextViewHolder vh, int position) {
//        vh.getLabel().setText((CharSequence) items.get(position));
//    }
//
//    private void configureViewHolder1(ViewHolder1 vh1, int position) {
//      //  User user = (User) items.get(position);
//        final ContactDemoDTO contact = (ContactDemoDTO)items.get(position);
//        if (contact != null) {
////            vh1.getLabel1().setText("Name: " + user.name);
////            vh1.getLabel2().setText("Hometown: " + user.hometown);
//
//
//            vh1.getName().setText(contact.name);
//
//            vh1.getBtnAccept().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    status = "1";
////                    new RequestAdapter.FollowRequestActionTask().execute(position + "");
////                    // removeAt(position);
////                    po=position;
//                }
//            });
//
//            vh1.getBtnDecline().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    status = "0";
////                    new RequestAdapter.FollowRequestActionTask().execute(position + "");
//////                removeAt(position);
////                    po=position;
//                }
//            });
//
//            Glide.with(context).load(contact.image).asBitmap()
//                    .placeholder(R.drawable.user_mobile).centerCrop().into(vh1.icon);
//            if (position == 0)
//                vh1.line.setVisibility(View.INVISIBLE);
//            else
//                vh1.line.setVisibility(View.VISIBLE);
//
//        }
//    }
//
//    private void configureViewHolder2(final ViewHolder2 vh2, int position) {
//       // vh2.getImageView().setImageResource(R.drawable.sunset);
//        final FollowingDTO current = (FollowingDTO) items.get(position);
//
//        if (current.getMsg() != null && !current.getMsg().equals("")) {
//            String time = Utils.timeCalculation(current.getAgo());
//            time = "<font COLOR=\'#D2D0D2\'><b>" + time + "</b></font>";
//            String txtAppend = current.getMsg();
//            txtAppend = "<b>" + txtAppend + "</b>" + " " + time;
//            Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
//                    Html.fromHtml(txtAppend));
//
//            Spannable processedText = removeUnderlines(spannedText);
//
//            if (vh2.txt_message != null) {
//                vh2.txt_message.setText(processedText);
//
//            }
//        }
//
//        vh2.txt_message.setMovementMethod(LinkMovementMethod.getInstance());
//
//
////        String time = Utils.timeCalculation(current.getAgo());
////        holder.textTime.setText(time);
////      holder.txt_message.setText(Html.fromHtml(current_data.get(position).getMsg()));
//
////      holder.textTime.setText(Utils.timeCalculation(current.getAgo()));
//
//        if (current.getImage_to() != null) {
//            Glide.with(context).load(current.getImage_to()).asBitmap().centerCrop().into(new BitmapImageViewTarget(vh2.postImage) {
//                @Override
//                protected void setResource(Bitmap resource) {
//                    RoundedBitmapDrawable circularBitmapDrawable =
//                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
//                    circularBitmapDrawable.setCircular(true);
//                    vh2.postImage.setImageDrawable(circularBitmapDrawable);
//                }
//            });
//        }
//        vh2.img.setVisibility(View.VISIBLE);
//        Glide.with(context).load(current.getImage_url()).asBitmap().centerCrop().into(new BitmapImageViewTarget(vh2.img));
//        if (current.getImages() != null && current.getImages().size() > 0) {
//            if (current.getImages().size() == 1) {
//                vh2.img.setVisibility(View.VISIBLE);
//                Glide.with(context).load(current.getImage_url()).asBitmap().centerCrop().into(new BitmapImageViewTarget(vh2.img));
//
//                vh2.img.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (((HomeActivity) context).fragmentManager != null) {
//                            PostDetailFragment fragment = PostDetailFragment.newInstance(String.valueOf(current.getImages().get(0).getId()));
//                            ((HomeActivity) context).fragmentManager.beginTransaction()
//                                    .add(R.id.main_frame,
//                                            fragment,
//                                            "POSTDETAILFRAGMENT")
//                                    .addToBackStack(null)
//                                    .commit();
//                        }
//                    }
//                });
//
//            } else {
//                vh2.img_layout.setVisibility(View.VISIBLE);
////                for (int i = 0; i < current.getImages().size(); i++) {
////                    if (i == 0) {
////                        Glide.with(context).load(current.getImages().get(i).getImage_url()).asBitmap().placeholder(R.drawable.default_avatar).centerCrop().into(new BitmapImageViewTarget(vh2.img1));
////
////                        vh2.img1.setOnClickListener(new View.OnClickListener() {
////                            @Override
////                            public void onClick(View v) {
////                                if (((HomeActivity) context).fragmentManager != null) {
////                                    PostDetailFragment fragment = PostDetailFragment.newInstance(String.valueOf(current.getImages().get(0).getId()));
////                                    ((HomeActivity) context).fragmentManager.beginTransaction()
////                                            .add(R.id.main_frame,
////                                                    fragment,
////                                                    "POSTDETAILFRAGMENT")
////                                            .addToBackStack(null)
////                                            .commit();
////
////                                }
////                            }
////                        });
////                    }
//////                    if (i == 1) {
//////                        Glide.with(context).load(current.getImages().get(i).getImage_url()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.img2));
//////
//////                        vh2.img2.setOnClickListener(new View.OnClickListener() {
//////                            @Override
//////                            public void onClick(View v) {
//////                                if (((HomeActivity) context).fragmentManager != null) {
//////                                    PostDetailFragment fragment = PostDetailFragment.newInstance(String.valueOf(current.getImages().get(1).getId()));
//////                                    ((HomeActivity) context).fragmentManager.beginTransaction()
//////                                            .add(R.id.main_frame,
//////                                                    fragment,
//////                                                    "POSTDETAILFRAGMENT")
//////                                            .addToBackStack(null)
//////                                            .commit();
//////
//////                                }
//////                            }
//////                        });
//////                    }
//////                    if (i == 2) {
//////                        Glide.with(context).load(current.getImages().get(i).getImage_url()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.img3));
//////                        vh2.img3.setOnClickListener(new View.OnClickListener() {
//////                            @Override
//////                            public void onClick(View v) {
//////                                if (((HomeActivity) context).fragmentManager != null) {
//////                                    PostDetailFragment fragment = PostDetailFragment.newInstance(String.valueOf(current.getImages().get(2).getId()));
//////                                    ((HomeActivity) context).fragmentManager.beginTransaction()
//////                                            .add(R.id.main_frame,
//////                                                    fragment,
//////                                                    "POSTDETAILFRAGMENT")
//////                                            .addToBackStack(null)
//////                                            .commit();
//////
//////
//////                                }
//////                            }
//////                        });
//////                    }
//////                    if (i == 3) {
////////                    Glide.with(context).load(current_data.get(position).getImages().get(i).getImage_url()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.img4) {
////////                        @Override
////////                        protected void setResource(Bitmap resource) {
////////                            RoundedBitmapDrawable circularBitmapDrawable =
////////                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
////////                            circularBitmapDrawable.setCircular(true);
////////                            holder.img4.setImageDrawable(circularBitmapDrawable);
////////                        }
////////                    });
//////                        Glide.with(context).load(current.getImages().get(i).getImage_url()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.img4));
//////                        vh2.img4.setOnClickListener(new View.OnClickListener() {
//////                            @Override
//////                            public void onClick(View v) {
//////                                if (((HomeActivity) context).fragmentManager != null) {
//////                                    PostDetailFragment fragment = PostDetailFragment.newInstance(String.valueOf(current.getImages().get(3).getId()));
//////                                    ((HomeActivity) context).fragmentManager.beginTransaction()
//////                                            .add(R.id.main_frame,
//////                                                    fragment,
//////                                                    "POSTDETAILFRAGMENT")
//////                                            .addToBackStack(null)
//////                                            .commit();
//////
//////                                }
//////                            }
//////                        });
//////                    }
////
////                }
//            }
//        }
//
//        vh2.postImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (((HomeActivity) context).fragmentManager != null) {
//                    String user_id_2 = String.valueOf(current.getUser_to());
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
//
//    }
//
//    public class RecyclerViewSimpleTextViewHolder  extends RecyclerView.ViewHolder {
//        private TextView label;
//        public RecyclerViewSimpleTextViewHolder(View v){
//            super(v);
//            label = (TextView) v.findViewById(R.id.text1);
//        }
//        public TextView getLabel() {
//            return label;
//        }
//
//        public void setLabel(TextView label1) {
//            this.label = label1;
//        }
//    }
//    public class ViewHolder1 extends RecyclerView.ViewHolder {
//
//        private TextView label1, label2;
//        TextView name, number, btnAccept, btnDecline;
//        ImageView icon;
//        View line;
//        public ViewHolder1(View itemView) {
//            super(itemView);
////            label1 = (TextView) v.findViewById(R.id.text1);
////            label2 = (TextView) v.findViewById(R.id.text2);
//
//            Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf");
//
//            // ((TextView) view.findViewById(R.id.txt_update_status)).setTypeface(face);
//            name = (TextView) itemView.findViewById(R.id.txt_name);
//            number = (TextView) itemView.findViewById(R.id.txt_number);
//            btnAccept = (TextView) itemView.findViewById(R.id.btn_accept);
//            btnDecline = (TextView) itemView.findViewById(R.id.btn_decline);
//            icon = (ImageView) itemView.findViewById(R.id.user_icon);
//            line = (View) itemView.findViewById(R.id.top_line);
//            btnAccept.setTypeface(face);
//            btnDecline.setTypeface(face);
//            Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_name), Config.NEXA, Config.BOLD);
//
//        }
//
//        public TextView getName() {
//            return name;
//        }
//
//        public void setName(TextView name) {
//            this.name = name;
//        }
//
//        public TextView getNumber() {
//            return number;
//        }
//
//        public void setNumber(TextView number) {
//            this.number = number;
//        }
//
//        public TextView getBtnAccept() {
//            return btnAccept;
//        }
//
//        public void setBtnAccept(TextView btnAccept) {
//            this.btnAccept = btnAccept;
//        }
//
//        public TextView getBtnDecline() {
//            return btnDecline;
//        }
//
//        public void setBtnDecline(TextView btnDecline) {
//            this.btnDecline = btnDecline;
//        }
//
//        public ImageView getIcon() {
//            return icon;
//        }
//
//        public void setIcon(ImageView icon) {
//            this.icon = icon;
//        }
//
//        public View getLine() {
//            return line;
//        }
//
//        public void setLine(View line) {
//            this.line = line;
//        }
//
////        public TextView getLabel1() {
////            return label1;
////        }
////
////        public void setLabel1(TextView label1) {
////            this.label1 = label1;
////        }
////
////        public TextView getLabel2() {
////            return label2;
////        }
////
////        public void setLabel2(TextView label2) {
////            this.label2 = label2;
////        }
//    }
//
//
//    public class ViewHolder2 extends RecyclerView.ViewHolder {
//
//        ImageView postImage, img1, img2, img3, img4, img;
//        TextView txt_message, textTime;
//        LinearLayout img_layout;
//
//        public ViewHolder2(View itemView) {
//            super(itemView);
//            postImage = (ImageView) itemView.findViewById(R.id.img_user_icon);
//            img = (ImageView) itemView.findViewById(R.id.img);
//            img1 = (ImageView) itemView.findViewById(R.id.img1);
//            img2 = (ImageView) itemView.findViewById(R.id.img2);
//            img3 = (ImageView) itemView.findViewById(R.id.img3);
//            img4 = (ImageView) itemView.findViewById(R.id.img4);
//            txt_message = (TextView) itemView.findViewById(R.id.txt_message);
////          textTime = (TextView) itemView.findViewById(R.id.textTime);
//            img_layout = (LinearLayout) itemView.findViewById(R.id.img_layout);
//           // Utils.setTypeface(context, (TextView) itemView.findViewById(R.id.txt_message), Config.QUICKSAND, Config.REGULAR);
//        }
//
//        public ImageView getPostImage() {
//            return postImage;
//        }
//
//        public void setPostImage(ImageView postImage) {
//            this.postImage = postImage;
//        }
//
//        public ImageView getImg1() {
//            return img1;
//        }
//
//        public void setImg1(ImageView img1) {
//            this.img1 = img1;
//        }
//
//        public ImageView getImg2() {
//            return img2;
//        }
//
//        public void setImg2(ImageView img2) {
//            this.img2 = img2;
//        }
//
//        public ImageView getImg3() {
//            return img3;
//        }
//
//        public void setImg3(ImageView img3) {
//            this.img3 = img3;
//        }
//
//        public ImageView getImg4() {
//            return img4;
//        }
//
//        public void setImg4(ImageView img4) {
//            this.img4 = img4;
//        }
//
//        public ImageView getImg() {
//            return img;
//        }
//
//        public void setImg(ImageView img) {
//            this.img = img;
//        }
//
//        public TextView getTxt_message() {
//            return txt_message;
//        }
//
//        public void setTxt_message(TextView txt_message) {
//            this.txt_message = txt_message;
//        }
//
//        public TextView getTextTime() {
//            return textTime;
//        }
//
//        public void setTextTime(TextView textTime) {
//            this.textTime = textTime;
//        }
//
//        public LinearLayout getImg_layout() {
//            return img_layout;
//        }
//
//        public void setImg_layout(LinearLayout img_layout) {
//            this.img_layout = img_layout;
//        }
//    }
//
//}