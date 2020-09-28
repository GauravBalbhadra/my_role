package com.myrole;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.bumptech.glide.Glide;
import com.myrole.adapter.CountrySpinnerAdapter;
import com.myrole.adapter.CustomSpinnerAdapter;
import com.myrole.camera_ck.crop.CropImage;
import com.myrole.camera_ck.trimmer.utils.FileUtils;
import com.myrole.dashboard.MainDashboardActivity;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.ProgressBarHandler;
import com.myrole.utils.RestClient;
import com.myrole.utils.Utils;
import com.myrole.vo.Country;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import id.zelory.compressor.Compressor;

//import com.gs.instavideorecorder.FFmpegRecordActivity;

//import life.knowledge4.videotrimmer.utils.KFileUtils;

public class BuildProfileActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    private Context context;
    private Activity activity;
    ArrayList<Country> nationalityList;
    ArrayList<String> appearanceList;
    ArrayList<String> colorList;
    ArrayList<String> genderList;
    ArrayList<String> heightList;
    Spinner spinnerAppearance, spinnerColor,spinnergender,spinnerheight;
    CustomSpinnerAdapter colorSpinnerAdapter, appearanceSpinnerAdapter,genderSpinnerAdapter,heightSpinnerAdapter;
    CountrySpinnerAdapter nationalitySpinnerAdapter;
    String dob = "", nationality = "IN", hobby = "",gender="",
    address="",district="",state="",pincode="",status="",
            height = "", weight = "", appearance = "", color = "";
    AutoCompleteTextView nationalityView;
    AutoCompleteAdapter nationalityAdapter;
    private int selectedMediaType;
    private String imagePath = "", videoPath = "", videoIcon = "";
    private Bitmap bitmap;
    private String videoUrl = "";
    String profileUrl = "";
    private ProgressBarHandler progressBarHandler;
    private RelativeLayout lay_edit_profile;
    static final String EXTRA_VIDEO_PATH  = "EXTRA_VIDEO_PATH";

    private AmazonS3 s3;
    private TransferUtility transferUtility;
    private String uniqueId;
    private String video_name;
    private String img_name;
    private AppPreferences pref;
    private File compressedImage;

    BroadcastReceiver crop_broadcast3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("VIDEO_CROP_3")) {
                intent.getExtras().get("URI");
                videoPath = Utils.getRealPathFromURI(BuildProfileActivity.this, (Uri) intent.getExtras().get("URI"));

                videoUrl = videoPath;
                ((RelativeLayout) findViewById(R.id.lay_vid_add)).setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.vid_update_icon)).setVisibility(View.VISIBLE);
                try{
                    Glide.with(context).asBitmap().load(videoPath)
                            .centerCrop().into(((ImageView) findViewById(R.id.vid_add_video)));
                }catch (Exception e){}
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("VIDEO_CROP_3");
        registerReceiver(crop_broadcast3, intentFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_profile);
        context = this;
        activity = this;
        credentialsProvider();
        setTransferUtility();
       pref = AppPreferences.getAppPreferences(BuildProfileActivity.this);
       progressBarHandler = new ProgressBarHandler(activity);
        lay_edit_profile = (RelativeLayout) findViewById(R.id.lay_edit_profile);
        String username = AppPreferences.getAppPreferences(this).getStringValue(AppPreferences.USER_NAME);
        ((TextView)findViewById(R.id.txt_welcome)).setText("Welcome "+username+" !");

        findViewById(R.id.txt_dob).setOnClickListener(this);
        findViewById(R.id.lay_img_add_pic).setOnClickListener(this);
        findViewById(R.id.lay_vid_add).setOnClickListener(this);
        findViewById(R.id.btn_proceed).setOnClickListener(this);
        findViewById(R.id.txt_skip).setOnClickListener(this);
      //  findViewById(R.id.img_update_icon).setOnClickListener(this);
      //  findViewById(R.id.vid_update_icon).setOnClickListener(this);
      //  findViewById(R.id.vid_icon_play).setOnClickListener(this);
        findViewById(R.id.img_add_pic).setOnClickListener(this);
        findViewById(R.id.img_icon).setOnClickListener(this);
        findViewById(R.id.ll_add_pic).setOnClickListener(this);
        findViewById(R.id.vid_add_video).setOnClickListener(this);

        nationalityView = ((AutoCompleteTextView) findViewById(R.id.auto_nationality));
        nationalityAdapter = new AutoCompleteAdapter(this, R.layout.layout_autocomplete_text);
        nationalityView.setAdapter(nationalityAdapter);

        ((EditText) findViewById(R.id.txt_weight)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("0")) {
                    ((EditText) findViewById(R.id.txt_weight)).setText(s.subSequence(0, s.length() - 1));
                }
                if (s.length() > 0) {
                    float w = Float.parseFloat(s.toString());
                    if (w > 300)
                        ((EditText) findViewById(R.id.txt_weight)).setError("Weight should not be more than 300 kilogram.");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        new CountryListTask().execute();
        String userId = AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID);
        new GetUserDetailTask().execute(userId);
        applyFont();

        spinnerAppearance = (Spinner) findViewById(R.id.spinner_appearance);
        spinnerColor = (Spinner) findViewById(R.id.spinner_color);
        spinnergender = (Spinner) findViewById(R.id.spinner_gender);
        spinnerheight = (Spinner) findViewById(R.id.spinner_height);
        // Spinner Drop down elements
        nationalityList = new ArrayList<Country>();
        Country country = new Country();
        country.name = "Indian";
        country.code = "IN";
        nationalityList.add(country);

        appearanceList = new ArrayList<String>();
        appearanceList.add("Select");
        appearanceList.add("Slim");
        appearanceList.add("Fat");
        appearanceList.add("Healthy");
        appearanceList.add("High Weight");

        colorList = new ArrayList<String>();
        colorList.add("Select");
        colorList.add("Light skin");
        colorList.add("Fair skin");
        colorList.add("Medium skin");
        colorList.add("Olive skin");
        colorList.add("Tan brown");
        colorList.add("Black brown skin");

        genderList = new ArrayList<String>();
        genderList.add("Select");
        genderList.add("Male");
        genderList.add("Female");

        heightList = new ArrayList<String>();
        heightList.add("Select");
