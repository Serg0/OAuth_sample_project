package com.example.oauth_sample_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.SQLException;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

public class DataProvider {

	private static final String LOG_TAG = "DataProvider";

	public static LinkedHashMap<String, String> getFeed(String _url,
			String token) throws NetworkOnMainThreadException, IOException,
			JSONException {
		long time = System.currentTimeMillis();
		Log.d(LOG_TAG, "Starting to download " + time);
		URL url = new URL(_url + token);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		LinkedHashMap<String, String> map;
		map = readStream(con.getInputStream());

		return map;

	}

	private static LinkedHashMap<String, String> readStream(InputStream in)
			throws JSONException {
		LinkedHashMap<String, String> localArray = new LinkedHashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			String result = sb.toString();

			JSONObject jObject = new JSONObject(result);

			String name;
			String value;
			Iterator<?> keys = jObject.keys();
			while (keys.hasNext()) {
				name = keys.next().toString();
				value = jObject.getString(name).toString();
				localArray.put(name, value);
				Log.d(LOG_TAG, "name: " + name + ", value: " + value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return localArray;
	}
}
