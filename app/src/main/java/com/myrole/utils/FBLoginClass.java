package com.myrole.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Vikesh on 29-04-2016.
 */
public class FBLoginClass implements FacebookCallback<LoginResult> {
    public CallbackManager callbackManager;
    public OnFBResultListener resultListener;
    private Context context;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;


    public FBLoginClass() {



    }



    public void sdkInitialize(Context context) {
        this.context = context;
        FacebookSdk.sdkInitialize(context);
        AppEventsLogger.activateApp(context);
        prepare();
    }

    private void prepare() {
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };

        startTracking();
        LoginManager.getInstance().registerCallback(callbackManager, this);


    }

    public void setFBResultListener(OnFBResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public void login(Activity activity) {
        ArrayList<String> list = new ArrayList<>();
        list.add("public_profile");
        list.add("email");
        list.add("user_friends");
        LoginManager.getInstance().logInWithReadPermissions(activity, list);
    }

    public void startTracking() {
        if (accessTokenTracker != null && profileTracker != null) {
            accessTokenTracker.startTracking();
            profileTracker.startTracking();
        }
    }

    public void stopTracking() {
        if (accessTokenTracker != null && profileTracker != null) {
            accessTokenTracker.stopTracking();
            profileTracker.stopTracking();
        }
    }


    private void displayMessage(Profile profile) {
        if (profile != null) {

            Toast.makeText(context, profile.getName(), Toast.LENGTH_SHORT).show();
            System.out.println("Profile Name"+profile.getName());

        }
    }


    @Override
    public void onSuccess(LoginResult loginResult) {
        //Toast.makeText(context, "onSuccess", Toast.LENGTH_SHORT).show();
        final AccessToken accessToken = loginResult.getAccessToken();
        final Profile profile = Profile.getCurrentProfile();
       final String id = loginResult.getAccessToken().getUserId();
    //    Toast.makeText(context, "fb id" + id, Toast.LENGTH_SHORT).show();
        displayMessage(profile);
        /*newMeRequest for self data*/
        GraphRequest request = GraphRequest.newMyFriendsRequest(accessToken, new GraphRequest.GraphJSONArrayCallback() {
            @Override
            public void onCompleted(JSONArray objects, GraphResponse response) {
                try {
                    if (resultListener != null)
                        resultListener.onFBResult(objects,profile,id);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, email, name, gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }


    public interface OnFBResultListener {
        void onFBResult(JSONArray objects , Profile profile,String id);
    }
}
