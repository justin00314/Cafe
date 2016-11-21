package com.cafe.funf;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import com.cafe.data.funf.FunfData;
import com.cafe.data.funf.ProbeName;
import com.cafe.funf.base.WriteAction;

import org.justin.utils.common.JsonUtils;
import org.justin.utils.common.LogUtils;
import org.justin.utils.common.TimeUtils;
import org.justin.utils.thread.ThreadUtils;

import java.util.Date;

/**
 * 音频特征采集Action
 * Created by Justin Z on 2016/11/2.
 * 502953057@qq.com
 */

public class AudioAction extends WriteAction {

	private final static String TAG = AudioAction.class.getSimpleName();

	//TODO:小米手机拨打电话，接听电话和播放QQ语音会导致音量信号强度整体变低,以计算平均值得方式来重启录音服务
	private static final int AVERAGE_LIMIT = 5;
	private int count = 0;
	private double dbSum = 0d;

	private AudioRecord audioRecorder;

	private int bufferSamples = 0;

	/**
	 * 构造方法
	 *
	 * @param context  Context
	 * @param savePath String
	 * @param maxSize  long
	 */
	public AudioAction(Context context, String savePath, long maxSize) {
		super(context, savePath, maxSize);
	}

	/**
	 * 开始录音
	 */
	private void startRecord() {
		int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
		int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
		int RECORDER_SAMPLE_RATE = 8000;
		int RECORDER_SOURCE = MediaRecorder.AudioSource.MIC;
		// 计算缓冲区大小
		int bufferSize = AudioRecord.getMinBufferSize(
				RECORDER_SAMPLE_RATE,
				RECORDER_CHANNELS,
				RECORDER_AUDIO_ENCODING);
		bufferSize = Math.max(bufferSize, RECORDER_SAMPLE_RATE * 2);
		bufferSamples = bufferSize / 2;
		// 初始化AudioRecorder
		audioRecorder = new AudioRecord(
				RECORDER_SOURCE,
				RECORDER_SAMPLE_RATE,
				RECORDER_CHANNELS,
				RECORDER_AUDIO_ENCODING,
				bufferSize);
		// 启动录音
		audioRecorder.startRecording();
		// 子线程处理音频数据
		ThreadUtils.runOnSubThread(new Runnable() {
			@Override
			public void run() {
				handleAudioStream();
			}
		});

	}

	/**
	 * 停止录音
	 */
	private void stopRecord() {
		audioRecorder.stop();
		audioRecorder.release();
	}

	private void handleAudioStream() {
		short shorts[] = new short[bufferSamples];
		int size;
		while (getState() == State.START) {
			size = audioRecorder.read(shorts, 0, bufferSamples);
			if (size > 0) {
				double db = calculateDB(shorts, size);
				restartRecorder(db);
				// 获取当前时间戳
				long currentTime = new Date().getTime();
				FunfData funfData = new FunfData();
				funfData.probeName = ProbeName.AUDIO;
				funfData.timestamp = currentTime;
				funfData.time = TimeUtils.formatTimeStamp(currentTime,
						TimeUtils.Template.YMDHMS);
				funfData.value = db + "";
				String json = JsonUtils.getInstance().serializeToJson(funfData);
				// 保存数据
				saveData(funfData.time, json);

			}
		}

	}

	/**
	 * 计算音量大小
	 */
	private double calculateDB(short shorts[], int shortSize) {
		long v = 0;

		for (int i = 0; i < shorts.length; i++) {
			v += shorts[i] * shorts[i];
		}
		// 平方和除以数据总长度，得到音量大小。
		double mean = v / (double) shortSize;
		double db = 10 * Math.log10(mean);
		LogUtils.i(TAG, "音量大小-->" + db);
		return db;
	}

	// 重启录音机
	private void restartRecorder(double db) {
		dbSum += db;
		count++;
		double value;
		if (count == AVERAGE_LIMIT) {
			value = dbSum / AVERAGE_LIMIT;
			Log.i(TAG, "db平均值-->" + value);
			if (value < 40) {
				Log.i(TAG, "--重启录音服务--");
				stopRecord();
				startRecord();
			}
			dbSum = 0d;
			count = 0;
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtils.i(TAG, "--采集音频特征Action开始--");
		startRecord();
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtils.i(TAG, "--采集音频特征Action结束--");
		stopRecord();
	}


}
