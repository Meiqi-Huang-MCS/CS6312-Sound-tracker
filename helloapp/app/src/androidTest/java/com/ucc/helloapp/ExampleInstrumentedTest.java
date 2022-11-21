package com.ucc.helloapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ucc.helloapp.service.AudioInfo;
import com.ucc.helloapp.service.SendThread;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final String url="http://172.20.10.2:8080/send";
    OkHttpClient client=new OkHttpClient();

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.ucc.helloapp", appContext.getPackageName());
    }

    @Test
    public void send() throws IOException {
        AudioInfo audioInfo = AudioInfo.getInstance();
        Gson gson=new Gson();
        String s = gson.toJson(audioInfo);
        RequestBody requestBody = RequestBody.create(JSON,s);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try   {
            Response response = client.newCall(request).execute();
            Log.d("response",response.headers().toString());
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void send2() throws IOException {
         String url="http://www.google.com";

        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
           Log.d("response",response.headers().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}