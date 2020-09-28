package com.myrole.quickaction;

import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.PopupWindow.OnDismissListener;

import com.myrole.R;
import com.myrole.holders.PostListDTO;

import java.util.ArrayList;
import java.util.List;


/**
 * QuickAction dialog.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 * 
 * Contributors:
 * - Kevin Peck <kevinwpeck@gmail.com>
 */
public class QuickAction extends PopupWindows implements OnDismissListener,OnClickListener {
	private Animation mTrackAnim;
	private LayoutInflater inflater;
	private OnActionItemClickListener mItemClickListener;
	private OnDismissListener mDismissListener;
	OnClickListener listener;
	private List<ActionItem> mActionItemList = new ArrayList<ActionItem>();
	
	private boolean mDidAction;
	private boolean mAnimateTrack;
	
	private int mChildPos;    
    private int mAnimStyle;
    
	public static final int ANIM_GROW_FROM_LEFT = 1;
	public static final int ANIM_GROW_FROM_RIGHT = 2;
	public static final int ANIM_GROW_FROM_CENTER = 3;
	public static final int ANIM_AUTO = 4;
	public PostListDTO postListDTO;
	Context context;
	/**
	 * Constructor.
	 *
	 * @param context Context
	 * @param tag
	 */
	public QuickAction(Context context, PostListDTO postListDTO, OnClickListener listener) {
		super(context);
		this.postListDTO=postListDTO;
		this.context=context;
		this.listener=listener;
		inflater 	= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mTrackAnim 	= AnimationUtils.loadAnimation(context, R.anim.slide_out_to_left);
		
		mTrackAnim.setInterpolator(new Interpolator() {
			public float getInterpolation(float t) {
	              // Pushes past the target area, then snaps back into place.
	                // Equation for graphing: 1.2-((x*1.6)-1.1)^2
				final float inner = (t * 1.55f) - 1.1f;
				
	            return 1.2f - inner * inner;
	        }
		});
	        
		setRootViewId(R.layout.class_edit_option);
		
		mAnimStyle		= ANIM_AUTO;
		mAnimateTrack	= true;
		mChildPos		= 0;
	}
	
	/**
     * Get action item at an index
     * 
     * @param index  Index of item (position from callback)
     * 
     * @return  Action Item at the position
     */
    public ActionItem getActionItem(int index) {
        return mActionItemList.get(index);
    }
    
	/**
	 * Set root view.
	 * 
	 * @param id Layout resource id
	 */
	public void setRootViewId(int id) {
		mRootView	= (ViewGroup) inflater.inflate(id, null);
		mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mRootView.findViewById(R.id.btn_report).setOnClickListener(listener);
        mRootView.findViewById(R.id.btn_share).setOnClickListener(listener);
        mRootView.findViewById(R.id.btn_delete).setOnClickListener(listener);
		setContentView(mRootView);
	}
	
	/**
	 * Animate track.
	 * 
	 * @param mAnimateTrack flag to animate track
	 */
	public void mAnimateTrack(boolean mAnimateTrack) {
		this.mAnimateTrack = mAnimateTrack;
	}
	
	/**
	 * Set animation style.
	 * 
	 * @param mAnimStyle animation style, default is set to ANIM_AUTO
	 */
	public void setAnimStyle(int mAnimStyle) {
		this.mAnimStyle = mAnimStyle;
	}

	/**
	 * Add action item
	 * 
	 * @param action  {@link ActionItem}
	 */
	public void addActionItem(ActionItem action) {
		
	}
	
	public void setOnActionItemClickListener(OnActionItemClickListener listener) {
		mItemClickListener = listener;
	}
	
	/**
	 * Show popup mWindow
	 */
	public void show (View anchor) {
		preShow();

		int[] location 		= new int[2];
		
		mDidAction 			= false;
		
		anchor.getLocationOnScreen(location);

		Rect anchorRect 	= new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] 
		                	+ anchor.getHeight());

		//mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		int rootWidth 		= mRootView.getMeasuredWidth();
		int rootHeight 		= mRootView.getMeasuredHeight();

		int screenWidth 	= mWindowManager.getDefaultDisplay().getWidth();
		//int screenHeight 	= mWindowManager.getDefaultDisplay().getHeight();
		
		int xPos 			= (int)screenWidth-rootWidth-20;

		int yPos	 		= anchorRect.top - rootHeight;

		boolean onTop		= true;
		
		// display on bottom
		if (rootHeight > anchor.getTop()) {
			yPos 	= anchorRect.bottom;
			onTop	= false;
		}

		
		setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
	
		mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
		
		if (mAnimateTrack) mRootView.startAnimation(mTrackAnim);
	}

	/**
	 * Set animation style
	 * 
	 * @param screenWidth Screen width
	 * @param requestedX distance from left screen
	 * @param onTop flag to indicate where the popup should be displayed. Set TRUE if displayed on top of anchor and vice versa
	 */
	private void setAnimationStyle(int screenWidth, int requestedX, boolean onTop) {
		switch (mAnimStyle) {
		/*case ANIM_GROW_FROM_LEFT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
			break;
					
		case ANIM_GROW_FROM_RIGHT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
			break;
					
		case ANIM_GROW_FROM_CENTER:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
		break;*/
		}
	}
	
	
	
	/**
	 * Set listener for window dismissed. This listener will only be fired if the quicakction dialog is dismissed
	 * by clicking outside the dialog or clicking on sticky item.
	 */
	public void setOnDismissListener(OnDismissListener listener) {
		setOnDismissListener(this);
		
		mDismissListener = listener;
	}
	
	@Override
	public void onDismiss() {
		if (!mDidAction && mDismissListener != null) {
			mDismissListener.onDismiss();
		}
	}
	
	/**
	 * Listener for item click
	 *
	 */
	public interface OnActionItemClickListener {
		void onItemClick(QuickAction source, int pos, int actionId);
	}
	
	/**
	 * Listener for window dismiss
	 * 
	 */
	public interface OnDismissListener {
		void onDismiss();
	}

	
	
	@Override
	public void onClick(View v) {

}
	
	
	
	
	
	
	
	
	
}