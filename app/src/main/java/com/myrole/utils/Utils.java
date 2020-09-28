package com.myrole.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.myrole.R;
import com.myrole.camera_ck.activity.camera.CameraActivity;
import com.myrole.camera_ck.activity.video.VideoActivity;
import com.myrole.camera_ck.configuration.Configuration;
import com.myrole.camera_ck.crop.CropImage;
import com.myrole.holders.TodayRoleDetailsDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.gs.instavideorecorder.FFmpegRecordActivity;
//import com.myrole.ImageCropActivity;
//import com.myrole.camera_ck.internal.utils.CameraHelper;
//import com.myrole.camera_ck.recorder.activity.FFmpegRecorderActivity;
//import com.myrole.camera_ck.recorder.activity.params.FFmpegRecorderActivityParams;
//import com.myrole.camera_ck.recorder.camera.CameraControllerI;
//import com.myrole.camera_ck.recorder.recorder.common.ImageFit;
//import com.myrole.camera_ck.recorder.recorder.common.ImageScale;
//import com.myrole.camera_ck.recorder.recorder.common.ImageSize;
//import com.myrole.camera_ck.recorder.recorder.params.EncoderParamsI;

public class Utils {

    public static boolean isFromProfile = false ;
    static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";
    private final static int CAMERA_RQ = 6969;
    private static final int REQUEST_VIDEO_TRIMMER = 0x01;
    private static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    public static TodayRoleDetailsDTO todayRoleDetailsDTO1;
    public static String id;

    public static HashMap getDeviceSize(Activity activity) {
        HashMap hashMap = new HashMap();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        hashMap.put("Height", displaymetrics.heightPixels);
        hashMap.put("Width", displaymetrics.widthPixels);
        return hashMap;
    }

    public static String loadJSONFromAsset(Activity activity, String filename) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static void setTypeface(Context context, TextView textview, String fontName, String style) {
        if (fontName.equalsIgnoreCase(Config.NEXA)) {
            if (style != null && style.equalsIgnoreCase(Config.BOLD)) {
                Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Bold.otf");
                textview.setTypeface(face);
            } else {
                Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Light.otf");
                textview.setTypeface(face);
            }
        }
        if (fontName.equalsIgnoreCase(Config.RALEWAY)) {
            if (style != null && style.equalsIgnoreCase(Config.BOLD)) {
                Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Bold.ttf");
                textview.setTypeface(face);
            } else {
                Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Regular.ttf");
                textview.setTypeface(face);
            }
        }
        if (fontName.equalsIgnoreCase(Config.QUICKSAND)) {
            if (style != null && style.equalsIgnoreCase(Config.BOLD)) {
                Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/Quicksand-Bold.otf");
                textview.setTypeface(face);
            } else {
                Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/Quicksand-Regular.otf");
                textview.setTypeface(face);
            }
        }
    }

