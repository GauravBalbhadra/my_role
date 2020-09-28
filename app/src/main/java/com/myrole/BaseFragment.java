package com.myrole;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myrole.dashboard.MainDashboardActivity;
import com.myrole.utils.Utils;

public class BaseFragment extends Fragment implements View.OnClickListener  {
    final static String TAG = "MYROLE";
    private ProgressDialog dialog;
    private ProgressBar progressBar;
    private ProgressBar mProgressBar;
    private OnFragmentInteractionListener mListener;
    public Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = new ProgressBar(getContext(),null,android.R.attr.progressBarStyleSmall);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
    }

    private void setFullScreen(){
        if (Build.VERSION.SDK_INT > 10) {
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN;

            if (Build.VERSION.SDK_INT >= 19) {
                flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }

            getActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
        } else {
            getActivity().getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                Utils.hideKeybord((MainDashboardActivity) getActivity(),v);
                ((MainDashboardActivity) getActivity()).onBackPressed();
                if (mListener != null)
                    mListener.onFragmentInteraction("BACK");
                break;
        }
    }

//    public void showProgessDialog() {
//        if (dialog != null && !dialog.isShowing() && !getActivity().isFinishing())
//            dialog.show();
//    }
//
//    public void showProgessDialog(String message) {
//        if (dialog != null && !dialog.isShowing() && !getActivity().isFinishing()) {
//            dialog.setMessage(message);
//            dialog.show();
//        }
//    }
//
//    public void dismissProgressDialog() {
//        if (dialog != null && dialog.isShowing() && !getActivity().isFinishing())
//            dialog.dismiss();
//    }


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




}
