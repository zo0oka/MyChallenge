package com.zo0okadev.mylibrary;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

class TokenUtils {

    private static final String TAG = TokenUtils.class.getSimpleName();

    static void sendTokenToServer(final String token) {
        Executors.newFixedThreadPool(1).execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("platform", 1);
                    jsonObject.put("token", token);

                    URL url = new URL("http://api.pushbots.com/2/subscriptions");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("x-pushbots-appid", "5d258e58b7941208c73fcfb7");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    OutputStream outputStream = connection.getOutputStream();
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                    dataOutputStream.writeBytes(jsonObject.toString());
                    int resultCode = connection.getResponseCode();
                    Log.d(TAG, "run: ResponseCode: " + resultCode);
                    outputStream.flush();
                    outputStream.close();

                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line.trim());
                    }

                    Log.d(TAG, "run: Response: " + stringBuilder.toString());

                    JSONObject response = new JSONObject(stringBuilder.toString());

                    if (response.has("_id")) {
                        Log.d(TAG, "run: Response: ID: " + response.getString("_id"));
                    }
                    if (response.has("inserted")) {
                        Log.d(TAG, "run: Response: Inserted: " + response.getBoolean("inserted"));
                    }
                    if (response.has("error")) {
                        Log.d(TAG, "run: Response: Error: " + response.getString("error"));
                    }

                    inputStream.close();
                    connection.disconnect();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
