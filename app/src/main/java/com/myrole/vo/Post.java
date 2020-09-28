package com.myrole.vo;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.volokh.danylo.visibility_utils.items.ListItem;

/**
 * Created by vikesh.kumar on 6/23/2016.
 */
public class Post implements ListItem {
    public String id = "";
    public String username = "";
    public String ownerId = "";
    public String postImage = "";
    public String owner_image = "";
    public String postTime = "";
    public String categoryName = "";
    public String description = "";
    public String numLoves = "0";
    public String islike = "0";
    public String comment_count = "0";
    public String numComment = "0";
    public String url = "";
    public String type = "POST";

    private final Rect mCurrentViewRect = new Rect();


    public Post() {
        super();
    }




    @Override
    public int getVisibilityPercents(View currentView) {
        //if(SHOW_LOGS) Logger.v(TAG, ">> getVisibilityPercents currentView " + currentView);

        int percents = 100;

        currentView.getLocalVisibleRect(mCurrentViewRect);
        //if(SHOW_LOGS) Logger.v(TAG, "getVisibilityPercents mCurrentViewRect top " + mCurrentViewRect.top + ", left " + mCurrentViewRect.left + ", bottom " + mCurrentViewRect.bottom + ", right " + mCurrentViewRect.right);

        int height = currentView.getHeight();
        //if(SHOW_LOGS) Logger.v(TAG, "getVisibilityPercents height " + height);

        if(viewIsPartiallyHiddenTop()){
            // view is partially hidden behind the top edge
            percents = (height - mCurrentViewRect.top) * 100 / height;
        } else if(viewIsPartiallyHiddenBottom(height)){
            percents = mCurrentViewRect.bottom * 100 / height;
        }

        setVisibilityPercentsText(currentView, percents);
        //if(SHOW_LOGS) Logger.v(TAG, "<< getVisibilityPercents, percents " + percents);

        return percents;
    }

    @Override
    public void setActive(View newActiveView, int newActiveViewPosition) {

    }

    @Override
    public void deactivate(View currentView, int position) {

    }

    private void setVisibilityPercentsText(View currentView, int percents) {
        //if(SHOW_LOGS) Logger.v(TAG, "setVisibilityPercentsText percents " + percents);
        RecyclerView.ViewHolder videoViewHolder = (RecyclerView.ViewHolder) currentView.getTag();
        String percentsText = "Visibility percents: " + percents;

        //((GeneralPostAdapter.PostViewHolder) videoViewHolder).num_loves.setText(percents+"");
        //((GeneralPostAdapter.PostViewHolder) videoViewHolder).name.setText(percents+"");
       // ((GeneralPostAdapter.PostViewHolder) videoViewHolder).visibilityPercent = percents;
       /* if(videoViewHolder instanceof GeneralPostAdapter.PostViewHolder) {
            if(percents > 70) {
            VideoPlayer vv = (VideoPlayer) ((RelativeLayout)((GeneralPostAdapter.PostViewHolder) videoViewHolder).postVideo).findViewById(R.id.video_player_view);
            if(vv != null && vv.getVisibility() == View.VISIBLE && !vv.isPlaying()) {
                vv.start();
                Log.v("play>>>>>>>>>>>>",((GeneralPostAdapter.PostViewHolder) videoViewHolder).description.getText().toString());
            }
            }else{
                VideoPlayer vv = (VideoPlayer) ((RelativeLayout)((GeneralPostAdapter.PostViewHolder) videoViewHolder).postVideo).findViewById(R.id.video_player_view);
                if(vv != null && vv.getVisibility() == View.VISIBLE && vv.isPlaying()) {
                    vv.pause();
                    Log.v("stop>>>>>>>>>",((GeneralPostAdapter.PostViewHolder) videoViewHolder).description.getText().toString());
                }
            }
        }*/
        //videoViewHolder.mVisibilityPercents.setText(percentsText);
    }

    private boolean viewIsPartiallyHiddenBottom(int height) {
        return mCurrentViewRect.bottom > 0 && mCurrentViewRect.bottom < height;
    }

    private boolean viewIsPartiallyHiddenTop() {
        return mCurrentViewRect.top > 0;
    }
}
