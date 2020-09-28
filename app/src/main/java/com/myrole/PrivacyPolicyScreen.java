package com.myrole;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.myrole.data.TermsAndConditionsDTO;
import com.myrole.utils.Config;
import com.myrole.utils.Utils;


public class PrivacyPolicyScreen extends BaseActivity {

    private String TAG = "TermsAndConditionScreen";
    private WebView mWebview;
//    private DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);
        findViewById(R.id.btn_back).setOnClickListener(this);
        TermsAndConditionsDTO termsAndConditionsDTO =  new TermsAndConditionsDTO();

        applyFont();

        mWebview=(WebView) findViewById(R.id.webView);
//        mWebview  = new WebView(this);

        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

//        mWebview.loadUrl("http://172.104.40.244/myrole/privacy-policy.htm");
        //mWebview.loadUrl("http://172.104.40.44/pages/privacy-policy.htm");
        mWebview.loadUrl("https://myrole.in/pages/privacy-policy.htm");

//        getTermsAndCondition();
    }


    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_title), Config.NEXA, Config.BOLD);
//        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_privacy_policy),  Config.QUICKSAND, Config.BOLD);
    }

//    private void init() {
//
//        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle(" ");
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        mToolbar.setNavigationIcon(R.drawable.back_btn);
//        TextView mTitle = (TextView) findViewById(R.id.toolbar_title);
//        mTitle.setText(R.string.terms_line);
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

//    private void getTermsAndCondition() {
//        if (Utils.isOnline(this)) {
//            Map<String, String> params = new HashMap<>();
//            params.put("action", WebserviceConstant.GET_TERMS_AND_CONDITIONS);
//            CustomProgressDialog.showProgDialog(this, null);
//            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST,
//                    WebserviceConstant.SERVICE_BASE_URL, params,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                if (Utils.getWebServiceStatus(response)) {
//                                    Utils.ShowLog(TAG, "got some response = " + response.toString());
//                                    TermsAndConditionsDTO termsAndConditionsDTO = new Gson().
//                                            fromJson(response.getJSONObject
//                                                    ("page").toString(), TermsAndConditionsDTO.class);
//                                    setTermsConditions(termsAndConditionsDTO);
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            CustomProgressDialog.hideProgressDialog();
//                        }
//                    }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    CustomProgressDialog.hideProgressDialog();
//                    Utils.showExceptionDialog(TermsAndConditionScreen.this);
//                    //       CustomProgressDialog.hideProgressDialog();
//                }
//            });
//            AppController.getInstance().getRequestQueue().add(postReq);
//            postReq.setRetryPolicy(new DefaultRetryPolicy(
//                    30000, 0,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            CustomProgressDialog.showProgDialog(this, null);
//        } else {
//            Utils.showNoNetworkDialog(this);
//        }
//
//    }


//    private void setTermsConditions(TermsAndConditionsDTO termsAndConditionsDTO) {
//
//        setViewText(R.id.txt_terms_and_condition, termsAndConditionsDTO.getDescription());
//    }
}