//        heightList.add("Female");

        int i=3;
        int j=0;
        for (i=3;i<8;i++)
        {
            for (j=0;j<12;j++){
                String s=i+"."+j;
                heightList.add(s);
            }
        }

        heightSpinnerAdapter = new CustomSpinnerAdapter(this, heightList);
        spinnerheight.setAdapter(heightSpinnerAdapter);
        spinnerheight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    height = "";
                }else{
                    height = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        genderSpinnerAdapter = new CustomSpinnerAdapter(this, genderList);
        spinnergender.setAdapter(genderSpinnerAdapter);
        spinnergender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    gender = "";
                }else {
                    gender = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        appearanceSpinnerAdapter = new CustomSpinnerAdapter(this, appearanceList);
        spinnerAppearance.setAdapter(appearanceSpinnerAdapter);
        spinnerAppearance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    appearance = "";
                }else {
                    appearance = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        colorSpinnerAdapter = new CustomSpinnerAdapter(this, colorList);
        spinnerColor.setAdapter(colorSpinnerAdapter);
        spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    color = "";
                }else {
                    color = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nationalitySpinnerAdapter = new CountrySpinnerAdapter(this, nationalityList);
    }

    public void credentialsProvider() {
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-south-1:f15a779c-7e5b-43b1-8ab4-7fd49b71599e", // Identity Pool ID
                Regions.AP_SOUTH_1 // Region
        );
        setAmazonS3Client(credentialsProvider);
    }

    public void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider) {
        // Create an S3 client
        s3 = new AmazonS3Client(credentialsProvider);
        s3.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
    }

    public void setTransferUtility() {

        transferUtility = new TransferUtility(s3, getApplicationContext());
    }

    public  void upload(String img_path){
        uniqueId = UUID.randomUUID().toString();
        File file = new File(img_path);
         img_name = uniqueId + ".jpg";
        TransferObserver transferObserver = transferUtility.upload(
                "myrole-app-data/images",     /* The bucket to upload to */
                img_name,       /* The key for the uploaded object */
                file       /* The file where the data to upload exists */
        );
//        transferObserverListener(transferObserver);

        transferObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("statechange", state + "");
                if (String.valueOf(state).equals("IN_PROGRESS")) {
                } else if (String.valueOf(state).equals("COMPLETED")) {
//                    new HomeActivity.PublishPostTask().execute(desc, role_id, "image", img_name);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error", "error");
//                Toast.makeText(HomeActivity.this, "error : " + ex, Toast.LENGTH_LONG).show();
            }
        });
    }

    public  void upload2(String video_path){
        uniqueId = UUID.randomUUID().toString();
        File file = new File(video_path);
         video_name = uniqueId + ".mp4";
        TransferObserver transferObserver = transferUtility.upload(
                "myroleapp",     /* The bucket to upload to */
                video_name,       /* The key for the uploaded object */
                file       /* The file where the data to upload exists */
        );
//        transferObserverListener(transferObserver);

        transferObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("statechange", state + "");
                if (String.valueOf(state).equals("IN_PROGRESS")) {
                } else if (String.valueOf(state).equals("COMPLETED")) {
//                    new HomeActivity.PublishPostTask().execute(desc, role_id, "image", img_name);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error", "error");
//                Toast.makeText(HomeActivity.this, "error : " + ex, Toast.LENGTH_LONG).show();
            }
        });
    }


    private ArrayList<String> autocomplete(String input) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (Country country : nationalityList) {
            String str = country.name;
            int len = input.length() > str.length() ? str.length() : input.length();
            if (len == 0)
                arrayList.add(str);
            else if (input.equalsIgnoreCase(str.substring(0, len)))
                arrayList.add(str);

        }
        return arrayList;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txt_skip:
                //startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                Intent intent = new Intent(this, MainDashboardActivity.class);

                startActivity(intent);
               // finishAffinity();
                break;
            case R.id.txt_dob:
                openDatePicker();
                break;
            case R.id.img_add_pic:
                selectedMediaType = Config.MEDIA_TYPE_IMAGE;
                Utils.showMediaSelectionDialog(this, selectedMediaType, "0");
                break;

            case R.id.lay_img_add_pic:
                selectedMediaType = Config.MEDIA_TYPE_IMAGE;
                Utils.showMediaSelectionDialog(this, selectedMediaType, "0");
                break;
            case R.id.lay_vid_add:
                selectedMediaType = Config.MEDIA_TYPE_VIDEO;
                Utils.showMediaSelectionDialog(this, selectedMediaType, "0");
                break;
            case R.id.img_icon:
                selectedMediaType = Config.MEDIA_TYPE_IMAGE;
                Utils.showMediaSelectionDialog(this, selectedMediaType, "0");
                break;
            case R.id.vid_add_video:
                selectedMediaType = Config.MEDIA_TYPE_VIDEO;
                Utils.showMediaSelectionDialog(this, selectedMediaType, "0");
                break;
            case R.id.ll_add_pic:
                imagePath = "";
                selectedMediaType = Config.MEDIA_TYPE_IMAGE;
                Utils.showMediaSelectionDialog(this, selectedMediaType, "0");
                break;
            case R.id.btn_proceed:
                address = ((EditText) findViewById(R.id.txt_address)).getText().toString().trim();
                district = ((EditText) findViewById(R.id.txt_district)).getText().toString().trim();
                state = ((EditText) findViewById(R.id.txt_state)).getText().toString().trim();
                pincode = ((EditText) findViewById(R.id.txt_pincode)).getText().toString().trim();
                state = ((EditText) findViewById(R.id.txt_state)).getText().toString().trim();
                hobby = ((EditText) findViewById(R.id.txt_hobby)).getText().toString().trim();
             //   height = ((EditText) findViewById(R.id.txt_height)).getText().toString().trim();
                weight = ((EditText) findViewById(R.id.txt_weight)).getText().toString().trim();
                String str = nationalityView.getText().toString().trim();
                if (str.isEmpty()) {
                    nationalityView.setError("Please enter nationality.");
                    nationalityView.requestFocus();
                    return;
                } else {
                    nationality = "";
                    for (Country country : nationalityList) {
                        if (country.name.equalsIgnoreCase(str)) {
                            nationality = country.code;
                        }
                    }
                    if (nationality.isEmpty()) {
                        nationalityView.setError("Invalid nationality");
                        nationalityView.requestFocus();
                        return;
                    }
                }
                if (!weight.isEmpty()) {
                    float w = Float.parseFloat(weight);
                    if (w > 300) {
                        ((EditText) findViewById(R.id.txt_weight)).setError("Weight should not be more than 300 kilogram.");
                        return;
                    }
                }
                new UpdateProfileTask().execute();
                break;
