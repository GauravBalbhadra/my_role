package com.myrole;//package com.myrole;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.v7.app.AppCompatActivity;
//import android.widget.Toast;
//
//import com.myrole.utils.Config;
//
//import java.io.File;
//
////import life.knowledge4.videotrimmer.K4LVideoTrimmer;
////import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;
//
//
//public class TrimmerActivity extends AppCompatActivity implements OnTrimVideoListener {
//
//    private K4LVideoTrimmer mVideoTrimmer;
//    String path = "",id="";
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_trimmer);
//
//        Intent extraIntent = getIntent();
//
//
//        if (extraIntent != null) {
//            path = extraIntent.getStringExtra(Config.EXTRA_VIDEO_PATH);
//            id = extraIntent.getStringExtra("id");
//
//        }
//
//        File saveFolder = new File(Environment.getExternalStorageDirectory(), "MyRoleApp");
//        mVideoTrimmer = ((K4LVideoTrimmer) findViewById(R.id.timeLine));
//        if (mVideoTrimmer != null) {
//            mVideoTrimmer.setMaxDuration(60);
//            mVideoTrimmer.setOnTrimVideoListener(this);
//            //mVideoTrimmer.setDestinationPath(saveFolder.toString());
//            mVideoTrimmer.setVideoURI(Uri.parse(path));
//        }
//    }
//
////    @Override
////    public void getResult(final Uri uri) {
////        runOnUiThread(new Runnable() {
////            @Override
////            public void run() {
////
////                Toast.makeText(TrimmerActivity.this, getString(R.string.video_saved_at, uri.getPath()), Toast.LENGTH_SHORT).show();
////
////            }
////        });
//
//        if (id.equals("1")){
//        Intent i = new Intent();
//        i.setAction("VIDEO_CROP_2");
//        i.putExtra("URI", uri);
//        sendBroadcast(i);
//        finish();
//        }else if (id.equals("2")){
//            Intent i = new Intent();
//            i.setAction("VIDEO_CROP_3");
//            i.putExtra("URI", uri);
//            sendBroadcast(i);
//            finish();
//        }else  if (id.equals("0")){
//            Intent i = new Intent();
//            i.setAction("VIDEO_CROP");
//            i.putExtra("URI", uri);
//            sendBroadcast(i);
//            finish();
//        }
//    }
//
//    @Override
//    public void cancelAction() {
//        mVideoTrimmer.destroy();
//        finish();
//    }
//
//}
