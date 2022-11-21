package com.ucc.helloapp.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Meiqi Huang
 * @date 2022-11-06
 * Send the List of AudioInfo value stored in AudioInfoPool to server every 60s.
 * Using Http protocol and Post method. If Successfully sent the data,
 * it would call init() to refresh AudioInfoPool's list
 */
public class SendThread extends Thread {

    public static final String RESPONSE = "response";

    public void run() {

        while (status == Status.STATUS_START) {
            AudioInfoPool audio_info_pool = AudioInfoPool.getInstance();
            if (audio_info_pool != null) {
                String s = gson.toJson(audio_info_pool.getAudioInfoArrayList());
                RequestBody requestBody = RequestBody.create(JSON, s);
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    Log.d(RESPONSE, response.headers().toString());
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    audio_info_pool.init();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                status = Status.STATUS_STOP;
            }
        }

    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final String url = "http://192.168.88.3:8080/send";
    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();
    Status status = Status.STATUS_PREPARE;

}
