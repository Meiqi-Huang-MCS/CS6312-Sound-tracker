package com.ucc.helloapp.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Meiqi Huang
 * @Date: 2022-11-06
 * Responsible for Start Recording, Stop Recording, Refresh recording file, and Assess Recording Noise Rate by calling NoiseDetector.
 * Start new record and stop every 20s, when Start, it would create a new empty file at internal directory. Then create a new AudioRecord object and call AudioRecord.startRecording method.
 * Meanwhile, it starts RecordingRunnable Thread to write buffer data to file at the same time.
 * when Stop, it change the status and thread stop writing data into file. It then calls NoiseDetector to analyse Recording File's Noise Rate. It would then update NoiseRate and CurrentVolume properties in AudioInfo.
 * When start to record again, it would empty the recording file and call RecordingRunnable thread to write the stream data to file again.
 * The thread then would continue to read the buffer data and write it to file when recording.
 *
 */
public class AudioClipRecorder {

    public static final String AUDIO = "AudioClipRecorder";

    /**
     * start recording, save data , write in pcm file.
     */
    @SuppressLint("MissingPermission")
    public void startRecord() {

        if (audioRecord == null) {
            Log.d(AUDIO, "Prepare Recording");
            audioRecord = new AudioRecord(AUDIO_INPUT, AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING, BUFFER_SIZE);

        }

        if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
            Log.d(AUDIO, "Start Recording");
            audioRecord.startRecording();
            recordingInProgress.set(true);
            status = Status.STATUS_START;
            if (recordingRunnable.getState() == Thread.State.NEW) {
                synchronized (recordingRunnable) {
                    Log.d(AUDIO, "RecordingRunnable Thread Start Saving File");
                    recordingRunnable.start();
                }
            } else if (recordingRunnable.getState() == Thread.State.RUNNABLE) {
                Log.d(AUDIO, "RecordingRunnable Thread Start Saving File");
            }

        }else{
            Log.d(AUDIO, "Start Recording Failed, Check Permission And Virtual Host Input");
        }
    }

    private class RecordingRunnable extends Thread {

        @Override
        public void run() {
            final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
            try {
                while (recordingInProgress.get()) {
                    if (status == Status.STATUS_START) { //从音频硬件读取音频数据
                        if (outputStream == null) {
                            outputStream = new FileOutputStream(file);
                        }

                        int result = audioRecord.read(buffer, BUFFER_SIZE);
                        if (result > 0) {
                            outputStream.write(buffer.array(), 0, BUFFER_SIZE);
                            buffer.clear();
                        }
                    } else if (status == Status.STATUS_STOP) {
                        if (outputStream != null) {
                            outputStream.close();
                            outputStream = null;
                        }
                    }
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }

    /**
     * stop recoring, fetch file and rate the noise.
     */
    public void stopRecord() {

        Log.d(AUDIO, "Stop Record");
        if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
            status = Status.STATUS_STOP;
            audioRecord.stop();
        }
        byte[] data = new byte[BUFFER_SIZE];
        short[] data2 = new short[BUFFER_SIZE/2];
        try {
           assess_radio_noise(data, data2);
            empty_file();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void assess_radio_noise(byte[] data, short[] data2) throws IOException {
        boolean heard = false;
        Log.d(AUDIO, "Read Recording File Into Short[] array");
        FileInputStream fileInputStream = new FileInputStream(file);
        // reads all bytes at a time, if end of the file, returns -1
        while (fileInputStream.read(data) != -1) {
            ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(data2);
        }
        Log.d(AUDIO, "Calculate Current Volume And Assess Radio Noise");
        noiseDetector.heard(data2, AUDIO_SAMPLE_RATE);
        fileInputStream.close();
    }

    public AudioClipRecorder(Context context) throws IOException {
        recordingRunnable = new RecordingRunnable();
        noiseDetector = new NoiseDetector();
        recordingInProgress = new AtomicBoolean(false);
        status = Status.STATUS_PREPARE;
        this.context = context;
        empty_file();
    }

    private void empty_file() throws IOException {
        file = new File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "recording.pcm");
        Log.d("FileLoaction",context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).toString());
        if (file.exists()) file.delete();
        file.createNewFile();
        file.setReadable(true);
        file.setWritable(true);
    }

    public void end_recording(){
//        audioRecord.release();
    }

    /*
* 实现的流程
音频源：可以使用麦克风作为采集音频的数据源。
采样率：一秒钟对声音数据的采样次数，采样率越高，音质越好。
音频通道：单声道，双声道等，
音频格式：一般选用PCM格式，即原始的音频样本。
缓冲区大小：音频数据写入缓冲区的总数，可以通过AudioRecord.getMinBufferSize获取最小的缓冲区。（将音频采集到缓冲区中然后再从缓冲区中读取）。
* */
    public final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;
    // 采样率
    // 44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    // 采样频率一般共分为22.05KHz、44.1KHz、48KHz三个等级
    public final static int AUDIO_SAMPLE_RATE = 44100;
    // 音频通道 单声道
    public final static int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_FRONT;
    // 音频格式：PCM编码
    public final static int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    // make more memory for minimum buffer size in case of record being dropped.
    public final static int BUFFER_SIZE_FACTOR = 3;
    // 缓冲区大小：缓冲区字节大小
    public final static int BUFFER_SIZE = AudioRecord.getMinBufferSize(AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING)*BUFFER_SIZE_FACTOR;
    // audiorecord
    @SuppressLint("MissingPermission")
    private static AudioRecord audioRecord;

    private Status status;

    // recordingInProgress
    private AtomicBoolean recordingInProgress;

    // noise detector
    private NoiseDetector noiseDetector;

    // store file
    RecordingRunnable recordingRunnable;

    private File file;

    FileOutputStream outputStream;

    private Context context;


}



