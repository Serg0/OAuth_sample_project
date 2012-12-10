package com.example.oauth_sample_project;

import java.io.IOException;

import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class OAuth extends Activity {
	private final static String G_PLUS_SCOPE = "oauth2:https://www.googleapis.com/auth/plus.me";
	private final static String USERINFO_SCOPE = "https://www.googleapis.com/auth/userinfo.profile";
	private final static String SCOPES = G_PLUS_SCOPE + " " + USERINFO_SCOPE;
	private int ReqCode;

	public int SOME_REQUEST_CODE = 1;

	private String accountName;

	private String accountType;
	private String token;

	TextView txtResult;
	
	public OAuth Instance;
	public String result = "initial Token";
	class getTokenTask extends AsyncTask<Void, Void, String> {
		
		@Override
		protected String doInBackground(Void... noargs) {
//			try {
//				result = getToken();
//				txtResult.setText(result);
//				return result;
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			result = "No Token";
			return result;
		}

		@Override
		protected void onPostExecute(String _result) {
			txtResult.setText(_result);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_oauth);
		TextView txtResult = (TextView) findViewById(R.id.textView1);
 Instance = this;
		ReqCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());

		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						Toast.makeText(getApplication(),
								Integer.toString(ReqCode), Toast.LENGTH_LONG)
								.show();

						Intent intent = AccountPicker.newChooseAccountIntent(
								null, null, new String[] { "com.google" },
								false, null, null, null, null);
						startActivityForResult(intent, SOME_REQUEST_CODE);
						// Toast.makeText(getApplication(), "Account chosen " +
						// accountName, Toast.LENGTH_LONG).show();
						// Toast.makeText(getApplication(),
						// "Account TYPE chosen " + accountType,
						// Toast.LENGTH_LONG).show();

					}
				});

		findViewById(R.id.button2).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(getApplication(),
								"Account chosen " + accountName,
								Toast.LENGTH_LONG).show();
						Toast.makeText(getApplication(),
								"Account TYPE chosen " + accountType,
								Toast.LENGTH_LONG).show();

//						try {
//							Toast.makeText(getApplication(),
//									"Token " + getToken(), Toast.LENGTH_LONG)
//									.show();
							getTokenTask _getTokenTask = new getTokenTask();
							_getTokenTask.execute();
							
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}

					}
				});
	}

	public String getToken() throws InterruptedException {

		
		 Runnable runnable = new Runnable() {
		
		 @Override
		 public void run() {
		try {
			token = GoogleAuthUtil.getToken(Instance.getApplicationContext(),
					accountName, SCOPES);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GoogleAuthException e) {
			e.printStackTrace();
		}

		 }};
		 Thread thread;
		 thread = new Thread(runnable);
		 thread.start();
		 thread.join();

		return token;
	}

	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		if (requestCode == SOME_REQUEST_CODE && resultCode == RESULT_OK) {
			accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
			accountType = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_oauth, menu);
		return true;
	}

}
