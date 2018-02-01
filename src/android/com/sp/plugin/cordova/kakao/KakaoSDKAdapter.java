package com.sp.plugin.cordova.kakao;

import android.app.Activity;
import android.content.Context;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;

/**
 * Class KakaoSDKAdapter
 *
 */
public class KakaoSDKAdapter extends KakaoAdapter {

	private Activity currentActivity;

	public KakaoSDKAdapter(Activity currentActivity){
		this.currentActivity = currentActivity;
	}

	@Override
	public ISessionConfig getSessionConfig() {

		return new ISessionConfig() {
			
			@Override
			public AuthType[] getAuthTypes() {
				return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
			}

			@Override
			public boolean isUsingWebviewTimer() {
				return false;
			}

			@Override
			public boolean isSecureMode() {
				return false;
			}

			@Override
			public ApprovalType getApprovalType() {
				return ApprovalType.INDIVIDUAL;
			}

			@Override
			public boolean isSaveFormData() {
				return true;
			}
		};
	}

	@Override
	public IApplicationConfig getApplicationConfig() {
		return new IApplicationConfig() {
			@Override
			public Context getApplicationContext() {
				return currentActivity.getApplicationContext();
			}
		};
	}
}