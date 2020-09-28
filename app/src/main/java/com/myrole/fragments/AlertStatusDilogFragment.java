package com.myrole.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.myrole.R;
import com.myrole.utils.Config;
import com.myrole.utils.Utils;
import com.myrole.widget.FetchInterfaceAlert;

import org.apache.commons.lang3.StringEscapeUtils;


public class AlertStatusDilogFragment extends DialogFragment {

    FetchInterfaceAlert fetchInterfaceAlert;
    Configuration config;
    String staus = "";
    EditText get_status;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.alert_deatil_status);
        init(dialog);
        dialog.show();
        return dialog;
    }

    public void init(final Dialog dialog) {
        Context context = getContext();
        TextView txt_update = (TextView) dialog.findViewById(R.id.txt_update);
        TextView txt_cancel = (TextView) dialog.findViewById(R.id.txt_cancel);

        get_status = (EditText) dialog.findViewById(R.id.get_status);

        Utils.setTypeface(getContext(), (TextView) dialog.findViewById(R.id.txt_update), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getContext(), (TextView) dialog.findViewById(R.id.txt_cancel), Config.NEXA, Config.REGULAR);
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                staus = get_status.getText().toString();
                sendResult(345);
                dialog.dismiss();
            }
        });
    }

    private void sendResult(int REQUEST_CODE) {
        Intent intent = new Intent();
        intent.putExtra("yo", StringEscapeUtils.escapeJava(staus));
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), REQUEST_CODE, intent);
    }
}