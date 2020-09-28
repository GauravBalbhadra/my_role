package com.myrole.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.myrole.R;
import com.myrole.utils.Config;
import com.myrole.utils.Utils;

//import com.bekground.aylife.Preferneces.AppPreferences;
//import com.bekground.aylife.R;
//import com.bekground.aylife.activity.SignInActivity;
//import com.bekground.aylife.model.ArticlesDTO;
//import com.bekground.aylife.utils.FetchInterfaceAlert;

public class AlertLoginDilogFragment extends DialogFragment {


    Configuration config;
    String getArgument;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
getArgument = getArguments().getString("yo");//Get pass data with its key value
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //that can stop the outside tuch didssmie dilog to stop
       // dialog.setCanceledOnTouchOutside(false);
        //end

        dialog.setContentView(R.layout.alert_deatil);
        init(dialog);
        dialog.show();
        return dialog;
    }
//

    public void init(final Dialog dialog) {
        Context context = getContext();
      //  TextView txt1 = (TextView) dialog.findViewById(R.id.txt1);
        TextView txt2 = (TextView) dialog.findViewById(R.id.txt2);
        txt2.setText(getArgument);
        Utils.setTypeface(getContext(), (TextView) dialog.findViewById(R.id.txt2), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getContext(), (TextView) dialog.findViewById(R.id.txt_cancel), Config.NEXA, Config.REGULAR);

        TextView txt_cancel = (TextView) dialog.findViewById(R.id.txt_cancel);
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                dialog.dismiss();

            }
        });

    }


}