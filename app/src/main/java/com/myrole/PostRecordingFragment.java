package com.myrole;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.myrole.dashboard.frags.DashFeedFragment;
import com.myrole.databinding.FragmentPostRecordingBinding;
import com.myrole.networking.UploadService;
import com.myrole.networking.UploadServiceClient;
import com.myrole.networking.UploadServiceResponse;
import com.myrole.utilities.MergeMediaEvent;
import com.myrole.utilities.MergeMediaMP4Parser;
import com.myrole.utils.AppPreferences;
import com.v9kmedia.v9krecorder.utils.V9krecorderutil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.myrole.utils.ActivityTransactions.getDashboard;

public class PostRecordingFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, Callback<UploadServiceResponse> {

    private static final String TAG = "PostRecordingFragment";
    private static final Boolean DEBUG = true;

    private ProgressDialog progressDialog;

    private FragmentPostRecordingBinding fragmentPostRecordingBinding;
    private EditText mDescriptionText;
    private Handler handler1 = new Handler();

    public static boolean isPost = false;
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMerge(MergeMediaEvent event) {
        if(DEBUG) Log.d(TAG, "onMergeEvent: " + event.getResponsePath());
        UploadVideo(event.getResponsePath());
    }

    int pStatus = 0;
    private void UploadVideo(String videoToUpload) {

        startProgress();
        //new
        isPost = true;
        RequestBody userId = createPartFromString(AppPreferences.getAppPreferences(getContext()).getStringValue(AppPreferences.USER_ID));

        MultipartBody.Part body = prepareFilePart("video", videoToUpload);

        RequestBody description = createPartFromString(mDescriptionText.getText().toString());

        RequestBody roleId = createPartFromString("0");

        HashMap<String, RequestBody> map = new HashMap<>();

        map.put("user_id", userId);
        map.put("description", description);
        map.put("role_id", roleId);

        final Retrofit retrofit = UploadServiceClient.getRetrofitClient();

        final UploadService uploadService = retrofit.create(UploadService.class);

        Call<UploadServiceResponse> call = uploadService.uploadVideo(map, body);

        call.enqueue(this);

        getDashboard(getContext());
        getActivity().finish();
    }

    private void startProgress() {
        progressDialog = new ProgressDialog(getContext(), R.style.MyTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading post");
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.show();
    }

    private void endProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private RequestBody createPartFromString(String s) {
        return RequestBody.create(MediaType.parse("text/plain"), s);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPostRecordingBinding = FragmentPostRecordingBinding.inflate(inflater, container, false);
        return fragmentPostRecordingBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

    }

    private void init() {

        try {

            Glide.with(getContext()).asBitmap().load(Uri.fromFile(new File(V9krecorderutil.mediaMap.get("video")))).into(fragmentPostRecordingBinding.ivSongImage);
        } catch (Exception e){

        }

        mDescriptionText = fragmentPostRecordingBinding.ivDescText;

        fragmentPostRecordingBinding.swMerge.setOnCheckedChangeListener(this);
        fragmentPostRecordingBinding.btnPost.setOnClickListener(this);
        fragmentPostRecordingBinding.tvSelectPhoto.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPost:
                mergeAndPost();
                break;
            case R.id.tvSelectPhoto:
                Toast.makeText(getContext(), "Select Thumbnail clicked", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void mergeAndPost() {
        if(V9krecorderutil.mediaMap.containsKey("remoteaac")) {
            new MergeMediaMP4Parser().execute(
                    V9krecorderutil.mediaMap.get("remoteaac"),
                    V9krecorderutil.mediaMap.get("video"),
                    V9krecorderutil.createStreamPath(Environment.DIRECTORY_MOVIES, "merged")
            );
        } else {
            UploadVideo(V9krecorderutil.mediaMap.get("video"));
        }

    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String videoPath) {

        File videoFile = new File(videoPath);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("video/mp4"),
                        videoFile
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, videoFile.getName(), requestFile);
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_merge:
                mergeSw();
                break;
            case R.id.sw_download:
                downloadSw();
                break;
        }
    }

    private void downloadSw() {

    }

    private void mergeSw() {

    }

    @Override
    public void onResponse(Call<UploadServiceResponse> call, Response<UploadServiceResponse> response) {
        if(response.body() != null) {
            Log.d(TAG,"upload response: " + response.body().isStatus() + " message: " + response.body().getMessage());
            ArrayList<String> value = new ArrayList<>();

            value.add(V9krecorderutil.mediaMap.get("video"));
            value.add(V9krecorderutil.mediaMap.get("audio"));
            value.add(V9krecorderutil.mediaMap.get("merged"));

            //Intent intent = new Intent();
            //intent.putStringArrayListExtra("records", value);
            //getActivity().setResult(Activity.RESULT_OK, intent);
            isPost = false;
            endProgress();
            //Toast.makeText(getContext(), "Uploaded successfully", Toast.LENGTH_LONG).show();
           // circularProgress1.setVisibility(View.GONE);
            //getActivity().finish();
        } else {
            isPost = false;
            //Toast.makeText(getContext(), "Failed!!\nPlease try again. ", Toast.LENGTH_LONG).show();
            //circularProgress1.setVisibility(View.GONE);
            //getActivity().finish();
        }
    }

    @Override
    public void onFailure(Call<UploadServiceResponse> call, Throwable t) {
        isPost = false;
        //circularProgress1.setVisibility(View.GONE);
        //Toast.makeText(getContext(), "Upload failed, please try again", Toast.LENGTH_LONG).show();
        //getActivity().finish();
    }

}
