package com.myrole.loadmore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.myrole.R;


/**
 * @author bian.xd
 */
public class PullToLoadView extends FrameLayout {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private PullCallback mPullCallback;
    private RecyclerViewPositionHelper mRecyclerViewHelper;
    protected ScrollDirection mCurScrollingDirection;
    protected int mPrevFirstVisibleItem = 0;
    private int mLoadMoreOffset = 1;
    private boolean mIsLoadMoreEnabled = false;

    public PullToLoadView(Context context) {
        this(context, null);
    }

    public PullToLoadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.loadview, this, true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        init();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(mRecyclerView);
    }

    private void init() {

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mCurScrollingDirection = null;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mCurScrollingDirection == null) { //User has just started a scrolling motion
                    mCurScrollingDirection = ScrollDirection.SAME;
                    mPrevFirstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();

                } else {
                    final int firstVisibleItem = mRecyclerViewHelper.findLastVisibleItemPosition();
                    if (firstVisibleItem > mPrevFirstVisibleItem) {
                        //User is scrolling up
                        mCurScrollingDirection = ScrollDirection.UP;
                    } else if (firstVisibleItem < mPrevFirstVisibleItem) {
                        //User is scrolling down
                        mCurScrollingDirection = ScrollDirection.DOWN;
                    } else {
                        mCurScrollingDirection = ScrollDirection.SAME;
                    }
                    mPrevFirstVisibleItem = firstVisibleItem;
                }


                if (mIsLoadMoreEnabled && (mCurScrollingDirection == ScrollDirection.UP)) {
                    //We only need to paginate if user scrolling near the end of the list
                    if (!mPullCallback.isLoading() && !mPullCallback.hasLoadedAllItems()) {
                        //Only trigger a load more if a load operation is NOT happening AND all the items have not been loaded
                        final int totalItemCount = mRecyclerViewHelper.getItemCount();
                        final int firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                        final int visibleItemCount = Math.abs(mRecyclerViewHelper.findLastVisibleItemPosition() - firstVisibleItem);
                        final int lastAdapterPosition = totalItemCount - 1;
                        final int lastVisiblePosition = (firstVisibleItem + visibleItemCount) - 1;
                        if (lastVisiblePosition >= (lastAdapterPosition - mLoadMoreOffset)) {
                            if (null != mPullCallback) {
                                mProgressBar.setVisibility(VISIBLE);
                                mPullCallback.onLoadMore();
                            }
                        }
                    }
                }
            }
        });
    }

    public void setComplete() {
        mProgressBar.setVisibility(GONE);
    }

    public void initLoad() {
        if (null != mPullCallback) {
            mPullCallback.onRefresh();
        }
    }

    public RecyclerView getRecyclerView() {
        return this.mRecyclerView;
    }

    public void setPullCallback(PullCallback mPullCallback) {
        this.mPullCallback = mPullCallback;
    }

    public void setLoadMoreOffset(int mLoadMoreOffset) {
        this.mLoadMoreOffset = mLoadMoreOffset;
    }

    public void isLoadMoreEnabled(boolean mIsLoadMoreEnabled) {
        this.mIsLoadMoreEnabled = mIsLoadMoreEnabled;
    }
}
