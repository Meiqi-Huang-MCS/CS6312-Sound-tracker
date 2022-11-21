package com.ucc.helloapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.ucc.helloapp.service.AudioClipRecorder;
import com.ucc.helloapp.service.AudioInfo;
import com.ucc.helloapp.service.AudioInfoPool;
import com.ucc.helloapp.service.FindLocation;
import com.ucc.helloapp.service.SendThread;
import com.ucc.helloapp.service.Status;

import java.io.IOException;

/**
 * @author Meiqi Huang
 * @date 2022-11-06
 *
 * When switch turns on, it calls Countdown() and Countdown2() method.
 * Countdown2 Method countdown the time to stop and restart the record. (every 20s)
 *  *  everytime Start record, FindLocation would update new Location value in AudioInfo instance.
 *  *  everytime Stop record, AudioCLipRecorder would extract the recording file's average volume and noise rate value and update them in AudioInfo.
 *  *  Mainactivity then would call AudioInfoPool.add(AudioInfo) method to store and refresh the AudioInfo's value.
 * Countdown Method countdown the time to sent the AudioInfo list to server.(every 60s)
 *  *  it calls SendThread.run() to send AudioInfoPool.ArrayList<AudioInfo> to server.
 *  *  If sent successfully, SendThread would refresh the AudioInfoPool's list.Otherwise, the AudioInfoPool still maintains the former list.
 */
public class MainActivity extends AppCompatActivity {

    public static final String AUDIO = "Audio";
    public static final int SENT_LIST_COUNTDOWN = 60000;
    public static final int REFRESH_RECORDING_COUNTDOWN = 20000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_main);
        {
            Switch sswitch = (Switch) findViewById(R.id.startapp);
            sswitch.setChecked(false);
            sswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b == true) {
                        state = Status.STATUS_PREPARE;
                        SwitchTurnedOn();
                    } else {
                        state = Status.STATUS_END;
                        SwitchTurnedOff();
                    }
                }
            });
        }

    }

    private void init() {
        count = 0;
        audioInfo = AudioInfo.getInstance();
        try {
            audioClipRecorder = new AudioClipRecorder(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SwitchTurnedOn() {
        state = Status.STATUS_PREPARE;
        Button appState = findViewById(R.id.appstate);
        appState.setText("ON");
        Button dayCountdown = findViewById(R.id.daycountdown);
        dayCountdown.setText("60");
        Countdown(dayCountdown, SENT_LIST_COUNTDOWN, 1);
        Button storeFileCountdown = findViewById(R.id.storefilecountdown);
        storeFileCountdown.setText("20");
        Countdown2(storeFileCountdown, REFRESH_RECORDING_COUNTDOWN, 1);
        GetLocation();
    }

    public void SwitchTurnedOff() {

        Button appState = findViewById(R.id.appstate);
        appState.setText("OFF");
        Button dayCountdown = findViewById(R.id.daycountdown);
        dayCountdown.setText("0");
        Button storeFileCountdown = findViewById(R.id.storefilecountdown);
        storeFileCountdown.setText("0");
        end();

    }

    private void GetLocation() {
        findLocation = new FindLocation(this);
        if (findLocation.canGetlocation()) {
            double latitude = findLocation.getLatitude();
            double longtitude = findLocation.getLongitude();
            Button location = findViewById(R.id.location);
            location.setText(latitude + "," + longtitude);
            audioInfo.setLatitude(latitude);
            audioInfo.setLongitude(longtitude);
            Log.d(AUDIO,"Update Location Of AudioInfo: "+ audioInfo.toString());
        } else {
            findLocation.Alert();
        }
    }

    private void SendAudioData() {
        sendThread = null;
        sendThread = new SendThread();
        sendThread.setStatus(Status.STATUS_START);
        Log.d(AUDIO, "Sent AudioInfoPool: \n" + AudioInfoPool.getInstance().toString()+" To Server");
        sendThread.start();
    }

    private void end() {
        Log.d(AUDIO, "Status.STATUS_END");
        state = Status.STATUS_END;
        audioClipRecorder.stopRecord();
        audioClipRecorder.end_recording();
        count = 0;
        sendThread = null;

    }

    private Status state = Status.STATUS_PREPARE;
    private AudioClipRecorder audioClipRecorder;
    private int count;
    private AudioInfo audioInfo;
    private FindLocation findLocation;
    SendThread sendThread;

    public void Countdown(Button view, long millisInFuture, long countDownInterval) {

        new CountDownTimer(millisInFuture, countDownInterval) {

            public void onTick(long millisUntilFinished) {

                if (state == Status.STATUS_START)
                    view.setText(millisUntilFinished / 1000 + "");
                else if (state == Status.STATUS_END) {
                    this.cancel();
                }

            }

            public void onFinish() {
                if (state != Status.STATUS_END) {
                    SendAudioData();
                    Countdown(view, millisInFuture, countDownInterval);
                }
            }
        }.start();
    }

    public void Countdown2(Button view, long millisInFuture, long countDownInterval) {

        if (state == Status.STATUS_PREPARE) {
            audioClipRecorder.startRecord();
            state = Status.STATUS_START;
        }

        new CountDownTimer(millisInFuture, countDownInterval) {


            public void onTick(long millisUntilFinished) {

                if (state == Status.STATUS_START)
                    view.setText(millisUntilFinished / 1000 + "");
                else if (state == Status.STATUS_END) {
                    this.cancel();
                }

            }

            public void onFinish() {

                if (state != Status.STATUS_END) {
                    RefreshRecord();
                    GetLocation();
                    Countdown2(view, millisInFuture, countDownInterval);
                }

            }
        }.start();
    }

    private void RefreshRecord() {
        count++;
        Log.d(AUDIO, "Complete NO." + count + " Recording");
        audioClipRecorder.stopRecord();
        Log.d(AUDIO, "Save AudioInfo Into AudioInfoPool " + AudioInfo.getInstance().toString());
        AudioInfoPool audioInfoPool = AudioInfoPool.getInstance();
        audioInfoPool.addAudioInfo();
        Log.d(AUDIO, "Refresh AudioInfo " + AudioInfo.getInstance().toString());
        Log.d(AUDIO, "Start NO." + (count + 1) + " Recording");
        audioClipRecorder.startRecord();

    }
}