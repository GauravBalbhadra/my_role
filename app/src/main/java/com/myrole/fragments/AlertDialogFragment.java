package com.myrole.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.myrole.PlaybackFragment;
import com.myrole.R;

public class AlertDialogFragment extends DialogFragment {

    private final String mType;
    public Fragment mContext;

    public AlertDialogFragment(Fragment fragment, String type) {
        mContext = fragment;
        mType = type;
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());

        if(mContext instanceof CameraFragment) {

            if(mType.equalsIgnoreCase("back")) {

                builder.setTitle(R.string.title);

                builder.setMessage(R.string.dialog_discard_records)
                        .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((CameraFragment)mContext).onExitAction();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ((CameraFragment)mContext).onCancelAction();

                            }
                        })
                        .setNeutralButton(R.string.reshoot, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ((CameraFragment)mContext).onReshootAction();

                            }
                        });

            } else {

                builder.setMessage("Delete last clip?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((CameraFragment)mContext).onDelete();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((CameraFragment)mContext).onCancelAction();
                            }
                        });
            }

        } else if(mContext instanceof PlaybackFragment) {

            if(mType.equalsIgnoreCase("back")) {

                builder.setTitle(R.string.title);

                builder.setMessage(R.string.dialog_discard_records)
                        .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((PlaybackFragment)mContext).onExitAction();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ((PlaybackFragment)mContext).onCancelAction();

                            }
                        });

            }
        }

        return builder.create();
    }

    private void hideSystemUi() {

        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.KEEP_SCREEN_ON
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}