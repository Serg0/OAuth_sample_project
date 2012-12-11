package com.example.oauth_sample_project;

import java.io.IOException;

import org.json.JSONException;

import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class OAuth extends Activity {
	private final static String G_PLUS_SCOPE = "oauth2:https://www.googleapis.com/auth/plus.me";
	private final static String USERINFO_SCOPE = "https://www.googleapis.com/auth/userinfo.profile";
//	private final static String USERINFO_SCOPE = "";
	private final static String SCOPES = G_PLUS_SCOPE + " " + USERINFO_SCOPE;
	private int ReqCode;

	private final static String GET_REQUEST = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
	public int SOME_REQUEST_CODE = 1;
	private int MY_ACTIVITYS_AUTH_REQUEST_CODE = 2;
	private int IS_GOOGLE_PLAY_SERVICE_AVALIABLE_REQUEST_CODE = 3;

	private String accountName;
	private String accountType;

	TextView txtResult;

	public OAuth Instance;
	public static String result = "initial Token";
	private String token;

	class getTokenTask extends AsyncTask<Void, Void, String> {

		

		@Override
		protected String doInBackground(Void... params) {

			String _result = "Initial Token";
			try {
				_result = getToken();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return _result;
		}

		@Override
		protected void onPostExecute(String _result) {

			super.onPostExecute(_result);
			Log.d("getToken", _result);
			txtResult.setText(_result);
			token = _result;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_oauth);
		txtResult = (TextView) findViewById(R.id.textView1);
		Instance = this;
		ReqCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());

		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (ReqCode == ConnectionResult.SUCCESS) {
							Intent intent = AccountPicker
									.newChooseAccountIntent(null, null,
											new String[] { "com.google" },
											false, null, null, null, null);
							startActivityForResult(intent, SOME_REQUEST_CODE);
						} else {
						 Dialog alert = GooglePlayServicesUtil
									.getErrorDialog(ReqCode, Instance,
											IS_GOOGLE_PLAY_SERVICE_AVALIABLE_REQUEST_CODE);
						 alert.show();
						}

						

					}
				});

		findViewById(R.id.button2).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
//						Toast.makeText(getApplication(),
//								"Account chosen " + accountName,
//								Toast.LENGTH_LONG).show();
//						Toast.makeText(getApplication(),
//								"Account TYPE chosen " + accountType,
//								Toast.LENGTH_LONG).show();
						new Thread(new Runnable(){

							@Override
							public void run() {
								try {
									DataProvider.getFeed(GET_REQUEST, token);
								
								} catch (IOException e) {
									e.printStackTrace();
								} catch (JSONException e) {
									e.printStackTrace();
								}
								
							}
						
						}).start();
					}
				});
	}

	public String getToken() throws InterruptedException {

		String token = "pre Token " + SCOPES;

		try {
			token = GoogleAuthUtil.getToken(Instance.getApplicationContext(),
					accountName, SCOPES);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GooglePlayServicesAvailabilityException e) {
			e.printStackTrace();
			// Dialog alert = GooglePlayServicesUtil.getErrorDialog(
			// e.getConnectionStatusCode(), this, SOME_REQUEST_CODE);
			Log.d("getToken",
					"GooglePlayServicesAvailabilityException e "
							+ e.getConnectionStatusCode());
		} catch (UserRecoverableAuthException userAuthEx) {
			Log.d("getToken", "UserRecoverableAuthException userAuthEx");
			startActivityForResult(userAuthEx.getIntent(),
					MY_ACTIVITYS_AUTH_REQUEST_CODE);
			userAuthEx.printStackTrace();
		} catch (GoogleAuthException e) {
			Log.d("getToken", "GoogleAuthException e");
			e.printStackTrace();
		}

		return token;
	}

	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		if (requestCode == SOME_REQUEST_CODE && resultCode == RESULT_OK) {
			accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
			accountType = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
			Toast.makeText(getApplication(), "Account chosen " + accountName,
					Toast.LENGTH_LONG).show();
			getTokenTask _getTokenTask = new getTokenTask();
			_getTokenTask.execute();
		}
		if (requestCode == MY_ACTIVITYS_AUTH_REQUEST_CODE) {
			getTokenTask _getTokenTask = new getTokenTask();
			_getTokenTask.execute();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_oauth, menu);
		return true;
	}

}