//
        }
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1990);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        Calendar now = Calendar.getInstance();
        dpd.setMaxDate(now);
       // dpd.show(getFragmentManager(), "Datepickerdialog");
    dpd.show(getSupportFragmentManager(),"Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        dob = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
        String date = dayOfMonth + " " + Utils.getMonthName(monthOfYear + 1) + " " + year;
        ((TextView) findViewById(R.id.txt_dob)).setText(date);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == 0)
                    Utils.launchCamera(this, selectedMediaType,"0");
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


                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri selectedImageUri = result.getUri();
                    // uri = selectedImageUri;
                    //  Toast.makeText(this, "yoynono", Toast.LENGTH_LONG).show();
                 String   imagePath244 = Utils.getRealPathFromURI(this, selectedImageUri);
                    imagePath=compress_image(imagePath244);
                    upload(imagePath);
                    ((RelativeLayout) findViewById(R.id.lay_img_add_pic)).setVisibility(View.GONE);
                    ((ImageView) findViewById(R.id.img_update_icon)).setVisibility(View.VISIBLE);
                    try{
                        Glide.with(getApplicationContext()).asBitmap().load(imagePath)
                                .centerCrop().into(((ImageView) findViewById(R.id.img_add_pic)));
                    }catch (Exception e){}
                    bitmap = BitmapFactory.decodeFile(imagePath);


                }
                else if (requestCode == Config.GALLERYIMAGE) {
                    CropImage.activity(data.getData())
                            .setFixAspectRatio(true)
                            .setDeleteImage(false)
                            .start(this);

                }
                else if (requestCode == Config.REQUEST_RECORD_VIDEO) {
                    // Toast.makeText(this, "REQUEST_RECORD_VIDEO", Toast.LENGTH_LONG).show();
                    switch (resultCode) {
                        case RESULT_OK:
                            Uri videoUri = data.getData();
                            String gy = String.valueOf(videoUri);
                            //   Toast.makeText(this, "RESULT_OK " + gy, Toast.LENGTH_LONG).show();
                            videoPath = Utils.getRealPathFromURI(BuildProfileActivity.this, videoUri);
                            upload2(videoPath);
                            bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND);
                            ((RelativeLayout) findViewById(R.id.lay_vid_add)).setVisibility(View.GONE);
                            // ((ImageView) findViewById(R.id.vid_icon_play)).setVisibility(View.VISIBLE);
                            ((ImageView) findViewById(R.id.vid_update_icon)).setVisibility(View.VISIBLE);
                            try{
                                Glide.with(getApplicationContext()).asBitmap().load(videoPath)
                                        .centerCrop().into(((ImageView) findViewById(R.id.vid_add_video)));
                            }catch (Exception e){}

                            break;
                        case Activity.RESULT_CANCELED:
                            break;

                    }

                }
                else if (requestCode == Config.REQUEST_VIDEO_GALLERY) {

                    Intent intent = new Intent(this, com.myrole.camera_ck.activity.trim.TrimmerActivity.class);
                    intent.putExtra(EXTRA_VIDEO_PATH, FileUtils.getPath(this, data.getData()));
                    activity.startActivityForResult(intent, Config.GALLERYVIDEO);
                    // startActivity(intent);
                }else if (requestCode == Config.GALLERYVIDEO) {
                    String returnValue = data.getStringExtra("some_key");
                    //  videoPath = Utils.getRealPathFromURI(HomeActivity.this, returnValue);
                    videoPath = returnValue;
                    upload2(videoPath);
                    bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND);

                    ((RelativeLayout) findViewById(R.id.lay_vid_add)).setVisibility(View.GONE);
                    // ((ImageView) findViewById(R.id.vid_icon_play)).setVisibility(View.VISIBLE);
                    ((ImageView) findViewById(R.id.vid_update_icon)).setVisibility(View.VISIBLE);
                    try{
                        Glide.with(getApplicationContext()).asBitmap().load(videoPath)
                                .centerCrop().into(((ImageView) findViewById(R.id.vid_add_video)));
                    }catch (Exception e){}
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong, please try again or use another image.", Toast.LENGTH_LONG).show();
            }


        }
    }

    public String compress_image(String actualImage_path) {
        String path = "";
        if (actualImage_path == null) {

        } else {
            // Compress image in main thread using custom Compressor
            try {
                compressedImage = new Compressor(this)
//                        .setMaxWidth(640)
//                        .setMaxHeight(480)
                        .setQuality(75)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .compressToFile(new File(actualImage_path));

                path = compressedImage.getPath();
            } catch (IOException e) {
                e.printStackTrace();
//                showError(e.getMessage());
            }

        }
        return path;
    }
    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_title), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_skip), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_few), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_add_pic), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_add_video), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_dob_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_dob), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_gender_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_nationality), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_hobby_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_hobby), Config.NEXA, Config.REGULAR);
    //    Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_height_hint), Config.NEXA, Config.REGULAR);
