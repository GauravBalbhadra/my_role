package com.myrole;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.myrole.data.TermsAndConditionsDTO;
import com.myrole.utils.Config;
import com.myrole.utils.Utils;


public class TermsAndConditionScreen extends BaseActivity {

    private WebView mWebview;
    private String TAG = "TermsAndConditionScreen";
//    private DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_condition_screen);
        findViewById(R.id.btn_back).setOnClickListener(this);

        TermsAndConditionsDTO termsAndConditionsDTO =  new TermsAndConditionsDTO();
        applyFont();
        mWebview=(WebView) findViewById(R.id.webView);
//        mWebview  = new WebView(this);

        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

//        mWebview.loadUrl("http://172.104.40.244/myrole/myrole-terms-of-use-agreement.htm");
        //mWebview.loadUrl("http://172.104.40.44/pages/myrole-terms-of-use-agreement.htm");
        mWebview.loadUrl("https://myrole.in/pages/myrole-terms-of-use-agreement.htm");
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_title), Config.NEXA, Config.BOLD);
//        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_terms_and_condition),  Config.QUICKSAND, Config.BOLD);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
