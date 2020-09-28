package com.myrole;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myrole.utils.Utils;

/**
 * Created by Deorwine-2 on 25-01-2018.
 */

public abstract class BaseToroFragment extends Fragment implements View.OnClickListener{



    final static String TAG = "MYROLE";
    private ProgressDialog dialog;
    private ProgressBar progressBar;
    private ProgressBar mProgressBar;
    private BaseFragment.OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // setFragmentInteractionListener(mListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = new ProgressBar(getContext(),null,android.R.attr.progressBarStyleSmall);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
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



    @CallSuper
    @Override public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 24) {
            dispatchFragmentActivated();
        }
    }

    @CallSuper @Override public void onStop() {
        if (Build.VERSION.SDK_INT >= 24) {
            dispatchFragmentDeActivated();
        }
        super.onStop();
    }

    @CallSuper @Override public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < 24) {
            dispatchFragmentActivated();
        }
    }

    @CallSuper @Override public void onPause() {
        if (Build.VERSION.SDK_INT < 24) {
            dispatchFragmentDeActivated();
        }
        super.onPause();
    }

    protected abstract void dispatchFragmentActivated();

    protected abstract void dispatchFragmentDeActivated();
}
