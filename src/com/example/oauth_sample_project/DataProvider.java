package com.example.oauth_sample_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
//			JSONArray jArray = new JSONArray(result);
//			Log.d(LOG_TAG, "jObject " + jObject.toString());
//			JSONObject feed = jObject.getJSONObject("feed");
//			JSONArray jArray = feed.getJSONArray("entry");
			Log.d(LOG_TAG, "jObject.toString() " + jObject.toString());
/*			for (int i = 0; i < jArray.length(); i++) {
//				JSONObject oneObject = jArray.getJSONObject(i);
				Log.d(LOG_TAG, "jArray.opt(i).toString(); " + jArray.opt(i).toString());
//				String updated = oneObject.getJSONObject("updated").getString(
//						"$t");
//				String title = oneObject.getJSONObject("title").getString("$t");
//				String content = oneObject.getJSONObject("content").getString(
//						"$t");
//				String published = oneObject.getJSONObject("published")
//						.getString("$t");
//				String id = oneObject.getJSONObject("id").getString("$t");
//
//				Article article = new Article(title, content, published,
//						updated, id);
//
//				localArray.add(article);

			}*/

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