//        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_weight_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_weight), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_appearance_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_color_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.auto_nationality), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (Button) findViewById(R.id.btn_proceed), Config.NEXA, Config.REGULAR);

        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_address_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_address), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_district_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_district), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_state_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_state), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_pincode_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_pincode), Config.NEXA, Config.REGULAR);
    }

    private class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;
        private Context context;
        private int layout;

        public AutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            this.context = context;
            this.layout = textViewResourceId;
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            Utils.setTypeface(context, (TextView) view.findViewById(R.id.autocomplete_text), Config.NEXA, Config.REGULAR);
            return view;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Assign the data to the FilterResults
                        resultList = autocomplete(constraint.toString());
                        filterResults.values = resultList;
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

    class CountryListTask extends AsyncTask<String, Void, String> {
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

            return HTTPUrlConnection.getInstance().load(getApplicationContext(), Config.GET_COUNTRY_LIST, postDataParams);
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
                    nationalityList.clear();
                    for (int i = 0; i < arr.length(); i++) {
                        Country country = new Country();
                        country.name = arr.getJSONObject(i).getString("name");
                        country.code = arr.getJSONObject(i).getString("iso");
                        nationalityList.add(i, country);
                    }
                    nationalitySpinnerAdapter.notifyDataSetChanged();
                    Log.v(TAG, result);
                } else {
                    Toast.makeText(BuildProfileActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    class UpdateProfileTask extends AsyncTask<String, Void, String> {

        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if (bitmap != null)
                pref.putImage(AppPreferences.USER_IMAGE, bitmap);

//            if (imagePath != null)
//            preferences.putStringValue(AppPreferences.USER_IMAGE_URL, imagePath);

            try {

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                RestClient client;

                entity.addPart("dob", new StringBody(dob));
                entity.addPart("nationality", new StringBody(nationality));
                entity.addPart("height", new StringBody(height));
                entity.addPart("weight", new StringBody(weight));
                entity.addPart("hobby", new StringBody(hobby));
                entity.addPart("apperance", new StringBody(appearance));
                entity.addPart("user_id", new StringBody(pref.getStringValue(AppPreferences.USER_ID)));
                entity.addPart("color", new StringBody(color));
                entity.addPart("gender", new StringBody(gender));
                entity.addPart("status", new StringBody(status));
                entity.addPart("address", new StringBody(address));
                entity.addPart("district", new StringBody(district));
                entity.addPart("state", new StringBody(state));
                entity.addPart("pincode", new StringBody(pincode));
                entity.addPart("incomplete",new StringBody("0"));
                if (img_name!=null)
                entity.addPart("image",new StringBody(img_name));
                if (video_name!=null)
                entity.addPart("video",new StringBody(video_name));
                client = new RestClient(Config.UPDATE_PROFILE);
                client.setMultipartEntity(entity);
                client.Execute(RestClient.RequestMethod.POST);
                response = client.getResponse();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.hide();
            try {

                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    Log.v(TAG, result);
                    JSONObject data = object.getJSONArray("data").getJSONObject(0);

                    AppPreferences pref = AppPreferences.getAppPreferences(getApplicationContext());
//                        pref.putStringValue(AppPreferences.USER_ID, data.getString("id"));
//                        pref.putStringValue(AppPreferences.USER_NAME, data.getString("username"));
//                        pref.putStringValue(AppPreferences.USER_PHONE, data.getString("mobile"));
                    pref.putStringValue(AppPreferences.USER_IMAGE_URL, data.getString("image"));
                    pref.putStringValue(AppPreferences.USER_PROFILE_INCOMPLETE,data.getString("incomplete"));
                    startActivity(new Intent(getApplicationContext(), MainDashboardActivity.class));
                  //  finishAffinity();
                } else {
                    Toast.makeText(BuildProfileActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class GetUserDetailTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
            lay_edit_profile.setVisibility(View.GONE);
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", params[0]);
            return HTTPUrlConnection.getInstance().load(context, Config.GET_USER, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.hide();
            lay_edit_profile.setVisibility(View.VISIBLE);
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONObject userDetail = object.getJSONArray("data").getJSONObject(0);

                    videoUrl = userDetail.getString("video");
                    profileUrl = userDetail.getString("image");
                    pref.putStringValue(AppPreferences.USER_IMAGE_URL, profileUrl);
                    if (!videoUrl.equals("null") && videoUrl != null && !videoUrl.isEmpty()) {


                        videoIcon = videoUrl.replace(".mp4", ".jpg");

                       // ((ImageView) findViewById(R.id.vid_icon_play)).setVisibility(View.VISIBLE);
                        ((ImageView) findViewById(R.id.vid_update_icon)).setVisibility(View.VISIBLE);
                        ((RelativeLayout) findViewById(R.id.lay_vid_add)).setVisibility(View.GONE);
                        try {
                            Glide.with(getApplicationContext()).asBitmap().load(videoIcon)
                                    .centerCrop().into(((ImageView) findViewById(R.id.vid_add_video)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {
                        ((RelativeLayout) findViewById(R.id.lay_vid_add)).setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.txt_add_video)).setText("ADD A SHORT VIDEO");
                        try {
                            Glide.with(getApplicationContext()).asBitmap().load(videoIcon)
                                    .placeholder(R.drawable.add_video_icon).centerCrop().into(((ImageView) findViewById(R.id.vid_icon)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    if (!profileUrl.equals("null") && profileUrl != null && !profileUrl.isEmpty()) {

                        ((ImageView) findViewById(R.id.img_icon)).setVisibility(View.GONE);
                        ((ImageView) findViewById(R.id.img_update_icon)).setVisibility(View.VISIBLE);
                        ((RelativeLayout) findViewById(R.id.lay_img_add_pic)).setVisibility(View.GONE);
                        try {
                            Glide.with(getApplicationContext()).asBitmap().load(userDetail.getString("image"))
                                    .centerCrop().into(((ImageView) findViewById(R.id.img_add_pic)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        ((RelativeLayout) findViewById(R.id.lay_img_add_pic)).setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.txt_add_pic)).setText("ADD A PROFILE PICTURE");
                        try {
                            Glide.with(getApplicationContext()).asBitmap().load(userDetail.getString("image"))
                                    .placeholder(R.drawable.add_profile_img_icon).centerCrop().into(((ImageView) findViewById(R.id.img_icon)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if ((!userDetail.getString("dob").equals("null") || userDetail.getString("dob") != null || !userDetail.getString("dob").equals(""))) {

                        dob = userDetail.getString("dob");

                        String date = Utils.formatdate(userDetail.getString("dob"));

                        ((TextView) findViewById(R.id.txt_dob)).setText(date);
                    }

                    if ((!userDetail.getString("nationality").equals("null") || userDetail.getString("nationality") != null || !userDetail.getString("nationality").equals(""))) {

                        for (Country country : nationalityList) {
                            String str = country.code;
                            if (userDetail.getString("nationality").equals(str)) {
                                String countryName = country.name;
                                ((TextView) findViewById(R.id.auto_nationality)).setText(countryName);
                                break;
                            }

                        }
                    }

                    if ((!userDetail.getString("hobby").equals("null") || userDetail.getString("hobby") != null || !userDetail.getString("hobby").equals(""))) {
                        if (userDetail.getString("hobby").equals("null")){
                            ((TextView) findViewById(R.id.txt_hobby)).setText("");
                        }else{
                            ((TextView) findViewById(R.id.txt_hobby)).setText(userDetail.getString("hobby"));
                        }
                    }

                    if ((!userDetail.getString("weight").equals("null") || userDetail.getString("weight") != null || !userDetail.getString("weight").equals(""))) {
                        if (userDetail.getString("weight").equals("null")){
                            ((TextView) findViewById(R.id.txt_weight)).setText("");
                        }else{
                            ((TextView) findViewById(R.id.txt_weight)).setText(userDetail.getString("weight"));
                        }

                    }
                } else {
                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
