package com.myrole.camera_ck.internal.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ListAdapter;

import com.myrole.R;
import com.myrole.camera_ck.listeners.OnItemClickListener;

//import com.myrole.camera_ck.R;

/**
 * Created by intel on 18-Jul-17.
 */

public class DialogUtils
{
    private static Dialog dialog;

    public static void cancel() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public static void showSingleChoiceDialog(Context context, String title, final ListAdapter adapter, int selected, final OnItemClickListener listener, boolean cancelable, String... args) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatDialog);
        builder.setTitle(title);
        builder.setSingleChoiceItems(adapter, selected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Button buttonPositive = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                buttonPositive.setEnabled(true);
            }
        });
        builder.setCancelable(cancelable);
        builder.setPositiveButton(args[0], new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                whichButton = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                listener.onItemClick(AlertDialog.BUTTON_POSITIVE, adapter.getItem(whichButton));
            }
        });
        if (args.length > 1 && !TextUtils.isEmpty(args[1])) {
            builder.setNegativeButton(args[1], new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                    whichButton = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    listener.onItemClick(AlertDialog.BUTTON_NEGATIVE,adapter.getItem(whichButton));
                }
            });
        }

        if (dialog == null || !dialog.isShowing()) {
            dialog = builder.create();
            dialog.show();
            if (selected == -1) {
                Button buttonPositive = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                buttonPositive.setEnabled(false);
            }
        }
    }
}