    public static boolean isNetworkConnected(Context context, boolean showToast) {
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        boolean isConnected = netInfo != null && netInfo.isConnected();
        if (!isConnected && showToast){
            Log.d("is net Con",String.valueOf(isConnected));
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();}
        return (netInfo != null && netInfo.isConnected());
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    public static Uri getOutputMediaFileUri(int type) {
        File file = getOutputMediaFile(type);
        Uri uri = null;
        if (file != null) {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(Config.IMAGE_DIRECTORY_NAME, "Oops! Failed create " + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == Config.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else if (type == Config.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static Bitmap getExifBitmap(Bitmap bitmap, String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            Log.e("joice", "cannot read exif", ex);
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
            Matrix m = new Matrix();
            m.preRotate(degree);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        }
        return bitmap;
    }

    public static File getVideoCacheDir(Context context) {
        return new File(context.getExternalCacheDir(), "video-cache");
    }

    public static void cleanVideoCacheDir(Context context) throws IOException {
        File videoCacheDir = getVideoCacheDir(context);
        cleanDirectory(videoCacheDir);
    }

    private static void cleanDirectory(File file) throws IOException {
        if (!file.exists()) {
            return;
        }
        File[] contentFiles = file.listFiles();
        if (contentFiles != null) {
            for (File contentFile : contentFiles) {
                delete(contentFile);
            }
        }
    }

    private static void delete(File file) throws IOException {
        if (file.isFile() && file.exists()) {
            deleteOrThrow(file);
        } else {
            cleanDirectory(file);
            deleteOrThrow(file);
        }
    }

    private static void deleteOrThrow(File file) throws IOException {
        if (file.exists()) {
            boolean isDeleted = file.delete();
            if (!isDeleted) {
                throw new IOException(String.format("File %s can't be deleted", file.getAbsolutePath()));
            }
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String pathToFile,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathToFile, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathToFile, options);
    }

    public static String getDateByTimestamp(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        SimpleDateFormat month_date = new SimpleDateFormat("MMM dd, yyyy");
        String month_name = month_date.format(cal.getTime());

        return month_name;
    }

    public static String formatdate(String date) {
        String datetime = null;
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat d = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date convertedDate = inputFormat.parse(date);
            datetime = d.format(convertedDate);

        } catch (ParseException e) {

        }
        return datetime;

    }

    public static String getMonthName(int num) {
        String month = "";
        switch (num) {
            case 1:
                month = "Jan";
                break;
            case 2:
                month = "Feb";
                break;
            case 3:
                month = "Mar";
                break;
            case 4:
                month = "Apr";
                break;
            case 5:
                month = "May";
                break;
            case 6:
                month = "Jun";
                break;
            case 7:
                month = "Jul";
                break;
            case 8:
                month = "Aug";
                break;
            case 9:
                month = "Sep";
                break;
            case 10:
                month = "Oct";
                break;
            case 11:
                month = "Nov";
                break;
            case 12:
                month = "Dec";
                break;

        }
        return month;
    }

    public static void showMediaSelectionDialog(final Activity activity, final int mediaType, final String ROLE_ID) {
        AppPreferences pref;
        pref = AppPreferences.getAppPreferences(activity);
        pref.putStringValue(AppPreferences.ROLE_ID, ROLE_ID);
        // todayRoleDetailsDTO1 = todayRoleDetailsDTO;
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request permission to save videos in external storage
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 6969);
        } else {
            File saveDir = null;
            File saveDir1 = null;

            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Only use external storage directory if permission is granted, otherwise cache directory is used by default
                saveDir = new File(Environment.getExternalStorageDirectory(), "MyRoleApp/Videos");
                if (!saveDir.exists())
                    saveDir.mkdirs();
                saveDir1 = new File(Environment.getExternalStorageDirectory(), "MyRoleApp/Images");
                if (!saveDir1.exists())
                    saveDir1.mkdirs();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            final CharSequence[] options;
            if (mediaType == Config.MEDIA_TYPE_IMAGE) {
                builder.setTitle("Add Photo !");
                options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
            } else if (mediaType == Config.MEDIA_TYPE_VIDEO) {
                builder.setTitle("Add Video !");
                options = new CharSequence[]{"Record Video", "Choose from Gallery", "Cancel"};
            } else {
                builder.setTitle("Select Media !");
//                options = new CharSequence[]{"Take Photo", "Record Video", "Photo from Gallery", "Video from Gallery", "Cancel"};
                options = new CharSequence[]{"Take Photo",  "Photo from Gallery",  "Cancel"};
            }

            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    Log.v("Item", item + "");
                    if (options[item].equals("Take Photo")) {
                        launchCamera(activity, Config.MEDIA_TYPE_IMAGE, ROLE_ID);
//                        if (ROLE_ID.equals("0")){
//                            launchCamera(activity, Config.MEDIA_TYPE_IMAGE,ROLE_ID);
//                        }else {
//
//                            Handler handler = new Handler();
//                            launchCamera(activity, Config.MEDIA_TYPE_IMAGE,ROLE_ID);
//                            handler.postDelayed(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    activity.onBackPressed();
//
//                                }
//                            }, 3
//                            );//end
//                        }


                    } else if (options[item].equals("Record Video")) {
                        launchCamera(activity, Config.MEDIA_TYPE_VIDEO, ROLE_ID);
//                    recordVideo(activity);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    } else {
                        boolean hasReadStoragePermission = ContextCompat.checkSelfPermission(
                                activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                        if (!hasReadStoragePermission) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                            return;
                        }
                        if (mediaType == Config.MEDIA_TYPE_IMAGE || options[item].equals("Photo from Gallery")) {
//
//                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        intent.setType("image/*");
//                      activity.startActivityForResult(intent, Config.GALLERYIMAGE);
                            // Toast.makeText(activity, "hello", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            activity.startActivityForResult(intent, Config.GALLERYIMAGE);
//                            Intent intent = new Intent(activity, ImageCropActivity.class);
//                            intent.putExtra("activity", "gallery");
//                            activity.startActivityForResult(intent, Config.REQUEST_IMAGE_CROP);
//                        activity.startActivityForResult(intent, Config.REQUEST_IMAGE_CROP);
                        } else if (mediaType == Config.MEDIA_TYPE_VIDEO || options[item].equals("Video from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                            activity.startActivityForResult(intent, Config.REQUEST_VIDEO_GALLERY);

                        }

                    }
                }
            });
            builder.show();
        }
    }

    public static void launchCamera(final Activity activity, int mediaType, String Role_id) {

        boolean hasCameraPermission = ContextCompat.checkSelfPermission(
                activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasReadStoragePermission = ContextCompat.checkSelfPermission(
                activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        boolean hasAudioRecordPermission = ContextCompat.checkSelfPermission(
                activity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        if (mediaType == Config.MEDIA_TYPE_IMAGE) {
            if (!hasCameraPermission || !hasReadStoragePermission) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return;
            }
        } else {
            if (!hasCameraPermission || !hasReadStoragePermission || !hasAudioRecordPermission) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO,}, 4);
                return;
            }
        }

        Config.CAMERAFILEURI = null;
        Config.CAMERAFILEURI = Utils.getOutputMediaFileUri(mediaType);
        if (mediaType == Config.MEDIA_TYPE_IMAGE) {
            final int REQUEST_CAMERA = 1002;
            if (Role_id.equals("0")) {
                Intent intent = new Intent(activity, CameraActivity.class);
                intent.putExtra(Configuration.MEDIA_ACTION, Configuration.MEDIA_ACTION_PHOTO);
                activity.startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            } else {
                Intent intent = new Intent(activity, CameraActivity.class);
                intent.putExtra(Configuration.MEDIA_ACTION, Configuration.MEDIA_ACTION_PHOTO);
                activity.startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        } else if (mediaType == Config.MEDIA_TYPE_VIDEO) {
            Intent intent = new Intent(activity, VideoActivity.class);
            activity.startActivityForResult(intent, Config.REQUEST_RECORD_VIDEO);
        }

    }

    //    public void openVideoRecorder(Activity activity, File videoFile, File thumbnailFile)
