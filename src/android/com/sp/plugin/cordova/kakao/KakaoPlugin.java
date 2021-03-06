/**
 */
package com.sp.plugin.cordova.kakao;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class KakaoPlugin extends CordovaPlugin {

    private static final String TAG = "KakaoPlugin";
    private static volatile Activity currentActivity;
    private SessionCallback callback;
    private static boolean initialized = false;


    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        Log.d(TAG, "Initializing KakaoPlugin");
        currentActivity = this.cordova.getActivity();
        if (!initialized){
            KakaoSDK.init(new KakaoSDKAdapter(currentActivity));
            initialized = true;
        }
    }

    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

        cordova.setActivityResultCallback(this);
        callback = new SessionCallback(callbackContext);
        Session.getCurrentSession().addCallback(callback);
        // Session.getCurrentSession().checkAndImplicitOpen(); //???

        boolean result = false;
        if (action.equals("login")) {

            this.login();
            result = true;

        } else if (action.equals("logout")) {

            this.logout(callbackContext);
            result = true;

        } else if (action.equals("share")) {

            this.share();
            result = true;

        }

        // Session.getCurrentSession().removeCallback(callback);
        return result;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.v(TAG, "kakao : onActivityResult : " + requestCode + ", code: " + resultCode);
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, intent)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }


    private void login() {

        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Session.getCurrentSession().open(AuthType.KAKAO_TALK, currentActivity);
            }
        });

    }

    private void logout(final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {

            @Override
            public void run() {
                UserManagement.requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        callbackContext.success();
                    }
                });
            }
        });
    }


    private void share() {

    }


    /**
     * Result
     *
     * @param userProfile
     */
    private JSONObject handleResult(UserProfile userProfile) {
        Log.v(TAG, "kakao : handleResult");

        System.out.println(userProfile);

        JSONObject response = new JSONObject();
        try {
            response.put("id", userProfile.getId());
            response.put("nickname", userProfile.getNickname());
            response.put("profile_image", userProfile.getProfileImagePath());
            response.put("thumbnail_image", userProfile.getThumbnailImagePath());
            response.put("email", userProfile.getEmail());


        } catch (JSONException e) {
            Log.v(TAG, "kakao : handleResult error - " + e.toString());
        }
        return response;
    }


    /**
     * Class SessonCallback
     */
    private class SessionCallback implements ISessionCallback {

        private CallbackContext callbackContext;

        public SessionCallback(final CallbackContext callbackContext) {
            this.callbackContext = callbackContext;
        }

        @Override
        public void onSessionOpened() {

            Log.v(TAG, "kakao : SessionCallback.onSessionOpened");

            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    callbackContext.error("kakao : SessionCallback.onSessionOpened.requestMe.onFailure - " + errorResult);
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.v(TAG, "kakao : SessionCallback.onSessionOpened.requestMe.onSessionClosed - " + errorResult);
                    Session.getCurrentSession().checkAndImplicitOpen();
                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    Log.v(TAG, "kakao : SessionCallback.onSessionOpened.requestMe.onSuccess - " + userProfile);
                    callbackContext.success(handleResult(userProfile));
                }

                @Override
                public void onNotSignedUp() {
                    callbackContext.error("this user is not signed up");
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Log.v(TAG, "kakao : onSessionOpenFailed" + exception.toString());
            }
        }
    }

}
