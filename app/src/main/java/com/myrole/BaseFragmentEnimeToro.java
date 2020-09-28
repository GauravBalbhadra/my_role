package com.myrole;

/**
 * Created by Deorwine-2 on 25-01-2018.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myrole.utils.Utils;

/**
 * @author eneim | 6/6/17.
 */

public class BaseFragmentEnimeToro extends Fragment implements View.OnClickListener {


    final static String TAG = "MYROLE";
    private ProgressDialog dialog;
    private ProgressBar progressBar;
    private ProgressBar mProgressBar;
    private BaseFragment.OnFragmentInteractionListener mListener;
    private static final boolean D = BuildConfig.DEBUG;

    // Save viewPagerMode flag on config change
    private static final String STATE_VIEW_PAGER_MODE = "toro:fragment:state:view_pager_mode";

    // protected String TAG = "Toro:" + getClass().getSimpleName();

    public static final String RESULT_EXTRA_PLAYER_ORDER = "toro:demo:player:result:order";
    public static final String RESULT_EXTRA_PLAYBACK_INFO = "toro:demo:player:result:playback";

    /**
     * The following flag is used for {@link Fragment} that is inside a ViewPager.
     * Default is {@code false} for non-ViewPager use.
     */
    protected boolean viewPagerMode = false;

    @SuppressWarnings("SameParameterValue")
    public void setViewPagerMode(boolean viewPagerMode) {
        this.viewPagerMode = viewPagerMode;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (D) Log.wtf(TAG, "onCreate() called with: bundle = [" + bundle + "]");
        progressBar = new ProgressBar(getContext(),null,android.R.attr.progressBarStyleSmall);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (D) Log.wtf(TAG, "onAttach() called with: context = [" + context + "]");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle bundle) {
        if (D) {
            Log.wtf(TAG, "onCreateView() called with: inflater = ["
                    + inflater
                    + "], container = ["
                    + container
                    + "], bundle = ["
                    + bundle
                    + "]");
        }
        return super.onCreateView(inflater, container, bundle);
    }

    @CallSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (D) {
            Log.wtf(TAG, "onViewCreated() called with: view = [" + view + "], bundle = [" + bundle + "]");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.btn_toggle:
                ((HomeActivity)getActivity()).drawer.openDrawer(Gravity.LEFT);
                break;*/
            case R.id.btn_back:
                Utils.hideKeybord((BaseActivity) getActivity(),v);
                ((BaseActivity) getActivity()).onBackPressed();
                if (mListener != null)
                    mListener.onFragmentInteraction("BACK");
                break;
        }
    }

    public void showProgessBar() {
        if (progressBar != null && !progressBar.isShown() && !getActivity().isFinishing())
            progressBar.setVisibility(View.VISIBLE);
    }


    public void dismissProgressBar() {
        if (progressBar != null && !progressBar.isShown() && !getActivity().isFinishing())
            progressBar.setVisibility(View.GONE);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String command);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        if (D) Log.wtf(TAG, "onActivityCreated() called with: bundle = [" + bundle + "]");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle bundle) {
        super.onViewStateRestored(bundle);
        if (D) Log.wtf(TAG, "onViewStateRestored() called with: bundle = [" + bundle + "]");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (D) Log.wtf(TAG, "onSaveInstanceState() called with: outState = [" + outState + "]");
        outState.putBoolean(STATE_VIEW_PAGER_MODE, this.viewPagerMode);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (D) Log.wtf(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (D) Log.wtf(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (D) Log.wtf(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (D) Log.wtf(TAG, "onStop() called");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (D) Log.wtf(TAG, "onDestroyView() called");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (D) Log.wtf(TAG, "onDetach() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (D) Log.wtf(TAG, "onDestroy() called");
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        if (D) {
            Log.wtf(TAG, "onMultiWindowModeChanged() called with: isInMultiWindowMode = ["
                    + isInMultiWindowMode
                    + "]");
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        if (D) {
            Log.wtf(TAG, "onPictureInPictureModeChanged() called with: isInPictureInPictureMode = ["
                    + isInPictureInPictureMode
                    + "]");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (D) {
            Log.wtf(TAG, "onActivityResult() called with: requestCode = ["
                    + requestCode
                    + "], resultCode = ["
                    + resultCode
                    + "], data = ["
                    + data
                    + "]");
        }
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (D)
            Log.wtf(TAG, "onAttachFragment() called with: childFragment = [" + childFragment + "]");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (D) {
            Log.d(TAG, "setUserVisibleHint() called with: isVisibleToUser = [" + isVisibleToUser + "]");
        }
    }
}