//    {
//        FFmpegRecorderActivityParams.Builder paramsBuilder =
//                FFmpegRecorderActivityParams.builder(activity)
//                        .setVideoOutputFileUri(videoFile)
//                        .setVideoThumbnailOutputFileUri(thumbnailFile);
//
//        paramsBuilder.recorderParamsBuilder()
//                .setVideoSize(new ImageSize(360, 360))
//                .setVideoCodec(EncoderParamsI.VideoCodec.H264)
//                .setVideoBitrate(800000)
//                .setVideoFrameRate(30)
//                .setVideoImageFit(ImageFit.FILL)
//                .setVideoImageScale(ImageScale.DOWNSCALE)
//                .setShouldCropVideo(true)
//                .setShouldPadVideo(true)
//                .setVideoCameraFacing(CameraControllerI.Facing.BACK)
//                .setAudioCodec(EncoderParamsI.AudioCodec.AAC)
//                .setAudioSamplingRateHz(44100)
//                .setAudioBitrate(64000)
//                .setAudioChannelCount(1)
//                .setOutputFormat(EncoderParamsI.OutputFormat.MP4);
//
//        paramsBuilder.interactionParamsBuilder()
//                .setMinRecordingMillis((int) TimeUnit.SECONDS.toMillis(MIN_TIME))
//                .setMaxRecordingMillis((int) TimeUnit.SECONDS.toMillis(MAX_TIME));
//
//
//        Intent intent = new Intent(activity, FFmpegRecorderActivity.class);
//        intent.putExtra(FFmpegRecorderActivity.REQUEST_PARAMS_KEY, paramsBuilder.build());
//        activity.startActivityForResult(intent, Config.REQUEST_RECORD_VIDEO);
//    }
    public static void performCrop(Activity activity, Uri picUri) {

       /* try {

            Config.CROPPEDIMAGEURI = Uri.fromFile(new File(activity.getFilesDir(), "test.jpg"));
            CropImageIntentBuilder cropImage = new CropImageIntentBuilder(900, 900, Config.CROPPEDIMAGEURI);
            cropImage.setOutlineColor(0xFF03A9F4);
            cropImage.setSourceImage(picUri);

            activity.startActivityForResult(cropImage.getIntent(activity), Config.CROPIMAGE);

        } catch (ActivityNotFoundException anfe) {
            Toast toast = Toast.makeText(activity, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }*/
    }

    public static void showAlertDialog(Context context, String title, String message, boolean isCancelable) {

        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(isCancelable);
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {

                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showDecisionDialog(Context context, String title, String message, final AlertCallback callbackListener) {

        try {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    callbackListener.callback();
                    dialog.dismiss();
                }
            });
            alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {

                    dialog.dismiss();
                }
            });
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String timeCalculation(String startDate) {
        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //  SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        format1.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));


        Calendar cal = Calendar.getInstance();
        String endDate = format1.format(cal.getTime());
        //  String endDate2 = format2.format(cal.getTime());

        System.out.println("TodayDate_" + endDate);


        String stringDate = "";
        Date d1 = null;
        Date d2 = null;
        Date d3 = null;
        StringBuilder timeCalculated = null;
        try {
            d1 = format1.parse(startDate);
            d2 = format1.parse(endDate);

            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT).parse(startDate);
            // set date string
            long year = d2.getYear() - d1.getYear();
            if (year == 0) {

                stringDate = new SimpleDateFormat("MMM dd", Locale.US).format(date);
            } else {
                stringDate = new SimpleDateFormat("MMM dd yyyy", Locale.US).format(date).toUpperCase(Locale.ROOT);
            }
            // set time string
            String stringTime = new SimpleDateFormat("HH:mm", Locale.ROOT).format(date);
//

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();


            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            System.out.print(year + " year, ");


            System.out.print(diffDays + " days, ");
            System.out.print(diffHours + " hours, ");
            System.out.print(diffMinutes + " minutes, ");
            System.out.print(diffSeconds + " seconds.");

            if (diffDays != 0) {

                timeCalculated = new StringBuilder(stringDate);
                //   timeCalculated = new StringBuilder(.);
            } else if (diffHours != 0) {
                if (timeCalculated != null) {
                    timeCalculated.append(diffHours + "h" + stringDate);
                } else {
                    timeCalculated = new StringBuilder(diffHours + " hours ago");
                }
            } else if (diffMinutes != 0) {
                if (timeCalculated != null) {

                } else {

                    timeCalculated = new StringBuilder(diffMinutes + " min ago");
                }
            } else {
                if (timeCalculated != null) {

                } else {
                    timeCalculated = new StringBuilder("Just Now");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeCalculated.toString();
    }

    public static String dayCalculation(String startDate) {
        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        String endDate = format.format(cal.getTime());
        System.out.println("TodayDate_" + endDate);
        Date d1 = null;
        Date d2 = null;
        StringBuilder timeCalculated = null;
        try {
            d1 = format.parse(startDate);
            d2 = format.parse(endDate);

            //in milliseconds
            long diff = d1.getTime() - d2.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            System.out.print(diffDays + " days, ");
            System.out.print(diffHours + " hours, ");
            System.out.print(diffMinutes + " minutes, ");
            System.out.print(diffSeconds + " seconds.");

            if (diffDays != 0) {
                timeCalculated = new StringBuilder(diffDays + " Days Left");
            } else if (diffHours != 0) {
                if (timeCalculated != null) {
                    timeCalculated.append(diffHours + " Hours Left");
                } else {
                    timeCalculated = new StringBuilder(diffHours + " Hours Left");
                }
            } else if (diffMinutes != 0) {
                if (timeCalculated != null) {

                } else {
                    timeCalculated = new StringBuilder(diffMinutes + " Min Left");
                }
            } else {
                if (timeCalculated != null) {

                } else {
                    timeCalculated = new StringBuilder("Last day");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeCalculated.toString();
    }

    public static boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches() && phoneNumber.length() >= 10;
        }
        return false;
    }

    public static boolean validateUsing_libphonenumber(String phNumber, String name, int i) throws JSONException {

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
//        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
        Phonenumber.PhoneNumber phoneNumber = null;
//        String countryCode = phoneNumberUtil.getRegionCodeForNumber(phoneNumber);
        try {
            //phoneNumber = phoneNumberUtil.parse(phNumber, "IN");  //if you want to pass region code
            phoneNumber = phoneNumberUtil.parse(phNumber, "IN");
        } catch (NumberParseException e) {
            System.err.println(e);
        }
        boolean isValid = false;
        try {
            isValid = phoneNumberUtil.isValidNumber(phoneNumber);
        } catch (Exception e) {

        }
        if (isValid) {
            String internationalFormat = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
//            Toast.makeText(this, "Phone Number is Valid " + internationalFormat, Toast.LENGTH_LONG).show();
            System.out.println("Phone Number is Valid " + internationalFormat);

            createPhoneContactJson(name, String.valueOf(phoneNumber.getNationalNumber()), i);

            return true;
        } else {
//            Toast.makeText(this, "Phone Number is Invalid " + phoneNumber, Toast.LENGTH_LONG).show();
            System.out.println("Phone Number is Invalid " + phoneNumber);
            return false;
        }
    }

    public static void createPhoneContactJson(String name, String pNo, int i) throws JSONException {
//        Config.CONTACTS_JSON = new JSONObject();

        boolean isExist = false;

        if (Config.CONTACTS_JSON.length() > 0) {

            Iterator<String> temp = Config.CONTACTS_JSON.keys();
            while (temp.hasNext()) {
                String key = temp.next();
                try {
                    if (Config.CONTACTS_JSON.getJSONObject(key + "").getString("phone").equalsIgnoreCase(pNo)) {
                        isExist = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        if (Config.CONTACTS_JSON.length() == 0) {
            JSONObject friend;
            friend = new JSONObject();
            friend.put("name", name);
            friend.put("phone", pNo);
            Config.CONTACTS_JSON.put(i + "", friend);
        } else {
            if (!isExist) {
                JSONObject friend;
                friend = new JSONObject();
                friend.put("name", name);
                friend.put("phone", pNo);
                Config.CONTACTS_JSON.put(i + "", friend);
            }
        }

        Log.d("Log", "Contact:" + name + ".................." + pNo);

    }

    public static void hideKeybord(Context context, View view) {


        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // check if no view has focus:
        View v = ((Activity) context).getCurrentFocus();
        if (v == null)
            return;

        manager.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }

    public static void hideKeybord2(Activity activity, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);

    }

    public interface AlertCallback {
        void callback();
    }

    public static class URLSpanNoUnderline extends URLSpan {

        public static final Parcelable.Creator<URLSpanNoUnderline> CREATOR
                = new Parcelable.Creator<URLSpanNoUnderline>() {
            public URLSpanNoUnderline createFromParcel(Parcel in) {
                return new URLSpanNoUnderline(in);
            }

            public URLSpanNoUnderline[] newArray(int size) {
                return new URLSpanNoUnderline[size];
            }
        };

        public URLSpanNoUnderline(String url) {
            super(url);
        }

        URLSpanNoUnderline(Parcel in) {
            super(in);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

}
