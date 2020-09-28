package com.myrole;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.UserDictionary;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.myrole.dashboard.MainDashboardActivity;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.ProgressBarHandler;
import com.myrole.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserNameActivity extends BaseActivity implements View.OnFocusChangeListener {

    private String username, phoneNumber, password, fullname, email = "", image = "";
    private boolean isValidUsername, isSubmitCalled;
    private AutoCompleteTextView userNameTextView;
    private ArrayList<String> userNameList;
    private AutoCompleteAdapter userNameAdapter;
    private Handler handler;
    private Bitmap bitmap;
    private ProgressBarHandler progressBarHandler;
    private Activity activity;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
        context = this;
        activity = this;
        progressBarHandler = new ProgressBarHandler(activity);
        phoneNumber = getIntent().getStringExtra("PHONE");
        userNameList = new ArrayList<String>();

        userNameTextView = (AutoCompleteTextView) findViewById(R.id.txt_user_name);

        findViewById(R.id.btn_done).setOnClickListener(this);
        findViewById(R.id.img_user).setOnClickListener(this);
        findViewById(R.id.txt_user_name).setOnFocusChangeListener(this);
        findViewById(R.id.txt_full_name).setOnFocusChangeListener(this);

        userNameAdapter = new AutoCompleteAdapter(this, R.layout.layout_autocomplete_text);
        userNameTextView.setAdapter(userNameAdapter);
        applyFont();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_done:
                username = (userNameTextView).getText().toString().trim();
                if (username.isEmpty()) {
                    (userNameTextView).setError(getString(R.string.error_username_empty));
                    break;
                } else if (!isValidUsername) {
                    isSubmitCalled = true;
                    new UserNameValidTask().execute();
                    break;
                }
                fullname = ((EditText) findViewById(R.id.txt_full_name)).getText().toString().trim();
                if (fullname.isEmpty()) {
                    ((EditText) findViewById(R.id.txt_full_name)).setError(getString(R.string.error_fullname_empty));
                    break;
                }
                password = ((EditText) findViewById(R.id.txt_password)).getText().toString().trim();
                if (password.isEmpty()) {
                    ((EditText) findViewById(R.id.txt_password)).setError(getString(R.string.error_password_empty));
                    break;
                } else if (password.length() < 6) {
                    ((EditText) findViewById(R.id.txt_password)).setError(getString(R.string.error_password_small));
                    break;
                }
                if (Utils.isNetworkConnected(this, true))
                    new RegisterUserTask().execute();
                break;
            case R.id.img_user:
                Utils.showMediaSelectionDialog(this, Config.MEDIA_TYPE_IMAGE, "0");
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == 0)
                    Utils.launchCamera(this, Config.MEDIA_TYPE_IMAGE, "0");
                break;
            case 2:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, Config.GALLERYIMAGE);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                if (requestCode == Config.CAMERAIMAGE) {
                    Utils.performCrop(UserNameActivity.this, Config.CAMERAFILEURI);
                } else if (requestCode == Config.GALLERYIMAGE) {
                    Uri selectedImageUri = data.getData();
                    if (selectedImageUri != null) {
                        Utils.performCrop(UserNameActivity.this, selectedImageUri);
                    }

                } else if (requestCode == Config.CROPIMAGE) {
                    if (Config.CROPPEDIMAGEURI != null) {
                        String imagePath = Utils.getRealPathFromURI(this, Config.CROPPEDIMAGEURI);
                        bitmap = BitmapFactory.decodeFile(imagePath);
                        ((CircleImageView) findViewById(R.id.img_user)).setImageBitmap(bitmap);
                    }
                } else if (requestCode == Config.ROTATEIMAGE) {
                    String imagePath = data.getStringExtra("imagePath");
                    bitmap = BitmapFactory.decodeFile(imagePath);
                    ((CircleImageView) findViewById(R.id.img_user)).setImageBitmap(bitmap);


                    if (Utils.isNetworkConnected(getApplicationContext(), true)) {
                        //new UpdateImageTask().execute();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong, please try again or use another image.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), userNameTextView, Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_full_name), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_password), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (Button) findViewById(R.id.btn_done), Config.NEXA, Config.BOLD);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.txt_user_name) {
            if (!hasFocus) {
                username = (userNameTextView).getText().toString().trim();
                if (!username.isEmpty()) {
                    new UserNameValidTask().execute();
                }
            } else {
                userNameTextView.showDropDown();
            }
        } else if (v.getId() == R.id.txt_full_name) {
            if (!hasFocus) {
                userNameList.clear();
                new UserNameSuggestionsTask().execute(((EditText) findViewById(R.id.txt_full_name)).getText().toString().trim(), phoneNumber);
            }
        }
    }

    private ArrayList<String> autocomplete(String input) {
        return userNameList;
    }

    private class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        // private ArrayList<String> resultList;

        public AutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return userNameList.size();
        }

        @Override
        public String getItem(int index) {
            return userNameList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Assign the data to the FilterResults
                        filterResults.values = autocomplete(constraint.toString());
                        filterResults.count = autocomplete(constraint.toString()).size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    class RegisterUserTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if (bitmap != null)
                AppPreferences.getAppPreferences(UserNameActivity.this).putImage(AppPreferences.USER_IMAGE, bitmap);

            if (image != null)
                AppPreferences.getAppPreferences(UserNameActivity.this).putStringValue(AppPreferences.USER_IMAGE_URL, image);

            postDataParams = new HashMap<String, String>();
            postDataParams.put("username", username);
            postDataParams.put("mobile", phoneNumber);
            postDataParams.put("password", password);
            postDataParams.put("name", fullname);
            postDataParams.put("email", "a@a.com");

            return HTTPUrlConnection.getInstance().load(getApplicationContext(), Config.REGISTER, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.hide();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONObject data = object.getJSONArray("data").getJSONObject(0);
                    AppPreferences pref = AppPreferences.getAppPreferences(getApplicationContext());
                    pref.putStringValue(AppPreferences.USER_ID, data.getString("id"));
//                    pref.putStringValue(AppPreferences.USER_NAME, data.getString("username"));
                    pref.putStringValue(AppPreferences.USER_NAME, data.getString("name"));
                    pref.putStringValue(AppPreferences.USER_PHONE, data.getString("mobile"));
                    pref.putStringValue(AppPreferences.USER_PROFILE_INCOMPLETE, data.getString("incomplete"));
                    //startActivity(new Intent(getApplicationContext(), FindContactsActivity.class).putExtra("Skip", "userNameActivity").putExtra("Activity", "2"));
                    startActivity(new Intent(getApplicationContext(), MainDashboardActivity.class).putExtra("Skip", "userNameActivity").putExtra("Activity", "2"));
                    finishAffinity();
                } else {
                    Toast.makeText(UserNameActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class UserNameValidTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("username", username);

            return HTTPUrlConnection.getInstance().load(getApplicationContext(), Config.VERIFY_USERNAME, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.hide();
            try {
                JSONObject object = new JSONObject(result);
                if (!object.getBoolean("status")) {
                    isValidUsername = true;
                    if (isSubmitCalled) {
                        isSubmitCalled = false;
                        findViewById(R.id.btn_done).performClick();
                    }
                } else {
                    isValidUsername = false;
                    (userNameTextView).setError(object.getString("message"));
                    Toast.makeText(UserNameActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class UserNameSuggestionsTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("username", params[0]);
            postDataParams.put("mobile", params[1]);

            return HTTPUrlConnection.getInstance().load(getApplicationContext(), Config.SUGGEST_USERNAME, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.hide();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONArray arr = object.getJSONArray("data");
                    for (int i = 0; i < arr.length(); i++) {
                        userNameList.add(i, arr.getJSONObject(i).getString("username"));
                        UserDictionary.Words.addWord(UserNameActivity.this,
                                arr.getJSONObject(i).getString("username"),
                                i, UserDictionary.Words.LOCALE_TYPE_CURRENT);

                    }
                    userNameAdapter.notifyDataSetChanged();

                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            userNameTextView.showDropDown();
                        }
                    }, 100);
                    Log.v(TAG, result);
                } else {
                    Toast.makeText(UserNameActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
